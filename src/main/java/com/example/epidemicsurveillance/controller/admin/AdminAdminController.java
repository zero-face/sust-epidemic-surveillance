package com.example.epidemicsurveillance.controller.admin;

import com.example.epidemicsurveillance.entity.Admin;
import com.example.epidemicsurveillance.entity.vo.AdminLoginVo;
import com.example.epidemicsurveillance.entity.vo.RegisterVo;
import com.example.epidemicsurveillance.exception.EpidemicException;
import com.example.epidemicsurveillance.response.ResponseResult;
import com.example.epidemicsurveillance.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


/**
 * @ClassName IndexController
 * @Author 朱云飞
 * @Date 2021/10/10 12:09
 * @Version 1.0
 **/
@RequestMapping("/api/v1/admin/admin")
@RestController
@CrossOrigin
@Api(tags = "后台管理员管理模块")
public class AdminAdminController {
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
    public ResponseResult login(@ApiParam(name = "loginForm",value = "登录对象,",required = true)
                                @RequestBody AdminLoginVo loginForm) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return adminService.login(loginForm);
    }

    @ApiOperation(value = "根据用户token获取用户信息")
    @GetMapping("getAdminInfo")
    public ResponseResult getAdminInfo(@ApiParam(name = "token",value = "用户token",required = true)
                                       @RequestParam String token){
        return  adminService.getAdminInfo(token);
    }

    @ApiOperation(value = "退出登录")
    @PostMapping("logout")
    public ResponseResult logout(){
        return ResponseResult.ok().message("退出成功");
    }

    @ApiOperation(value = "邮箱验证码获取")
    @GetMapping("getEmailCode")
    public  ResponseResult getEmailCode(@ApiParam(name = "email",value = "用户邮箱",required = true)
                                        @RequestParam String email){
        if(email == null){
            throw new EpidemicException("邮箱不能为空");
        }
        adminService.getEmailCode(email);
        return ResponseResult.ok().message("邮件已经发送，验证码十分钟内有效");
    }

    @ApiOperation(value = "用户注册")
    @PostMapping("register")
    public ResponseResult register(@ApiParam(name = "admin",value = "注册对象",required = true)
                                   @RequestBody RegisterVo admin){
        return adminService.register(admin);
    }

    @ApiOperation(value = "查询所有已经审核通过的用户")
    @GetMapping("getAdmins/{type}")
    public ResponseResult getAdmins(@ApiParam(name = "type",value = "查询类型 0待审核,1已通过审核")
                                    @PathVariable Integer type){
        return adminService.getAdmins(type);
    }

    @ApiOperation(value = "根据管理员Id查询管理员全部信息")
    @GetMapping("getAdminById/{adminId}")
    public ResponseResult getAdminById(@ApiParam(name = "adminId",value = "管理员Id",required = true)
                                       @PathVariable Integer adminId){
        return adminService.getAdminById(adminId);
    }

    @ApiOperation(value = "添加管理员")
    @PostMapping("addAdmin")
    public ResponseResult addAdmin(@ApiParam(name = "admin",value = "管理员对象",required = true)
                                   @RequestBody Admin admin) {
        return adminService.addAdmin(admin);
    }

    @ApiOperation(value = "修改管理员信息")
    @PutMapping("updateAdmin")
    public ResponseResult updateAdmin(@ApiParam(name = "admin",value = "管理员对象",required = true)
                                      @RequestBody Admin admin){
        return adminService.updateAdmin(admin);
    }

    @ApiOperation(value = "根据管理员Id删除管理员信息")
    @DeleteMapping("deleteAdmin/{adminId}")
    public ResponseResult deleteAdmin(@ApiParam(name = "adminId",value = "管理员Id",required = true)
                                      @PathVariable Integer adminId){
        return adminService.deleteAdmin(adminId);
    }

    @ApiOperation(value = "管理员注册审核通过")
    @GetMapping("adminAdopt/{adminId}")
    public ResponseResult adminAdopt(@ApiParam(name = "adminId",value = "待审核管理员Id",required = true)
                                     @PathVariable Integer adminId){
        return adminService.adminAdopt(adminId);
    }

    @ApiOperation(value = "管理员注册审核失败")
    @GetMapping("adminFail/{adminId}")
    public ResponseResult adminFail(@ApiParam(name = "adminId",value = "待审核管理员Id",required = true)
                                    @PathVariable Integer adminId){
        return adminService.adminFail(adminId);
    }

}
