package com.example.epidemicsurveillance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.epidemicsurveillance.entity.Admin;
import com.example.epidemicsurveillance.entity.Role;
import com.example.epidemicsurveillance.entity.vo.AdminLoginVo;
import com.example.epidemicsurveillance.entity.vo.RegisterVo;
import com.example.epidemicsurveillance.response.ResponseResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

/**
 * <p>
 * 管理员表 服务类
 * </p>
 *
 * @author zyf
 * @since 2021-10-09
 */
public interface IAdminService extends IService<Admin> {

    Admin getAdminByUsername(String username);

    List<Role> getRoles(Integer id);

    ResponseResult login(AdminLoginVo adminLoginVo) throws NoSuchAlgorithmException, InvalidKeySpecException;

    void getKaptcha(HttpServletRequest request, HttpServletResponse response, String key);

    ResponseResult getAdminInfo(String token);

    void getEmailCode(String email);

    ResponseResult register(RegisterVo admin);

    ResponseResult getAdmins(Integer type);

    ResponseResult getAdminById(Integer adminId);

    ResponseResult addAdmin(Admin admin);

    ResponseResult updateAdmin(Admin admin);

    ResponseResult deleteAdmin(Integer adminId);

    ResponseResult adminAdopt(Integer adminId);

    ResponseResult adminFail(Integer adminId);
}
