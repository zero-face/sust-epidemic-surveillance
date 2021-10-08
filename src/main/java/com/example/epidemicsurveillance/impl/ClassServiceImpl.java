package com.example.epidemicsurveillance.impl;

import com.example.epidemicsurveillance.entity.Class;
import com.example.epidemicsurveillance.mapper.ClassMapper;
import com.example.epidemicsurveillance.service.ClassService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 班级表 服务实现类
 * </p>
 *
 * @author zero
 * @since 2021-10-09
 */
@Service
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements ClassService {

}
