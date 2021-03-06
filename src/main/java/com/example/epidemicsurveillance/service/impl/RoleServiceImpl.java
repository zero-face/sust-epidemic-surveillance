package com.example.epidemicsurveillance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.epidemicsurveillance.entity.Admin;
import com.example.epidemicsurveillance.entity.Role;
import com.example.epidemicsurveillance.entity.vo.RoleRenderVo;
import com.example.epidemicsurveillance.entity.vo.RoleVo;
import com.example.epidemicsurveillance.exception.EpidemicException;
import com.example.epidemicsurveillance.mapper.AdminMapper;
import com.example.epidemicsurveillance.mapper.RoleMapper;
import com.example.epidemicsurveillance.response.ResponseResult;
import com.example.epidemicsurveillance.service.IRoleService;
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

    @Autowired
    private AdminMapper adminMapper;

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

    @Override
    public ResponseResult getAllRolesWithAdmins() {
        List<Role> roleList=roleMapper.selectList(null);
        List<RoleRenderVo> list=new LinkedList<>();
        for (Role role: roleList) {
            RoleRenderVo roleRenderVo=new RoleRenderVo();
            roleRenderVo.setId(role.getId());
            roleRenderVo.setRoleName(role.getRoleName());
            List<Admin> adminList = adminMapper.getAdminByRoleId(role.getId());
            StringBuilder admins=new StringBuilder();
            for (int i = 0; i <adminList.size() ; i++) {
                if(i == 0){
                    admins.append(adminList.get(i).getUsername());
                }else {
                    admins.append(",").append(adminList.get(i).getUsername());
                }
            }
            roleRenderVo.setAdmins(admins.toString());
            list.add(roleRenderVo);
        }
        return ResponseResult.ok().data("list",list);
    }

    @Override
    public ResponseResult addRole(Role role) {
        if(role == null || role.getRoleName() == null){
            throw new EpidemicException("角色名称不能为空");
        }
        QueryWrapper<Role> wrapper=new QueryWrapper<>();
        wrapper.eq("role_name",role.getRoleName());
        Role role1 = roleMapper.selectOne(wrapper);
        if(role1 != null){
            throw new EpidemicException("该角色已经存在");
        }
        role.setCollageId(1);
        roleMapper.insert(role);
        return ResponseResult.ok().message("添加成功");
    }

    @Override
    public ResponseResult updateRole(Role role) {
        if(role == null || role.getRoleName() == null){
            throw new EpidemicException("角色名称不能为空");
        }
        QueryWrapper<Role> wrapper=new QueryWrapper<>();
        wrapper.eq("role_name",role.getRoleName());
        Role role1 = roleMapper.selectOne(wrapper);
        if(role1 != null && !role.getId().equals(role1.getId())){
            throw new EpidemicException("该角色已经存在");
        }
        roleMapper.updateById(role);
        return ResponseResult.ok().message("修改成功");
    }

    @Override
    public ResponseResult getRoleById(Integer roleId) {
        if(roleId == null){
            throw new EpidemicException("该角色信息不存在");
        }
        Role role=roleMapper.selectById(roleId);
        return ResponseResult.ok().data("role",role);
    }

    @Override
    public ResponseResult deleteRoleById(Integer roleId) {
        List<Admin> adminList=adminMapper.getAdminByRoleId(roleId);
        if(adminList.size() != 0){
            throw new EpidemicException("存在管理员分配到了该角色，无法删除");
        }
        roleMapper.deleteById(roleId);
        return ResponseResult.ok().message("删除成功");
    }
}
