package com.example.epidemicsurveillance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.epidemicsurveillance.entity.Admin;
import com.example.epidemicsurveillance.entity.Role;
import com.example.epidemicsurveillance.entity.vo.AdminLoginVo;
import com.example.epidemicsurveillance.exception.EpidemicException;
import com.example.epidemicsurveillance.mapper.AdminMapper;
import com.example.epidemicsurveillance.response.ResponseResult;
import com.example.epidemicsurveillance.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.epidemicsurveillance.utils.JwtTokenUtil;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author zyf
 * @since 2021-10-09
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public Admin getAdminByUsername(String username) {
        if(StringUtils.isBlank(username)){
            throw new EpidemicException("用户名不能为空");
        }
        QueryWrapper<Admin> wrapper=new QueryWrapper<>();
        wrapper.eq("username",username);
        Admin admin=adminMapper.selectOne(wrapper);
        if(admin == null){
            throw new EpidemicException("该用户不存在");
        }
        return admin;
    }

    @Override
    public List<Role> getRoles(Integer adminId) {
        return adminMapper.getRoles(adminId);
    }

    @Override
    public void getKaptcha(HttpServletRequest request, HttpServletResponse response, String key) {
        // 定义response输出类型为image/jpeg类型
        response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");
        // return a jpeg
        response.setContentType("image/jpeg");
        //-------------------生成验证码 begin --------------------------
        //获取验证码文本内容
        String text=defaultKaptcha.createText();
        System.out.println("验证码内容"+text);
        //将验证码文本内容放入redis  五分钟内有效
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key,text,5L, TimeUnit.MINUTES);
        //根据文本验证码内容创建图形验证码
        BufferedImage image = defaultKaptcha.createImage(text);
        ServletOutputStream outputStream=null;
        try {
            outputStream = response.getOutputStream();
            //输出流输出图片,格式为jpg
            ImageIO.write(image, "jpg",outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(outputStream !=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //-------------------生成验证码 end --------------------------
    }

    @Override
    public ResponseResult login(AdminLoginVo adminLoginVo) throws NoSuchAlgorithmException, InvalidKeySpecException {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        String kaptchaCode=(String) operations.get(adminLoginVo.getKaptchaCodeId());
        if(kaptchaCode == null){
            throw new EpidemicException("验证码已经过期，请重新输入");
        }
        if(!kaptchaCode.equals(adminLoginVo.getKaptchaCode())){
            throw new EpidemicException("验证码错误，请重试");
        }
        QueryWrapper<Admin> wrapper=new QueryWrapper<>();
        wrapper.eq("username",adminLoginVo.getUsername());
        Admin admin = adminMapper.selectOne(wrapper);
        if(admin == null){
            throw new EpidemicException("该用户不存在");
        }
        if(!admin.getPassword().equals(adminLoginVo.getPassword())){
            throw new EpidemicException("密码错误，请重试");
        }
        String token= jwtTokenUtil.generateToken(admin);
        return ResponseResult.ok().data("token",token);
    }
}
