package com.example.epidemicsurveillance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.epidemicsurveillance.entity.Admin;
import com.example.epidemicsurveillance.entity.Role;
import com.example.epidemicsurveillance.exception.EpidemicException;
import com.example.epidemicsurveillance.mapper.AdminMapper;
import com.example.epidemicsurveillance.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author zyf
 * @since 2021-10-09
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin getAdminByUsername(String username) {
        if(StringUtils.isBlank(username)){
            throw new EpidemicException("用户名不能为空");
        }
        QueryWrapper<Admin> wrapper=new QueryWrapper<>();
        wrapper.eq("username",username);
        Admin admin=adminMapper.selectOne(wrapper);
        if(admin == null){
            throw new EpidemicException("该用户不存在");
        }
        return admin;
    }

    @Override
    public List<Role> getRoles(Integer adminId) {
        return adminMapper.getRoles(adminId);
    }
}
