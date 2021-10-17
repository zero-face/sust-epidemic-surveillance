package com.example.epidemicsurveillance.task.epidemic_data_task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.epidemicsurveillance.entity.GlobalEpidemicData;
import com.example.epidemicsurveillance.entity.spider.global.GlobalCountryData;
import com.example.epidemicsurveillance.entity.spider.global.GlobalEpidemic;
import com.example.epidemicsurveillance.entity.spider.global.GlobalTotalData;
import com.example.epidemicsurveillance.entity.vo.globaldata.AllGlobalData;
import com.example.epidemicsurveillance.entity.vo.globaldata.CityData;
import com.example.epidemicsurveillance.entity.vo.globaldata.CountryData;
import com.example.epidemicsurveillance.entity.vo.globaldata.ProvinceData;
import com.example.epidemicsurveillance.service.IGlobalEpidemicDataService;
import com.example.epidemicsurveillance.spider.SpiderToGetData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

    //@Scheduled(cron = "* 0 2 * * ? *")//每日凌晨两点执行
    @Transactional
    public void getGlobalEpidemicData(){
        System.out.println("爬取全球疫情数据任务开始");
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
            }
        }
        iGlobalEpidemicDataService.updateBatchById(dbList);
        System.out.println("爬取全球疫情数据任务结束");
    }


    /**
     * 封装所有地区的数据，存入redis
     */
    @Transactional
    //@Scheduled(cron = "* 0 2 * * ? *")//每日凌晨两点执行
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
        if(valueOperations.get("allGlobalData") != null){
            valueOperations.getAndSet("allGlobalData",allGlobalData);
        }else {
            valueOperations.set("allGlobalData",allGlobalData);
        }
    }
}
