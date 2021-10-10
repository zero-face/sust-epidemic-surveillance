package com.example.epidemicsurveillance.config.security;


import com.example.epidemicsurveillance.config.security.dynamicroute.CustomFilter;
import com.example.epidemicsurveillance.config.security.dynamicroute.CustomUrlDecisionManager;
import com.example.epidemicsurveillance.config.security.jwtfilter.JwtAuthencationTokenFilter;
import com.example.epidemicsurveillance.config.security.handler.*;
import com.example.epidemicsurveillance.config.security.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @ClassName SecurityConfig
 * @Author 朱云飞
 * @Date 2021/6/12 15:24
 * @Version 1.0
 **/
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private RestAuthorizationEntryPoint restAuthorizationEntryPoint;

    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;

    @Autowired
    private CustomFilter customFilter;

    @Autowired
    private CustomUrlDecisionManager customUrlDecisionManager;

    @Autowired
    private CustomizeAuthenticationSuccessHandler customizeAuthenticationSuccessHandler;

    @Autowired
    private CustomizeAuthenticationFailureHandler customizeAuthenticationFailureHandler;

    @Autowired
    private MyUserDetailService myUserDetailService;

    //自定义数据源处理登录请求信息
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailService)
            .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //添加jwt登录授权过滤器  判断是否登录
        http.addFilterBefore(jwtAuthencationTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        http.csrf()
            .disable()//禁用csrf
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)//基于token，不需要Session
        .and()
            .headers()
            .cacheControl();//禁用缓存

        //路径认证规则
        http.authorizeRequests()
            .antMatchers("/doc.html").permitAll()
            .anyRequest()
            .authenticated();

        //登录成功失败处理
        http.formLogin()
            .successHandler(customizeAuthenticationSuccessHandler)
            .failureHandler(customizeAuthenticationFailureHandler)
        .and()
            //添加自定义未授权和未登录结果返回
            .exceptionHandling()
            .accessDeniedHandler(restfulAccessDeniedHandler)//无权限
            .authenticationEntryPoint(restAuthorizationEntryPoint);//未登录 执行该逻辑
        //动态权限配置
        http.authorizeRequests()
            .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                @Override
                public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                    o.setAccessDecisionManager(customUrlDecisionManager);
                    o.setSecurityMetadataSource(customFilter);
                    return o;
                }
            });
    }

    //将登录过滤器注入
    @Bean
    public JwtAuthencationTokenFilter jwtAuthencationTokenFilter(){
        return new JwtAuthencationTokenFilter();
    }

    //需要放行的资源
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/login",
                "/logout",
                "/css/**",
                "/js/**",
                //首页
                "/index.html",
                //网页图标
                "favicon.ico",
                //Swagger2
                "/doc.html","/webjars/**","/img.icons/**",
                "/swagger-resources/**","/**","/v2/api-docs", "/swagger-ui.html",
                //放行图像验证码
                "/captcha",
                //WebSocket
                "/ws/**"
        );
    }
}
