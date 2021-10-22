package com.example.epidemicsurveillance.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *后台角色管理角色列表渲染对象
 *
 * @ClassName RoleRenderVo
 * @Author 朱云飞
 * @Date 2021/10/22 19:16
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleRenderVo {
    private Integer id;
    private String roleName;
    private String admins;
    private String permissions;
}
