package com.example.epidemicsurveillance.utils.dataanalysis;

import org.springframework.stereotype.Component;

/**
 * @author Zero
 * @date 2021/10/27 0:16
 * @description
 * @since 1.8
 **/
@Component
public class GangAoTaiDataUtil {

    private String[] extractData(String text) {
        return text.split("\\D+");
    }

    public Integer getAllReport(String text) {
        final String[] allData = extractData(text);
        return Integer.valueOf(allData[0]);
    }

    public Integer getHongKongReport(String text) {
        final String[] allData = extractData(text);
        return Integer.valueOf(allData[1]);
    }
    public Integer getHongKongCure(String text) {
        final String[] allData = extractData(text);
        return Integer.valueOf(allData[2]);
    }
    public Integer getHongKongDead(String text) {
        final String[] allData = extractData(text);
        return Integer.valueOf(allData[3]);
    }
    public Integer getMacauReport(String text) {
        final String[] allData = extractData(text);
        return Integer.valueOf(allData[4]);
    }
    public Integer getMacauCure(String text) {
        final String[] allData = extractData(text);
        return Integer.valueOf(allData[5]);
    }
    public Integer getMacauDead(String text) {
        final String[] allData = extractData(text);
        return Integer.valueOf(allData[4]) - Integer.valueOf(allData[5]);
    }
    public Integer TaiwanReport(String text) {
        final String[] allData = extractData(text);
        return Integer.valueOf(allData[6]) ;
    }
    public Integer TaiwanCure(String text) {
        final String[] allData = extractData(text);
        return Integer.valueOf(allData[7]) ;
    }
    public Integer TaiwanDead(String text) {
        final String[] allData = extractData(text);
        return Integer.valueOf(allData[8]) ;
    }



}
