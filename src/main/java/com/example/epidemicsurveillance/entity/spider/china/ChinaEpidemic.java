package com.example.epidemicsurveillance.entity.spider.china;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName ChinaEpidemic
 * @Author 朱云飞
 * @Date 2021/10/15 11:34
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChinaEpidemic {
    private List<ChinaEpidemicAreaTree> areaTree;
}
