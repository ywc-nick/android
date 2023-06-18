package com.example.project.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtils {


    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";


    public static String TimestampToDateString(String stringTimestamp){
        long timestamp = Long.parseLong(stringTimestamp); // 将字符串类型时间戳转换为长整型
        Date date = new Date(timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT);
        String formattedDate = dateFormat.format(date);
        return formattedDate.toString();
    }
    public static String TimestampToDateString2(String stringTimestamp){
        long timestamp = Long.parseLong(stringTimestamp); // 将字符串类型时间戳转换为长整型
        Date date = new Date(timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(date);
        return formattedDate.toString();
    }


    public static String timestampToString2(String time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String format = df.format(date);
        return format;
    }



    public static String timestampToString(String time){
        // 创建 SimpleDateFormat 对象，指定日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 设置时区为东八区
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        // 将时间戳转换为 Date 类型
        Date date = new Date(time);
        // 将 Date 类型转换为指定格式的日期字符串
        String dateStr = sdf.format(date);
        System.out.println(dateStr);
        return dateStr;
    }

}