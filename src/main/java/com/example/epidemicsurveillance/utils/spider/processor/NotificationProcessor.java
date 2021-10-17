package com.example.epidemicsurveillance.utils.spider.processor;

import com.example.epidemicsurveillance.entity.Article;
import com.example.epidemicsurveillance.service.IArticleService;
import com.example.epidemicsurveillance.utils.rabbitmq.spider.SpiderErrorSendMailToAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.time.LocalDateTime;

/**
 * @ClassName NotificationProcessor
 * @Author 朱云飞
 * @Date 2021/10/17 16:25
 * @Version 1.0
 **/
@Component
public class NotificationProcessor implements PageProcessor {

    @Autowired
    private IArticleService articleService;

    @Autowired
    private SpiderErrorSendMailToAdmin spiderErrorSendMailToAdmin;

    @Override
    public void process(Page page) {
        try {
            String title=page.getHtml().css("title").toString().replaceAll("_腾讯新闻","");
            String context=page.getHtml().css("div.content-article").toString();
            String url=page.getUrl().toString();
            String time = LocalDateTime.now().toString();
            Article article=new Article();
            article.setTitle(title);
            article.setContent(context);
            article.setUrl(url);
            article.setTime(time);
            if(context.length() < 1){
                spiderErrorSendMailToAdmin.sendEmailToAdmin("2690534598@qq.com","爬取最新疫情通告失败,url是"+url);
            }
            articleService.save(article);
        } catch (Exception e) {
            e.printStackTrace();
            spiderErrorSendMailToAdmin.sendEmailToAdmin("2690534598@qq.com","爬取最新疫情通告出错");
        }
    }

    private Site site = Site.me()
            .setCharset("UTF-8") // 设置编码
            .setRetrySleepTime(3000) // 设置重试的间隔时间
            .setSleepTime(5); // 设置重试次数
    @Override
    public Site getSite() {
        return this.site;
    }
}
