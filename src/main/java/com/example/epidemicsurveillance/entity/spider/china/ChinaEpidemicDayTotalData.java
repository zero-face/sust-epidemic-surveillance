package com.example.epidemicsurveillance.entity.spider.china;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ChinaEpidemicDayTotalData
 * @Author 朱云飞
 * @Date 2021/10/15 14:05
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChinaEpidemicDayTotalData {
    /**
     * 现有确诊
     */
    private Integer nowConfirm;
    /**
     * 累计确诊
     */
    private Integer confirm;
    /**
     * 疑似
     */
    private Integer suspect;
    /**
     * 累计死亡
     */
    private Integer dead;
    /**
     * 累计治愈
     */
    private Integer heal;
}
