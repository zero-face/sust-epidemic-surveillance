package com.example.epidemicsurveillance.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.epidemicsurveillance.entity.User;
import com.example.epidemicsurveillance.exception.EpidemicException;
import com.example.epidemicsurveillance.mapper.UserMapper;
import com.example.epidemicsurveillance.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

/**
 * <p>
 * 普通用户表 服务实现类
 * </p>
 *
 * @author zyf
 * @since 2021-10-09
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String getOpenIdByCode(String url, Map<String, String> map) {
        log.info("获取openid开始=========》");
        StringBuffer params = new StringBuffer();
        params.append("?appid=" + map.get("appid")+"&secret="+map.get("secret") + "&"+"js_code=" + map.get("code") + "&"
                + "grant_type=authorization_code");
        final ResponseEntity<String> exchange = restTemplate.exchange(new RequestEntity<>(HttpMethod.GET, URI.create(url
                + params)), String.class);
        if(exchange.getStatusCodeValue() == 200) {
            log.info(exchange.getBody());
            final JSONObject jsonObject = JSON.parseObject(exchange.getBody());
            final String code = jsonObject.getString("openid");
            return code;
        } else if(exchange.getStatusCode().value() == 40029){
            throw new EpidemicException("无效的code");
        }else if(exchange.getStatusCode().value() == 45011){
            throw new EpidemicException("登录过于频繁");
        } else {
            throw new EpidemicException("系统繁忙");
        }
    }
}
