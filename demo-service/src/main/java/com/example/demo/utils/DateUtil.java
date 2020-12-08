package com.example.demo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 *
 * @Author: fg
 * @Date: 2020/11/09
 */
public class DateUtil {

    public static long SECOND_24 = 60 * 60 * 24;

    public static long getTodayRemainSeconds() {
        long currentTimeMillis = System.currentTimeMillis();
        long currentTimeSecond = currentTimeMillis / 1000;
        long EVERYDAY_SECOND = 60 * 60 * 24;
        long SECOND_8 = 60 * 60 * 8;
        return EVERYDAY_SECOND - (currentTimeSecond % EVERYDAY_SECOND + SECOND_8);
    }

    /**
     * 获取当前时间时间戳
     */
    public static Long getCurrentTimestamp() {
        Long timestamp = System.currentTimeMillis();
        return timestamp;
    }

    /**
     * 解析标准时间格式参数为十三位时间戳
     *
     * @param time 标准格式时间
     * @param sdf  时间格式
     */
    public static Long parseTime(String time, SimpleDateFormat sdf) throws ParseException {
        try {
            Date date = sdf.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            throw new ParseException("时间格式解析异常", 999);
        }
    }

    /**
     * 格式化时间戳为标准格式时间
     *
     * @param timeStamp 时间戳
     * @param sdf       时间格式
     */
    public static String formatTime(Long timeStamp, SimpleDateFormat sdf) {
        Date date = new Date(timeStamp);
        String time = sdf.format(date);
        return time;
    }

    /**
     * 获取两个日期相差的月数
     */
    public static int getMonthDiff(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH);
        int month2 = c2.get(Calendar.MONTH);
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        int day2 = c2.get(Calendar.DAY_OF_MONTH);
        // 获取年的差值
        int yearInterval = year1 - year2;
        // 如果 d1的 月-日 小于 d2的 月-日 那么 yearInterval-- 这样就得到了相差的年数
        if (month1 < month2 || month1 == month2 && day1 < day2) {
            yearInterval--;
        }
        // 获取月数差值
        int monthInterval = (month1 + 12) - month2;
        if (day1 < day2) {
            monthInterval--;
        }
        monthInterval %= 12;
        return Math.abs(yearInterval * 12 + monthInterval);
    }

    /**
     * 获取n个月后的时间str，不传时分秒则默认获取当前时间时分秒
     *
     * @param date     开始时间
     * @param dateType 日期类型
     * @param month    月数
     * @return 末尾时间
     */
    public static String addMonth(String date, String dateType, int month) {
        String nowDate = null;
        if (date.length() < 15) {
            date += com.example.demo.utils.StrUtil.date2string(new Date()).substring(10);
        }
        SimpleDateFormat format = new SimpleDateFormat(dateType);
        try {
            Date parse = format.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parse);
            calendar.add(Calendar.MONTH, month);
            nowDate = format.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return nowDate;
    }

    public static void main(String[] args) {
        Date date = new Date();

        String endDate = DateUtil.
                addMonth("2019-12-31", "yyyy-MM-dd HH:mm:ss", 2);
        System.out.println(endDate);
    }

}
