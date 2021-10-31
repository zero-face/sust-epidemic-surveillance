package com.example.epidemicsurveillance.entity.vo.globaldata;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ChinaMapGlobalData
 * @Author 朱云飞
 * @Date 2021/10/30 23:36
 * @Version 1.0
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(value="ChinaMapGlobalData对象", description="前端地图数据展示封装类")
public class ChinaMapGlobalData {
    private String name;
    private Integer value;
}
