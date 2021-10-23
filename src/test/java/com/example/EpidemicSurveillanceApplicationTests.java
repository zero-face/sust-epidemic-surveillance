package com.example;

import com.example.epidemicsurveillance.service.ICityPolicyService;
import com.example.epidemicsurveillance.spider.SpiderToGetData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URISyntaxException;


@SpringBootTest
class EpidemicSurveillanceApplicationTests {

    @Autowired
    private SpiderToGetData spiderToGetData;

    @Autowired
    private ICityPolicyService cityPolicyService;


    @Test
    void getCityCodeTest() {
        spiderToGetData.getCityCOde("http://www.mca.gov.cn/article/sj/xzqh/2020/20201201.html");
    }
    @Test
    void preventionPolicy() {
//        cityPolicyService.GuideData("西安市");
    }

    @Test
    void test1() {
        System.out.println(System.currentTimeMillis());
    }

}
