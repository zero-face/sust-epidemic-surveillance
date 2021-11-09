package com.example.epidemicsurveillance.entity.vo.globaldata;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName WorldMapGlobalData
 * @Author 朱云飞
 * @Date 2021/11/7 11:41
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="WorldMapGlobalData", description="前端世界地图数据展示封装类")
public class WorldMapGlobalData {
    private String name;
    private Integer value;
}
