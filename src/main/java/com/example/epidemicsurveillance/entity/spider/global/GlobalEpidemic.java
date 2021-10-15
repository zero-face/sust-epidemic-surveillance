package com.example.epidemicsurveillance.entity.spider.global;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName GlobalEpidemic
 * @Author 朱云飞
 * @Date 2021/10/14 14:06
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalEpidemic implements Serializable {
    private Integer ret;
    private String info;
    private GlobalEpidemicRequireData data;
}
