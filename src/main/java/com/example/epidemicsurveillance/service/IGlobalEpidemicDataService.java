package com.example.epidemicsurveillance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.epidemicsurveillance.entity.GlobalEpidemicData;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.epidemicsurveillance.entity.query.GlobalEpidemicDataQuery;
import com.example.epidemicsurveillance.response.ResponseResult;

/**
 * <p>
 * 全球疫情数据表 服务类
 * </p>
 *
 * @author zyf
 * @since 2021-10-12
 */
public interface IGlobalEpidemicDataService extends IService<GlobalEpidemicData> {

    void pageQuery(Page<GlobalEpidemicData> pageResult, GlobalEpidemicDataQuery globalEpidemicDataQuery);

    ResponseResult getCountry();

    ResponseResult getProvince(Integer countryId);

    ResponseResult getCity(Integer provinceId);
}