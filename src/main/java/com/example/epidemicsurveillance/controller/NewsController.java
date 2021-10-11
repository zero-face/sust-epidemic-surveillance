package com.example.epidemicsurveillance.controller;

import com.example.epidemicsurveillance.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Zero
 * @Date 2021/10/10 23:39
 * @Since 1.8
 * @Description
 **/
@Slf4j
@RestController
@RequestMapping("/api/v1/news")
public class NewsController {

    @GetMapping
    public ResponseResult getArticleList(){
        return ResponseResult.ok();
    }

}
