package com.example.epidemicsurveillance.service;

import com.example.epidemicsurveillance.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.epidemicsurveillance.entity.Role;

import java.util.List;

/**
 * <p>
 * 管理员表 服务类
 * </p>
 *
 * @author zyf
 * @since 2021-10-09
 */
public interface IAdminService extends IService<Admin> {

    Admin getAdminByUsername(String username);

    List<Role> getRoles(Integer id);
}
