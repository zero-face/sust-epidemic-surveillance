package com.example.epidemicsurveillance.service.impl;

import com.example.epidemicsurveillance.entity.Article;
import com.example.epidemicsurveillance.mapper.ArticleMapper;
import com.example.epidemicsurveillance.service.IArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
