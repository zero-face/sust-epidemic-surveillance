package com.example.epidemicsurveillance.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Zero
 * @date 2021/10/23 1:12
 * @description
 * @since 1.8
 **/
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CityPolicy {
    private String id;

    /**
     * 地区编码
     */
    private String code;

    /**
     * 省
     */
    private String province;

    /**
     * 城市（直辖市到市级）
     */
    private String city;

    /**
     * 创建时间
     */
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
     * 入境政策
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
