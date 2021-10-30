package com.example.epidemicsurveillance.utils.dataanalysis;

import com.example.epidemicsurveillance.utils.dataanalysis.analyzer.ParaAnalyzerFactory;
import com.example.epidemicsurveillance.utils.dataanalysis.analyzer.ParagraphAnalyzer;
import com.example.epidemicsurveillance.utils.dataanalysis.exception.DataAnalysisException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Zero
 * @date 2021/10/27 21:08
 * @description TODO: 受文章结构影响，暂未实现全文输入处理
 * @since 1.8
 **/
public class DataAnalyzer {

    private String[] paragraphs;

    private String title;

    private Integer paragraphNum = 6;

    private List<ParagraphData> paraData;


    @Deprecated
    public DataAnalyzer put(String title,String text) {
        if(text != null && title != null) {
//            this.paragraphs = text;
            this.title = title;
            return this;
        }
        return null;
    }

    /**
     * 将段落依次放入分析器中
     * @param title
     * @param paragraph
     * @return
     */
    public DataAnalyzer put(String title,String ...paragraph) {
        if(title !=null && paragraph.length != 0) {
            this.title = title;
            this.paragraphs = paragraph;
            this.paragraphNum = paragraph.length;
            return this;
        }
        return null;
    }

    /**
     * 开始进行分析
     * @return
     */
    public DataAnalyzer startAnalyze() {
        if(this.paragraphs == null || this.paragraphs.length == 0) {
            throw new DataAnalysisException("The analyzed data cannot be empty");
        }
        //顺序流处理
        this.paraData = IntStream.range(0, paragraphNum)
                .mapToObj(i -> handle(paragraphs[i], i + 1))
                .collect(Collectors.toList());
        return this;
    }

    public Map<String,Integer> todayData() {
        Map<String,Integer> maps = new HashMap<>();
        //第五段
        final Integer asymptomatic = paraData.get(4).getData().get("asymptomatic");
        //第三段
        final Integer outside = paraData.get(2).getData().get("outside");
        //国内现有疫情
        final Map<String, Integer> fourData = paraData.get(3).getData();
        //国内现存确诊
        final Integer nowDiagnosis = fourData.get("nowDiagnosis");
        final Integer nowSuspected = fourData.get("nowSuspected");
        final Integer existingSevere = fourData.get("severeCase");
        final Integer cumulativeConfirmed = fourData.get("cumulativeDiagnosis");
        final Integer cumulativeCure = fourData.get("cumulativeCure");
        final Integer cumulativeDead = fourData.get("cumulativeDead");

        //港澳台地区现有疫情
        final Map<String, Integer> sixData = paraData.get(5).getData();
        final Integer hnowDiagnosis = sixData.get("HnowDiagnosis");
        final Integer mnowDiagnosis = sixData.get("MnowDiagnosis");
        final Integer tnowDiagnosis = sixData.get("TnowDiagnosis");
        //上报确诊
        final Integer THMcumulativeDiagnosis = sixData.get("THMcumulativeDiagnosis");
        //三地治愈
        final Integer HcumulativeCure = sixData.get("HcumulativeCure");
        final Integer McumulativeCure = sixData.get("McumulativeCure");
        final Integer TcumulativeCure = sixData.get("TcumulativeCure");
        //三地死亡
        final Integer hcumulativeDead = sixData.get("HcumulativeDead")==null? 0: sixData.get("HcumulativeDead");
        final Integer McumulativeDead = sixData.get("McumulativeDead")==null? 0: sixData.get("McumulativeDead");
        final Integer TcumulativeDead = sixData.get("TcumulativeDead")==null? 0: sixData.get("TcumulativeDead");

        Integer THMcumulativeDead = hcumulativeDead + McumulativeDead +TcumulativeDead;

        //无症状
        maps.put("asymptomatic",asymptomatic);
        //镜外输入确诊
        maps.put("outsideInput", outside);
        //现有确诊
        maps.put("existingConfirmed", nowDiagnosis + hnowDiagnosis + mnowDiagnosis + tnowDiagnosis);
        //现有疑似
        maps.put("existingSuspected", nowSuspected);
        //现有重症
        maps.put("existingSevere", existingSevere);
        //累计确诊
        maps.put("cumulativeConfirmed", cumulativeConfirmed + THMcumulativeDiagnosis);
        //累计治愈
        maps.put("cumulativeCure", TcumulativeCure + McumulativeCure + HcumulativeCure + cumulativeCure);
        //累计死亡
        maps.put("cumulativeDeath", cumulativeDead + THMcumulativeDead);
        return maps;
    }
    /**
     * 分析处理每个段落的文本
     * @param text
     * @param order
     * @return
     */
    private ParagraphData handle(String text, int order) {
        final ParagraphAnalyzer analyzer = ParaAnalyzerFactory.createAnalyzer(order);
        return analyzer.analysis(text,order);
    }

    /**
     * 获取指定段落的分析数据
     * @param order
     * @return
     */
    public Map<String,Integer> getParaData(Integer order) {
        if(order == null || order <= 0 || order > paragraphNum) {
            return null;
        }
        return this.paraData.get(order - 1).getData();
    }

    /**
     * 获取处理段落数
     * @return
     */
    public Integer getAnalysisParaNum() {
        return this.paragraphNum;
    }

}
