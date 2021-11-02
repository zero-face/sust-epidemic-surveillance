package com.example.epidemicsurveillance.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 疫情趋势表
 * </p>
 *
 * @author zyf
 * @since 2021-10-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="EpidemicDataTrend对象", description="疫情趋势表")
public class EpidemicDataTrend implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "地区名称")
    private String areaName;

    @ApiModelProperty(value = "新增确诊")
    private Integer existingDiagnosis;

    @ApiModelProperty(value = "新增无症状")
    private Integer asymptomatic;

    @ApiModelProperty(value = "新增疑似")
    private Integer suspected;

    @ApiModelProperty(value = "新增重症")
    private Integer severe;

    @ApiModelProperty(value = "新增累计确诊")
    private Integer totalDiagnosis;

    @ApiModelProperty(value = "新增境外输入")
    private Integer overseasInput;

    @ApiModelProperty(value = "新增累计治愈")
    private Integer totalCure;

    @ApiModelProperty(value = "新增累计死亡")
    private Integer totalDeath;

    @ApiModelProperty(value = "统计当日日期")
    private String today;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime gmtModified;


}
