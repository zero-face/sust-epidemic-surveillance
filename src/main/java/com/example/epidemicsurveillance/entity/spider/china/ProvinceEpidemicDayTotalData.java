package com.example.epidemicsurveillance.entity.spider.china;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ProvinceEpidemicDayTotalData
 * @Author 朱云飞
 * @Date 2021/10/15 14:13
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProvinceEpidemicDayTotalData {
    /**
     * 现有确诊
     */
    private Integer nowConfirm;
    /**
     * 累计确诊
     */
    private Integer confirm;
    /**
     * 疑似
     */
    private Integer suspect;
    /**
     * 死亡人数
     */
    private Integer dead;
    /**
     * 治愈人数
     */
    private Integer heal;
}
