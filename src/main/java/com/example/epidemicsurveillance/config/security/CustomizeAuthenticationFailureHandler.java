package com.example.epidemicsurveillance.config.security;

import com.alibaba.fastjson.JSON;
import com.example.epidemicsurveillance.response.ResponseResult;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.www.NonceExpiredException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author Zero
 * @Date 2021/10/9 23:48
 * @Since 1.8
 * @Description
 **/
@Component
public class CustomizeAuthenticationFailureHandler implements AuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        //返回json数据
        ResponseResult result = null;
        if (e instanceof AccountExpiredException) {
            //账号过期
            result = ResponseResult.error().message("账号过期");
        } else if (e instanceof InternalAuthenticationServiceException) {
            //密码错误
            result = ResponseResult.error().message("用户不存在");
        } else if(e instanceof BadCredentialsException) {
            //用户不存在
            result = ResponseResult.error().message(e.getMessage());
        } else if (e instanceof CredentialsExpiredException) {
            //密码过期
            result = ResponseResult.error().message("密码过期");
        } else if (e instanceof DisabledException) {
            //账号不可用
            result = ResponseResult.error().message("账号被禁用");
        } else if (e instanceof LockedException) {
            //账号锁定
            result = ResponseResult.error().message("账号锁定");
        } else if(e instanceof NonceExpiredException) {
            //异地登录
            result = ResponseResult.error().message("异地登录");
        } else if(e instanceof SessionAuthenticationException) {
            //session异常
            result = ResponseResult.error().message("session错误");
        } else {
            //其他未知异常
            result = ResponseResult.error().message(e.getMessage());
        }
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(result));
    }


}