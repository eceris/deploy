package com.daou.deploy.util;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {

    public static boolean equals(String str1, String str2) {
        return StringUtils.equals(str1, str2);
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        return StringUtils.equalsIgnoreCase(str1, str2);
    }

    public static boolean notEquals(String arg1, String arg2) {
        return !equals(arg1, arg2);
    }

    public static boolean isEmpty(Object o) {
        return org.springframework.util.StringUtils.isEmpty(o);
    }

    public static boolean isNotEmpty(Object o) {
        return !isEmpty(o);
    }

    public static boolean containsIgnoreCase(String str1, String str2) {
        return str1.toUpperCase().contains(str2.toUpperCase()) || str1.toLowerCase().contains(str2.toLowerCase());
    }

    public static String searchkeyIgnoreCaseLike(String searchkey) {
        return "%" + searchkey + "%";
    }
}
