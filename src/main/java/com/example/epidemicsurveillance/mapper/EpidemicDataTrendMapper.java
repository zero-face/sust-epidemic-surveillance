package com.example.epidemicsurveillance.mapper;

import com.example.epidemicsurveillance.entity.EpidemicDataTrend;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 疫情趋势表 Mapper 接口
 * </p>
 *
 * @author zyf
 * @since 2021-10-30
 */
@Component
public interface EpidemicDataTrendMapper extends BaseMapper<EpidemicDataTrend> {

    EpidemicDataTrend getTodayEpidemicDataTrend();
}

