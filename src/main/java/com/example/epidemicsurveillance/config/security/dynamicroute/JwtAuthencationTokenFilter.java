package com.example.epidemicsurveillance.config.security.dynamicroute;

import com.example.epidemicsurveillance.config.security.service.MyUserDetailService;
import com.example.epidemicsurveillance.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Jwt登录授权过滤器
 * @ClassName JwtAuthencationTokenFilter
 * @Author 朱云飞
 * @Date 2021/6/12 15:42
 * @Version 1.0
 **/
public class JwtAuthencationTokenFilter extends OncePerRequestFilter {
   //Jwt存储头
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    //Jwt头部信息
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private MyUserDetailService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //token存储在Jwt的请求头中
        //通过key：tokenHeader拿到value：token

        //这里我们定义的token后期以：Bearer开头，空格分割，加上真正的jwt
        //通过tokenHeader(Authorization)拿到以Bearer开头 空格分割 加上真正的jwt的字符串
        String authHeader = httpServletRequest.getHeader(tokenHeader);

        //判断这个token的请求头是否为空且是以配置信息中要求的tokenHead开头
        if(authHeader != null && authHeader.startsWith(tokenHead)){
            //截取真正的jwt
            String authToken=authHeader.substring(tokenHead.length());
            String username=jwtTokenUtil.getUsernameFormToken(authToken);
            //token存在用户名但是未登录
            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                //登录
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                //验证token是否有效，重新设置用户对象
                if(jwtTokenUtil.TokenIsValid(authToken,userDetails)){
                    //把对象放到Security的全局中
                    UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    //将请求中的Session等信息放入Details，再放入Security全局中
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        //放行
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
