package com.example.epidemicsurveillance.entity.spider.global;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName GlobalTotalData
 * @Author 朱云飞
 * @Date 2021/10/14 14:36
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalTotalData implements Serializable {
    /**
     * 现有确诊人数
     */
    private Integer nowConfirm;
    /**
     * 现有新增确诊人数
     */
    private Integer nowConfirmAdd;
    /**
     * 总共确诊人数
     */
    private Integer confirm;
    /**
     * 新增确诊人数
     */
    private Integer confirmAdd;
    /**
     * 治愈人数
     */
    private Integer heal;
    /**
     * 新增治愈人数
     */
    private Integer healAdd;
    /**
     * 死亡人数
     */
    private Integer dead;
    /**
     * 新增死亡人数
     */
    private Integer deadAdd;
    /**
     * 治愈率
     */
    private Double curerate;
    /**
     * 死亡率
     */
    private Double deathrate;

}
