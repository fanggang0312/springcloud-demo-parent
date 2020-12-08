package com.example.demo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: fg
 * @Date: 2020/11/20
 */
public class RegexUtil {
    /**
     * 手机号码正则检验
     */
    public static Boolean phoneNumRegex(String phoneNum) {
        String regex = "^1[3|4|5|6|7|8|9][0-9]\\d{8}$";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(phoneNum);

        return matcher.matches();
    }

    /**
     * 验证码正则检验
     */
    public static Boolean vercodeRegex(String verCode) {
        String regex = "^[1-9]\\d{5}$";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(verCode);

        return matcher.matches();
    }
}
