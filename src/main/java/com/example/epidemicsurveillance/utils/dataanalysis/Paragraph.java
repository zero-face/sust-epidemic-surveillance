package com.example.epidemicsurveillance.utils.dataanalysis;

import java.util.Map;

/**
 * @author Zero
 * @date 2021/10/27 21:20
 * @description
 * @since 1.8
 **/
public class Paragraph {
    private Map<String,String> dataPair;

    private String order;

    private String value;

    public Paragraph(String text,Integer order) {
        this.value = text;
    }


}
