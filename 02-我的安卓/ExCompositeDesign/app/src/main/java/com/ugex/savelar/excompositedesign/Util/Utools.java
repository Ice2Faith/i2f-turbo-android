package com.ugex.savelar.excompositedesign.Util;

public class Utools {
    public static String regularStrFromObj(Object obj){
        if(obj!=null){
            return obj.toString().trim();
        }
        return null;
    }
    public static String regularStr(String str){
        if(str!=null){
            return str.trim();
        }
        return str;
    }
}
