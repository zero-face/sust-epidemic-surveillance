package com.example.epidemicsurveillance.config.security;

import com.example.epidemicsurveillance.service.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;

/**
 * 权限控制
 * 根据Url分析请求所需的角色
 * @ClassName CustomFilter
 * @Author 朱云飞
 * @Date 2021/6/17 16:56
 * @Version 1.0
 **/
@Component
public class CustomFilter implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private IPermissionService permissionService;

    AntPathMatcher antPathMatcher=new AntPathMatcher();

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        //获取请求的URL
//        String requestUrl = ((FilterInvocation) o).getRequestUrl();
//        List<Permission> menus = permissionService.getMenuWithRole();
//        //将URL所需要的角色放入Menu中
//        for (Permission menu:menus) {
//            //判断请求Url与菜单角色拥有的url是否匹配
//            if(antPathMatcher.match(menu.getUrl(),requestUrl)){
//                // 该Url所需要的角色
//                String[] str = menu.getRoles().stream().map(Role::getRoleName).toArray(String[]::new);
//                //如果匹配上放入配置中,需要的角色
//                return SecurityConfig.createList(str);
//            }
//        }
//        //没匹配的url默认登录即可访问
        return SecurityConfig.createList("ROLE_LOGIN");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
