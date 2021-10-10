package com.example.epidemicsurveillance.config.request;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @Author Zero
 * @Date 2021/8/25 22:42
 * @Since 1.8
 * @Description
 **/
@Configuration
public class RestTemplateConfig {

    @Autowired
    private MyFormHttpMessageConverter myFormHttpMessageConverter;

    @Bean
    public RestTemplate getRestTemplate(){
        RestTemplate restTemplate = new RestTemplate();

        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(2000);
        httpRequestFactory.setConnectTimeout(10000);
        httpRequestFactory.setReadTimeout(72000);
        HttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new DefaultRedirectStrategy()).build();
        //设置重定向策略为默认策略
        httpRequestFactory.setHttpClient(httpClient);
        restTemplate.setRequestFactory(httpRequestFactory);

        List<HttpMessageConverter<?>> httpMessageConverterList = restTemplate.getMessageConverters();
        //提供将map类型转换为UrlEncoded能力
        httpMessageConverterList.add(myFormHttpMessageConverter);
        restTemplate.setMessageConverters(httpMessageConverterList);

//        restTemplate.setInterceptors(Collections.singletonList(httpRequestInterceptor));
        return restTemplate;
    }

}
