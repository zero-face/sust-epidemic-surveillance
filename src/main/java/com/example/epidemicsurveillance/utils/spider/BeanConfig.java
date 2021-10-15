package com.example.epidemicsurveillance.utils.spider;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName SpiderUtils
 * @Author 朱云飞
 * @Date 2021/10/14 10:17
 * @Version 1.0
 **/
@Configuration
public class BeanConfig {
    @Bean
    public CloseableHttpClient httpClient(){
        return HttpClients.createDefault();
    }
}
