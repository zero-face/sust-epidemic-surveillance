package com.example.epidemicsurveillance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.epidemicsurveillance.entity.DomesticData;
import com.example.epidemicsurveillance.mapper.DomesticDataMapper;
import com.example.epidemicsurveillance.service.IDomesticDataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Zero
 * @date 2021/10/26 0:32
 * @description
 * @since 1.8
 **/
@Service
public class DomesticDataServiceImpl extends ServiceImpl<DomesticDataMapper, DomesticData> implements IDomesticDataService {
    @Resource
    private DomesticDataMapper domesticDataMapper;

    @Override
    public DomesticData getTodayData() {
        return domesticDataMapper.getTodayData();
    }

    @Override
    public DomesticData getYesterdayData() {
        return domesticDataMapper.getYesterdayData();
    }
}
