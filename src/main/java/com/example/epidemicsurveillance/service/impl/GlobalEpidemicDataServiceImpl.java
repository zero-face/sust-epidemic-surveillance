package com.example.epidemicsurveillance.service.impl;

import com.example.epidemicsurveillance.entity.GlobalEpidemicData;
import com.example.epidemicsurveillance.mapper.GlobalEpidemicDataMapper;
import com.example.epidemicsurveillance.service.IGlobalEpidemicDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 全球疫情数据表 服务实现类
 * </p>
 *
 * @author zyf
 * @since 2021-10-12
 */
@Service
public class GlobalEpidemicDataServiceImpl extends ServiceImpl<GlobalEpidemicDataMapper, GlobalEpidemicData> implements IGlobalEpidemicDataService {

}
