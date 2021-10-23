package com.example.epidemicsurveillance.controller.admin;

import com.example.epidemicsurveillance.response.ResponseResult;
import com.example.epidemicsurveillance.service.IPermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
