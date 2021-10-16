package com.example.epidemicsurveillance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.epidemicsurveillance.entity.UserAuth;
import com.example.epidemicsurveillance.mapper.UserAuthMapper;
import com.example.epidemicsurveillance.service.IUserAuthService;
import org.springframework.stereotype.Service;

/**
 * @Author Zero
 * @Date 2021/10/16 14:02
 * @Since 1.8
 * @Description
 **/
@Service
public class UserAuthServiceImpl extends ServiceImpl<UserAuthMapper, UserAuth> implements IUserAuthService {
}
