package com.example.epidemicsurveillance.controller;


import com.example.epidemicsurveillance.entity.vo.globaldata.AllGlobalData;
import com.example.epidemicsurveillance.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 全球疫情数据表 前端控制器
 * </p>
 *
 * @author zyf
 * @since 2021-10-12
 */
@RestController
@RequestMapping("/global-epidemic-data")
@CrossOrigin
@Api(tags = "前台全球疫情数据模块")
public class GlobalEpidemicDataController {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;


    @ApiOperation(value = "获取全部地区的名称")
    @GetMapping("getAllAreaData")
    public ResponseResult getAllAreaData(){
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        AllGlobalData allGlobalData = (AllGlobalData)operations.get("allGlobalData");
        return ResponseResult.ok().data("allGlobalData",allGlobalData);
    }
}
