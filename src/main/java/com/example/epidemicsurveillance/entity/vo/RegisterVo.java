package com.example.epidemicsurveillance.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName RegisterVo
 * @Author 朱云飞
 * @Date 2021/10/20 22:59
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterVo {
    /**
     * 用户名称
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 验证码
     */
    private String code;
    /**
     * 手机号
     */
    private String phone;
}
