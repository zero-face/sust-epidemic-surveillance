package com.example.epidemicsurveillance.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName Swagger2Config
 * @Author 朱云飞
 * @Date 2021/10/9 10:36
 * @Version 1.0
 **/
@Configuration
@EnableSwagger2WebMvc
public class Swagger2Config {

    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                //基础设置
                .apiInfo(apiInfo())
                //扫描哪个包
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.epidemicsurveillance.controller"))
                //任何路径都可以
                .paths(PathSelectors.any())
                .build()
                .securityContexts(securityContexts())
                .securitySchemes(securitySchemes());
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("SUST疫情防控平台")
                .description("SUST疫情防控平台接口文档")
                .contact(new Contact("朱云飞", "http:localhost:8400/doc.html","2690534598@qq.com"))
                .version("1.0.0")
                .build();

    }

    private List<ApiKey> securitySchemes() {
        //设置请求头信息
        List<ApiKey> result=new ArrayList<>();
        ApiKey apiKey=new ApiKey("Authorization", "Authorization","Header");
        result.add(apiKey);
        return result;
    }

    private List<SecurityContext> securityContexts(){
        //设置需要登录认证的路径
        List<SecurityContext> result=new ArrayList<>();
        result.add(getContextByPath("/hello/.*"));
        return result;
    }

    private SecurityContext getContextByPath(String pathRegex) {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())//添加全局认证
                .forPaths(PathSelectors.regex(pathRegex)) //带有pathRegex字段的接口访问不带添加的Authorization全局变量
                .build();
    }

    //添加Swagger全局的Authorization  全局认证    固定的代码
    private List<SecurityReference> defaultAuth() {
        List<SecurityReference> result=new ArrayList<>();
        //设置范围为全局
        AuthorizationScope authorizationScope=new AuthorizationScope("global","accessEeverything");
        AuthorizationScope[]authorizationScopes=new AuthorizationScope[1];
        authorizationScopes[0]=authorizationScope;
        result.add((new SecurityReference("Authorization",authorizationScopes)));//这里的Authorization和上文ApiKey第二个参数一致
        return  result;
    }
}