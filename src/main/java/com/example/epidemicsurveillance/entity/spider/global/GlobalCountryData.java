package com.example.epidemicsurveillance.entity.spider.global;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName GlobalCountryData
 * @Author 朱云飞
 * @Date 2021/10/14 14:22
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalCountryData implements Serializable {
    /**
     * 地区
     */
    private String continent;
    /**
     * 国家
     */
    private String name;
    /**
     * 确诊人数
     */
    private Integer confirm;
    /**
     * 新增确诊人数
     */
    private Integer confirmAdd;
    /**
     * 死亡人数
     */
    private Integer dead;
    /**
     * 新增死亡
     */
    private Integer deadCompare;
    /**
     * 治愈人数
     */
    private Integer heal;
    /**
     * 新增治愈人数
     */
    private Integer healCompare;
    /**
     * 数据日期
     */
    private String pub_date;
    /**
     * 现有确诊人数
     */
    private Integer nowConfirm;
}
