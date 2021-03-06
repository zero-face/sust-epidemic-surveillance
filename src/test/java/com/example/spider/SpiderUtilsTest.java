package com.example.spider;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @ClassName SpiderUtilsTest
 * @Author 朱云飞
 * @Date 2021/10/14 10:47
 * @Version 1.0
 **/
public class SpiderUtilsTest {

    @Test
    public void test() throws IOException {
        DateFormat df = new SimpleDateFormat("MM-dd");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 0);
        date = (Date) calendar.getTime();
        String day = df.format(date);

        System.out.println(day);
    }
}
