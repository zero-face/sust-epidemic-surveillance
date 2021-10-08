package com.example.epidemicsurveillance.impl;

import com.example.epidemicsurveillance.entity.Role;
import com.example.epidemicsurveillance.mapper.RoleMapper;
import com.example.epidemicsurveillance.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author zero
 * @since 2021-10-09
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
