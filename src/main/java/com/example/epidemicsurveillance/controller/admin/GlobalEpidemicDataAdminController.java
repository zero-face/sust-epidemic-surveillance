package com.example.epidemicsurveillance.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.epidemicsurveillance.entity.GlobalEpidemicData;
import com.example.epidemicsurveillance.entity.query.GlobalEpidemicDataQuery;
import com.example.epidemicsurveillance.response.ResponseResult;
import com.example.epidemicsurveillance.service.IGlobalEpidemicDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName GlobalEpidemicDataAdminController
 * @Author 朱云飞
 * @Date 2021/10/12 22:47
 * @Version 1.0
 **/
@RestController
@RequestMapping("/api/v1/admin/globalepidemicdata")
@CrossOrigin
@Api(tags = "全球疫情模块")
public class GlobalEpidemicDataAdminController {
    @Autowired
    private IGlobalEpidemicDataService globalEpidemicDataService;

    @ApiOperation(value = "分页查询所有地区疫情数据")
    @PostMapping("getAllAreaEpidemicData/{page}/{limit}")
    public ResponseResult getAllAreaEpidemicData(@ApiParam(name = "page",value = "当前页号",required = true) @PathVariable Long page,
                                                 @ApiParam(name = "limit" ,value = "每页数据量",required = true) @PathVariable Long limit,
                                                 @ApiParam(name = "globalEpidemicDataQuery",value = "疫情数据查询类",required = false) @RequestBody(required = false) GlobalEpidemicDataQuery globalEpidemicDataQuery){
        // 创建分页对象
        Page<GlobalEpidemicData> pageResult=new Page<>(page,limit);
        //将查询的数据放入pageResult中
        globalEpidemicDataService.pageQuery(pageResult, globalEpidemicDataQuery);
        long total=pageResult.getTotal();//数据总数
        List<GlobalEpidemicData> list=pageResult.getRecords();//EduCourse的list集合
        return ResponseResult.ok().data("total",total).data("rows",list);
    }

    @ApiOperation(value = "查询国家列表")
    @GetMapping("getCountry")
    public ResponseResult getCountry(){
        return globalEpidemicDataService.getCountry();
    }

    @ApiOperation(value = "根据国家Id查询省份列表")
    @GetMapping("getProvince/{countryId}")
    public ResponseResult getProvince(@ApiParam(name = "countryId",value = "国家Id",required = true)
                                      @PathVariable Integer countryId){
        return globalEpidemicDataService.getProvince(countryId);
    }

    @ApiOperation(value = "根据省份Id查询城市列表")
    @GetMapping("getCity/{provinceId}")
    public ResponseResult getCity(@ApiParam(name = "provinceId",value = "省份Id",required = true)
                                  @PathVariable Integer provinceId){
        return globalEpidemicDataService.getCity(provinceId);
    }
}
