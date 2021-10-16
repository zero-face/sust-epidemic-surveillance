package com.example.epidemicsurveillance.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author Zero
 * @Date 2021/10/16 13:46
 * @Since 1.8
 * @Description
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="UserAuthVO对象", description="实名返回对象")
public class UserAuthVO {
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
    private String collage;

    @ApiModelProperty(value = "班级id")
    private String clazz;

    @ApiModelProperty(value = "学号")
    private String number;

    @ApiModelProperty(value = "是否是负责人")
    private boolean isCharge;

    @ApiModelProperty(value = "身份证号")
    private String profileId;
}
