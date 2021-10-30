package com.example.epidemicsurveillance.spider;

import com.alibaba.fastjson.JSON;
import com.example.epidemicsurveillance.entity.spider.china.ChinaEpidemic;
import com.example.epidemicsurveillance.entity.spider.global.GlobalEpidemic;
import com.example.epidemicsurveillance.entity.spider.notification.AllNotificationData;
import com.example.epidemicsurveillance.entity.spider.notification.NotificationDataDetails;
import com.example.epidemicsurveillance.utils.rabbitmq.EmailSendUtil;
import com.example.epidemicsurveillance.utils.spider.SpiderEpidemicDataUtils;
import com.example.epidemicsurveillance.utils.spider.pipeline.DomesticDataPipeline;
import com.example.epidemicsurveillance.utils.spider.processor.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

import java.util.List;

/**
 * @ClassName GlobalDataSpider
 * @Author 朱云飞
 * @Date 2021/10/14 15:29
 * @Version 1.0
 **/
@Component
public class SpiderToGetData {
    @Autowired
    private EmailSendUtil emailSendUtil;

    @Autowired
    private SpiderEpidemicDataUtils spiderUtils;

    @Autowired
    private SustProcessor sustProcessor;

    @Autowired
    private NotificationProcessor notificationProcessor;

    @Autowired
    private RealTimeInfoProcessor realTimeInfoProcessor;

    @Autowired
    private CityCodeProcessor cityCodeProcessor;

    @Autowired
    private DomesticDataProcessor domesticDataProcessor;

    @Autowired
    private DomesticDataPipeline domesticDataPipeline;

    /**
     * 爬取全球数据
     */
    public GlobalEpidemic getGlobalEpidemicData(String url){
        try {
            String dataJson = spiderUtils.getGlobalDataJson(url);
            GlobalEpidemic globalEpidemic = JSON.parseObject(dataJson, GlobalEpidemic.class);
            if(globalEpidemic==null || globalEpidemic.getData().getWomAboard() == null){
                emailSendUtil.sendEmailToAdmin("2690534598@qq.com","爬取全球数据失败，url是"+url);
            }
            return globalEpidemic;
        } catch (Exception e) {
            e.printStackTrace();
            emailSendUtil.sendEmailToAdmin("2690534598@qq.com","爬取全球数据报错，url是"+url);
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
                emailSendUtil.sendEmailToAdmin("2690534598@qq.com","爬取中国数据失败，url是"+url);
            }
            return chinaEpidemic;
        } catch (Exception e) {
            e.printStackTrace();
            emailSendUtil.sendEmailToAdmin("2690534598@qq.com","爬取中国数据报错，url是"+url);
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
            emailSendUtil.sendEmailToAdmin("2690534598@qq.com","爬取科大新闻报错,Url是https://www.sust.edu.cn/xxyw/yxz1.htm");
        }
    }
    /**
     * 爬取最新通报
     */
    public void getNotification(){
        try {
            String dataJson = spiderUtils.getNotificationData("https://i.news.qq.com/trpc.qqnews_web.kv_srv.kv_srv_http_proxy/list?sub_srv_id=antip&srv_id=pc&offset=0&limit=60&strategy=1&ext=%7B%22pool%22%3A[%22hot%22],%22is_filter%22:2,%22check_title%22:0,%22check_type%22:true%7D");
            AllNotificationData allNotificationData = JSON.parseObject(dataJson, AllNotificationData.class);
            List<NotificationDataDetails> list = allNotificationData.getData().getList();
            for (NotificationDataDetails detail:list) {
                Spider.create(notificationProcessor)
                        //初始访问url地址
                        .addUrl(detail.getUrl())
                        //.thread(10)
                        .run(); // 执行爬虫
            }
        } catch (Exception e) {
            e.printStackTrace();
            emailSendUtil.sendEmailToAdmin("2690534598@qq.com","爬取疫情最新通报报错,Url是https://i.news.qq.com/trpc.qqnews_web.kv_srv.kv_srv_http_proxy/list?sub_srv_id=antip&srv_id=pc&offset=0&limit=60&strategy=1&ext=%7B%22pool%22%3A[%22hot%22],%22is_filter%22:2,%22check_title%22:0,%22check_type%22:true%7D");
        }
    }

    /**
     * 爬取今日资讯最新新闻
     */
    public void getRealTimeInfo(){
        try {
            Spider.create(realTimeInfoProcessor)
                    //初始访问url地址
                    .addUrl("http://swt.changsha.gov.cn/fwwb/zxzx/jrzx/")
                    //.thread(10)
                    .run(); // 执行爬虫
        } catch (Exception e) {
            e.printStackTrace();
            emailSendUtil.sendEmailToAdmin("2690534598@qq.com","爬取最新资讯报错,Url是http://swt.changsha.gov.cn/fwwb/zxzx/jrzx/");
        }
    }

    /**
     * 爬取地区编码
     * @param url
     */
    public void getCityCOde(String url) {
        try {
            Spider.create(cityCodeProcessor)
                    .addUrl(url)
                    .run();
        }catch (Exception e) {
            e.printStackTrace();
            emailSendUtil.sendEmailToAdmin("1444171773@qq.com", "爬取地区编码出错");
        }
    }

    /**
     * 爬取国内疫情数据
     * @param url
     */
    public void getDomesticData(String url) {
        Spider.create(domesticDataProcessor)
                .addUrl(url)
                .addPipeline(domesticDataPipeline)
                .run();
    }
}
