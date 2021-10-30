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
 * @date 2021/10/29 15:04
 * @description
 * @since 1.8
 **/
@Component
@Slf4j
public class sixAnalyzer implements ParagraphAnalyzer {

    @Override
    public ParagraphData analysis(String text, Integer order) {
        if(null == text) {
            return new ParagraphData(order);
        }
        final ParagraphData data = new ParagraphData(order);
        Map<String,Integer> params = new HashMap<>();
        final String[] split = text.split("。");
        String regex = "([^\\x00-\\xff]+)(\\d+)";
        final Matcher matcher = Pattern.compile(regex).matcher(split[0]);
        while(matcher.find()) {
            params.put("THMcumulativeDiagnosis",Integer.valueOf(matcher.group(2)));
        }
        final String[] split1 = split[1].split("）");
        //处理香港地区
        final List<Integer> HongKong = preHMT(split1[0], regex);
        if(HongKong.size() > 0) {
            if(HongKong.size() == 4) {
                params.put("HcumulativeDead", HongKong.get(2));
            }
            params.put("HcumulativeDiagnosis",HongKong.get(0));
            params.put("HcumulativeCure", HongKong.get(1));
            params.put("HnowDiagnosis",HongKong.get(HongKong.size() - 1));
        }


        //澳门地区
        final List<Integer> Macao = preHMT(split1[1], regex);
        if(Macao.size() > 0) {
            if(Macao.size() == 4) {
                params.put("McumulativeDead", Macao.get(2));
            }
            params.put("McumulativeDiagnosis",Macao.get(0));
            params.put("McumulativeCure", Macao.get(1));
            params.put("MnowDiagnosis",Macao.get(Macao.size() - 1));
        }


        //台湾地区
        final List<Integer> TaiWan = preHMT(split1[2], regex);
        if(TaiWan.size() > 0) {
            if(TaiWan.size() == 4) {
                params.put("TcumulativeDead", TaiWan.get(2));
            }
            params.put("TcumulativeDiagnosis",TaiWan.get(0));
            params.put("TcumulativeCure", TaiWan.get(1));
            params.put("TnowDiagnosis",TaiWan.get(TaiWan.size() - 1));
        }

        data.setData(params);
        return data;
    }

    /**
     * 处理地区
     * @param s
     * @param regex
     * @return
     */
    private List<Integer> preHMT(String s, String regex) {
        final Matcher matcher = Pattern.compile(regex).matcher(s);
        List<Integer> list = new ArrayList<>();
        while(matcher.find()) {
            list.add(Integer.valueOf(matcher.group(2)));
        }
        if(list.size() > 0) {
            int index= list.get(0);
            int sum = 0;
            for(int i = 1; i < list.size();i++) {
                sum += list.get(i);
            }
            list.add(index-sum);
        }
        return list;
    }
}
