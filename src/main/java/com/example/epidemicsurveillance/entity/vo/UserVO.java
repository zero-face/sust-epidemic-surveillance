package com.example.epidemicsurveillance.entity.vo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

/**
 * @Author Zero
 * @Date 2021/10/10 21:05
 * @Since 1.8
 * @Description
 **/
public class UserVO {
    @ApiModelProperty(value = "用户Id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "国家")
    private String country;

    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "性别 0男 1女")
    private Integer sex;

    @ApiModelProperty(value = "头像地址")
    private String avator;

    @ApiModelProperty(value = "是否是负责人 0不是,1是")
    private Integer isCharge;

    @ApiModelProperty(value = "班级Id")
    private Integer classId;
}
