package com.example.epidemicsurveillance.service.impl;

import com.example.epidemicsurveillance.entity.Admin;
import com.example.epidemicsurveillance.mapper.AdminMapper;
import com.example.epidemicsurveillance.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author zyf
 * @since 2021-10-09
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

}
