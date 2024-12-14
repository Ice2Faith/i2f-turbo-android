package com.ugex.savelar.cloudclassroom.Tools;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

public class SharedPreferHelper {
    public static void writeMapToSharedPreferences(Context cxt, String spName, Map<String,String> values){
        SharedPreferences sp=cxt.getSharedPreferences(spName,cxt.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        Set<String> keys=values.keySet();
        for(String key :keys){
            editor.putString(key,values.get(key));
        }
        editor.commit();
    }
    public static void readMapFromSharedPreferences(Context cxt, String spName, Map<String,String> values){
        SharedPreferences sp=cxt.getSharedPreferences(spName,cxt.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        Set<String> keys=values.keySet();
        for(String key :keys){
            values.put(key,sp.getString(key,""));
        }
        editor.commit();
    }
}
