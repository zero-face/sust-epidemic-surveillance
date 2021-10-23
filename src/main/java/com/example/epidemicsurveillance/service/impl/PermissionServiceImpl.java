package com.example.epidemicsurveillance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.epidemicsurveillance.entity.Admin;
import com.example.epidemicsurveillance.entity.Permission;
import com.example.epidemicsurveillance.entity.Role;
import com.example.epidemicsurveillance.mapper.AdminMapper;
import com.example.epidemicsurveillance.mapper.PermissionMapper;
import com.example.epidemicsurveillance.mapper.RoleMapper;
import com.example.epidemicsurveillance.response.ResponseResult;
import com.example.epidemicsurveillance.service.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public List<Permission> getMenuWithRole() {
        return permissionMapper.getMenuWithRole();
    }

    @Override
    public ResponseResult getPermissionTree() {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        List<Permission> list= (List<Permission>) operations.get("permissionList");
        if (list == null || list.size() == 0) {
            list=new LinkedList<>();
            //第一层
            Permission rootPermission = permissionMapper.selectById(1);
            List<Role> rootPermissionRoleList=roleMapper.getRoleListByPermissionId(rootPermission.getId());
            List<Admin> rootPermissionAdminList=adminMapper.getAdminByPermissionId(rootPermission.getId());
            rootPermission.setRoles(rootPermissionRoleList);
            rootPermission.setRoleNames(roleListToRoleString(rootPermissionRoleList));
            rootPermission.setAdminUsernames(adminListToRoleString(rootPermissionAdminList));
            //第二层
            QueryWrapper<Permission> secondPermissionWrapper=new QueryWrapper<>();
            secondPermissionWrapper.eq("parent_id",1);
            secondPermissionWrapper.ne("id",1);
            List<Permission> secondPermissions = permissionMapper.selectList(secondPermissionWrapper);
            //第三层
            for (Permission secondPermission: secondPermissions) {
                List<Role> secondPermissionRoleList=roleMapper.getRoleListByPermissionId(secondPermission.getId());
                List<Admin> secondPermissionAdminList=adminMapper.getAdminByPermissionId(secondPermission.getId());
                secondPermission.setRoles(secondPermissionRoleList);
                secondPermission.setRoleNames(roleListToRoleString(secondPermissionRoleList));
                secondPermission.setAdminUsernames(adminListToRoleString(secondPermissionAdminList));
                QueryWrapper<Permission> thirdPermissionWrapper=new QueryWrapper<>();
                thirdPermissionWrapper.eq("parent_id",secondPermission.getId());
                List<Permission> thirdPermissions = permissionMapper.selectList(thirdPermissionWrapper);
                //第四层
                for (Permission thirdPermission: thirdPermissions) {
                    List<Role> thirdPermissionRoleList=roleMapper.getRoleListByPermissionId(thirdPermission.getId());
                    List<Admin> thirdPermissionAdminList=adminMapper.getAdminByPermissionId(thirdPermission.getId());
                    thirdPermission.setRoles(thirdPermissionRoleList);
                    thirdPermission.setRoleNames(roleListToRoleString(thirdPermissionRoleList));
                    thirdPermission.setAdminUsernames(adminListToRoleString(thirdPermissionAdminList));
                    QueryWrapper<Permission> fourthPermissionWrapper=new QueryWrapper<>();
                    fourthPermissionWrapper.eq("parent_id",thirdPermission.getId());
                    List<Permission> fourthPermissions = permissionMapper.selectList(fourthPermissionWrapper);
                    for (Permission fourthPermission: fourthPermissions) {
                        List<Role> fourthPermissionRoleList=roleMapper.getRoleListByPermissionId(fourthPermission.getId());
                        List<Admin> fourthPermissionAdminList=adminMapper.getAdminByPermissionId(fourthPermission.getId());
                        fourthPermission.setRoles(fourthPermissionRoleList);
                        fourthPermission.setRoleNames(roleListToRoleString(fourthPermissionRoleList));
                        fourthPermission.setAdminUsernames(adminListToRoleString(fourthPermissionAdminList));
                    }
                    thirdPermission.setChildren(fourthPermissions);
                }
                secondPermission.setChildren(thirdPermissions);
            }
            rootPermission.setChildren(secondPermissions);
            list.add(rootPermission);
            //两天更新一次
            operations.set("permissionList",list,2, TimeUnit.DAYS);
            return ResponseResult.ok().data("list",list);
        }
        return ResponseResult.ok().data("list",list);
    }
    /**
     * 将角色列表转换为角色字符串
     */
    public String roleListToRoleString(List<Role> roleList){
        StringBuilder roleString=new StringBuilder();
        for (int i = 0; i <roleList.size() ; i++) {
            if(i==0){
                roleString.append(roleList.get(i).getRoleName());
            }else {
                roleString.append(",").append(roleList.get(i).getRoleName());
            }
        }
        return roleString.toString();
    }
    /**
     * 将管理员列表转换为字符串
     */
    public String adminListToRoleString(List<Admin> adminList){
        StringBuilder adminString=new StringBuilder();
        for (int i = 0; i <adminList.size() ; i++) {
            if(i==0){
                adminString.append(adminList.get(i).getUsername());
            }else {
                adminString.append(",").append(adminList.get(i).getUsername());
            }
        }
        return adminString.toString();
    }
}
