package com.example.epidemicsurveillance.config.swagger;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @ClassName Swagger2Config
 * @Author 朱云飞
 * @Date 2021/10/9 10:36
 * @Version 1.0
 **/
//@Configuration
//@EnableSwagger2
public class Swagger2Config {
    @Bean
    public Docket webApiConfigEdu(){

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi")
                .apiInfo(webApiInfo())
                .select()
                .paths(Predicates.not(PathSelectors.regex("/admin/.*")))
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build();

    }

    private ApiInfo webApiInfo(){

        return new ApiInfoBuilder()
                .title("SUST疫情防控网站API文档")
                .description("本文档描述了SUST疫情防控网站接口定义")
                .version("1.0")
                .contact(new Contact("朱云飞", "http://localhost:8400/doc.html", "2690534598@qq.com"))
                .build();
    }
}