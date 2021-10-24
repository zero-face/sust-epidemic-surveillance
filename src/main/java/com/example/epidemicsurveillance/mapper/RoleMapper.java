package com.example.epidemicsurveillance.mapper;

import com.example.epidemicsurveillance.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author zyf
 * @since 2021-10-09
 */
@Component
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> getRoles(Integer adminId);


    List<Role> getRoleListByPermissionId(Integer permissionId);
}
