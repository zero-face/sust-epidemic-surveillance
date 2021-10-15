package com.example.epidemicsurveillance.entity.spider.global;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName GlobalEpidemicRequireData
 * @Author 朱云飞
 * @Date 2021/10/14 14:20
 * @Version 1.0
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GlobalEpidemicRequireData implements Serializable {
    private String FAutoCountryConfirmAdd;
    //各国数据
    private List<GlobalCountryData> WomAboard;
    //世界数据
    private GlobalTotalData  WomWorld;
}
