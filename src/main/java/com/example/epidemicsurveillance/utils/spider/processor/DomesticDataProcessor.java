package com.example.epidemicsurveillance.utils.spider.processor;

import com.alibaba.fastjson.JSON;
import com.example.epidemicsurveillance.entity.DomesticData;
import com.example.epidemicsurveillance.utils.dataanalysis.DataAnalyzer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Zero
 * @date 2021/10/25 18:29
 * @description
 * @since 1.8
 **/
@Slf4j
@Component
public class DomesticDataProcessor implements PageProcessor {

    @Override
    public void process(Page page) {
        String date = LocalDate.now().toString();
        String regexDay =date.substring(date.lastIndexOf("-") + 1);
        final String regexDate = date.substring(0,date.lastIndexOf("-"));

        final List<Selectable> nodes = page.getHtml().xpath("//*[@id=\"tab-1\"]").links().regex("http://www.gov.cn/xinwen/" + regexDate + "/" + regexDay + "/.*?.htm$").nodes();
        List<String> accessUrl = new ArrayList<>();
        if(nodes.size() > 0) {
            for(int i = 0; i < nodes.size(); i++) {
                accessUrl.add(nodes.get(i).get());
            }
            page.addTargetRequests(accessUrl);
        }
        String title = page.getHtml().xpath("/html/head/title/text()").toString();
        log.info("title:{}",title);
        if(title.startsWith("截至")){
        /*    page.getHtml().xpath("//*[@id=\"UCAP-CONTENT\"]/p/text()").all().stream().forEach(paragraph -> {
                paragraph.replaceAll(System.getProperty("line.separator"),"");
                context.append(paragraph);
            });
            log.info("获取的文本：{}",context);*/
            final String first = page.getHtml().xpath("//*[@id=\"UCAP-CONTENT\"]/p[1]/text()").toString();
            final String second = page.getHtml().xpath("//*[@id=\"UCAP-CONTENT\"]/p[2]/text()").toString();
            final String third = page.getHtml().xpath("//*[@id=\"UCAP-CONTENT\"]/p[3]/text()").toString();
            final String four = page.getHtml().xpath("//*[@id=\"UCAP-CONTENT\"]/p[4]/text()").toString();
            final String five = page.getHtml().xpath("//*[@id=\"UCAP-CONTENT\"]/p[5]/text()").toString();
            final String six = page.getHtml().xpath("//*[@id=\"UCAP-CONTENT\"]/p[6]/text()").toString();
            final DataAnalyzer dataAnalyzer = new DataAnalyzer();
            final Map<String, Integer> todayData = dataAnalyzer.put(title, first, second, third, four, five, six)
                    .startAnalyze()
                    .todayData();
            System.out.println(todayData);
            page.putField("todayData", todayData);
        }

    }

    @Override
    public Site getSite() {
        return Site.me()
                .setRetryTimes(3)
                .setSleepTime(3000);
    }


}
