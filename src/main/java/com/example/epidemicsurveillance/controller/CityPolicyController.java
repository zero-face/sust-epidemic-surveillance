package com.example.epidemicsurveillance.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.epidemicsurveillance.entity.CityPolicy;
import com.example.epidemicsurveillance.entity.CityServicePhone;
import com.example.epidemicsurveillance.entity.vo.CityPhoneVO;
import com.example.epidemicsurveillance.entity.vo.CityPolicyVO;
import com.example.epidemicsurveillance.exception.EpidemicException;
import com.example.epidemicsurveillance.response.ResponseResult;
import com.example.epidemicsurveillance.service.ICityPolicyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Zero
 * @date 2021/10/23 16:49
 * @description 出行指南
 * @since 1.8
 **/
@RestController
@RequestMapping("/api/v1/user/policy")
@Slf4j
@Validated
public class CityPolicyController {

    @Value("${gov.cityKey}")
    private String cityKey;

    @Value("${gov.phoneKey}")
    private String phoneKey;

    @Autowired
    private ICityPolicyService cityPolicyService;

    @GetMapping
    public ResponseResult getCityPolicy(@RequestParam("city")String location) {
        final CityPolicy cityPolicy = cityPolicyService.getCityPolicy(location,cityKey);
        final List<CityServicePhone> cityServerPhones = cityPolicyService.getCityServerPhone(location, phoneKey);
        if(cityPolicy == null || cityServerPhones==null) {
            throw new EpidemicException("生成出行计划失败");
        }
        final List<CityPhoneVO> cityPhoneVOS = cityServerPhones.stream().map(phone -> {
            final CityPhoneVO cityPhoneVO = new CityPhoneVO();
            BeanUtils.copyProperties(phone, cityPhoneVO);
            return cityPhoneVO;
        }).collect(Collectors.toList());

        final CityPolicyVO cityPolicyVO = new CityPolicyVO();
        BeanUtils.copyProperties(cityPolicy, cityPolicyVO);
        cityPolicyVO.setCityPhone(cityPhoneVOS);
        Map<String, Object> customPolicy = JSON.parseObject(JSON.toJSONString(cityPolicyVO), new TypeReference<Map<String, Object>>() {});
        log.info("返回的对象：{}",customPolicy );
        return ResponseResult.ok().message("本次出行查询成功").data(customPolicy);
    }
}
