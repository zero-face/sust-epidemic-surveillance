package com.example.epidemicsurveillance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.epidemicsurveillance.entity.Admin;
import com.example.epidemicsurveillance.entity.Permission;
import com.example.epidemicsurveillance.entity.Role;
import com.example.epidemicsurveillance.entity.RolePermission;
import com.example.epidemicsurveillance.exception.EpidemicException;
import com.example.epidemicsurveillance.mapper.AdminMapper;
import com.example.epidemicsurveillance.mapper.PermissionMapper;
import com.example.epidemicsurveillance.mapper.RoleMapper;
import com.example.epidemicsurveillance.mapper.RolePermissionMapper;
import com.example.epidemicsurveillance.response.ResponseResult;
import com.example.epidemicsurveillance.service.IPermissionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Deque;
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
 * @description 第二作者认为这段代码其丑（zero）
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
    private RolePermissionMapper rolePermissionMapper;

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
            //获取超级管理员权限对应的角色
            List<Role> rootPermissionRoleList=roleMapper.getRoleListByPermissionId(rootPermission.getId());
            //根据超级管理员权限获取对应的账户
            List<Admin> rootPermissionAdminList=adminMapper.getAdminByPermissionId(rootPermission.getId());
            //permission权限中添加对应的角色
            rootPermission.setRoles(rootPermissionRoleList);
            //将权限对应的角色字符串添加进入权限对象
            rootPermission.setRoleNames(roleListToRoleString(rootPermissionRoleList));
            //将权限对应的管理员添加进入对象中
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

    @Override
    public ResponseResult getPermissionTreeByRoleId(Integer roleId) {
        List<Permission> permissions=permissionMapper.getPermissionTreeByRoleId(roleId);
        List<Permission> result = permissionLayered(permissions);
        return ResponseResult.ok().data("list",result);
    }

    @Override
    public ResponseResult getPermissionListById(Integer permissionId) {
        if(permissionId == null){
            throw new EpidemicException("待查询的权限记录不存在");
        }
        Permission permission = permissionMapper.selectById(permissionId);
        permission.setRoleNames(roleListToRoleString(roleMapper.getRoleListByPermissionId(permissionId)));
        permission.setAdminUsernames(adminListToRoleString(adminMapper.getAdminByPermissionId(permissionId)));
        List<Permission> parentPermission=new LinkedList<>();
        parentPermission.add(permission);
        return ResponseResult.ok().data("parentPermission",parentPermission );
    }

    @Override
    public ResponseResult addPermission(Integer permissionId, Permission permission) {
        if(StringUtils.isBlank(permission.getPermissionName())){
            throw new EpidemicException("权限名称不能为空");
        }
        if(StringUtils.isBlank(permission.getUrl())){
            throw new EpidemicException("权限路由不能为空");
        }
        QueryWrapper<Permission> permissionNameWrapper=new QueryWrapper<>();
        permissionNameWrapper.eq("permission_name",permission.getPermissionName());
        Permission permission1 = permissionMapper.selectOne(permissionNameWrapper);
        if(permission1 != null){
            throw new EpidemicException("该权限名称已经存在");
        }
        QueryWrapper<Permission> permissionUrlWrapper=new QueryWrapper<>();
        permissionUrlWrapper.eq("url",permission.getUrl());
        Permission permission2 = permissionMapper.selectOne(permissionUrlWrapper);
        if(permission2 != null){
            throw new EpidemicException("该权限路由已经存在");
        }
        permission.setParentId(permissionId);
        permissionMapper.insert(permission);
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        RedisOperations<String, Object> redisOperations = operations.getOperations();
        redisOperations.delete("permissionList");
        return ResponseResult.ok().message("添加权限成功");
    }

    @Override
    public ResponseResult getPermissionById(Integer permissionId) {
        if(permissionId == null){
            throw new EpidemicException("待查询的权限记录不存在");
        }
        Permission permission = permissionMapper.selectById(permissionId);
        return ResponseResult.ok().data("permission",permission);
    }

    @Override
    public ResponseResult updatePermission(Permission permission) {
        if(StringUtils.isBlank(permission.getPermissionName())){
            throw new EpidemicException("权限名称不能为空");
        }
        if(StringUtils.isBlank(permission.getUrl())){
            throw new EpidemicException("权限路由不能为空");
        }
        QueryWrapper<Permission> permissionNameWrapper=new QueryWrapper<>();
        permissionNameWrapper.eq("permission_name",permission.getPermissionName());
        Permission permission1 = permissionMapper.selectOne(permissionNameWrapper);
        if(permission1 != null && !permission1.getId().equals(permission.getId())){
            throw new EpidemicException("该权限名称已经存在");
        }
        QueryWrapper<Permission> permissionUrlWrapper=new QueryWrapper<>();
        permissionUrlWrapper.eq("url",permission.getUrl());
        Permission permission2 = permissionMapper.selectOne(permissionUrlWrapper);
        if(permission2 != null && !permission2.getId().equals(permission.getId())){
            throw new EpidemicException("该权限路由已经存在");
        }
        permissionMapper.updateById(permission);
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        RedisOperations<String, Object> redisOperations = operations.getOperations();
        redisOperations.delete("permissionList");
        return ResponseResult.ok().message("修改成功");
    }

    @Override
    public ResponseResult deletePermissionById(Integer permissionId) {
        Permission permission = permissionMapper.selectById(permissionId);
        if(permission == null){
            throw new EpidemicException("待删除的权限信息不存在");
        }
        QueryWrapper<Permission> wrapper=new QueryWrapper<>();
        wrapper.eq("parent_id",permissionId);
        List<Permission> permissions = permissionMapper.selectList(wrapper);
        if(permissions != null && permissions.size() > 0){
            throw new EpidemicException("该权限还存在子权限，无法删除");
        }
        permissionMapper.deleteById(permissionId);
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        RedisOperations<String, Object> redisOperations = operations.getOperations();
        redisOperations.delete("permissionList");
        return ResponseResult.ok().message("删除成功");
    }

    @Override
    public ResponseResult getRoleNotOwnedPermissionByRoleId(Integer roleId) {
        List<Permission> ownerPermissions=permissionMapper.getPermissionTreeByRoleId(roleId);
        List<Permission> allPermission=permissionMapper.selectList(null);
        List<Permission> notOwnerPermission=new LinkedList<>();
        for (Permission permission:allPermission) {
            boolean isOwner=false;//记录该权限是否已经拥有
            for (Permission ownerPermission: ownerPermissions) {
                if(permission.getId().equals(ownerPermission.getId())){
                    isOwner=true;//已经拥有
                    break;
                }
            }
            if(!isOwner){
                notOwnerPermission.add(permission);
            }
        }
        List<Permission> result = permissionLayered(notOwnerPermission);
        return ResponseResult.ok().data("list",result);
    }

    @Override
    public ResponseResult distributionPermission(Integer roleId, Integer permissionId, List<Permission> notOwnerList) {
        //未拥有的权限
       List<Permission> notOwnerPermissionList=getAllChildrenPermission(notOwnerList);
       //根据权限Id获取权限及其全部子权限
        List<Permission> allChildrenPermissionList=getChildrenPermissionByPermissionId(permissionId);
        for (Permission notOwnerPermission:notOwnerPermissionList) {
            for (Permission permission:allChildrenPermissionList) {
                if(permission.getId().equals(notOwnerPermission.getId())){
                    RolePermission rolePermission=new RolePermission();
                    rolePermission.setRoleId(roleId);
                    rolePermission.setPermissionId(permission.getId());
                    rolePermissionMapper.insert(rolePermission);
                }
            }
        }
        return ResponseResult.ok().message("分配权限成功");
    }


    @Override
    public ResponseResult notDistributionPermission(Integer roleId, Integer permissionId, List<Permission> ownerList) {
        List<Permission> ownerPermissionList=getAllChildrenPermission(ownerList);
        List<Permission> allChildrenPermissionList=getChildrenPermissionByPermissionId(permissionId);
        //拥有的权限列表
        for (Permission ownerPermission:ownerPermissionList) {
            for (Permission permission:allChildrenPermissionList) {
                if(permission.getId().equals(ownerPermission.getId())){
                    QueryWrapper<RolePermission> wrapper=new QueryWrapper<>();
                    wrapper.eq("role_id",roleId);
                    wrapper.eq("permission_id",permission.getId());
                    rolePermissionMapper.delete(wrapper);
                }
            }
        }
        return ResponseResult.ok().message("取消授权成功");
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
    /**
     * 权限分层
     */
    public List<Permission> permissionLayered(List<Permission> permissions){
       //四层权限 从下往上，下层权限在相邻的上层权限有其父权限，则加入父权限的孩子，如果没用，有下层上升一级
        Permission rootPermission=null;
        List<Permission> result=new LinkedList<>();
        List<Permission> secondPermissionList=new LinkedList<>();
        List<Permission> thirdPermissionList=new LinkedList<>();
        List<Permission> fourthPermissionList=new LinkedList<>();
        for (Permission permission:permissions) {
            permission.setChildren(new LinkedList<>());
            permission.setRoleNames(roleListToRoleString(roleMapper.getRoleListByPermissionId(permission.getId())));
            permission.setAdminUsernames(adminListToRoleString(adminMapper.getAdminByPermissionId(permission.getParentId())));
            //第一层
            if(permission.getId() == 1){
                rootPermission=permission;
                continue;
            }
            //第二层
            if(permission.getParentId()==1 && permission.getParentId() != 1){
                secondPermissionList.add(permission);
                continue;
            }
            //第四层
            QueryWrapper<Permission> wrapper=new QueryWrapper<>();
            wrapper.eq("parent_id",permission.getId());
            List<Permission> fifthPermissionList = permissionMapper.selectList(wrapper);
            if(fifthPermissionList == null || fifthPermissionList.size() == 0){
                fourthPermissionList.add(permission);
                continue;
            }
            //第三层
            thirdPermissionList.add(permission);
        }
        //处理第四层
        for (Permission fourthPermission:fourthPermissionList) {
            boolean isAdd=false;//记录上层权限是否有其父权限
            for (Permission thirdPermission:thirdPermissionList) {
                if(fourthPermission.getParentId().equals(thirdPermission.getId())){
                    thirdPermission.getChildren().add(fourthPermission);
                    isAdd=true;
                }
            }
            if(!isAdd){//如果没有，加到上层目录
                thirdPermissionList.add(fourthPermission);
            }
        }
        //处理第三层
        for (Permission thirdPermission:thirdPermissionList) {
            boolean isAdd=false;//记录上层权限是否有其父权限
            for (Permission secondPermission:secondPermissionList) {
                if(thirdPermission.getParentId().equals(secondPermission.getId())){
                    secondPermission.getChildren().add(thirdPermission);
                    isAdd=true;
                }
            }
            if(!isAdd){//如果没有，加到上层目录
                secondPermissionList.add(thirdPermission);
            }
        }
        //处理第二层
        if(rootPermission != null){
            rootPermission.getChildren().addAll(secondPermissionList);
            result.add(rootPermission);
        }else {
            result.addAll(secondPermissionList);
        }
        return result;
    }

    /**
     * 获取子权限中的全部权限
     */
    public List<Permission> getAllChildrenPermission(List<Permission> parentPermission){
        List<Permission> permissionList=new LinkedList<>();
        Deque<Permission> queue=new LinkedList<>();
        queue.addAll(parentPermission);
        while (!queue.isEmpty()){
            Permission permission=queue.poll();
            if(permission.getChildren() != null && permission.getChildren().size() > 0){
                queue.addAll(permission.getChildren());
            }
            permissionList.add(permission);
        }
        return permissionList;
    }

    private List<Permission> getChildrenPermissionByPermissionId(Integer permissionId) {
        List<Permission> permissionList=new LinkedList<>();
        Permission permission = permissionMapper.selectById(permissionId);
        Deque<Permission> queue=new LinkedList<>();
        queue.add(permission);
        while (!queue.isEmpty()){
            Permission poll = queue.poll();
            QueryWrapper<Permission> wrapper=new QueryWrapper<>();
            wrapper.eq("parent_id",poll.getId());
            wrapper.ne("id",1);
            List<Permission> permissionList1 = permissionMapper.selectList(wrapper);
            if(permissionList1 != null && permissionList1.size() > 0){
                queue.addAll(permissionList1);
            }
            permissionList.add(poll);
        }
        return permissionList;
    }
}
