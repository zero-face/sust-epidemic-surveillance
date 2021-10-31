package com.example.epidemicsurveillance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.epidemicsurveillance.entity.GlobalEpidemicData;
import com.example.epidemicsurveillance.entity.query.GlobalEpidemicDataQuery;
import com.example.epidemicsurveillance.entity.vo.globaldata.ChinaMapGlobalData;
import com.example.epidemicsurveillance.entity.vo.globaldata.CityData;
import com.example.epidemicsurveillance.entity.vo.globaldata.CountryData;
import com.example.epidemicsurveillance.entity.vo.globaldata.ProvinceData;
import com.example.epidemicsurveillance.mapper.GlobalEpidemicDataMapper;
import com.example.epidemicsurveillance.response.ResponseResult;
import com.example.epidemicsurveillance.service.IGlobalEpidemicDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

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

    @Autowired
    private GlobalEpidemicDataMapper globalEpidemicDataMapper;

    @Override
    public void pageQuery(Page<GlobalEpidemicData> pageResult, GlobalEpidemicDataQuery globalEpidemicDataQuery) {
        if(globalEpidemicDataQuery == null){
            globalEpidemicDataMapper.selectPage(pageResult, null);
            return;
        }
        Integer countryId = globalEpidemicDataQuery.getCountryId();
        Integer provinceId = globalEpidemicDataQuery.getProvinceId();
        Integer cityId = globalEpidemicDataQuery.getCityId();
        String begin = globalEpidemicDataQuery.getBegin();
        String end = globalEpidemicDataQuery.getEnd();
        QueryWrapper<GlobalEpidemicData> wrapper=new QueryWrapper<>();
        if (!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin);
        }

        if (!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create", end);
        }
        if(cityId != 0){
            wrapper.eq("id",cityId);
        }else {
            if(provinceId != 0){
                wrapper.eq("id",provinceId);
            }else {
                if(countryId != 0){
                    wrapper.eq("id",countryId);
                }
            }
        }
        globalEpidemicDataMapper.selectPage(pageResult, wrapper);
    }

    //查询所有国家：id!=1 && parent_id=1
    @Override
    public ResponseResult getCountry() {
        QueryWrapper<GlobalEpidemicData> wrapper=new QueryWrapper<>();
        wrapper.eq("parent_id",1);
        wrapper.ne("id",1);
        List<GlobalEpidemicData> countryList = globalEpidemicDataMapper.selectList(wrapper);
        return ResponseResult.ok().data("countryList",countryList);
    }

    @Override
    public ResponseResult getProvince(Integer countryId) {
        QueryWrapper<GlobalEpidemicData> wrapper=new QueryWrapper<>();
        wrapper.eq("parent_id",countryId);
        List<GlobalEpidemicData> provinceList = globalEpidemicDataMapper.selectList(wrapper);
        return ResponseResult.ok().data("provinceList",provinceList);
    }

    @Override
    public ResponseResult getCity(Integer provinceId) {
        QueryWrapper<GlobalEpidemicData> wrapper=new QueryWrapper<>();
        wrapper.eq("parent_id",provinceId);
        List<GlobalEpidemicData> cityList = globalEpidemicDataMapper.selectList(wrapper);
        return ResponseResult.ok().data("cityList",cityList);
    }

    @Override
    public List<CountryData> getAllCountryData() {
        return globalEpidemicDataMapper.getAllCountryData();
    }

    @Override
    public List<ProvinceData> getEveryCountryProvinceData(Integer countryId) {
        return globalEpidemicDataMapper.getEveryCountryProvinceData(countryId);
    }


    @Override
    public List<CityData> getEveryProvinceCityData(Integer provinceId) {
        return globalEpidemicDataMapper.getEveryProvinceCityData(provinceId);
    }

    @Override
    public ResponseResult getChinaEpidemicTotalData() {
        GlobalEpidemicData chinaEpidemicTotalData = globalEpidemicDataMapper.selectById(2);
        return ResponseResult.ok().data("chinaEpidemicTotalData",chinaEpidemicTotalData);
    }

    @Override
    public ResponseResult getAllProvinceEpidemicData() {
        QueryWrapper<GlobalEpidemicData> wrapper=new QueryWrapper<>();
        wrapper.eq("parent_id",2);
        List<GlobalEpidemicData> globalEpidemicDatas = globalEpidemicDataMapper.selectList(wrapper);
        List<ChinaMapGlobalData> dataList=new LinkedList<>();
        for (GlobalEpidemicData globalEpidemicData:globalEpidemicDatas) {
            ChinaMapGlobalData chinaMapGlobalData=new ChinaMapGlobalData();
            chinaMapGlobalData.setName(globalEpidemicData.getAreaName());
            chinaMapGlobalData.setValue(globalEpidemicData.getTotalDiagnosis());
            dataList.add(chinaMapGlobalData);
        }
        
        return ResponseResult.ok().data("list",dataList);
    }
}
