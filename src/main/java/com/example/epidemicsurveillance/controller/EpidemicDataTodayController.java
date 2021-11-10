package com.example.epidemicsurveillance.controller;


import com.example.epidemicsurveillance.response.ResponseResult;
import com.example.epidemicsurveillance.service.IEpidemicDataTodayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 今日疫情新增表 前端控制器
 * </p>
 *
 * @author zyf
 * @since 2021-11-06
 */
@RestController
@RequestMapping("/api/v1/user/epidemic-data-today")
@Api(tags = "今日疫情新增模块")
@CrossOrigin
public class EpidemicDataTodayController {
    @Autowired
    private IEpidemicDataTodayService iEpidemicDataTodayService;

    @ApiOperation("获取中国各个地区今日新增疫情数据加载微信小程序树形表格")
    @GetMapping("getAllProvinceEpidemicDataTodayAddForWXFrom")
    public ResponseResult getAllProvinceEpidemicDataTodayAddForWXFrom(){
        return iEpidemicDataTodayService.getAllProvinceEpidemicDataTodayAddForWXFrom();
    }

}
