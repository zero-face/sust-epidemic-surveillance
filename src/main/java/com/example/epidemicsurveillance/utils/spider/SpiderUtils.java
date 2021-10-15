package com.example.epidemicsurveillance.utils.spider;

import com.example.epidemicsurveillance.exception.EpidemicException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @ClassName SpiderUtils
 * @Author 朱云飞
 * @Date 2021/10/14 10:25
 * @Version 1.0
 **/
@Component
public class SpiderUtils {
    @Autowired
    private CloseableHttpClient httpClient;

    @Autowired
    private ObjectMapper objectMapper;

    public String getDataJson(String url) {
        try {
            HttpGet httpGet=new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = response.getEntity();
                String context = EntityUtils.toString(httpEntity, "utf8");
                return context;
            }else {
                throw new EpidemicException("数据获取失败,url是"+url);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new EpidemicException("数据获取失败,url是"+url);
        }
    }

}
