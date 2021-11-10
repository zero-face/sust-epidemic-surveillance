package com.example.epidemicsurveillance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.epidemicsurveillance.entity.EpidemicDataToday;
import com.example.epidemicsurveillance.response.ResponseResult;

/**
 * <p>
 * 今日疫情新增表 服务类
 * </p>
 *
 * @author zyf
 * @since 2021-11-06
 */
public interface IEpidemicDataTodayService extends IService<EpidemicDataToday> {

    ResponseResult getAllProvinceEpidemicDataTodayAddForWXFrom();
}
