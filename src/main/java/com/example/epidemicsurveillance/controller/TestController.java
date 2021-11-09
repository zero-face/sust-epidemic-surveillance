package com.example.epidemicsurveillance.controller;

import com.example.epidemicsurveillance.response.ResponseResult;
import com.example.epidemicsurveillance.task.epidemic_data_task.GlobalEpidemicTask;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName TestController
 * @Author 朱云飞
 * @Date 2021/11/6 14:19
 * @Version 1.0
 **/
@Api(tags = "测试控制器")
@CrossOrigin
@RestController
@RequestMapping("/api/v1/user/test")
public class TestController {
    @Autowired
    private GlobalEpidemicTask globalEpidemicTask;

    @ApiOperation(value = "获取疫情数据")
    @GetMapping("getGlobalEpidemicData")
    public ResponseResult getGlobalEpidemicData(){
        globalEpidemicTask.getGlobalEpidemicData();
        return ResponseResult.ok();
    }
}
