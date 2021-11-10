package com.example.epidemicsurveillance.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.epidemicsurveillance.entity.DomesticData;
import com.example.epidemicsurveillance.entity.GlobalEpidemicData;
import com.example.epidemicsurveillance.entity.vo.EpidemicDataVO;
import com.example.epidemicsurveillance.entity.vo.globaldata.AllGlobalData;
import com.example.epidemicsurveillance.response.ResponseResult;
import com.example.epidemicsurveillance.service.IDomesticDataService;
import com.example.epidemicsurveillance.service.IGlobalEpidemicDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 全球疫情数据表 前端控制器
 * </p>
 *
 * @author zyf
 * @since 2021-10-12
 */
@Validated
@Slf4j
@RestController
@RequestMapping("/api/v1/user/epidemic-data")
@CrossOrigin
@Api(tags = "前台全球疫情数据模块")
public class GlobalEpidemicDataController {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private IGlobalEpidemicDataService globalEpidemicDataService;

    @Resource
    private IDomesticDataService domesticDataService;


    @ApiOperation(value = "获取全部地区的名称")
    @GetMapping
    public ResponseResult getAllAreaData() {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        AllGlobalData allGlobalData = (AllGlobalData)operations.get("allGlobalData");
        return ResponseResult.ok().data("allGlobalData",allGlobalData);
    }

    @GetMapping("/city/data/{city}")
    public ResponseResult getDataById(@PathVariable(value = "city" ) String city) {
        final GlobalEpidemicData one = globalEpidemicDataService.getOne(new QueryWrapper<GlobalEpidemicData>().eq(city!=null, "area_name", city));
        if(one == null) {
            return ResponseResult.error().message("未收录该信息");
        }
        EpidemicDataVO epidemicDataVO = new EpidemicDataVO();
        BeanUtils.copyProperties(one, epidemicDataVO);
        log.info("返回的vo：{}",epidemicDataVO);
        Map<String, Object> epidemicVo = JSON.parseObject(JSON.toJSONString(epidemicDataVO), new TypeReference<Map<String, Object>>() {});
        log.info("返回的疫情信息：{}",epidemicVo);
        return ResponseResult.ok().message("获取成功").data(epidemicVo);
    }

    @ApiOperation(value = "获取全国疫情数据")
    @GetMapping("getChinaEpidemicTotalData")
    public ResponseResult getChinaEpidemicTotalData() {
        return globalEpidemicDataService.getChinaEpidemicTotalData();
    }

    @GetMapping("/domestic")
    public ResponseResult getTodayData() {
        final DomesticData todayData = domesticDataService.getTodayData();
        final DomesticData yesterdayData = domesticDataService.getYesterdayData();
        Map<String, Object> todayDatavo = JSON.parseObject(JSON.toJSONString(todayData), new TypeReference<Map<String, Object>>() {});
        if(todayData != null && yesterdayData != null) {
            return ResponseResult.ok().data(todayDatavo).message("获取成功");
        }
        return ResponseResult.error().message("获取失败");
    }

    @ApiOperation(value = "获取中国各个省疫情数据加载地图")
    @GetMapping("getAllProvinceEpidemicData")
    public ResponseResult getAllProvinceEpidemicData(){
        return globalEpidemicDataService.getAllProvinceEpidemicData();
    }

    @ApiOperation(value = "获取中国各个省现有确诊的疫情数据加载地图")
    @GetMapping("getAllProvinceNowConfirmData")
    public ResponseResult getAllProvinceNowConfirmData(){
        return globalEpidemicDataService.getAllProvinceNowConfirmData();
    }

    @ApiOperation(value = "获取中国各个省份的详细疫情数据加载微信小程序树形表格")
    @GetMapping("getAllProvinceEpidemicDataForWXFrom")
    public ResponseResult getAllProvinceEpidemicDataForWXFrom(){
        return globalEpidemicDataService.getAllProvinceEpidemicDataForWXFrom();
    }

    @ApiOperation(value = "获取世界各国累计确诊数据加载地图")
    @GetMapping("getAllCountryEpidemicData")
    public ResponseResult getAllCountryEpidemic(){
        return globalEpidemicDataService.getAllCountryEpidemic();
    }

    @ApiOperation(value = "获取世界各国现有确诊数据加载地图")
    @GetMapping("getAllCountryNowConfirmEpidemicData")
    public ResponseResult getAllCountryNowConfirmEpidemicData(){
        return globalEpidemicDataService.getAllCountryNowConfirmEpidemicData();
    }

    @ApiOperation(value = "获取世界疫情总数据和新增数据")
    @GetMapping("getWorldEpidemicDataAndTodayAddEpidemicData")
    public ResponseResult getWorldEpidemicDataAndTodayAddEpidemicData(){
        return globalEpidemicDataService.getWorldEpidemicDataAndTodayAddEpidemicData();
    }

    @ApiOperation(value = "获取全球疫情数据Top10，加载柱状图")
    @GetMapping("getEpidemicDataTopTenCountry/{type}")
    public ResponseResult getEpidemicDataTopTenCountry(@ApiParam(name = "type",value = "柱状图类型",required = true)
                                                                 @PathVariable Integer type){
        return globalEpidemicDataService.getEpidemicDataTopTenCountry(type);
    }

    @ApiOperation(value = "获取国内外十五日内新增疫情趋势数据")
    @GetMapping("getChinaAndWorldAddConfirmTrend")
    public ResponseResult getChinaAndWorldAddConfirmTrend(){
        return globalEpidemicDataService.getChinaAndWorldAddConfirmTrend();
    }

    @ApiOperation(value = "获取世界各国最新疫情数据")
    @GetMapping("getAllCountryEpidemicDataForWXFrom")
    public ResponseResult getAllCountryEpidemicDataForWXFrom(){
        return globalEpidemicDataService.getAllCountryEpidemicDataForWXFrom();
    }
}
