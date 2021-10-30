package com.example.epidemicsurveillance.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.epidemicsurveillance.entity.CityPolicy;
import com.example.epidemicsurveillance.entity.CityServicePhone;
import com.example.epidemicsurveillance.entity.vo.CityPhoneVO;
import com.example.epidemicsurveillance.entity.vo.CityPolicyVO;
import com.example.epidemicsurveillance.exception.EpidemicException;
import com.example.epidemicsurveillance.service.ICityPolicyService;
import com.example.epidemicsurveillance.utils.citypolicy.PolicyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Zero
 * @date 2021/10/23 12:38
 * @description
 * @since 1.8
 **/
@Slf4j
@Service
public class CityPolicyServiceImpl implements ICityPolicyService {

    @Autowired
    private PolicyUtil policyUtil;

    @Override
    public CityPolicy getCityPolicy(String city, String key) {
        if(city == null || key == null) {
            throw new EpidemicException("参数错误");
        }
        final String body = policyUtil.GuideData(city, key);
        final JSONObject jsonObject = JSON.parseObject(body);
        final JSONObject data = jsonObject.getJSONObject("data");
        final String levelTag = data.getString("levelTag");
        final JSONArray list = data.getJSONArray("list");
        if(list.size() == 0) {
            return null;
        }
        final List<CityPolicy> cityPolicies = list.toJavaList(CityPolicy.class);
        final CityPolicy cityPolicy = cityPolicies.stream().findFirst().get();
        cityPolicy.setLevelTag(levelTag);
        log.info("地区防控政策对象：{}",cityPolicy);
//            final Update update = Update.update("sendTime", cityPolicy.getSendTime())
//                    .update("createTime", cityPolicy.getCreateTime())
//                    .update("levelTag", cityPolicy.getLevelTag())
//                    .update("comePolicy", cityPolicy.getComePolicy())
//                    .update("comePolicy", cityPolicy.getComePolicy())
//                    .update("leavePolicy", cityPolicy.getLeavePolicy())
//                    .update("aviation", cityPolicy.getAviation())
//                    .update("highway", cityPolicy.getHighway())
//                    .update("railway", cityPolicy.getRailway())
//                    .update("waterway", cityPolicy.getWaterway())
//                    .update("provider", cityPolicy.getProvider());
//            final UpdateResult upsert = mongoTemplate.upsert(new Query(Criteria.where("code").is(cityCode.getCode())), update, "city_policy");
//            System.out.println(upsert.wasAcknowledged());
//            if(upsert.wasAcknowledged()) {
//                log.info("更新{}的防控政策成功:{}",city,cityPolicy);
        return cityPolicy;
//            }
//            log.info("更新{}的防控政策失败",city);
//            return null;

    }

    @Override
    public List<CityServicePhone> getCityServerPhone(String city, String key) {
        if(city == null || key == null) {
            throw new EpidemicException("获取服务电话参数错误");
        }
        final String body = policyUtil.GuideData(city, key);
        final JSONObject jsonObject = JSON.parseObject(body);
        final JSONObject data = jsonObject.getJSONObject("data");
        final JSONArray list = data.getJSONArray("list");
        if(list.size() == 0) {
            return null;
        }
        final List<CityServicePhone> cityServicePhones = list.toJavaList(CityServicePhone.class);
        log.info("地区防服务电话策：{}",cityServicePhones);
        return cityServicePhones;
    }

    @Override
    public CityPolicyVO customizedTravelPlan(CityPolicy locationPolicy, CityPolicy destinationPolicy,List<CityServicePhone> locationPhone, List<CityServicePhone> destinationPhone) {
        if(locationPolicy == null || destinationPolicy == null) {
            return null;
        }
        final List<CityPhoneVO> locPhoneVOS = convertCityPhoneToPhoneVO(locationPhone);
        final List<CityPhoneVO> desPhoneVOS = convertCityPhoneToPhoneVO(destinationPhone);
        final CityPolicyVO customPlan = new CityPolicyVO();
        //服务电话
        customPlan.setLocationPhone(locPhoneVOS);
        customPlan.setDestinationPhone(desPhoneVOS);
        //城市
        customPlan.setDestinationCity(destinationPolicy.getCity());
        customPlan.setLocationcity(locationPolicy.getCity());
        //省份
        customPlan.setDestinationProvince(destinationPolicy.getProvince());
        customPlan.setLocationProvince(locationPolicy.getProvince());
        //风险等级
        customPlan.setLocationLevelTag(locationPolicy.getLevelTag());
        customPlan.setDestinationLevelTag(destinationPolicy.getLevelTag());
        //提供者
        customPlan.setLocationProvider(locationPolicy.getProvider());
        customPlan.setDestinationProvider(destinationPolicy.getProvider());
        //政策信息
        customPlan.setLocationPolicy(locationPolicy.getLeavePolicy());
        customPlan.setDestinationPolicy(destinationPolicy.getComePolicy());
        customPlan.setSendTime(locationPolicy.getSendTime() > destinationPolicy.getSendTime()? locationPolicy.getSendTime() : destinationPolicy.getSendTime());
        customPlan.setLocationCode(locationPolicy.getCode());
        customPlan.setDestinationCode(destinationPolicy.getCode());
        //航空信息
        customPlan.setDestinationAviation(destinationPolicy.getAviation());
        customPlan.setLocationAviation(locationPolicy.getAviation());

        //高铁信息
        customPlan.setLocationHighway(locationPolicy.getHighway());
        customPlan.setDestinationHighway(destinationPolicy.getHighway());

        //公路信息
        customPlan.setLocationRailway(locationPolicy.getRailway());
        customPlan.setDestinationRailway(destinationPolicy.getRailway());

        //水路信息
        customPlan.setLocationWaterway(locationPolicy.getWaterway());
        customPlan.setDestinationWaterway(destinationPolicy.getWaterway());

        return customPlan;
    }

    private List<CityPhoneVO> convertCityPhoneToPhoneVO(List<CityServicePhone> phoneList) {
        return phoneList.stream().map(phone -> {
            final CityPhoneVO cityPhoneVO = new CityPhoneVO();
            BeanUtils.copyProperties(phone, cityPhoneVO);
            return cityPhoneVO;
        }).collect(Collectors.toList());
    }
}
