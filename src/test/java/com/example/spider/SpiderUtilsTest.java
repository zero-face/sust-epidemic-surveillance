package com.example.spider;

import com.example.epidemicsurveillance.utils.spider.SpiderEpidemicDataUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @ClassName SpiderUtilsTest
 * @Author 朱云飞
 * @Date 2021/10/14 10:47
 * @Version 1.0
 **/
public class SpiderUtilsTest {

    @Test
    public void test() throws IOException {
        SpiderEpidemicDataUtils spiderUtils=new SpiderEpidemicDataUtils();
        String dataJson = spiderUtils.getGlobalDataJson("https://api.inews.qq.com/newsqa/v1/automation/modules/list?modules=FAutoCountryConfirmAdd,WomWorld,WomAboard");
        System.out.println(dataJson);
    }
}
