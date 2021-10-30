package com.example.epidemicsurveillance.utils.dataanalysis.analyzer;

import com.example.epidemicsurveillance.utils.dataanalysis.ParagraphData;

/**
 * @author Zero
 * @date 2021/10/29 21:34
 * @description
 * @since 1.8
 **/
public class SecondAnalyzer implements ParagraphAnalyzer{
    @Override
    public ParagraphData analysis(String text, Integer order) {
        return new ParagraphData(order);
    }
}
