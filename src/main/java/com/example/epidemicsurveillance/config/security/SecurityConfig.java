package com.example.epidemicsurveillance.config.security;


import com.example.epidemicsurveillance.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private IAdminService adminService;

    @Autowired
    private RestAuthorizationEntryPoint restAuthorizationEntryPoint;

    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;

    @Autowired
    private CustomFilter customFilter;

    @Autowired
    private CustomUrlDecisionManager customUrlDecisionManager;

    //让SpringSecurity走我们自己登陆的UserDetailsService逻辑

    //认证信息的管理 用户的存储 这里配置的用户信息会覆盖掉SpringSecurity默认生成的账号密码
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }
    //密码加解密
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Override
    @Bean  //注入到IOC中，在登录时使用到的userDetailsService就是这个Bean，loadUserByUsername方法是这里重写过的
    public UserDetailsService userDetailsService(){
        return username->{
//            Admin admin=adminService.getAdminByUsername(username);
//            if(admin != null){
//                admin.setRoles(adminService.getRoles(admin.getId()));
//                return admin;
//            }
            throw new UsernameNotFoundException("用户名或密码错误");
        };
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //使用Jwt不需要csrf
        http.csrf().disable()
                //基于token，不需要Session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //授权认证
                .authorizeRequests()
                .antMatchers("/doc.html").permitAll()
                //除了上面，所有的请求都要认证
                .anyRequest()
                .authenticated()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    //动态权限配置
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setAccessDecisionManager(customUrlDecisionManager);
                        o.setSecurityMetadataSource(customFilter);
                        return o;
                    }
                })
                .and()
                //禁用缓存
                .headers()
                .cacheControl();

        //添加jwt登录授权过滤器  判断是否登录
        http.addFilterBefore(jwtAuthencationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        //添加自定义未授权和未登录结果返回
        http.exceptionHandling()
                //无权限
                .accessDeniedHandler(restfulAccessDeniedHandler)
                //未登录 执行该逻辑
                .authenticationEntryPoint(restAuthorizationEntryPoint);

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
                "/doc.html",
                "/webjars/**",
                "/swagger-resources/**",
                "/v2/api-docs/**",
                //放行图像验证码
                "/captcha",
                //WebSocket
                "/ws/**"
        );
    }
}
