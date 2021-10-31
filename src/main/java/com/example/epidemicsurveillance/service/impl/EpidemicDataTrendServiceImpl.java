package com.example.epidemicsurveillance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.epidemicsurveillance.entity.EpidemicDataTrend;
import com.example.epidemicsurveillance.mapper.EpidemicDataTrendMapper;
import com.example.epidemicsurveillance.response.ResponseResult;
import com.example.epidemicsurveillance.service.IEpidemicDataTrendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 疫情趋势表 服务实现类
 * </p>
 *
 * @author zyf
 * @since 2021-10-30
 */
@Service
public class EpidemicDataTrendServiceImpl extends ServiceImpl<EpidemicDataTrendMapper, EpidemicDataTrend> implements IEpidemicDataTrendService {

    @Autowired
    private EpidemicDataTrendMapper epidemicDataTrendMapper;

    @Override
    public ResponseResult getTodayEpidemicDataTrend() {
        EpidemicDataTrend epidemicDataTrend=epidemicDataTrendMapper.getTodayEpidemicDataTrend();
        return ResponseResult.ok().data("chinaEpidemicAddData",epidemicDataTrend);
    }
}
