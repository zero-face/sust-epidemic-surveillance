package com.example.epidemicsurveillance.config.security;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.epidemicsurveillance.response.ResponseResult;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author Zero
 * @Date 2021/10/9 23:52
 * @Since 1.8
 * @Description
 **/
@Component
public class CustomizeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {



    @SneakyThrows
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        //更新用户表上次登录时间、更新人、更新时间等字段
        //从security上下文中拿出用户信息
        org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", userDetails.getUsername());
//        org.zero.entity.User sysUser = userMapper.selectOne(wrapper);
        //更改数据库汇总用户信息
        /*if(null == sysUser) {
            throw new UsernameNotFoundException("用户不存在");
        }*/
        /*sysUser.setUpdateUser(sysUser.getId());
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("username", userDetails.getUsername());
        userMapper.update(sysUser,updateWrapper);*/
        //此处还可以进行一些处理，比如登录成功之后可能需要返回给前台当前用户有哪些菜单权限，
        //进而前台动态的控制菜单的显示等，具体根据自己的业务需求进行扩展
        //返回json数据
        ResponseResult result = ResponseResult.ok().message("登录成功");
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(result));
    }
}
