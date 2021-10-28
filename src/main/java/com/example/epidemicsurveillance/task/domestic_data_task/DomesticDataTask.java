package com.example.epidemicsurveillance.task.domestic_data_task;

import com.example.epidemicsurveillance.spider.SpiderToGetData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Zero
 * @date 2021/10/25 19:25
 * @description
 * @since 1.8
 **/
@Component
@Slf4j
public class DomesticDataTask {

    @Autowired
    private SpiderToGetData spiderToGetData;

    //中国政府网官网数据地址
    private static final String DOMESTIC_DATA_URL="http://www.gov.cn/fuwu/zt/yqfwzq/zxqk.htm#0";

    public void getChinaEpidemicData() {
        log.info("开始爬取国内疫情最新通报");
        spiderToGetData.getDomesticData(DOMESTIC_DATA_URL);

    }



}
