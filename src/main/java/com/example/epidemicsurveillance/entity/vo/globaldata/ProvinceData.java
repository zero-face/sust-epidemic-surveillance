package com.example.epidemicsurveillance.entity.vo.globaldata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName ProvinceData
 * @Author 朱云飞
 * @Date 2021/10/17 1:21
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProvinceData {
    private Integer id;
    private String name;
    private List<CityData> children;
}
