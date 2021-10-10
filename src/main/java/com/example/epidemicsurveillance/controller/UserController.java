package com.example.epidemicsurveillance.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.epidemicsurveillance.entity.User;
import com.example.epidemicsurveillance.response.ResponseResult;
import com.example.epidemicsurveillance.service.IUserService;
import com.example.epidemicsurveillance.utils.JwtTokenUtil;
import com.example.epidemicsurveillance.vo.UserVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 普通用户表 前端控制器
 * </p>
 *
 * @author zyf
 * @since 2021-10-09
 */
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

    private String GETOPENIDURL= "https://api.weixin.qq.com/sns/jscode2session";



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
        final User one = userService.getOne(new QueryWrapper<User>().eq("open_id", openid));
        if(null == one) {
            log.info("不存在存在该用户，正在登陆....");
            final User tUser = new User();
            BeanUtils.copyProperties(user, tUser);
            tUser.setWechatId(openid);
            userService.save(tUser);
        }else {
            final User tUser = new User();
            System.out.println("传输的用户信息：" + user);
            BeanUtils.copyProperties(one, tUser);
            tUser.setWechatId(openid);
            System.out.println("userinfo"+tUser);
            userService.update(tUser,new UpdateWrapper<User>().eq("open_id", openid));
        }
        final Map<String, Object> maps = new HashMap<String, Object>(){{
            put("username", user.getNickname());
        }};
        final String token = jwtTokenUtil.generateToken(maps);
        final User id = userService.getOne(new QueryWrapper<User>().eq("open_id", openid));
        final UserVO userVO = new UserVO();
        BeanUtils.copyProperties(id, userVO);
        Map<String, Object> reToken = JSON.parseObject(JSON.toJSONString(userVO), new TypeReference<Map<String,
                Object>>() {
        });
        reToken.put("token",token);
        return ResponseResult.ok().data(reToken).message("登录成功");
    }
}
