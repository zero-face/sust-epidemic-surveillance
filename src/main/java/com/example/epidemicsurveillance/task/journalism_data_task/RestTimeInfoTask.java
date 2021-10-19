package com.example.epidemicsurveillance.task.journalism_data_task;

import com.example.epidemicsurveillance.spider.SpiderToGetData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName RestTimeInfoTask
 * @Author 朱云飞
 * @Date 2021/10/19 13:19
 * @Version 1.0
 **/
@Component
public class RestTimeInfoTask {

    @Autowired
    private SpiderToGetData spiderToGetData;


    public void getRealTimeInfo(){
        spiderToGetData.getRealTimeInfo();
    }
}
