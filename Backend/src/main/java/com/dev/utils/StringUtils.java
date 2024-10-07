package com.dev.utils;

/**
 * @author Rupesh Dangi
 * @date: 2024/10/06 21/02
 */

public final class StringUtils {
    public static boolean isNotEmpty(String str){
        return str!=null && !str.trim().isEmpty();
    }
    public static boolean isEmpty(String str){
        return str==null || str.isEmpty();
    }
}
