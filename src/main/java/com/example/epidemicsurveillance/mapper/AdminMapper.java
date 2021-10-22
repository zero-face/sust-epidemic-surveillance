package com.example.epidemicsurveillance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.epidemicsurveillance.entity.Admin;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 管理员表 Mapper 接口
 * </p>
 *
 * @author zyf
 * @since 2021-10-09
 */
@Component
public interface AdminMapper extends BaseMapper<Admin> {


    List<Admin> getAdmins(Integer type);
}
