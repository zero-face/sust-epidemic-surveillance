package com.example.epidemicsurveillance.entity.vo.globaldata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName AllGlobalData
 * @Author 朱云飞
 * @Date 2021/10/17 1:18
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllGlobalData {
    private List<CountryData> globalDataList;
}
