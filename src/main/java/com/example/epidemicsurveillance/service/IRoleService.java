package com.example.epidemicsurveillance.service;

import com.example.epidemicsurveillance.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.epidemicsurveillance.response.ResponseResult;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author zyf
 * @since 2021-10-09
 */
public interface IRoleService extends IService<Role> {

    ResponseResult getAllRoles();

    ResponseResult getAllRolesWithAdmins();

    ResponseResult addRole(Role role);

    ResponseResult updateRole(Role role);

    ResponseResult getRoleById(Integer roleId);

    ResponseResult deleteRoleById(Integer roleId);
}
