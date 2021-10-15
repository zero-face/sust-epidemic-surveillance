package com.example.epidemicsurveillance.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class EmailVo implements Serializable {
    @ApiModelProperty(value = "邮件内容")
    private String emailMessage;

    @ApiModelProperty(value = "邮箱地址")
    private String adminEmail;
}