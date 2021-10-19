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
 * 爬取科大新闻
 * @ClassName SustProcessor
 * @Author 朱云飞
 * @Date 2021/10/16 16:11
 * @Version 1.0
 **/
@Component
public class SustProcessor implements PageProcessor {
    @Autowired
    private IArticleService articleService;

    @Autowired
    private SpiderErrorSendMailToAdmin spiderErrorSendMailToAdmin;

    @Transactional
    @Override
    public void process(Page page) {
        // 筛选链接
        List<Selectable> tables = page.getHtml().css("table").links().regex("https://www.sust.edu.cn/info/1071/.*htm$").nodes();
        List<String> urlList=new LinkedList<String>();
        if(tables.size() != 0){
            //需要爬取的页面
            for (Selectable table:tables ) {
                urlList.add(table.get());
            }
            page.addTargetRequests(urlList);
        }else {
            //需要解析的页面
            String title = page.getHtml().css("title").toString();
            String html = page.getHtml().css("div#vsb_content_2").toString();
            String content = html.replaceAll("src=\"", "src=\"https://www.sust.edu.cn");
            String url = page.getUrl().toString();
            int start = url.lastIndexOf('/');
            int end = url.lastIndexOf('.');
            int sort = Integer.parseInt(url.substring(start + 1, end));
            Article article=new Article();
            article.setTitle(title);
            article.setUrl(url);
            article.setContent(content);
            article.setSort(sort);
            articleService.save(article);
        }
    }

    private Site site = Site.me()
            .setCharset("utf8") // 设置编码
            .setRetrySleepTime(3000) // 设置重试的间隔时间
            .setSleepTime(5); // 设置重试次数

    @Override
    public Site getSite() {
        return site;
    }
}
