package com.example.epidemicsurveillance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.epidemicsurveillance.entity.Permission;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 *
 * @author zyf
 * @since 2021-10-09
 */
@Component
public interface PermissionMapper extends BaseMapper<Permission> {

    List<Permission> getMenuWithRole();

}
