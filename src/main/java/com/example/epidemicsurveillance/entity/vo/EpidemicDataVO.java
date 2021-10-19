package com.example.epidemicsurveillance.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author Zero
 * @Date 2021/10/17 13:38
 * @Since 1.8
 * @Description
 **/
@Data
public class EpidemicDataVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "地区名称")
    private String areaName;

    @ApiModelProperty(value = "现有确诊")
    private Integer existingDiagnosis;

    @ApiModelProperty(value = "无症状")
    private Integer asymptomatic;

    @ApiModelProperty(value = "疑似")
    private Integer suspected;

    @ApiModelProperty(value = "重症")
    private Integer severe;

    @ApiModelProperty(value = "累计确诊")
    private Integer totalDiagnosis;

    @ApiModelProperty(value = "境外输入")
    private Integer overseasInput;

    @ApiModelProperty(value = "累计治愈")
    private Integer totalCure;

    @ApiModelProperty(value = "累计死亡")
    private Integer totalDeath;
}
