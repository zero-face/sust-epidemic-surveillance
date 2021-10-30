package com.example.epidemicsurveillance.entity.spider.china;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ChinaTotal
 * @Author 朱云飞
 * @Date 2021/10/30 10:56
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChinaTotal {
    /**
     * 确诊
     */
    private Integer confirm;
    /**
     * 治愈
     */
    private Integer heal;
    /**
     * 死亡
     */
    private Integer dead;
    /**
     * 现有确诊
     */
    private Integer nowConfirm;
    /**
     * 疑似
     */
    private Integer suspect;
    /**
     * 重症
     */
    private Integer nowSevere;
    /**
     * 无症状
     */
    private Integer noInfect;
    /**
     * 境外输入
     */
    private Integer importedCase;

}
