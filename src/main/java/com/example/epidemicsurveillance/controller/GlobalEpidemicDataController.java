package com.example.epidemicsurveillance.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.epidemicsurveillance.entity.GlobalEpidemicData;
import com.example.epidemicsurveillance.entity.vo.EpidemicDataVO;
import com.example.epidemicsurveillance.entity.vo.globaldata.AllGlobalData;
import com.example.epidemicsurveillance.response.ResponseResult;
import com.example.epidemicsurveillance.service.IGlobalEpidemicDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/api/v1/epidemic-data")
@CrossOrigin
@Api(tags = "前台全球疫情数据模块")
public class GlobalEpidemicDataController {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private IGlobalEpidemicDataService globalEpidemicDataService;

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
    public ResponseResult getChinaEpidemicTotalData(){
        return globalEpidemicDataService.getChinaEpidemicTotalData();
    }

}
