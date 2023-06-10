package com.example.project.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    public static String timestampToString(long time){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//定义格式，不显示毫秒

        Timestamp now= new Timestamp(time);//获取系统当前时间

        return df.format(now);
    }
    /**
     * 将时间戳转换为字符串
     *
     * @param timestamp 时间戳（单位为毫秒）
     * @param pattern   日期格式
     * @return 日期字符串
     */
    public static String format(long timestamp, String pattern) {
        DateFormat format = new SimpleDateFormat(pattern);
        return format.format(new Date(timestamp));
    }

    /**
     * 将日期对象转换为字符串
     *
     * @param date    日期对象
     * @param pattern 日期格式
     * @return 日期字符串
     */
    public static String format(Date date, String pattern) {
        DateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 将日期字符串转换为日期对象
     *
     * @param dateString 日期字符串
     * @param pattern    日期格式
     * @return 日期对象
     */
    public static Date parse(String dateString, String pattern) {
        DateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            LoggerUtils.e("Error parsing date string: " + dateString, e.toString());
            return null;
        }
    }

    /**
     * 将时间戳转换为日期对象
     *
     * @param timestamp 时间戳（单位为毫秒）
     * @return 日期对象
     */
    public static Date toDate(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return calendar.getTime();
    }

    /**
     * 将日期对象转换为时间戳
     *
     * @param date 日期对象
     * @return 时间戳（单位为毫秒）
     */
    public static long toTimestamp(Date date) {
        return date.getTime();
    }
}