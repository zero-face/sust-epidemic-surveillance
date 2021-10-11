package com.example.epidemicsurveillance.service.impl;

import com.example.epidemicsurveillance.service.INewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author Zero
 * @Date 2021/10/11 0:13
 * @Since 1.8
 * @Description
 **/
@Service
public class INewsServiceImpl implements INewsService {

    @Autowired
    private MongoTemplate mongoTemplate;



}
