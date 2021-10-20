package com.example.epidemicsurveillance.controller.admin;

import com.example.epidemicsurveillance.task.epidemic_data_task.ChinaEpidemicTask;
import com.example.epidemicsurveillance.task.epidemic_data_task.GlobalEpidemicTask;
import com.example.epidemicsurveillance.task.journalism_data_task.NotificationTask;
import com.example.epidemicsurveillance.task.journalism_data_task.RestTimeInfoTask;
import com.example.epidemicsurveillance.task.journalism_data_task.SustJournalismTask;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName SpiderAdminController
 * @Author 朱云飞
 * @Date 2021/10/14 15:11
 * @Version 1.0
 **/
@RestController
@Api(tags = "数据爬取")
@RequestMapping("/api/v1/admin/spider")
@CrossOrigin
public class SpiderAdminController {
    @Autowired
    private GlobalEpidemicTask globalEpidemicTask;

    @Autowired
    private ChinaEpidemicTask chinaEpidemicTask;

    @Autowired
    private NotificationTask notificationTask;

    @Autowired
    private RestTimeInfoTask restTimeInfoTask;

    @Autowired
    private SustJournalismTask sustJournalismTask;

    @ApiOperation(value = "爬取数据")
    @GetMapping("spiderData/{type}")
    public void spiderData(@ApiParam(name = "type",value = "爬取数据的类型",required = true)
                           @PathVariable("type")Integer type){
        try {
            if(type == 1){//爬取全球各国疫情数据
                globalEpidemicTask.getGlobalEpidemicData();
            }else if (type == 2){//爬取全国各地区疫情数据
                chinaEpidemicTask.getChinaEpidemicData();
            }else if (type == 3){//爬取疫情最新通报数据
                notificationTask.getNotificationData();
            }else if (type == 4){//爬取实时资讯通用数据
                restTimeInfoTask.getRealTimeInfo();
            }else if(type == 5){//爬取科大最新新闻
                sustJournalismTask.getSustJournalismData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
