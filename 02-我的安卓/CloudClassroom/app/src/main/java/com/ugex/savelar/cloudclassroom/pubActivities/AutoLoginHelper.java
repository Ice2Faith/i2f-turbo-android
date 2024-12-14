package com.ugex.savelar.cloudclassroom.pubActivities;

import android.content.Context;

import com.ugex.savelar.cloudclassroom.Tools.SharedPreferHelper;
import com.ugex.savelar.cloudclassroom.Tools.UtilHelper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AutoLoginHelper {
    public static final String AUTO_LOGIN_INFO_FILE_NAME="autologin";
    public static final String KEY_TYPE="type";
    public static final String KEY_ACCOUNT="account";
    public static final String KEY_PASSWORD="password";
    private static final String KEY_ILEGELDATE="ilegaldate";
    public static void saveLoginInfo(Context cxt,String type, String account, String password){
        Map<String,String> map=new HashMap<>();
        map.put(KEY_TYPE,type.trim());
        map.put(KEY_ACCOUNT,account.trim());
        map.put(KEY_PASSWORD,password.trim());
        Date date=new Date();
        map.put(KEY_ILEGELDATE,""+(date.getTime()+7*24*60*60*1000));
        SharedPreferHelper.writeMapToSharedPreferences(cxt,AUTO_LOGIN_INFO_FILE_NAME,map);
    }
    public static Map<String,String> readLoginInfo(Context cxt){
        Map<String,String> map=new HashMap<>();
        map.put(KEY_TYPE,"");
        map.put(KEY_ACCOUNT,"");
        map.put(KEY_PASSWORD,"");
        Date date=new Date();
        map.put(KEY_ILEGELDATE,""+date.getTime());
        SharedPreferHelper.readMapFromSharedPreferences(cxt,AUTO_LOGIN_INFO_FILE_NAME,map);
        if(UtilHelper.stringIsNullOrEmpty(map.get(KEY_ILEGELDATE))==false){
            long delayTime=Long.parseLong(map.get(KEY_ILEGELDATE));
            if(date.getTime()>delayTime){
                map.put(KEY_TYPE,"");
                map.put(KEY_ACCOUNT,"");
                map.put(KEY_PASSWORD,"");
            }
        }
        return map;
    }
}
