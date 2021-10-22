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
public class SpiderEpidemicDataUtils {
    @Autowired
    private CloseableHttpClient httpClient;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 爬取全球疫情数据JSON
     * @param url
     * @return
     */
    public String getGlobalDataJson(String url) {
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

    /**
     * 爬取中国疫情数据
     */
    public String getChinaDataJson(String url){
        try {
            HttpGet httpGet=new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = response.getEntity();
                //爬取的JSON数据有误，这里进行处理
                String context = EntityUtils.toString(httpEntity, "utf8").replaceAll("\\\\","");
                int areaTree = context.indexOf("areaTree");
                String substring = "{"+context.substring(areaTree - 1, context.length() - 3);
                return substring;
            }else {
                throw new EpidemicException("数据获取失败,url是"+url);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new EpidemicException("数据获取失败,url是"+url);
        }
    }


}
