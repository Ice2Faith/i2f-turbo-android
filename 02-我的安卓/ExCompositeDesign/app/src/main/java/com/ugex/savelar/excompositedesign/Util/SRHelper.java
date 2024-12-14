package com.ugex.savelar.excompositedesign.Util;

import android.content.Context;
import android.content.SharedPreferences;

public class SRHelper {
    public static void putDataToSharedPreference(Context cxt,String preferenceName,String key,String value){
        SharedPreferences preferences=cxt.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static String getDataFromSharedPreference(Context cxt,String preferenceName,String key,String defValue){
        SharedPreferences preferences=cxt.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);

        return preferences.getString(key,defValue);
    }
}
