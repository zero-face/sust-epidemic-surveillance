package com.example.epidemicsurveillance.task.epidemic_data_task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.epidemicsurveillance.entity.EpidemicDataToday;
import com.example.epidemicsurveillance.entity.EpidemicDataTrend;
import com.example.epidemicsurveillance.entity.GlobalEpidemicData;
import com.example.epidemicsurveillance.entity.spider.china.*;
import com.example.epidemicsurveillance.entity.vo.WxTodayFromVo;
import com.example.epidemicsurveillance.entity.vo.WxTotalFromVo;
import com.example.epidemicsurveillance.mapper.GlobalEpidemicDataMapper;
import com.example.epidemicsurveillance.service.IEpidemicDataTodayService;
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 全国疫情数据爬取定时任务
 * @ClassName ChinaEpidemicTask
 * @Author 朱云飞
 * @Date 2021/10/15 14:47
 * @Version 1.0
 **/
@Component
public class ChinaEpidemicTask {
    @Autowired
    private SpiderToGetData spiderToGetData;

    @Autowired
    private IGlobalEpidemicDataService iGlobalEpidemicDataService;

    @Autowired
    private IEpidemicDataTrendService iEpidemicDataTrendService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private GlobalEpidemicDataMapper globalEpidemicDataMapper;

    @Autowired
    private IEpidemicDataTodayService iEpidemicDataTodayService;



