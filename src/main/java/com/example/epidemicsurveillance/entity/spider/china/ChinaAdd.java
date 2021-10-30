package com.example.epidemicsurveillance.entity.spider.china;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ChinaAdd
 * @Author 朱云飞
 * @Date 2021/10/30 11:04
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChinaAdd {
    /**
     * 新增累计确诊
     */
    private Integer confirm;
    /**
     * 新增治愈
     */
    private Integer heal;
    /**
     * 新增死亡
     */
    private Integer dead;
    /**
     * 新增确诊
     */
    private Integer nowConfirm;
    /**
     * 新增疑似
     */
    private Integer suspect;
    /**
     * 新增重症
     */
    private Integer nowSevere;
    /**
     * 新增境外输入
     */
    private Integer importedCase;
    /**
     * 新增无症状
     */
    private Integer noInfect;
}
