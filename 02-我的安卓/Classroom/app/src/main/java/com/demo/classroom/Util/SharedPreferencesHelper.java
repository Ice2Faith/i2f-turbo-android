package com.demo.classroom.Util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

public class SharedPreferencesHelper {
    //这两个方法，可以应用到自动登录上，登录成功之后使用saveTo
    //在页面初始化的时候，读取对应的键，检查是否有数据，有数据就进行自动登录，没有就什么也不做
    //保存键值对到SharedPreferences
    public static void saveTo(Context ctx,String fileName, Map<String,String>values){
        SharedPreferences sp=ctx.getSharedPreferences(fileName,ctx.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        Set<String> keys=values.keySet();
        for(String key :keys){
            editor.putString(key,values.get(key));
        }
        editor.commit();
    }
    //根据传入的键，从对应的SharedPreferences获取对应的值
    public static void readFrom(Context cxt, String spName, Map<String,String> values){
        SharedPreferences sp=cxt.getSharedPreferences(spName,cxt.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        Set<String> keys=values.keySet();
        for(String key :keys){
            values.put(key,sp.getString(key,""));
        }
        editor.commit();
    }
}
