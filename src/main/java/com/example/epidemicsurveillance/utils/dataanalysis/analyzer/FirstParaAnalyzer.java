package com.example.epidemicsurveillance.utils.dataanalysis.analyzer;

import com.example.epidemicsurveillance.utils.dataanalysis.ParagraphData;
import com.example.epidemicsurveillance.utils.dataanalysis.analyzer.ParagraphAnalyzer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Zero
 * @date 2021/10/28 0:47
 * @description
 * @since 1.8
 **/
@Component
@Slf4j
public class FirstParaAnalyzer implements ParagraphAnalyzer {

    @Override
    public ParagraphData analysis(String text,Integer order) {
        if(text == null) {
            return new ParagraphData(order);
        }
        final ParagraphData data = new ParagraphData(order);
        Map<String,Integer> params = new HashMap<>();
        //分割每一句话
        final String[] words = text.split("。");
        log.info("分割成为{}段",words.length);
        String regex1= "(新增确诊病例)(\\d+)";
        final Matcher matcher = Pattern.compile(regex1).matcher(words[0]);
        if(matcher.find()) {
            final Integer newDiagnosis = Integer.valueOf(matcher.group(2));
            params.put("newDiagnosis",newDiagnosis);
        }
        return data;
    }
}
