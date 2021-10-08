package com.example.epidemicsurveillance.impl;

import com.example.epidemicsurveillance.entity.School;
import com.example.epidemicsurveillance.mapper.SchoolMapper;
import com.example.epidemicsurveillance.service.SchoolService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 学校表 服务实现类
 * </p>
 *
 * @author zero
 * @since 2021-10-09
 */
@Service
public class SchoolServiceImpl extends ServiceImpl<SchoolMapper, School> implements SchoolService {

}
