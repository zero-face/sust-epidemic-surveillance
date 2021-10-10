package com.example.epidemicsurveillance.service;

import com.example.epidemicsurveillance.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 普通用户表 服务类
 * </p>
 *
 * @author zyf
 * @since 2021-10-09
 */
public interface IUserService extends IService<User> {
    String getOpenIdByCode(String url, Map<String,String> map);
}
