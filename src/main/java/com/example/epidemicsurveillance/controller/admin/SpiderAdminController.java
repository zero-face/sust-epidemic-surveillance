package com.example.epidemicsurveillance.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.epidemicsurveillance.entity.GlobalEpidemicData;
import com.example.epidemicsurveillance.response.ResponseResult;
import com.example.epidemicsurveillance.service.IGlobalEpidemicDataService;
import com.example.epidemicsurveillance.spider.SpiderToGetData;
import com.example.epidemicsurveillance.task.epidemic_data_task.ChinaEpidemicTask;
import com.example.epidemicsurveillance.task.epidemic_data_task.GlobalEpidemicTask;
import com.example.epidemicsurveillance.utils.spider.SpiderEpidemicDataUtils;
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
    private SpiderEpidemicDataUtils spiderUtils;

    @Autowired
    private SpiderToGetData spiderToGetData;

    @Autowired
    private GlobalEpidemicTask globalEpidemicTask;

    @Autowired
    private ChinaEpidemicTask chinaEpidemicTask;

    @Autowired
    private IGlobalEpidemicDataService iGlobalEpidemicDataService;

    @ApiOperation(value = "数据爬取")
    @GetMapping("/")
    public ResponseResult get() throws IOException {
        spiderToGetData.getSustJournalism();
        return ResponseResult.ok();
    }

    @ApiOperation(value = "测试")
    @GetMapping("/1")
    public ResponseResult send() throws IOException {
        QueryWrapper<GlobalEpidemicData> wrapper=new QueryWrapper<>();
        wrapper.eq("area_name","地区待确认");
        wrapper.eq("area_name","境外输入");
        iGlobalEpidemicDataService.remove(wrapper);
        return ResponseResult.ok();
    }


}
