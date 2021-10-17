package com.example.epidemicsurveillance.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.epidemicsurveillance.entity.GlobalEpidemicData;
import com.example.epidemicsurveillance.response.ResponseResult;
import com.example.epidemicsurveillance.service.IGlobalEpidemicDataService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 全球疫情数据表 前端控制器
 * </p>
 *
 * @author zyf
 * @since 2021-10-12
 */
@RestController
@RequestMapping("/api/v1/epidemic/{id}")
public class GlobalEpidemicDataController {

    @Autowired
    private IGlobalEpidemicDataService globalEpidemicDataService;

    @GetMapping
    public ResponseResult getEpidemicData(@PathVariable("epidemicId")Integer epidemicId) {
        final GlobalEpidemicData one = globalEpidemicDataService.getOne(new QueryWrapper<GlobalEpidemicData>().eq("id", 1));

        return ResponseResult.ok();
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
