package com.example.epidemicsurveillance.task.journalism_data_task;

import com.example.epidemicsurveillance.spider.SpiderToGetData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName RealTimeInfoTask
 * @Author 朱云飞
 * @Date 2021/10/17 12:24
 * @Version 1.0
 **/
@Component
public class NotificationTask {
    @Autowired
    private SpiderToGetData spiderToGetData;

    /**
     * 定时爬取最新通报
     */
    public void getNotificationData(){
        spiderToGetData.getNotification();
    }


}
