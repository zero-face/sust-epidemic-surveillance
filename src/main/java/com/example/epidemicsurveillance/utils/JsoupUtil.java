package com.example.epidemicsurveillance.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * @Author Zero
 * @Date 2021/10/10 22:38
 * @Since 1.8
 * @Description
 **/
public class JsoupUtil {
    public void getHtml(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            System.out.println(document.title());
            final Elements li = document.getElementsByTag("div");
            for(Element e:li) {
                System.out.println(e.text());
            }
//            System.out.println(document.body());
        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        final JsoupUtil jsoupUtil = new JsoupUtil();
        jsoupUtil.getHtml("https://voice.baidu.com/act/newpneumonia/newpneumonia/?from=osari_pc_3#tab4");

    }
}
