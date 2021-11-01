package com.example.epidemicsurveillance.entity.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author Zero
 * @date 2021/10/23 20:29
 * @description
 * @since 1.8
 **/
@Data
@ToString
public class CityPolicyVO {
    private String id;

    /**
     * 地区编码
     */
    private String locationCode;

    private String destinationCode;

    /**
     * 服务电话
     */
    private List<CityPhoneVO> locationPhone;

    private List<CityPhoneVO> destinationPhone;

    /**
     * 省
     */
    private String locationProvince;

    private String destinationProvince;

    /**
     * 城市（直辖市到市级）
     */
    private String locationcity;

    private String destinationCity;

    private String locationTime;

    private String destinationTime;


    /**
     * 推送时间戳
     */
    private long sendTime;

    /**
     * 风险等级
     */
    private String locationLevelTag;

    private String destinationLevelTag;

    /**
     * 入境地政策
     */
    private String destinationPolicy;

    /**
     * 处境政策
     */
    private String locationPolicy;

    /**
     * 航空政策
     */
    private String destinationAviation;

    private String locationAviation;

    /**
     * 公路政策
     */
    private String locationHighway;

    private String destinationHighway;

    /**
     * 铁路交通政策
     */
    private String locationRailway;
    private String destinationRailway;

    /**
     * 水路政策
     */
    private String locationWaterway;
    private String destinationWaterway;

    /**
     * 消息提供者
     */
    private String locationProvider;
    private String destinationProvider;
}
