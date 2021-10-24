package com.example.epidemicsurveillance.service;

import com.example.epidemicsurveillance.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.epidemicsurveillance.response.ResponseResult;

import java.util.List;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author zyf
 * @since 2021-10-09
 */
public interface IPermissionService extends IService<Permission> {

    List<Permission> getMenuWithRole();

    ResponseResult getPermissionTree();

    ResponseResult getPermissionTreeByRoleId(Integer roleId);

    ResponseResult getPermissionListById(Integer permissionId);

    ResponseResult addPermission(Integer permissionId, Permission permission);

    ResponseResult getPermissionById(Integer permissionId);

    ResponseResult updatePermission(Permission permission);

    ResponseResult deletePermissionById(Integer permissionId);

    ResponseResult getRoleNotOwnedPermissionByRoleId(Integer roleId);
}
