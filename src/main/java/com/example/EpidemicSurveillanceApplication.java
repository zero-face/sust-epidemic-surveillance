package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.example.epidemicsurveillance.mapper")
@ComponentScan("com.example.epidemicsurveillance")
@EnableScheduling//定时任务
public class EpidemicSurveillanceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EpidemicSurveillanceApplication.class, args);
    }
}
