package com.example.epidemicsurveillance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.epidemicsurveillance.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.epidemicsurveillance.entity.query.ArticleDataQuery;
import com.example.epidemicsurveillance.response.ResponseResult;

/**
 * <p>
 * 文章数据表 服务类
 * </p>
 *
 * @author zyf
 * @since 2021-10-16
 */
public interface IArticleService extends IService<Article> {

    void pageQuery(Page<Article> pageResult, ArticleDataQuery articleDataQuery);

    ResponseResult getArticleListByType(Integer type);

    ResponseResult getArticleById(Integer id);
}
