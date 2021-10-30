package com.example.epidemicsurveillance.utils.dataanalysis.analyzer;

import com.example.epidemicsurveillance.utils.dataanalysis.ParagraphData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Zero
 * @date 2021/10/29 21:12
 * @description
 * @since 1.8
 **/
@Component
@Slf4j
public class FiveAnalyzer implements ParagraphAnalyzer{
    @Override
    public ParagraphData analysis(String text, Integer order) {
        if(text == null) {
            return new ParagraphData(order);
        }
        Map<String,Integer> params = new HashMap<>();
        final ParagraphData data = new ParagraphData(order);
        String regex = "(尚在医学观察的无症状感染者)(\\d+)";
        final Matcher matcher = Pattern.compile(regex).matcher(text);
        if(matcher.find()) {
            final String group = matcher.group(2);
            params.put("asymptomatic", Integer.valueOf(group));
        }
        data.setData(params);
        return data;
    }
}
