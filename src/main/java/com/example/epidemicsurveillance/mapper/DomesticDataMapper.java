package com.example.epidemicsurveillance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.epidemicsurveillance.entity.DomesticData;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Zero
 * @date 2021/10/26 0:30
 * @description
 * @since 1.8
 **/
@Mapper
public interface DomesticDataMapper extends BaseMapper<DomesticData> {

    DomesticData getTodayData();

    DomesticData getYesterdayData();
}
