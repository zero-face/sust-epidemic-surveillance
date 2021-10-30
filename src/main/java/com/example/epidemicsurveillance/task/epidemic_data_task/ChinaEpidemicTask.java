package com.example.epidemicsurveillance.task.epidemic_data_task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.epidemicsurveillance.entity.EpidemicDataTrend;
import com.example.epidemicsurveillance.entity.GlobalEpidemicData;
import com.example.epidemicsurveillance.entity.spider.china.*;
import com.example.epidemicsurveillance.service.IEpidemicDataTrendService;
import com.example.epidemicsurveillance.service.IGlobalEpidemicDataService;
import com.example.epidemicsurveillance.spider.SpiderToGetData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
        ChinaAdd chinaAdd=chinaEpidemicData.getChinaAdd();
        china.setExistingDiagnosis(chinaTotal.getNowConfirm());//现有确诊
        china.setAsymptomatic(chinaTotal.getNoInfect());//无症状
        china.setSuspected(chinaTotal.getSuspect());//疑似
        china.setSevere(chinaTotal.getNowSevere());//现有重症
        china.setTotalDiagnosis(chinaTotal.getConfirm());//累计确诊
        china.setOverseasInput(chinaTotal.getImportedCase());//境外输入
        china.setTotalDeath(chinaTotal.getDead());//累计死亡
        china.setTotalCure(chinaTotal.getHeal());//累计治愈
        waitForUpdateList.add(china);

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
        iEpidemicDataTrendService.save(trend);


        //更新各个省每日疫情数据
        List<ProvinceEpidemicAreaTree> provinceList=tree.getChildren();
        for (ProvinceEpidemicAreaTree province:provinceList) {
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
                }
            }
        }
        iGlobalEpidemicDataService.updateBatchById(waitForUpdateList);
    }
}
