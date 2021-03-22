package com.example.demo.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 统一社会信用代码工具类
 *
 * @Author: fg
 * @Date: 2021/3/22
 */
public class CreditCodeUtil {
    private static final String BASE_STRING = "0123456789ABCDEFGHJKLMNPQRTUWXY";
    private static final char[] BASE_ARRAY = BASE_STRING.toCharArray();
    private static final List<Character> BASE_CODES = new ArrayList<>();
    private static final String BASE_REGEX = "[" + BASE_STRING + "]{18}";
    private static final int[] WEIGHT = {1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30, 28};

    static {
        for (char c : BASE_ARRAY) {
            BASE_CODES.add(c);
        }
    }

    /**
     * 是否是有效的统一社会信用代码
     *
     * @param socialCreditCode 统一社会信用代码
     * @return true-有效
     */
    public static boolean isValidSocialCreditCode(String socialCreditCode) {
        if (StringUtils.isBlank(socialCreditCode) || !Pattern.matches(BASE_REGEX, socialCreditCode)) {
            return false;
        }
        char[] businessCodeArray = socialCreditCode.toCharArray();
        char check = businessCodeArray[17];
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            char key = businessCodeArray[i];
            sum += (BASE_CODES.indexOf(key) * WEIGHT[i]);
        }
        int value = 31 - sum % 31;
        return check == BASE_ARRAY[value % 31];
    }
}
