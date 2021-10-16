package com.example.epidemicsurveillance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.epidemicsurveillance.entity.Clazz;
import com.example.epidemicsurveillance.mapper.ClassMapper;
import com.example.epidemicsurveillance.service.IClassService;
import org.springframework.stereotype.Service;

/**
 * @Author Zero
 * @Date 2021/10/16 13:58
 * @Since 1.8
 * @Description
 **/
@Service
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Clazz> implements IClassService {
}
