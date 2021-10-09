package com.example.epidemicsurveillance.service;

import com.example.epidemicsurveillance.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
