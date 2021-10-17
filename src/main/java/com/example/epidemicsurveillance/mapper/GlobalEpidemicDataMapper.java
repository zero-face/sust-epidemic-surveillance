package com.example.epidemicsurveillance.mapper;

import com.example.epidemicsurveillance.entity.GlobalEpidemicData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.epidemicsurveillance.entity.vo.globaldata.CityData;
import com.example.epidemicsurveillance.entity.vo.globaldata.CountryData;
import com.example.epidemicsurveillance.entity.vo.globaldata.ProvinceData;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 全球疫情数据表 Mapper 接口
 * </p>
 *
 * @author zyf
 * @since 2021-10-12
 */
@Component
public interface GlobalEpidemicDataMapper extends BaseMapper<GlobalEpidemicData> {

    List<CountryData> getAllCountryData();

    List<ProvinceData> getEveryCountryProvinceData(Integer countryId);

    List<CityData> getEveryProvinceCityData(Integer provinceId);
}
