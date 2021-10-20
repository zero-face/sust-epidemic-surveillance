package com.example.epidemicsurveillance.entity.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ArticleDataQuery
 * @Author 朱云飞
 * @Date 2021/10/19 13:47
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDataQuery {
    @ApiModelProperty(value = "文章标题，模糊查询")
    private String title;

    @ApiModelProperty(value = "文章类型")
    private Integer type;

    @ApiModelProperty(value = "查询开始时间", example = "2021-08-29 17:10:10")
    private String begin;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换

    @ApiModelProperty(value = "查询结束时间", example = "2021-08-29 17:10:10")
    private String end;
}
