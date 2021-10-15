package com.example.epidemicsurveillance.mapper;

import com.example.epidemicsurveillance.entity.SchoolEpidemicData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 校园疫情数据表 Mapper 接口
 * </p>
 *
 * @author zyf
 * @since 2021-10-12
 */
@Component
public interface SchoolEpidemicDataMapper extends BaseMapper<SchoolEpidemicData> {

    List<SchoolEpidemicData> getSchool();
}
