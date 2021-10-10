package com.example.epidemicsurveillance.config.security.service;

import com.example.epidemicsurveillance.entity.Admin;
import com.example.epidemicsurveillance.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @Author Zero
 * @Date 2021/10/10 13:22
 * @Since 1.8
 * @Description
 **/
@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private IAdminService adminService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin=adminService.getAdminByUsername(username);
        if(admin != null){
            admin.setRoles(adminService.getRoles(admin.getId()));
            return admin;
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }
}
