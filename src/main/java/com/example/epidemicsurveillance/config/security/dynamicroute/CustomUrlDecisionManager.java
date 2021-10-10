package com.example.epidemicsurveillance.config.security.dynamicroute;

import com.example.epidemicsurveillance.config.security.dynamicroute.CustomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * 权限控制
 * 判断用户角色
 * @ClassName CustomUrlDecisionManager
 * @Author 朱云飞
 * @Date 2021/6/17 17:20
 * @Version 1.0
 **/
@Component
public class CustomUrlDecisionManager implements AccessDecisionManager {
    @Autowired
    private CustomFilter customFilter;
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        for (ConfigAttribute configAttribute: collection) {
            // 当前url所需要的角色
            List<ConfigAttribute> list= (List<ConfigAttribute>) customFilter.getAttributes(o);
            String[] needRoles=new String[list.size()];
            for (int i = 0; i <list.size() ; i++) {
                needRoles[i]=list.get(i).getAttribute();
            }
            //判断角色是否登录即可访问的角色,此角色在CustomFilter中设置

            for (String needRole:needRoles) {
                if ("ROLE_LOGIN".equals((needRole))) {
                    //判断是否已经登录
                    if(authentication instanceof AnonymousAuthenticationToken){
                        throw new AccessDeniedException("尚未登录，请登录");
                    }else {
                        return;
                    }
                }
            }
            //判断用户角色是否为url所需要的角色
            //得到用户拥有的角色  这里在Admin类中已经将用户的角色放入了
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (String needRole:needRoles) {
                for (GrantedAuthority authority: authorities) {
                    if(authority.getAuthority().equals(needRole)){
                        return;
                    }
                }
            }
            throw new AccessDeniedException("权限不足，请联系管理员");
        }
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return false;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
