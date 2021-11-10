package com.example.epidemicsurveillance.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 微信小程序全国全部地区的全部疫情数据封装类
 * @ClassName WxFromVo
 * @Author 朱云飞
 * @Date 2021/11/6 11:50
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxTotalFromVo {
    private Integer id;
    private Integer index;
    private Integer flag;
    private String areaName;
    private Integer nowConfirm;
    private Integer suspect;
    private Integer confirm;
    private Integer dead;
    private Integer heal;
    private Integer addConfirm;
    private List<WxTotalFromVo> nodes;
}
