package com.example.epidemicsurveillance.service.impl;

import com.example.epidemicsurveillance.entity.Role;
import com.example.epidemicsurveillance.entity.vo.RoleVo;
import com.example.epidemicsurveillance.mapper.RoleMapper;
import com.example.epidemicsurveillance.response.ResponseResult;
import com.example.epidemicsurveillance.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author zyf
 * @since 2021-10-09
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public ResponseResult getAllRoles() {
        List<Role> roleList = roleMapper.selectList(null);
        List<RoleVo> list=new LinkedList<>();
        for (Role role: roleList) {
            RoleVo roleVo=new RoleVo();
            roleVo.setKey(role.getId());
            roleVo.setLabel(role.getRoleName());
            list.add(roleVo);
        }
        return ResponseResult.ok().data("list",list);
    }
}