    //@Scheduled(cron = "* 10 2 * * ? *")//每日凌晨两点十分执行
    @Transactional
    public void getChinaEpidemicData(){
        ChinaEpidemic chinaEpidemicData = spiderToGetData.getChinaEpidemicData("https://view.inews.qq.com/g2/getOnsInfo?name=disease_h5&callback=jQuery35108028357975576601_1634228907365&_=1634228907366");
        ChinaEpidemicAreaTree tree=chinaEpidemicData.getAreaTree().get(0);
        //数据库中待更新的全国各地区疫情数据List
        List<GlobalEpidemicData> waitForUpdateList=new ArrayList<>();

        //更新中国每日疫情数据
        QueryWrapper<GlobalEpidemicData> wrapper=new QueryWrapper<>();
        wrapper.eq("area_name","中国");
        GlobalEpidemicData china = iGlobalEpidemicDataService.getOne(wrapper);
        ChinaTotal chinaTotal=chinaEpidemicData.getChinaTotal();
        china.setExistingDiagnosis(chinaTotal.getNowConfirm());//现有确诊
        china.setAsymptomatic(chinaTotal.getNoInfect());//无症状
        china.setSuspected(chinaTotal.getSuspect());//疑似
        china.setSevere(chinaTotal.getNowSevere());//现有重症
        china.setTotalDiagnosis(chinaTotal.getConfirm());//累计确诊
        china.setOverseasInput(chinaTotal.getImportedCase());//境外输入
        china.setTotalDeath(chinaTotal.getDead());//累计死亡
        china.setTotalCure(chinaTotal.getHeal());//累计治愈
        china.setAddConfirm(tree.getToday().getConfirm());//今日新增
        waitForUpdateList.add(china);

        //更新中国新增每日疫情趋势数据
        ChinaAdd chinaAdd=chinaEpidemicData.getChinaAdd();
        EpidemicDataTrend trend=new EpidemicDataTrend();
        trend.setAreaName("中国");
        trend.setExistingDiagnosis(chinaAdd.getNowConfirm());//现有确诊
        trend.setAsymptomatic(chinaAdd.getNoInfect());//无症状
        trend.setSuspected(chinaAdd.getSuspect());//疑似
        trend.setSevere(chinaAdd.getNowSevere());//现有重症
        trend.setTotalDiagnosis(chinaAdd.getConfirm());//累计确诊
        trend.setOverseasInput(chinaAdd.getImportedCase());//境外输入
        trend.setTotalDeath(chinaAdd.getDead());//累计死亡
        trend.setTotalCure(chinaAdd.getHeal());//累计治愈
        trend.setToday(DateUtil.getTodayTimeString());
        iEpidemicDataTrendService.save(trend);

        //更新各个省份及市每日新增疫情数据
        List<EpidemicDataToday> todayList=new LinkedList<>();
        if (tree.getToday().getConfirm() != 0) {
            EpidemicDataToday chinaToday=new EpidemicDataToday();
            chinaToday.setAreaName("中国");
            chinaToday.setAddConfirm(tree.getToday().getConfirm());//今日新增
            chinaToday.setNowConfirm(chinaTotal.getNowConfirm());//现有确诊
            todayList.add(chinaToday);
        }


        //更新各个省每日疫情数据
        List<ProvinceEpidemicAreaTree> provinceList=tree.getChildren();
        for (ProvinceEpidemicAreaTree province:provinceList) {
            //更新各个省每日疫情数据
            QueryWrapper<GlobalEpidemicData> provinceWrapper=new QueryWrapper<>();
            provinceWrapper.eq("area_name",province.getName());
            GlobalEpidemicData provinceData=iGlobalEpidemicDataService.getOne(provinceWrapper);
            provinceData.setAreaName(province.getName());//名称
            provinceData.setExistingDiagnosis(province.getTotal().getNowConfirm());//现有确诊
            provinceData.setSuspected(province.getTotal().getSuspect());//疑似
            provinceData.setTotalDiagnosis(province.getTotal().getConfirm());//累计确诊
            provinceData.setTotalDeath(province.getTotal().getDead());//累计死亡
            provinceData.setTotalCure(province.getTotal().getHeal());//累计治愈
            provinceData.setParentId(china.getId());//父Id是中国Id
            waitForUpdateList.add(provinceData);

            //添加各个省当日新增疫情数据
            if (province.getToday().getConfirm() != 0) {
                EpidemicDataToday provinceToday=new EpidemicDataToday();
                provinceToday.setAreaName(province.getName());
                provinceToday.setParentName("中国");
                provinceToday.setAddConfirm(province.getToday().getConfirm());
                provinceToday.setNowConfirm(province.getTotal().getNowConfirm());
                todayList.add(provinceToday);
            }


            //更新省所属各个市每日疫情数据
            List<CityEpidemicAreaTree> cityList=province.getChildren();
            for (CityEpidemicAreaTree city:cityList) {
                if(city.getName().equals("地区待确认") || city.getName().equals("境外输入")){
                    continue;
                }
                if(city.getName().equals("吉林")){
                    city.setName("吉林市");
                }
                QueryWrapper<GlobalEpidemicData> cityWrapper=new QueryWrapper<>();
                cityWrapper.eq("area_name",city.getName());
                GlobalEpidemicData cityData=iGlobalEpidemicDataService.getOne(cityWrapper);
                if (cityData != null) {
                    cityData.setAreaName(city.getName());
                    cityData.setExistingDiagnosis(city.getTotal().getNowConfirm());//现有确诊
                    cityData.setSuspected(city.getTotal().getSuspect());//疑似
                    cityData.setTotalDiagnosis(city.getTotal().getConfirm());//累计确诊
                    cityData.setTotalDeath(city.getTotal().getDead());//累计死亡
                    cityData.setTotalCure(city.getTotal().getHeal());//累计治愈
                    cityData.setParentId(provinceData.getId());
                    waitForUpdateList.add(cityData);

                    //各个市区今日新增疫情数据
                    if (city.getToday().getConfirm() != 0) {
                        EpidemicDataToday cityToday=new EpidemicDataToday();
                        cityToday.setAreaName(city.getName());
                        cityToday.setAddConfirm(city.getToday().getConfirm());
                        cityToday.setNowConfirm(city.getTotal().getNowConfirm());
                        cityToday.setParentName(provinceData.getAreaName());
                        todayList.add(cityToday);
                    }
                }
            }
        }
        iGlobalEpidemicDataService.updateBatchById(waitForUpdateList);
        //删除当日疫情新增表中的全部数据
        iEpidemicDataTodayService.remove(null);
        iEpidemicDataTodayService.saveBatch(todayList);
        deleteRedisGlobalData();
        getAllProvinceEpidemicDataForWXFromInputRedis();
        getAllProvinceEpidemicDataTodayAddForWXFromInputRedis();
    }

