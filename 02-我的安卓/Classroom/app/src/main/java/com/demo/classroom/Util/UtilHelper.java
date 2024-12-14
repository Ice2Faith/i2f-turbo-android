package com.demo.classroom.Util;

public class UtilHelper {
    public static String getSafeString(String str){
        if(str==null)
            return "";
        return str.trim();
    }
    public static boolean isEmptyString(String str){
        return str==null || "".equals(str.trim());
    }
}
