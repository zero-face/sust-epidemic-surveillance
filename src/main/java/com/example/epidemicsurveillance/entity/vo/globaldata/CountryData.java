package com.example.epidemicsurveillance.entity.vo.globaldata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName GlobalData
 * @Author 朱云飞
 * @Date 2021/10/17 1:19
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryData {
    private Integer id;
    private String name;
    private List<ProvinceData> children;
}
