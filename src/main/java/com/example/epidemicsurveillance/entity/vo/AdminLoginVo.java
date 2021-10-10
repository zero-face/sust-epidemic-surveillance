package com.example.epidemicsurveillance.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName AdminLoginVo
 * @Author 朱云飞
 * @Date 2021/10/10 12:23
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminLoginVo {
    private String username;
    private String password;
    /**
     * 图形验证码
     */
    private String kaptchaCode;
    /**
     * 图形验证码的唯一标识
     */
    private String kaptchaCodeId;
}
