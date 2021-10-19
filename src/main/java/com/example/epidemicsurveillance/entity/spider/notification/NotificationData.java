package com.example.epidemicsurveillance.entity.spider.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName NotificationdATA
 * @Author 朱云飞
 * @Date 2021/10/17 16:52
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationData {
    private List<NotificationDataDetails> list;
}
