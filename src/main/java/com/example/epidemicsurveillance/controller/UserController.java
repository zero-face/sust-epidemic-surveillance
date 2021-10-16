package com.example.epidemicsurveillance.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.epidemicsurveillance.entity.Clazz;
import com.example.epidemicsurveillance.entity.Collage;
import com.example.epidemicsurveillance.entity.User;
import com.example.epidemicsurveillance.entity.UserAuth;
import com.example.epidemicsurveillance.entity.vo.UserAuthVO;
import com.example.epidemicsurveillance.exception.EpidemicException;
import com.example.epidemicsurveillance.response.ResponseResult;
import com.example.epidemicsurveillance.service.*;
import com.example.epidemicsurveillance.utils.jwt.JwtTokenUtil;
import com.example.epidemicsurveillance.entity.vo.UserVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 普通用户表 前端控制器
 *
 * </p>
 *
 * @author zyf
 * @since 2021-10-09
 */
@Validated
@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Value("${wx.appid}")
    private String appid;

    @Value("${wx.secret}")
    private String secret;

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private IClassService classService;

    @Autowired
    private ICollageService collageService;

    @Autowired
    private IUserAuthService userAuthService;

    private String GETOPENIDURL= "https://api.weixin.qq.com/sns/jscode2session";

    @GetMapping
    public ResponseResult getRealInfo(@RequestParam("id") @NotBlank String openid) {
        UserAuth userAuth = userAuthService.getOne(new QueryWrapper<UserAuth>().eq("openid", openid));
        if(userAuth == null) {
            return ResponseResult.error().message("实名信息获取失败").data("cause","NOT_AUTH");
        }
        final Clazz clazz = classService.getOne(new QueryWrapper<Clazz>().eq("id", userAuth.getClassId()));
        if(clazz == null) {
            throw new EpidemicException("班级不存在");
        }
        final Collage collage = collageService.getOne(new QueryWrapper<Collage>().eq("id", userAuth.getCollageId()));
        if(collage == null) {
            throw new EpidemicException("学院不存在");
        }
        final UserAuthVO authVO = new UserAuthVO();
        BeanUtils.copyProperties(userAuth, authVO);
        authVO.setClazz(clazz.getClassName());
        authVO.setCollage(collage.getCollageName());
        Map<String, Object> userAuthVO = JSON.parseObject(JSON.toJSONString(authVO), new TypeReference<Map<String, Object>>() {});
        return ResponseResult.ok().message("实名信息获取成功").data(userAuthVO);
    }

    @PostMapping("/auth")
    public ResponseResult auth(@RequestParam("openid") @NotBlank String openid,
                               @RequestParam("username") @NotBlank String username,
                               @RequestParam("number") @NotBlank String number,
                               @RequestParam("profile")@NotBlank String profile,
                               @RequestParam("phone") @NotBlank String phone,
                               @RequestParam("collage") @NotBlank String collage,
                               @RequestParam("clazz") @NotBlank String clazz,
                               @RequestParam("charge") Boolean charge) {
        final Collage collageName = collageService.getOne(new QueryWrapper<Collage>().eq("collage_name", collage));
        if(collageName == null) {
            throw new EpidemicException("学院不存在");
        }
        final Clazz className = classService.getOne(new QueryWrapper<Clazz>().eq("class_name", clazz));
        if(className == null) {
            throw new EpidemicException("班级不存在");
        }
        final UserAuth userAuth = new UserAuth(){{
            setOpenid(openid);
            setUsername(username);
            setNumber(number);
            setProfileId(profile);
            setPhone(phone);
            setCollageId(collageName.getId());
            setClassId(className.getId());
            setCharge(charge);
        }};
        final boolean update = userAuthService.saveOrUpdate(userAuth, new UpdateWrapper<UserAuth>().eq("openid", openid));
        if(update) {
            //认证成功则更改用户的通过认证字段
            userService.update(new UpdateWrapper<User>().eq("authed", true));
            return ResponseResult.ok().message("认证成功");
        }
        return ResponseResult.error().message("认证失败");

    }


    @PostMapping("/code")
    @ApiOperation("根据code获取openid并得到登录token")
    public ResponseResult receiveCode(@RequestParam("code") String code, User user) throws NoSuchAlgorithmException, InvalidKeySpecException {
        log.info("获取code开始=========》{}",code);
        //接收到临时登录的code，向微信服务器发起请求，获取openid,session_key，unionid
        Map<String,String> map = new HashMap<>();
        map.put("appid", appid);
        map.put("code", code);
        map.put("secret", secret);
        String openid = userService.getOpenIdByCode(GETOPENIDURL, map);
        log.info("获取到openid:{}=========》",openid);
        //查询数据库中是否含有这个openID，如果有，说明已经授权，查询用户信息，返回信息以及token；没有则保存，但是其他的用户信息是空，返回用户信息以及token
        final User one = userService.getOne(new QueryWrapper<User>().eq("openid", openid));
        if(null == one) {
            log.info("不存在存在该用户，正在登陆....");
            final User tUser = new User();
            BeanUtils.copyProperties(user, tUser);
            tUser.setOpenid(openid);
            userService.save(tUser);
        } else {
            final User tUser = new User();
            System.out.println("传输的用户信息：" + user);
            BeanUtils.copyProperties(one, tUser);
            tUser.setOpenid(openid);
            System.out.println("userinfo"+tUser);
            userService.update(tUser,new UpdateWrapper<User>().eq("openid", openid));
        }
        final Map<String, Object> maps = new HashMap<String, Object>(){{
            put("username", user.getNickname());
        }};
        final String token = jwtTokenUtil.generateToken(maps);
        final User id = userService.getOne(new QueryWrapper<User>().eq("openid", openid));
        final UserVO userVO = new UserVO();
        BeanUtils.copyProperties(id, userVO);
        userVO.setSex(id.getSex() > 1? "女":"男");
        log.info("返回的用户信息" + userVO);
        Map<String, Object> reToken = JSON.parseObject(JSON.toJSONString(userVO), new TypeReference<Map<String, Object>>() {});
        reToken.put("token",token);
        log.info("登录成功，返回消息:{}",reToken);
        return ResponseResult.ok().data(reToken).message("登录成功");
    }
}
