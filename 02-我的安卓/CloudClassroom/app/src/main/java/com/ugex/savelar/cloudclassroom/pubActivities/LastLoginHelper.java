package com.ugex.savelar.cloudclassroom.pubActivities;

import android.content.Context;

import com.ugex.savelar.cloudclassroom.Tools.SharedPreferHelper;
import com.ugex.savelar.cloudclassroom.Tools.UtilHelper;

import java.util.HashMap;
import java.util.Map;

public class LastLoginHelper {
    public static final String LAST_LOGIN_FILE_NAME="lastlogin";
    public static final String KEY_LAST_LOGIN_TYPE="login_type";
    public static final String KEY_LAST_LOGIN_ACCOUNT="login_account";
    public static final String KEY_LAST_LOGIN_PASSWORD="login_password";
    public static void setLastLoginInfo(Context cxt, String type, String account, String password){
        Map<String,String> map=new HashMap<>();
        map.put(KEY_LAST_LOGIN_TYPE,type.trim());
        map.put(KEY_LAST_LOGIN_ACCOUNT,account.trim());
        map.put(KEY_LAST_LOGIN_PASSWORD,password.trim());
        SharedPreferHelper.writeMapToSharedPreferences(cxt,LAST_LOGIN_FILE_NAME,map);
    }
    public static Map<String,String> getLastLoginType(Context cxt){
        Map<String,String> map=new HashMap<>();
        map.put(KEY_LAST_LOGIN_TYPE, UtilHelper.LoginTypeValue.TYPE_STUDENT);
        map.put(KEY_LAST_LOGIN_ACCOUNT,"");
        map.put(KEY_LAST_LOGIN_PASSWORD,"");
        SharedPreferHelper.readMapFromSharedPreferences(cxt,LAST_LOGIN_FILE_NAME,map);
        return map;
    }
}
