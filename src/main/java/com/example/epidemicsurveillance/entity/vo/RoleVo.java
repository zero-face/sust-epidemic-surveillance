package com.example.epidemicsurveillance.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 后台授权角色返回对象
 * @ClassName RoleVo
 * @Author 朱云飞
 * @Date 2021/10/21 22:29
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleVo {
    private Integer key;
    private String label;
}
