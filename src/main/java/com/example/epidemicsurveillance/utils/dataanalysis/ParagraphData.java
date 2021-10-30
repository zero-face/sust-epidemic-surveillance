package com.example.epidemicsurveillance.utils.dataanalysis;

import java.util.Map;

/**
 * @author Zero
 * @date 2021/10/28 23:53
 * @description
 * @since 1.8
 **/

public class ParagraphData {
    private Integer order;

    private Map<String,Integer> data;

    public ParagraphData() {
    }

    public ParagraphData(Integer order) {
        this.order = order;
    }

    public ParagraphData(Integer order, Map<String, Integer> data) {
        this.order = order;
        this.data = data;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Map<String, Integer> getData() {
        return data;
    }

    public void setData(Map<String, Integer> data) {
        this.data = data;
    }
}
