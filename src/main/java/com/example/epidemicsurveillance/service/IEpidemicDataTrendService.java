package com.example.epidemicsurveillance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.epidemicsurveillance.entity.EpidemicDataTrend;
import com.example.epidemicsurveillance.response.ResponseResult;

/**
 * <p>
 * 疫情趋势表 服务类
 * </p>
 *
 * @author zyf
 * @since 2021-10-30
 */
public interface IEpidemicDataTrendService extends IService<EpidemicDataTrend> {

    ResponseResult getTodayEpidemicDataTrend();
}
