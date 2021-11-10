package com.example.epidemicsurveillance.task.epidemic_data_task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.epidemicsurveillance.entity.EpidemicDataTrend;
import com.example.epidemicsurveillance.entity.GlobalEpidemicData;
import com.example.epidemicsurveillance.entity.spider.global.GlobalCountryData;
import com.example.epidemicsurveillance.entity.spider.global.GlobalEpidemic;
import com.example.epidemicsurveillance.entity.spider.global.GlobalTotalData;
import com.example.epidemicsurveillance.entity.vo.WxTotalFromVo;
import com.example.epidemicsurveillance.entity.vo.globaldata.AllGlobalData;
import com.example.epidemicsurveillance.entity.vo.globaldata.CityData;
import com.example.epidemicsurveillance.entity.vo.globaldata.CountryData;
import com.example.epidemicsurveillance.entity.vo.globaldata.ProvinceData;
import com.example.epidemicsurveillance.mapper.GlobalEpidemicDataMapper;
import com.example.epidemicsurveillance.service.IEpidemicDataTrendService;
import com.example.epidemicsurveillance.service.IGlobalEpidemicDataService;
import com.example.epidemicsurveillance.spider.SpiderToGetData;
import com.example.epidemicsurveillance.utils.dateutil.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * 全球疫情数据爬取定时任务
 * @ClassName GlobalEpidemicTask
 * @Author 朱云飞
 * @Date 2021/10/14 22:02
 * @Version 1.0
 **/
@Component
public class GlobalEpidemicTask {
    @Autowired
    private SpiderToGetData spiderToGetData;

    @Autowired
    private IGlobalEpidemicDataService iGlobalEpidemicDataService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private GlobalEpidemicDataMapper globalEpidemicDataMapper;

    @Autowired
    private IEpidemicDataTrendService iEpidemicDataTrendService;

    //@Scheduled(cron = "* 0 2 * * ? *")//每日凌晨两点执行
    @Transactional
    public void getGlobalEpidemicData(){
        GlobalEpidemic globalEpidemicData = spiderToGetData.getGlobalEpidemicData("https://api.inews.qq.com/newsqa/v1/automation/modules/list?modules=FAutoCountryConfirmAdd,WomWorld,WomAboard");
        List<GlobalCountryData> list = globalEpidemicData.getData().getWomAboard();
        QueryWrapper<GlobalEpidemicData> wrapper=new QueryWrapper<>();
        wrapper.eq("parent_id",1);
        List<GlobalEpidemicData> dbList=iGlobalEpidemicDataService.list(wrapper);
        for (GlobalCountryData countryData:list) {
            for (GlobalEpidemicData data:dbList) {
                if(countryData.getName().equals(data.getAreaName())){
                    data.setExistingDiagnosis(countryData.getNowConfirm());//现有确诊
                    data.setTotalDiagnosis(countryData.getConfirm());//总共确诊
                    data.setTotalCure(countryData.getHeal());//累计治愈
                    data.setTotalDeath(countryData.getDead());//累计死亡
                    data.setAddConfirm(countryData.getConfirmAdd());//新增确诊
                }
            }
        }
        for (GlobalEpidemicData data:dbList) {
            if(data.getAreaName().equals("全球")){
                GlobalTotalData globalTotalData = globalEpidemicData.getData().getWomWorld();
                data.setExistingDiagnosis(globalTotalData.getNowConfirm());//现有确诊
                data.setTotalDiagnosis(globalTotalData.getConfirm());//总共确诊
                data.setTotalCure(globalTotalData.getHeal());//治愈人数
                data.setTotalDeath(globalTotalData.getDead());//总共死亡
                data.setAddConfirm(globalTotalData.getConfirmAdd());//新增确诊
                EpidemicDataTrend trend=new EpidemicDataTrend();
                trend.setAreaName("全球");
                trend.setExistingDiagnosis(globalTotalData.getNowConfirmAdd());//新增现有确诊
                trend.setTotalDiagnosis(globalTotalData.getConfirmAdd());//新增累计确诊
                trend.setTotalCure(globalTotalData.getHealAdd());//新增累计治愈
                trend.setTotalDeath(globalTotalData.getDeadAdd());//新增累计死亡
                trend.setHealRate(globalTotalData.getCurerate());//治愈率
                trend.setDeadRate(globalTotalData.getDeathrate());//死亡率
                trend.setToday(DateUtil.getTodayTimeString());
                iEpidemicDataTrendService.save(trend);
            }
        }
        iGlobalEpidemicDataService.updateBatchById(dbList);
        deleteRedisGlobalData();
        getGlobalEpidemicDataForFront();
        getAllCountryEpidemicDataInputRedis();
    }


