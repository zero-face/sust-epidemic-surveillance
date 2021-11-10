package com.example.epidemicsurveillance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.epidemicsurveillance.entity.EpidemicDataToday;
import com.example.epidemicsurveillance.entity.vo.WxTodayFromVo;
import com.example.epidemicsurveillance.mapper.EpidemicDataTodayMapper;
import com.example.epidemicsurveillance.response.ResponseResult;
import com.example.epidemicsurveillance.service.IEpidemicDataTodayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 今日疫情新增表 服务实现类
 * </p>
 *
 * @author zyf
 * @since 2021-11-06
 */
@Service
public class EpidemicDataTodayServiceImpl extends ServiceImpl<EpidemicDataTodayMapper, EpidemicDataToday> implements IEpidemicDataTodayService {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public ResponseResult getAllProvinceEpidemicDataTodayAddForWXFrom() {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        WxTodayFromVo wxTodayAddTable = (WxTodayFromVo)valueOperations.get("wxTodayAddTable");
        return ResponseResult.ok().data("result",wxTodayAddTable);
    }
}
