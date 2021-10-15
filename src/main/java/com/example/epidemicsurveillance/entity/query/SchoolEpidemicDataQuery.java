package com.example.epidemicsurveillance.entity.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName SchoolEpidemicDataQuery
 * @Author 朱云飞
 * @Date 2021/10/13 14:58
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolEpidemicDataQuery {
    @ApiModelProperty(value = "学校Id")
    private Integer schoolId;

    @ApiModelProperty(value = "学院Id")
    private Integer collageId;

    @ApiModelProperty(value = "查询开始时间", example = "2021-08-29 17:10:10")
    private String begin;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换

    @ApiModelProperty(value = "查询结束时间", example = "2021-08-29 17:10:10")
    private String end;
}
