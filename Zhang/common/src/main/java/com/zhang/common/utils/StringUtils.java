package com.zhang.common.utils;

/**
 * String字符串相关工具
 */
public class StringUtils {
   public static boolean isBlank(String value){
        if (value==null||value.trim().equals("")){
            return  true;
        }
        return  false;
    }
}
