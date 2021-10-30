package com.example.epidemicsurveillance.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Zero
 * @date 2021/10/25 23:53
 * @description
 * @since 1.8
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DomesticData {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 现有确诊
     */
    @ApiModelProperty(value = "创建时间")
    private Long existingConfirmed;

    /**
     * 无症状
     */
    @ApiModelProperty(value = "创建时间")
    private Long asymptomatic;

    /**
     * 现有疑似
     */
    @ApiModelProperty(value = "创建时间")
    private Long existingSuspected;

    /**
     * 现有重症
     */
    @ApiModelProperty(value = "创建时间")
    private Long existingSevere;

    /**
     * 累计确诊
     */
    @ApiModelProperty(value = "创建时间")
    private Long cumulativeConfirmed;

    /**
     * 镜外输入
     */
    @ApiModelProperty(value = "创建时间")
    private Long outsideInput;

    /**
     * 累计治愈
     */
    @ApiModelProperty(value = "创建时间")
    private Long cumulativeCure;

    /**
     * 累计死亡
     */
    @ApiModelProperty(value = "创建时间")
    private Long cumulativeDeath;

    /**
     * 是否删除
     */
    @TableLogic
    private Boolean isDelete;

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
