package com.example.epidemicsurveillance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.epidemicsurveillance.entity.EpidemicDataTrend;
import com.example.epidemicsurveillance.entity.GlobalEpidemicData;
import com.example.epidemicsurveillance.entity.query.GlobalEpidemicDataQuery;
import com.example.epidemicsurveillance.entity.vo.WxTotalFromVo;
import com.example.epidemicsurveillance.entity.vo.globaldata.*;
import com.example.epidemicsurveillance.mapper.EpidemicDataTrendMapper;
import com.example.epidemicsurveillance.mapper.GlobalEpidemicDataMapper;
import com.example.epidemicsurveillance.response.ResponseResult;
import com.example.epidemicsurveillance.service.IGlobalEpidemicDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
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

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private EpidemicDataTrendMapper epidemicDataTrendMapper;

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

    @Override
    public ResponseResult getAllProvinceNowConfirmData() {
        QueryWrapper<GlobalEpidemicData> wrapper=new QueryWrapper<>();
        wrapper.eq("parent_id",2);
        List<GlobalEpidemicData> globalEpidemicDatas = globalEpidemicDataMapper.selectList(wrapper);
        List<ChinaMapGlobalData> dataList=new LinkedList<>();
        for (GlobalEpidemicData globalEpidemicData:globalEpidemicDatas) {
            ChinaMapGlobalData chinaMapGlobalData=new ChinaMapGlobalData();
            chinaMapGlobalData.setName(globalEpidemicData.getAreaName());
            chinaMapGlobalData.setValue(globalEpidemicData.getExistingDiagnosis());
            dataList.add(chinaMapGlobalData);
        }
        return ResponseResult.ok().data("list",dataList);
    }

    @Override
    public ResponseResult getAllProvinceEpidemicDataForWXFrom() {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        WxTotalFromVo wxtable = (WxTotalFromVo)valueOperations.get("wxEpidemicTotalDataTable");
        return ResponseResult.ok().data("result",wxtable);
    }

    @Override
    public ResponseResult getAllCountryEpidemic() {
        QueryWrapper<GlobalEpidemicData> wrapper=new QueryWrapper<>();
        wrapper.eq("parent_id",1);
        wrapper.ne("id",1);
        List<GlobalEpidemicData> globalEpidemicDatas = globalEpidemicDataMapper.selectList(wrapper);
        List<WorldMapGlobalData> list=new LinkedList<>();
        for(GlobalEpidemicData globalEpidemicData:globalEpidemicDatas){
            WorldMapGlobalData worldMapGlobalData=new WorldMapGlobalData();
            worldMapGlobalData.setName(globalEpidemicData.getAreaName());
            worldMapGlobalData.setValue(globalEpidemicData.getTotalDiagnosis());
            list.add(worldMapGlobalData);
        }
        return ResponseResult.ok().data("list",list);
    }

    @Override
    public ResponseResult getAllCountryNowConfirmEpidemicData() {
        QueryWrapper<GlobalEpidemicData> wrapper=new QueryWrapper<>();
        wrapper.eq("parent_id",1);
        wrapper.ne("id",1);
        List<GlobalEpidemicData> globalEpidemicDatas = globalEpidemicDataMapper.selectList(wrapper);
        List<WorldMapGlobalData> list=new LinkedList<>();
        for(GlobalEpidemicData globalEpidemicData:globalEpidemicDatas){
            WorldMapGlobalData worldMapGlobalData=new WorldMapGlobalData();
            worldMapGlobalData.setName(globalEpidemicData.getAreaName());
            worldMapGlobalData.setValue(globalEpidemicData.getExistingDiagnosis());
            list.add(worldMapGlobalData);
        }
        return ResponseResult.ok().data("list",list);
    }

    @Override
    public ResponseResult getWorldEpidemicDataAndTodayAddEpidemicData() {
        QueryWrapper<GlobalEpidemicData> wrapper=new QueryWrapper<>();
        wrapper.eq("area_name","全球");
        GlobalEpidemicData worldEpidemicTotalData=globalEpidemicDataMapper.selectOne(wrapper);
        EpidemicDataTrend  worldEpidemicAddData=epidemicDataTrendMapper.getWorldTodayEpidemicAddData();
        return ResponseResult.ok().data("worldEpidemicTotalData",worldEpidemicTotalData)
                .data("worldEpidemicAddData",worldEpidemicAddData);
    }

    @Override
    public ResponseResult getEpidemicDataTopTenCountry(Integer type) {
        List<String> xdata=new LinkedList<>();
        List<Integer> ydata=new LinkedList<>();
        String barTitle=null;
        List<GlobalEpidemicData> list=null;
        if (type == 1){//累计确诊Top10
            list=globalEpidemicDataMapper.getEpidemicConfirmDataTopTenCountry();
            for (GlobalEpidemicData data:list){
                xdata.add(data.getAreaName());
                ydata.add(data.getTotalDiagnosis());
            }
            barTitle="累计确诊Top10";
        }else if(type == 2){//现有确诊Top10
            list=globalEpidemicDataMapper.getEpidemicNowConfirmDataTopTenCountry();
            for (GlobalEpidemicData data:list){
                xdata.add(data.getAreaName());
                ydata.add(data.getExistingDiagnosis());
            }
            barTitle="现有确诊Top10";
        }else if(type == 3){//新增确诊Top10
            list=globalEpidemicDataMapper.getEpidemicAddConfirmDataTopTenCountry();
            for (GlobalEpidemicData data:list){
                xdata.add(data.getAreaName());
                ydata.add(data.getAddConfirm());
            }
            barTitle="新增确诊Top10";
        }else if(type == 4){
            list=globalEpidemicDataMapper.getEpidemicDeadDataTopTenCountry();
            for (GlobalEpidemicData data:list){
                xdata.add(data.getAreaName());
                ydata.add(data.getTotalDeath());
            }
            barTitle="累计死亡Top10";
        }else if (type == 5){
            list=globalEpidemicDataMapper.getEpidemicHealDataTopTenCountry();
            for (GlobalEpidemicData data:list){
                xdata.add(data.getAreaName());
                ydata.add(data.getTotalCure());
            }
            barTitle="累计治愈Top10";
        }
        return ResponseResult.ok().data("xdata",xdata).data("ydata",ydata).data("barTitle",barTitle);
    }

    @Override
    public ResponseResult getChinaAndWorldAddConfirmTrend() {
        List<String> xdata=new LinkedList<>();
        List<Integer> ydata1=new LinkedList<>();
        List<Integer> ydata2=new LinkedList<>();
        List<EpidemicDataTrend> list=epidemicDataTrendMapper.getChinaAndWorldAddConfirmTrend();
        for (EpidemicDataTrend trend : list){
            if(trend.getAreaName().equals("中国")){
                ydata1.add(trend.getExistingDiagnosis());
                xdata.add(trend.getToday());
            }else {
                    ydata2.add(trend.getExistingDiagnosis());
            }
        }
        return ResponseResult.ok().data("xdata",xdata).data("ydata1",ydata1).data("ydata2",ydata2).data("lineTitle","国内/国外新增确诊 趋势");
    }

    @Override
    public ResponseResult getAllCountryEpidemicDataForWXFrom() {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        WxTotalFromVo result=(WxTotalFromVo)valueOperations.get("allCountryDataForWxFrom");
        return ResponseResult.ok().data("result",result);
    }
}
