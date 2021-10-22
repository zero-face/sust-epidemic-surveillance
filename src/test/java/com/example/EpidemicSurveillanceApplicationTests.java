package com.example;

import com.example.epidemicsurveillance.spider.SpiderToGetData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;


@SpringBootTest
class EpidemicSurveillanceApplicationTests {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SpiderToGetData spiderToGetData;

    @Test
    void contextLoads() {
    }
    @Test
    void test() throws URISyntaxException {
        spiderToGetData.getCityCOde("http://www.mca.gov.cn/article/sj/xzqh/2020/20201201.html");
    }
}
