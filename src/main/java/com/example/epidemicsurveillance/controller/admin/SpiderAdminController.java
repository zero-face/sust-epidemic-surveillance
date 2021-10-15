package com.example.epidemicsurveillance.controller.admin;

import com.alibaba.fastjson.JSON;
import com.example.epidemicsurveillance.entity.spider.global.GlobalEpidemic;
import com.example.epidemicsurveillance.response.ResponseResult;
import com.example.epidemicsurveillance.spider.SpiderToGetData;
import com.example.epidemicsurveillance.task.epidemic_data_task.GlobalEpidemicTask;
import com.example.epidemicsurveillance.utils.spider.SpiderUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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
    private SpiderUtils spiderUtils;

    @Autowired
    private SpiderToGetData spiderToGetData;

    @Autowired
    private GlobalEpidemicTask globalEpidemicTask;

    @ApiOperation(value = "数据爬取")
    @GetMapping("/")
    public ResponseResult get() throws IOException {
        String data=spiderUtils.getDataJson("https://api.inews.qq.com/newsqa/v1/automation/modules/list?modules=FAutoCountryConfirmAdd,WomWorld,WomAboard");
        GlobalEpidemic globalEpidemic = JSON.parseObject(data, GlobalEpidemic.class);
        return ResponseResult.ok().data("data",globalEpidemic);
    }

    @ApiOperation(value = "邮件发送")
    @GetMapping("/1")
    public ResponseResult send() throws IOException {
       globalEpidemicTask.getGlobalEpidemicData();
       return ResponseResult.ok();
    }
}
