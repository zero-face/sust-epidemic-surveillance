package com.example.epidemicsurveillance.utils.spider.processor;

import com.example.epidemicsurveillance.entity.Article;
import com.example.epidemicsurveillance.service.IArticleService;
import com.example.epidemicsurveillance.utils.rabbitmq.spider.SpiderErrorSendMailToAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName RealTimeInfoProcessor
 * @Author 朱云飞
 * @Date 2021/10/18 10:27
 * @Version 1.0
 **/
@Component
public class RealTimeInfoProcessor implements PageProcessor {
    @Autowired
    private IArticleService iArticleService;

    @Autowired
    private SpiderErrorSendMailToAdmin spiderErrorSendMailToAdmin;

    @Transactional
    @Override
    public void process(Page page) {
        List<Selectable> selectableList = page.getHtml().css("div.right_list").links().nodes();
        if(selectableList.size() !=0 ){
            List<String> urlList=new LinkedList<>();
            //需要爬取的页面
            for (Selectable selectable:selectableList) {
                urlList.add(selectable.get());
            }
            page.addTargetRequests(urlList);
        }else {
            //需要解析的页面
            String contentTemp=page.getHtml().css("div#zoom").get();
            String timeTemp = page.getHtml().css("div.pages_zz span").get();
            if (contentTemp != null && timeTemp != null) {
                int start=timeTemp.indexOf(">");
                int end=timeTemp.indexOf("/");
                String time =timeTemp.substring(start+1,end-1);

                int pos = contentTemp.indexOf("<div class=\"fujian-box\">");
                String content=contentTemp.substring(0,pos-3);
                String title=page.getHtml().css("title").get();
                String url=page.getUrl().toString();
                Article article=new Article();
                article.setType(4);
                article.setTitle(title);
                article.setContent(content);
                article.setTime(time);
                article.setUrl(url);
                iArticleService.save(article);
            }
        }
    }

    private Site site = Site.me()
            .setCharset("utf8") // 设置编码
            .setRetrySleepTime(3000) // 设置重试的间隔时间
            .setSleepTime(5); // 设置重试次数

    @Override
    public Site getSite() {
        return this.site;
    }
}
