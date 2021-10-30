package com.example.epidemicsurveillance.utils.dataanalysis.analyzer;

import com.example.epidemicsurveillance.utils.dataanalysis.ParagraphData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Zero
 * @date 2021/10/29 14:47
 * @description
 * @since 1.8
 **/
@Component
@Slf4j
public class fourAnalyzer implements ParagraphAnalyzer {

    @Override
    public ParagraphData analysis(String text, Integer order) {
        if(null == text) {
            return new ParagraphData(order);
        }
        final ParagraphData data = new ParagraphData(order);
        Map<String,Integer> params = new HashMap<>();
        String regex = "([^\\x00-\\xff]+)(\\d+)";
        final Matcher matcher = Pattern.compile(regex).matcher(text);
        List<Integer> list = new ArrayList<>();
        while(matcher.find()) {
            if(matcher.group(1).contains("病例")) {
                list.add(Integer.valueOf(matcher.group(2)));
            }
        }
        if(list.size() > 0) {
            //内地现有确诊
            params.put("nowDiagnosis", list.get(0));
            //重症
            params.put("severeCase", list.get(1));
            //内地累计治愈
            params.put("cumulativeCure", list.get(2));
            //内地累计死亡
            params.put("cumulativeDead",list.get(3));
            //内地累计确诊
            params.put("cumulativeDiagnosis",list.get(4));
            //现有疑似
            params.put("nowSuspected",list.get(5));
        }

        data.setData(params);
        return data;
    }
}
