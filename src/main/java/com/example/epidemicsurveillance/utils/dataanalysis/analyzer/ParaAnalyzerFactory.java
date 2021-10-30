package com.example.epidemicsurveillance.utils.dataanalysis.analyzer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Zero
 * @date 2021/10/28 23:24
 * @description
 * @since 1.8
 **/
public class ParaAnalyzerFactory {
    private static final Map<Integer,ParagraphAnalyzer> cacheAnalyzer = new HashMap<>();
    static {
        cacheAnalyzer.put(1, new FirstParaAnalyzer());
        cacheAnalyzer.put(2, new SecondAnalyzer());
        cacheAnalyzer.put(3, new ThirdAnalyzer());
        cacheAnalyzer.put(4, new fourAnalyzer());
        cacheAnalyzer.put(5, new FiveAnalyzer());
        cacheAnalyzer.put(6, new sixAnalyzer());
    }
    public static ParagraphAnalyzer createAnalyzer(Integer id) {
        if(id == null) {
            return null;
        }
        return cacheAnalyzer.get(id);
    }
}
