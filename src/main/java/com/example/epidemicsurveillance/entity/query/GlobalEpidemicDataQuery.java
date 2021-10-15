package com.example.epidemicsurveillance.entity.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName GlobalEpidemicDataQuery
 * @Author 朱云飞
 * @Date 2021/10/12 23:25
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalEpidemicDataQuery {

    @ApiModelProperty(value = "国家Id")
    private Integer countryId;

    @ApiModelProperty(value = "省份Id")
    private Integer provinceId;

    @ApiModelProperty(value = "城市Id")
    private Integer cityId;

    @ApiModelProperty(value = "查询开始时间", example = "2021-08-29 17:10:10")
    private String begin;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换

    @ApiModelProperty(value = "查询结束时间", example = "2021-08-29 17:10:10")
    private String end;
}
