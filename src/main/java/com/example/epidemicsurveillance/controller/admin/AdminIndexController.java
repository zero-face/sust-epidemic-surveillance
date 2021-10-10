package com.example.epidemicsurveillance.controller.admin;

import com.example.epidemicsurveillance.entity.vo.AdminLoginVo;
import com.example.epidemicsurveillance.response.ResponseResult;
import com.example.epidemicsurveillance.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @ClassName IndexController
 * @Author 朱云飞
 * @Date 2021/10/10 12:09
 * @Version 1.0
 **/
@RequestMapping("/admin/epidemicsurveillance/index")
@RestController
@CrossOrigin
@Api(tags = "后台用户管理模块")
public class AdminIndexController {
    @Autowired
    private IAdminService adminService;

    @ApiOperation("生成谷歌图新验证码")
    @GetMapping(value = "getKaptcha/{key}",produces = "image/jpeg")
    public void getKaptcha(HttpServletRequest request, HttpServletResponse response,
                           @ApiParam(name = "key",value = "图形验证码在Redis中存储的key",required = true)
                           @PathVariable("key")String key){
        adminService.getKaptcha(request,response,key);
    }

    @ApiOperation(value = "管理员登录")
    @PostMapping("/login")
    public ResponseResult login(@ApiParam(name = "adminLoginVo",value = "管理员对象",required = true)
                                @RequestBody AdminLoginVo adminLoginVo){
        return adminService.login(adminLoginVo);
    }


}
