package com.example.epidemicsurveillance.spider;

import com.alibaba.fastjson.JSON;
import com.example.epidemicsurveillance.entity.spider.china.ChinaEpidemic;
import com.example.epidemicsurveillance.entity.spider.global.GlobalEpidemic;
import com.example.epidemicsurveillance.utils.rabbitmq.spider.SpiderErrorSendMailToAdmin;
import com.example.epidemicsurveillance.utils.spider.SpiderEpidemicDataUtils;
import com.example.epidemicsurveillance.utils.spider.processor.SustProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

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
    private SpiderEpidemicDataUtils spiderUtils;

    @Autowired
    private SustProcessor sustProcessor;
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

    /**
     * 爬取SUST今日新闻
     */
    public void getSustJournalism(){
        try {
            Spider.create(sustProcessor)
                    //初始访问url地址
                    .addUrl("https://www.sust.edu.cn/xxyw/yxz1.htm")
                    //.thread(10)
                    .run(); // 执行爬虫
        } catch (Exception e) {
            e.printStackTrace();
            spiderErrorSendMailToAdmin.sendEmailToAdmin("2690534598@qq.com","爬取科大新闻报错,Url是https://www.sust.edu.cn/xxyw/yxz1.htm");
        }
    }
}
