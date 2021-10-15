package com.example.epidemicsurveillance.entity.spider.china;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName CityEpidemicAreaTree
 * @Author 朱云飞
 * @Date 2021/10/15 14:18
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityEpidemicAreaTree {
    /**
     * 地区
     */
    private String name;
    /**
     * 各市每日疫情数据统计
     */
    private CityEpidemicDayTotal total;
}
