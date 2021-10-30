package com.example.epidemicsurveillance.utils.dataanalysis.analyzer;

import com.example.epidemicsurveillance.utils.dataanalysis.ParagraphData;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Zero
 * @date 2021/10/28 23:36
 * @description 镜外输入段
 * @since 1.8
 **/
public class ThirdAnalyzer implements ParagraphAnalyzer{
    @Override
    public ParagraphData analysis(String text,Integer order) {
        if(text == null) {
            return new ParagraphData(order);
        }
        Map<String,Integer> params = new HashMap<>();
        final ParagraphData data = new ParagraphData(order);
        String regex = "(累计确诊病例)(\\d+)";
        final Matcher matcher = Pattern.compile(regex).matcher(text);
        while(matcher.find()) {
            final Integer outside = Integer.valueOf(matcher.group(2));
            params.put("outside", outside);
        }
        data.setData(params);
        return data;
    }
}
