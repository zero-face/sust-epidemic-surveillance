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

import java.time.LocalDateTime;

/**
 * @Author Zero
 * @Date 2021/10/16 13:37
 * @Since 1.8
 * @Description
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="userAuthr对象", description="实名认证表")
public class UserAuth {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户Id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "微信号")
    private String openid;

     @ApiModelProperty(value = "真实姓名")
    private String username;

     @ApiModelProperty(value = "手机号")
    private String phone;

     @ApiModelProperty(value = "学院id")
    private Integer collageId;

     @ApiModelProperty(value = "班级id")
    private Integer classId;

     @ApiModelProperty(value = "学号")
    private String number;

     @ApiModelProperty(value = "是否是负责人")
    private boolean isCharge;

     @ApiModelProperty(value = "身份证号")
    private String profileId;

    @ApiModelProperty(value = "0未删除,1已删除")
    @TableLogic
    private Integer deleted;

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
