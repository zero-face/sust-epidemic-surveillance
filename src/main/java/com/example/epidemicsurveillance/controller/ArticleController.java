package com.example.epidemicsurveillance.controller;


import com.example.epidemicsurveillance.response.ResponseResult;
import com.example.epidemicsurveillance.service.IArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 文章数据表 前端控制器
 * </p>
 *
 * @author zyf
 * @since 2021-10-16
 */
@RestController
@RequestMapping("/api/v1/user/article")
@CrossOrigin
@Api(tags = "前台资讯管理")
public class ArticleController {
    @Autowired
    private IArticleService iArticleService;

    @ApiOperation(value = "获取文章列表")
    @GetMapping("getArticleListByType/{type}")
    public ResponseResult getArticleListByType(@ApiParam(name = "type",value = "文章类型",required = true)
                                               @PathVariable Integer type){
        return iArticleService.getArticleListByType(type);
    }

    @ApiOperation(value = "根据文章id获取文章详情")
    @GetMapping("getArticleById/{id}")
    public ResponseResult getArticleById(@ApiParam(name = "id",value = "文章Id",required = true)
                                         @PathVariable Integer id){
        return iArticleService.getArticleById(id);
    }
}
