package com.example.epidemicsurveillance.controller.admin;

import com.example.epidemicsurveillance.response.ResponseResult;
import com.example.epidemicsurveillance.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName RoleAdminController
 * @Author 朱云飞
 * @Date 2021/10/21 22:31
 * @Version 1.0
 **/
@RestController
@RequestMapping("/api/v1/admin/role")
@Api(tags = "后台角色模块")
@CrossOrigin
public class RoleAdminController {
    @Autowired
    private IRoleService iRoleService;

    @ApiOperation(value = "获取全部角色列表")
    @GetMapping("getAllRoles")
    public ResponseResult getAllRoles(){
        return iRoleService.getAllRoles();
    }
}
