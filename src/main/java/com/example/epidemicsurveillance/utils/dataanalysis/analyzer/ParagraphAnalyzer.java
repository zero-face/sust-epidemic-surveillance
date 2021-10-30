package com.example.epidemicsurveillance.utils.dataanalysis.analyzer;

import com.example.epidemicsurveillance.utils.dataanalysis.ParagraphData;

import java.util.Map;

/**
 * @author Zero
 * @date 2021/10/28 0:44
 * @description
 * @since 1.8
 **/
public interface ParagraphAnalyzer {
    ParagraphData analysis(String text,Integer order);
}
