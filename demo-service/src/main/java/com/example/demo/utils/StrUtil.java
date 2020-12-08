package com.example.demo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @Author: fg
 * @Date: 2020/11/09
 */
public class StrUtil {

    /**
     * 判空
     *
     * @param o
     * @return
     */
    public static boolean isNullString(Object o) {
        if (null == o) {
            return true;
        } else {
            if ("".equals(o.toString()) || "null".equals(o.toString())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNotNull(Object str) {
        return (null != str && !"".equals(str.toString().trim()));
    }

    public static boolean isNullOrEmpty(Object str) {
        return (null == str || "".equals(str.toString().trim()));
    }

    /**
     * 获取随机数
     *
     * @param max 随机数最大值，如：999999
     * @param min 随机数最小值，如：100000
     * @return
     */
    public static String calRandomScore(int max, int min) {
        Random random = new Random();
        Integer randomSum = random.nextInt(max) % (max - min + 1) + min;
        return randomSum + "";
    }

    /**
     * 生成18位订单编号
     *
     * @return
     */
    public static String createOrderNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
        String format = sdf.format(new Date());
        Random random = new Random();
        for (int j = 0; j < 3; j++) {
            format += random.nextInt(10);
        }
        return format;
    }

    /**
     * 生成指定位数的随机数
     *
     * @param length
     * @return
     */
    public static String getRandom(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            val += random.nextInt(10);
        }
        return val;
    }

    /**
     * 对象转string
     *
     * @param obj
     * @return
     */
    public static String objToStr(Object obj) {
        if (null != obj) {
            return obj.toString();
        }
        return null;
    }

    /**
     * 对象转int
     *
     * @param obj
     * @return
     */
    public static int objToInt(Object obj, int defaultVal) {
        if (null != obj) {
            return string2int(obj.toString(), defaultVal);
        }
        return defaultVal;
    }

    /**
     * int 与 String互相转换
     *
     * @param value      value
     * @param defaultVal default
     * @return result
     */
    public static int string2int(String value, int defaultVal) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultVal;
        }
    }


    /**
     * 判断是否附件
     * 文档：txt|xls|xlsx|doc|docx|pdf|ppt
     * 网页：html
     * 视频：mp4|avi|mov|rmvb|flv
     * 音频：mp3|wav|wmv
     *
     * @param fileName 文件名
     * @return
     */
    public static boolean isEnclosure(String fileName) {
        if (!fileName.toLowerCase().matches(".*\\.(txt|xls|xlsx|doc|docx|pdf|ppt|html|mp4|avi|mov|rmvb|flv|mp3|wav|wmv)$")) {
            return false;
        }
        return true;
    }

    /**
     * 是否图片
     *
     * @param fileName
     * @return
     */
    public static boolean isImage(String fileName) {
        if (!fileName.toLowerCase().matches(".*\\.(jpg|gif|jpeg|png|bmp)$")) {
            return false;
        }
        return true;
    }

    /**
     * 是否apk
     *
     * @param fileName
     * @return
     */
    public static boolean isApk(String fileName) {
        if (!fileName.toLowerCase().matches(".*\\.(apk)$")) {
            return false;
        }
        return true;
    }

    /**
     * 获取当前时间 年/月
     *
     * @return yyyy/MM
     */
    public static String getYearMonth() {
        SimpleDateFormat sdfYearMonth = new SimpleDateFormat("yyyy/MM");
        return sdfYearMonth.format(new Date());
    }

    /**
     * 将Date（）转化为String类型
     *
     * @param date date
     * @return result
     */
    public static String date2string(Date date) {
        return date2string(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String date2string_ms(Date date) {
        return date2string(date, "yyyy-MM-dd HH:mm:ss.ms");
    }

    public static String date2string(long date) {
        Date theTime = new Date(date);
        return date2string(theTime);
    }

    public static String date2string(Date date, String format) {
        if (date == null) {
            date = new Date();
        }
        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.format(date);
    }

    /**
     * 将字符串（yyyy-MM-dd格式）转化为Date类型
     *
     * @param sDate date
     * @return result
     */
    public static Date string2date(String sDate) {
        return
                string2date(sDate, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date string2date(String sDate, String sFormat) {
        SimpleDateFormat sf = new SimpleDateFormat(sFormat);
        try {
            return sf.parse(sDate);
        } catch (Exception ex) {
            return new Date();
        }
    }

    public static boolean equals(String a, String b) {
        if (null == a && null == b) {
            return true;
        }
        if (null != a && a.equals(b)) {
            return true;
        }
        if (null != b && b.equals(a)) {
            return true;
        }
        return false;
    }


    /**
     * 将html串清理为纯文本串
     *
     * @param html html字符串
     * @return 去掉html标签的纯文本
     */
    public static String htmlToPlainText(String html) {
        // 去掉html标签
        String regEx_html = "<[^>]+>";
        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(html);
        String outStr = m_html.replaceAll("");
        // 去掉空行，空格和制表符
        Pattern p_line = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p_line.matcher(outStr);
        String result = m.replaceAll("");
        // 将html特殊字符转义回来
        return result;
    }

    /**
     * 移除list重复元素
     *
     * @param list 原list
     * @return 无序list
     */
    public static List<String> removeRepeatObject(List<String> list) {
        HashSet<String> set = new HashSet<String>(list);
        list.clear();
        list.addAll(set);
        return list;
    }

    /**
     * 没有时分秒的str 补齐00:00;00
     *
     * @param dateTime
     */
    public static String addStartHhMmSs(String dateTime) {
        if (null != dateTime && dateTime.length() < 12) {
            dateTime = dateTime + " 00:00:00";
        }
        return dateTime;
    }

    /**
     * 没有时分秒的str 补齐23:59;59
     *
     * @param dateTime
     * @return
     */
    public static String addEndHhMmSs(String dateTime) {
        if (null != dateTime && dateTime.length() < 12) {
            dateTime = dateTime + " 23:59;59";
        }
        return dateTime;
    }

    public static void main(String[] args) {
        System.out.println(htmlToPlainText("dfasfDfasdfasdfas<img src='' > sdafsdf<p>我是p标签</p>是否违法违法"));
    }

}
