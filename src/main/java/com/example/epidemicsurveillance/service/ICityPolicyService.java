package com.example.epidemicsurveillance.service;

import com.example.epidemicsurveillance.entity.CityPolicy;
import com.example.epidemicsurveillance.entity.CityServicePhone;
import com.example.epidemicsurveillance.entity.vo.CityPolicyVO;

import java.util.List;

/**
 * @author Zero
 * @date 2021/10/23 12:37
 * @description
 * @since 1.8
 **/
public interface ICityPolicyService {
    CityPolicy getCityPolicy(String city,String key);

    List<CityServicePhone> getCityServerPhone(String city, String key);

}
