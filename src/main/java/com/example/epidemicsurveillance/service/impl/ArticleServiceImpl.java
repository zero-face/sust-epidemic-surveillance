package com.example.epidemicsurveillance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.epidemicsurveillance.entity.Article;
import com.example.epidemicsurveillance.entity.query.ArticleDataQuery;
import com.example.epidemicsurveillance.mapper.ArticleMapper;
import com.example.epidemicsurveillance.response.ResponseResult;
import com.example.epidemicsurveillance.service.IArticleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 文章数据表 服务实现类
 * </p>
 *
 * @author zyf
 * @since 2021-10-16
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public void pageQuery(Page<Article> pageResult, ArticleDataQuery articleDataQuery) {
        if(articleDataQuery == null){
            articleMapper.selectPage(pageResult,null);
            return;
        }
        QueryWrapper<Article> wrapper=new QueryWrapper<>();
        String title=articleDataQuery.getTitle();
        Integer type=articleDataQuery.getType();
        String begin=articleDataQuery.getBegin();
        String end=articleDataQuery.getEnd();
        if(title !=null){
            wrapper.like("title",title);
        }
        if(type != 0){
            wrapper.eq("type",type);
        }
        if (!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin);
        }

        if (!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create", end);
        }
        articleMapper.selectPage(pageResult,wrapper);
    }

    @Override
    public ResponseResult getArticleListByType(Integer type) {
        List<Article> list=new LinkedList<>();
        if(type == 0){//今日资讯
            QueryWrapper<Article> wrapper=new QueryWrapper<>();
            wrapper.eq("type",4);
            wrapper.orderByDesc("time");
            list=articleMapper.selectList(wrapper);
        }else if(type == 1){//科大要闻
            QueryWrapper<Article> wrapper=new QueryWrapper<>();
            wrapper.eq("type",1);
            wrapper.orderByDesc("sort");
            list=articleMapper.selectList(wrapper);
        }else if(type == 2){//通知公告
            QueryWrapper<Article> wrapper=new QueryWrapper<>();
            wrapper.eq("type",2);
            wrapper.orderByDesc("time");
            list=articleMapper.selectList(wrapper);
        }
        return ResponseResult.ok().data("list",list);
    }

    @Override
    public ResponseResult getArticleById(Integer id) {
        Article article=articleMapper.selectById(id);
        return ResponseResult.ok().data("article",article);
    }
}
