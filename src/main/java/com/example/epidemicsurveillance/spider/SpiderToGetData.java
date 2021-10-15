package com.example.epidemicsurveillance.spider;

import com.alibaba.fastjson.JSON;
import com.example.epidemicsurveillance.entity.spider.china.ChinaEpidemic;
import com.example.epidemicsurveillance.entity.spider.global.GlobalEpidemic;
import com.example.epidemicsurveillance.utils.rabbitmq.spider.SpiderErrorSendMailToAdmin;
import com.example.epidemicsurveillance.utils.spider.SpiderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName GlobalDataSpider
 * @Author 朱云飞
 * @Date 2021/10/14 15:29
 * @Version 1.0
 **/
@Component
public class SpiderToGetData {
    @Autowired
    private SpiderErrorSendMailToAdmin spiderErrorSendMailToAdmin;

    @Autowired
    private SpiderUtils spiderUtils;
    /**
     * 爬取全球数据
     */
    public GlobalEpidemic getGlobalEpidemicData(String url){
        try {
            String dataJson = spiderUtils.getGlobalDataJson(url);
            GlobalEpidemic globalEpidemic = JSON.parseObject(dataJson, GlobalEpidemic.class);
            if(globalEpidemic==null || globalEpidemic.getData().getWomAboard() == null){
                spiderErrorSendMailToAdmin.sendEmailToAdmin("2690534598@qq.com","爬取全球数据失败，url是"+url);
            }
            return globalEpidemic;
        } catch (Exception e) {
            e.printStackTrace();
            spiderErrorSendMailToAdmin.sendEmailToAdmin("2690534598@qq.com","爬取全球数据报错，url是"+url);
        }
        return null;
    }


    /**
     * 爬取中国疫情数据
     */
    public ChinaEpidemic getChinaEpidemicData(String url){
        try {
            String dataJson = spiderUtils.getChinaDataJson(url);
            System.out.println(dataJson);
            ChinaEpidemic chinaEpidemic = JSON.parseObject(dataJson, ChinaEpidemic.class);
            if(chinaEpidemic==null || chinaEpidemic.getAreaTree()== null){
                spiderErrorSendMailToAdmin.sendEmailToAdmin("2690534598@qq.com","爬取中国数据失败，url是"+url);
            }
            return chinaEpidemic;
        } catch (Exception e) {
            e.printStackTrace();
            spiderErrorSendMailToAdmin.sendEmailToAdmin("2690534598@qq.com","爬取中国数据报错，url是"+url);
        }
        return null;
    }
}
