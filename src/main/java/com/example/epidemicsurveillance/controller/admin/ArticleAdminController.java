package com.example.epidemicsurveillance.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.epidemicsurveillance.entity.Article;
import com.example.epidemicsurveillance.entity.query.ArticleDataQuery;
import com.example.epidemicsurveillance.exception.EpidemicException;
import com.example.epidemicsurveillance.response.ResponseResult;
import com.example.epidemicsurveillance.service.IArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName ArticleAdminController
 * @Author 朱云飞
 * @Date 2021/10/19 13:42
 * @Version 1.0
 **/
@RequestMapping("/api/v1/admin/article")
@CrossOrigin
@RestController
@Api(tags = "后台新闻资讯管理模块")
public class ArticleAdminController {
    @Autowired
    private IArticleService iArticleService;

    @ApiOperation(value = "分页查询新闻资讯列表")
    @PostMapping("getAllArticles/{page}/{limit}")
    public ResponseResult getAllArticles(@ApiParam(name = "page",value = "当前页号",required = true) @PathVariable Long page,
                                         @ApiParam(name = "limit" ,value = "每页数据量",required = true) @PathVariable Long limit,
                                         @ApiParam(name = "articleDataQuery",value = "新闻资讯查询类",required = false)@RequestBody ArticleDataQuery articleDataQuery){
        // 创建分页对象
        Page<Article> pageResult=new Page<>(page,limit);
        //将查询的数据放入pageResult中
        iArticleService.pageQuery(pageResult, articleDataQuery);
        long total=pageResult.getTotal();//数据总数
        List<Article> list=pageResult.getRecords();//EduCourse的list集合
        return ResponseResult.ok().data("total",total).data("rows",list);
    }

    @ApiOperation(value = "根据文章Id删除文章")
    @DeleteMapping("deleteArticleById/{articleId}")
    public ResponseResult deleteArticleById(@ApiParam(name = "articleId",value = "文章Id",required = true)
                                            @PathVariable Integer articleId){
        try {
            iArticleService.removeById(articleId);
            return ResponseResult.ok().message("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            throw new EpidemicException("删除文章失败");
        }
    }
}