    /**
     * 封装所有地区的数据，存入redis
     */
    public void getGlobalEpidemicDataForFront(){
        AllGlobalData allGlobalData=new AllGlobalData();
        List<CountryData> countryDataList=iGlobalEpidemicDataService.getAllCountryData();
        for (CountryData countryData:countryDataList) {
            List<ProvinceData> provinceDataList=iGlobalEpidemicDataService.getEveryCountryProvinceData(countryData.getId());
            for (ProvinceData provinceData:provinceDataList) {
                List<CityData> cityDataList=iGlobalEpidemicDataService.getEveryProvinceCityData(provinceData.getId());
                provinceData.setChildren(cityDataList);
            }
            countryData.setChildren(provinceDataList);
        }
        allGlobalData.setGlobalDataList(countryDataList);
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("allGlobalData",allGlobalData);
    }

    /**
     * 封装世界各国最新疫情数据，存入redis，加载小程序树形表格
     */
    public void getAllCountryEpidemicDataInputRedis(){
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        WxTotalFromVo allCountryDataForWxFrom =(WxTotalFromVo) valueOperations.get("allCountryDataForWxFrom");
        if (allCountryDataForWxFrom == null) {
            GlobalEpidemicData world=globalEpidemicDataMapper.selectById(1);
            QueryWrapper<GlobalEpidemicData> wrapper=new QueryWrapper<>();
            wrapper.eq("parent_id",1);
            wrapper.ne("id",1);
            List<GlobalEpidemicData> globalEpidemicDataList=globalEpidemicDataMapper.selectList(wrapper);
            WxTotalFromVo result=new WxTotalFromVo();
            result.setIndex(1);
            result.setFlag(1);
            result.setId(world.getId());
            result.setAreaName(world.getAreaName());
            result.setNowConfirm(world.getExistingDiagnosis());//现有
            result.setConfirm(world.getTotalDiagnosis());//累计
            result.setAddConfirm(world.getAddConfirm());//新增
            result.setDead(world.getTotalDeath());//累计死亡
            result.setHeal(world.getTotalCure());//累计治愈
            List<WxTotalFromVo> wxTotalFromVoLinkedList=new LinkedList<>();
            for(GlobalEpidemicData data:globalEpidemicDataList){
                WxTotalFromVo vo=new WxTotalFromVo();
                vo.setIndex(2);
                vo.setFlag(0);
                vo.setId(data.getId());
                vo.setAreaName(data.getAreaName());
                vo.setNowConfirm(data.getExistingDiagnosis());//现有
                vo.setConfirm(data.getTotalDiagnosis());//累计
                vo.setAddConfirm(data.getAddConfirm());//新增
                vo.setDead(data.getTotalDeath());//累计死亡
                vo.setHeal(data.getTotalCure());//累计治愈
                wxTotalFromVoLinkedList.add(vo);
            }
            result.setNodes(wxTotalFromVoLinkedList);
            valueOperations.set("allCountryDataForWxFrom",result);
        }

    }



    /**
     * 每日更新疫情数据，删除缓存中所以相关的数据
     */
    private void deleteRedisGlobalData(){
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        RedisOperations<String, Object> operations = valueOperations.getOperations();
        operations.delete("allCountryDataForWxFrom");
        operations.delete("allGlobalData");
    }
}
