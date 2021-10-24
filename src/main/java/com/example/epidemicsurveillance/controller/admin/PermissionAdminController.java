package com.example.epidemicsurveillance.controller.admin;

import com.example.epidemicsurveillance.entity.Permission;
import com.example.epidemicsurveillance.response.ResponseResult;
import com.example.epidemicsurveillance.service.IPermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName PermissionAdminController
 * @Author 朱云飞
 * @Date 2021/10/22 20:58
 * @Version 1.0
 **/
@Api(tags = "后台权限管理模块")
@CrossOrigin
@RestController
@RequestMapping("/api/v1/admin/permission")
public class PermissionAdminController {
    @Autowired
    private IPermissionService iPermissionService;

    @ApiOperation(value = "树形获取全部权限列表")
    @GetMapping("getPermissionTree")
    public ResponseResult getPermissionTree(){
        return iPermissionService.getPermissionTree();
    }

    @ApiOperation(value = "根据角色获取权限树形列表")
    @GetMapping("getPermissionTreeByRoleId/{roleId}")
    public ResponseResult getPermissionTreeByRoleId(@ApiParam(name = "roleId",value = "角色Id",required = true)
                                                    @PathVariable Integer roleId){
        return iPermissionService.getPermissionTreeByRoleId(roleId);
    }

    @ApiOperation(value = "根据权限Id获取权限的list")
    @GetMapping("getPermissionListById/{permissionId}")
    public ResponseResult getPermissionListById(@ApiParam(name = "permissionId",value = "权限Id",required = true)
                                            @PathVariable Integer permissionId){
        return iPermissionService.getPermissionListById(permissionId);
    }

    @ApiOperation(value = "添加权限")
    @PostMapping("addPermission/{permissionId}")
    public ResponseResult addPermission(@ApiParam(name = "permissionId",value = "权限Id",required = true)
                                        @PathVariable Integer permissionId,
                                        @ApiParam(name = "permission",value = "权限对象",required = true)
                                        @RequestBody Permission permission){
        return iPermissionService.addPermission(permissionId,permission);
    }

    @ApiOperation(value = "根据权限Id获取权限信息")
    @GetMapping("getPermissionById/{permissionId}")
    public ResponseResult getPermissionById(@ApiParam(name = "permissionId",value = "权限Id",required = true)
                                            @PathVariable Integer permissionId){
        return iPermissionService.getPermissionById(permissionId);
    }

    @ApiOperation(value = "修改权限信息")
    @PutMapping("updatePermission")
    public ResponseResult updatePermission(@ApiParam(name = "permission",value = "权限对象",required = true)
                                           @RequestBody Permission permission){
        return iPermissionService.updatePermission(permission);
    }

    @ApiOperation(value = "删除权限信息")
    @DeleteMapping("deletePermissionById/{permissionId}")
    public ResponseResult deletePermissionById(@ApiParam(name = "permissionId",value = "权限Id",required = true)
                                               @PathVariable Integer permissionId){
        return iPermissionService.deletePermissionById(permissionId);
    }

    @ApiOperation(value = "根据角色Id查询角色未拥有的权限树")
    @GetMapping("getRoleNotOwnedPermissionByRoleId/{roleId}")
    public ResponseResult getRoleNotOwnedPermissionByRoleId(@ApiParam(name = "roleId",value = "角色Id",required = true)
                                                            @PathVariable Integer roleId){
        return iPermissionService.getRoleNotOwnedPermissionByRoleId(roleId);
    }
}
