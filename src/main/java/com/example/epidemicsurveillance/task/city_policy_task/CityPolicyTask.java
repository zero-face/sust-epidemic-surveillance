package com.example.epidemicsurveillance.task.city_policy_task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.epidemicsurveillance.entity.CityCode;
import com.example.epidemicsurveillance.mapper.CityCodeMapper;
import com.example.epidemicsurveillance.utils.citypolicy.PolicyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Zero
 * @date 2021/10/23 15:26
 * @description 已弃用 真晦气
 * @since 1.8
 **/
@Component
@Slf4j
@Deprecated
public class CityPolicyTask {
    @Autowired
    private PolicyUtil policyUtil;

    @Autowired
    private CityCodeMapper cityCodeMapper;

    @Value("gov.cityKey")
    private String cityKey;

//    @Scheduled(cron = "10 27 16 * * ?")
    public void updateCityPolicy() {
        log.info("开始执行更新防控政策数据");
        final List<CityCode> cityCodes = cityCodeMapper.selectList(new QueryWrapper<CityCode>().le("code","659010").isNotNull("city"));
        log.info("查询到的城市{}",cityCodes);
        cityCodes.stream().forEach(cityCode -> {
            log.info("开始获取{}",cityCode);
            policyUtil.GuideData(cityCode.getCity(),cityKey);
        });
        log.info("本次更新结束");
    }
}
