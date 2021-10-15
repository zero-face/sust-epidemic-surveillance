package com.example.epidemicsurveillance.task.epidemic_data_task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.epidemicsurveillance.entity.GlobalEpidemicData;
import com.example.epidemicsurveillance.entity.spider.china.ChinaEpidemic;
import com.example.epidemicsurveillance.entity.spider.china.ChinaEpidemicAreaTree;
import com.example.epidemicsurveillance.entity.spider.china.CityEpidemicAreaTree;
import com.example.epidemicsurveillance.entity.spider.china.ProvinceEpidemicAreaTree;
import com.example.epidemicsurveillance.service.IGlobalEpidemicDataService;
import com.example.epidemicsurveillance.spider.SpiderToGetData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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

    @Scheduled(cron = "0 0 1 * * ?  ")//每日凌晨一点执行
    @Transactional
    public void getChinaEpidemicData(){
        System.out.println("定时任务开始");
        ChinaEpidemic chinaEpidemicData = spiderToGetData.getChinaEpidemicData("https://view.inews.qq.com/g2/getOnsInfo?name=disease_h5&callback=jQuery35108028357975576601_1634228907365&_=1634228907366");
        ChinaEpidemicAreaTree tree=chinaEpidemicData.getAreaTree().get(0);
        //数据库中待更新的全国各地区疫情数据List
        List<GlobalEpidemicData> waitForUpdateList=new ArrayList<>();

        //更新中国每日疫情数据
        QueryWrapper<GlobalEpidemicData> wrapper=new QueryWrapper<>();
        wrapper.eq("area_name","中国");
        GlobalEpidemicData china = iGlobalEpidemicDataService.getOne(wrapper);
        china.setExistingDiagnosis(tree.getTotal().getNowConfirm());//现有确诊
        china.setSuspected(tree.getTotal().getSuspect());//疑似
        china.setTotalDiagnosis(tree.getTotal().getConfirm());//累计确诊
        china.setTotalDeath(tree.getTotal().getDead());//累计死亡
        china.setTotalCure(tree.getTotal().getHeal());//累计治愈
        waitForUpdateList.add(china);

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
        iGlobalEpidemicDataService.updateBatchById(waitForUpdateList);
        System.out.println("定时任务结束");
    }
}
