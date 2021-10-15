package com.example.epidemicsurveillance.entity.spider.china;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName ProvinceEpidemic
 * @Author 朱云飞
 * @Date 2021/10/15 14:09
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvinceEpidemicAreaTree {
    /**
     * 地区名称
     */
    private String name;
    /**
     * 全省疫情数据每日统计
     */
    private ProvinceEpidemicDayTotalData total;
    /**
     * 各市区疫情数据每日统计
     */
    private List<CityEpidemicAreaTree> children;
}
