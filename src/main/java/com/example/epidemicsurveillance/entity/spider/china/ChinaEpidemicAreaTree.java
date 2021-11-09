package com.example.epidemicsurveillance.entity.spider.china;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName ChinaEpidemicAreaTree
 * @Author 朱云飞
 * @Date 2021/10/15 11:47
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChinaEpidemicAreaTree {
    /**
     * 国家
     */
    private String name;
    /**
     * 新增确诊人数
     */
    private ChinaEpidemicToday today;
    /**
     * 全国疫情数据每日统计
     */
    private ChinaEpidemicDayTotalData total;
    /**
     * 各省疫情数据统计
     */
    private List<ProvinceEpidemicAreaTree> children;
}
