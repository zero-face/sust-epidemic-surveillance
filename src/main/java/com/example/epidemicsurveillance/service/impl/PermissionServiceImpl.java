package com.example.epidemicsurveillance.service.impl;

import com.example.epidemicsurveillance.entity.Permission;
import com.example.epidemicsurveillance.mapper.PermissionMapper;
import com.example.epidemicsurveillance.service.IPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author zyf
 * @since 2021-10-09
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {

}
