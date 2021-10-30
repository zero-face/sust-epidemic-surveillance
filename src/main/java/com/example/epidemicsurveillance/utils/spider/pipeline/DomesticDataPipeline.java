package com.example.epidemicsurveillance.utils.spider.pipeline;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.epidemicsurveillance.entity.DomesticData;
import com.example.epidemicsurveillance.mapper.DomesticDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * @author Zero
 * @date 2021/10/30 11:23
 * @description
 * @since 1.8
 **/
@Component
public class DomesticDataPipeline implements Pipeline {

    @Autowired
    private DomesticDataMapper domesticDataMapper;

    @Override
    public void process(ResultItems resultItems, Task task) {
        if(resultItems.get("todayData") != null) {
            final DomesticData domesticData = JSON.parseObject(JSON.toJSONString(resultItems.get("todayData")), DomesticData.class);
            final DomesticData todayData = domesticDataMapper.getTodayData();
            if(todayData == null) {
                domesticDataMapper.insert(domesticData);
            }
        }
    }
}
