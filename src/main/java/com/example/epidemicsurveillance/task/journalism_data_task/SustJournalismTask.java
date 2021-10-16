package com.example.epidemicsurveillance.task.journalism_data_task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.epidemicsurveillance.entity.Article;
import com.example.epidemicsurveillance.service.IArticleService;
import com.example.epidemicsurveillance.spider.SpiderToGetData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 爬取Sust新闻定时任务
 * @ClassName SustJournalismTask
 * @Author 朱云飞
 * @Date 2021/10/16 16:38
 * @Version 1.0
 **/
@Component
public class SustJournalismTask {
    @Autowired
    private SpiderToGetData spiderToGetDatal;

    @Autowired
    private IArticleService iArticleService;

    //@Scheduled(cron = "0 0 3 * * ? ")//每日凌晨两点十五执行
    @Scheduled(cron = "* 15 2 * * ? *")
    public void getSustJournalismData(){
        //删除昨日热点数据
        QueryWrapper<Article> wrapper=new QueryWrapper<>();
        wrapper.eq("type",1);
        iArticleService.remove(wrapper);
        //获取今日热点数据
        spiderToGetDatal.getSustJournalism();
    }
}
