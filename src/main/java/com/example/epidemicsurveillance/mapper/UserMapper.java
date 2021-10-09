package com.example.epidemicsurveillance.mapper;

import com.example.epidemicsurveillance.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 普通用户表 Mapper 接口
 * </p>
 *
 * @author zyf
 * @since 2021-10-09
 */
@Component
public interface UserMapper extends BaseMapper<User> {

}
