package com.example.epidemicsurveillance.utils.dataanalysis;

import com.example.epidemicsurveillance.utils.dataanalysis.exception.DataAnalysisException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Zero
 * @date 2021/10/27 21:08
 * @description
 * @since 1.8
 **/
public class DataAnalyzer {

    private String[] paragraphs;

    private String title;

    private Integer paragraphNum = 6;

    public DataAnalyzer put(String title,String text) {
        if(text != null && title != null) {
//            this.paragraphs = text;
            this.title = title;
            return this;
        }
        return null;
    }
    public DataAnalyzer put(String title,String ...paragraph) {
        if(title !=null && paragraph.length != 0) {
            this.title = title;
            this.paragraphs = paragraph;
            return this;
        }
        return null;
    }

    /**
     * 开始分析
     */
    public void startAnalyze() {
        if(this.paragraphs == null) {
            throw new DataAnalysisException("The analyzed data cannot be empty");
        }
        //第一部分分析
        paragraphAnalyze(paragraphs);
    }

    /**
     * 处理每一段
     * @param paragraphs
     */
    private void paragraphAnalyze(String[] paragraphs) {
        final List<Object> list = IntStream.range(0, paragraphs.length)
                .mapToObj(i -> handle(paragraphs[i], i + 1))
                .collect(Collectors.toList());
    }

    private Object handle(String text, int order) {
        final Paragraph paragraph = new Paragraph(text, order);
        if(order == 1) {

        } else if(order == 2){

        } else {

        }
        return null;
    }

}
