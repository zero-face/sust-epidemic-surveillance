package com.example.epidemicsurveillance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.epidemicsurveillance.entity.UserAuth;
import com.example.epidemicsurveillance.entity.vo.UserAuthVO;
import com.sun.org.glassfish.gmbal.ManagedObject;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author Zero
 * @Date 2021/10/16 14:00
 * @Since 1.8
 * @Description
 **/
@Mapper
public interface UserAuthMapper extends BaseMapper<UserAuth> {
}
