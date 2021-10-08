package com.example.epidemicsurveillance.impl;

import com.example.epidemicsurveillance.entity.User;
import com.example.epidemicsurveillance.mapper.UserMapper;
import com.example.epidemicsurveillance.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 普通用户表 服务实现类
 * </p>
 *
 * @author zero
 * @since 2021-10-09
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
