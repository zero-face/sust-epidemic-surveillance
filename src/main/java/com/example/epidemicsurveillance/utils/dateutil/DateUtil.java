package com.example.epidemicsurveillance.utils.dateutil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @ClassName DateUitl
 * @Author 朱云飞
 * @Date 2021/11/2 22:44
 * @Version 1.0
 **/
public  class DateUtil {
    public static String getTodayTimeString(){
        DateFormat df = new SimpleDateFormat("MM-dd");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 0);
        date = (Date) calendar.getTime();
        return df.format(date);
    }
}