    //封装各个省市区疫情数据加载小程序表格
    public void getAllProvinceEpidemicDataForWXFromInputRedis() {
        WxTotalFromVo result= null;
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        WxTotalFromVo wxEpidemicTotalDataTable = (WxTotalFromVo)valueOperations.get("wxEpidemicTotalDataTable");
        if (wxEpidemicTotalDataTable == null) {
            GlobalEpidemicData china = globalEpidemicDataMapper.selectById(2);
            result = new WxTotalFromVo();
            result.setIndex(1);
            result.setFlag(1);
            result.setId(china.getId());
            result.setAreaName(china.getAreaName());
            result.setNowConfirm(china.getExistingDiagnosis());
            result.setSuspect(china.getSuspected());
            result.setConfirm(china.getTotalDiagnosis());
            result.setDead(china.getTotalDeath());
            result.setHeal(china.getTotalCure());
            List<WxTotalFromVo> list=new LinkedList<>();
            QueryWrapper<GlobalEpidemicData> wrapper=new QueryWrapper<>();
            wrapper.eq("parent_id",2);
            List<GlobalEpidemicData> globalEpidemicDataList = globalEpidemicDataMapper.selectList(wrapper);
            for (GlobalEpidemicData globalEpidemicData: globalEpidemicDataList) {
                List<WxTotalFromVo> nodes=new LinkedList<>();
                QueryWrapper<GlobalEpidemicData> wrapper1=new QueryWrapper<>();
                wrapper1.eq("parent_id",globalEpidemicData.getId());
                List<GlobalEpidemicData> globalEpidemicDataNodes = globalEpidemicDataMapper.selectList(wrapper1);
                for (GlobalEpidemicData node:globalEpidemicDataNodes) {
                    WxTotalFromVo vo=new WxTotalFromVo();
                    vo.setIndex(3);
                    vo.setFlag(0);
                    vo.setId(node.getId());
                    vo.setAreaName(node.getAreaName());
                    vo.setNowConfirm(node.getExistingDiagnosis());
                    vo.setSuspect(node.getSuspected());
                    vo.setConfirm(node.getTotalDiagnosis());
                    vo.setDead(node.getTotalDeath());
                    vo.setHeal(node.getTotalCure());
                    nodes.add(vo);
                }
                WxTotalFromVo parentVo=new WxTotalFromVo();
                parentVo.setIndex(2);
                parentVo.setFlag(1);
                parentVo.setId(globalEpidemicData.getId());
                parentVo.setAreaName(globalEpidemicData.getAreaName());
                parentVo.setNowConfirm(globalEpidemicData.getExistingDiagnosis());
                parentVo.setSuspect(globalEpidemicData.getSuspected());
                parentVo.setConfirm(globalEpidemicData.getTotalDiagnosis());
                parentVo.setDead(globalEpidemicData.getTotalDeath());
                parentVo.setHeal(globalEpidemicData.getTotalCure());
                parentVo.setNodes(nodes);
                list.add(parentVo);
            }
            result.setNodes(list);
            valueOperations.set("wxEpidemicTotalDataTable",result);
        }
    }

    //封装各个省市区今日新增疫情数据加载小程序表格
    public void getAllProvinceEpidemicDataTodayAddForWXFromInputRedis(){
        WxTodayFromVo result= null;
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        WxTodayFromVo wxTodayAddTable = (WxTodayFromVo)valueOperations.get("wxTodayAddTable");
        if (wxTodayAddTable == null) {
            QueryWrapper<EpidemicDataToday> wrapper=new QueryWrapper<>();
            wrapper.eq("area_name","中国");
            EpidemicDataToday china = iEpidemicDataTodayService.getOne(wrapper);
            result = new WxTodayFromVo();
            result.setIndex(1);
            result.setFlag(1);
            result.setId(china.getId());
            result.setAreaName(china.getAreaName());
            result.setAddConfirm(china.getAddConfirm());
            result.setNowConfirm(china.getNowConfirm());
            List<WxTodayFromVo> list=new LinkedList<>();
            QueryWrapper<EpidemicDataToday> wrapper1=new QueryWrapper<>();
            wrapper1.eq("parent_name",china.getAreaName());
            List<EpidemicDataToday> epidemicDataTodayList = iEpidemicDataTodayService.list(wrapper1);
            for (EpidemicDataToday epidemicDataToday: epidemicDataTodayList) {
                List<WxTodayFromVo> nodes=new LinkedList<>();
                QueryWrapper<EpidemicDataToday> wrapper2=new QueryWrapper<>();
                wrapper2.eq("parent_name",epidemicDataToday.getAreaName());
                List<EpidemicDataToday> epidemicDataTodayNodes = iEpidemicDataTodayService.list(wrapper2);
                for (EpidemicDataToday node:epidemicDataTodayNodes) {
                    WxTodayFromVo vo=new WxTodayFromVo();
                    vo.setIndex(3);
                    vo.setFlag(0);
                    vo.setId(node.getId());
                    vo.setAreaName(node.getAreaName());
                    vo.setNowConfirm(node.getNowConfirm());
                    vo.setAddConfirm(node.getAddConfirm());
                    nodes.add(vo);
                }
                WxTodayFromVo parentVo=new WxTodayFromVo();
                parentVo.setIndex(2);
                parentVo.setFlag(1);
                parentVo.setId(epidemicDataToday.getId());
                parentVo.setAreaName(epidemicDataToday.getAreaName());
                parentVo.setNowConfirm(epidemicDataToday.getNowConfirm());
                parentVo.setAddConfirm(epidemicDataToday.getAddConfirm());
                parentVo.setNodes(nodes);
                list.add(parentVo);
            }
            result.setNodes(list);
            valueOperations.set("wxTodayAddTable",result);
        }
    }

    /**
     * 每日更新疫情数据，删除缓存中所以相关的数据
     */
    private void deleteRedisGlobalData(){
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        RedisOperations<String, Object> operations = valueOperations.getOperations();
        operations.delete("wxEpidemicTotalDataTable");
        operations.delete("wxTodayAddTable");
    }
}
