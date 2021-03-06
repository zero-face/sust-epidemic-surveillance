package com.example;

import com.example.epidemicsurveillance.service.ICityPolicyService;
import com.example.epidemicsurveillance.spider.SpiderToGetData;
import com.example.epidemicsurveillance.task.domestic_data_task.DomesticDataTask;
import com.example.epidemicsurveillance.utils.nat_facility.NATUtil;
import com.example.epidemicsurveillance.utils.spider.SpiderEpidemicDataUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SpringBootTest
class EpidemicSurveillanceApplicationTests {

    @Autowired
    private SpiderToGetData spiderToGetData;

    @Autowired
    private ICityPolicyService cityPolicyService;

    @Autowired
    private DomesticDataTask domesticDataTask;

    @Autowired
    private SpiderEpidemicDataUtils spiderEpidemicDataUtils;


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

    @Test
    void test2() {
        String date = LocalDate.now().toString();
        System.out.println(date.substring(0,date.lastIndexOf("-")));
    }

    @Autowired
    private NATUtil NATUtil;
    @Test
    void test3() {
        //获取shuju
        final String s = NATUtil.PlacesFind("陕西省", "西安市", 2, "5");
        System.out.println(s);
    }

    @Test
    void testDomesticDataTask() {
        domesticDataTask.getChinaEpidemicData();
    }

    @Test
    void regex() {
        String regex = "\\d+";
        String str = "aaa2223bb";
        final Pattern compile = Pattern.compile(regex);
        final Matcher matcher = compile.matcher(str);
//        System.out.println(matcher.group(0));
        System.out.println(matcher.group());
//        System.out.println(matcher.group(1));
//        System.out.println(matcher.group(2));

    }

    @Test
    void spider(){
        System.out.println(spiderEpidemicDataUtils.getChinaDataJson("https://view.inews.qq.com/g2/getOnsInfo?name=disease_h5&callback=jQuery35108028357975576601_1634228907365&_=1634228907366"));
    }
}
