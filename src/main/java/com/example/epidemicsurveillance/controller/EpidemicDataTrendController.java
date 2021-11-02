package com.example.epidemicsurveillance.controller;


import com.example.epidemicsurveillance.response.ResponseResult;
import com.example.epidemicsurveillance.service.IEpidemicDataTrendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 疫情趋势表 前端控制器
 * </p>
 *
 * @author zyf
 * @since 2021-10-30
 */
@RestController
@RequestMapping("/api/v1/user/epidemic-data-trend")
@CrossOrigin
@Api(tags = "疫情趋势模块")
public class EpidemicDataTrendController {
    @Autowired
    private IEpidemicDataTrendService iEpidemicDataTrendService;

    @ApiOperation(value = "获取当日疫情新增数据")
    @GetMapping("getTodayEpidemicDataTrend")
    public ResponseResult getTodayEpidemicDataTrend(){
        return iEpidemicDataTrendService.getTodayEpidemicDataTrend();
    }

    @ApiOperation(value = "获取近十五天国内新增疫情数据")
    @GetMapping("getLatelyAddConfirmEpidemicData")
    public ResponseResult getLatelyAddConfirmEpidemicData(){
        return iEpidemicDataTrendService.getLatelyAddConfirmEpidemicData();
    }

    @ApiOperation(value = "获取近十五天内国内境外输入疫情数据")
    @GetMapping("getLatelyImportedCaseEpidemicData")
    public ResponseResult getLatelyImportedCaseEpidemicData(){
        return iEpidemicDataTrendService.getLatelyImportedCaseEpidemicData();
    }
}
