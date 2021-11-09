package com.example.epidemicsurveillance.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 微信小程序国内当日疫情新增数据封装类
 * @ClassName WxTodayVo
 * @Author 朱云飞
 * @Date 2021/11/6 16:34
 * @Version 1.0
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WxTodayFromVo {
    private Integer id;
    private Integer index;
    private Integer flag;
    private String areaName;
    private Integer nowConfirm;
    private Integer addConfirm;
    private List<WxTodayFromVo> nodes;
}
