package com.example.epidemicsurveillance.entity;

import com.baomidou.mybatisplus.annotation.*;
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
 * 全球疫情数据表
 * </p>
 *
 * @author zyf
 * @since 2021-10-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="GlobalEpidemicData对象", description="全球疫情数据表")
public class GlobalEpidemicData implements Serializable {

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

    @ApiModelProperty(value = "父id")
    private Integer parentId;

    @ApiModelProperty(value = "0未删除,1已删除")
    @TableLogic
    private Integer isDelete;

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
