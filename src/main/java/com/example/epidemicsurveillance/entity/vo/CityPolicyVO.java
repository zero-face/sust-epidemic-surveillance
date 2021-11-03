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
    private String code;

    /**
     * 服务电话
     */
    private List<CityPhoneVO> cityPhone;


    /**
     * 省
     */
    private String province;


    /**
     * 城市（直辖市到市级）
     */
    private String city;


    private String createTime;


    /**
     * 推送时间戳
     */
    private long sendTime;

    /**
     * 风险等级
     */
    private String levelTag;


    /**
     * 入境地政策
     */
    private String comePolicy;

    /**
     * 处境政策
     */
    private String leavePolicy;

    /**
     * 航空政策
     */

    private String aviation;

    /**
     * 公路政策
     */
    private String highway;

    /**
     * 铁路交通政策
     */
    private String railway;

    /**
     * 水路政策
     */
    private String waterway;
    /**
     * 消息提供者
     */
    private String provider;
}
