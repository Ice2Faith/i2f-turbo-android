package com.demo.classroom.Util;

import android.content.Context;
import android.graphics.Bitmap;

import com.demo.classroom.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ActivityHelper {
    public static final String KEY_PHONE ="account";
    public static final String KEY_TYPE="type";

    public static final String KEY_REQUEST_ITEM_ID="request_item_id";
    public static final String KEY_ID="id";


    public static final String SEX_MAN="男";
    public static final String SEX_WOMAN="女";

    public static final String TYPE_STUDENT="student";
    public static final String TYPE_TEACHER="teacher";
    public static final String TYPE_ADMIN="admin";

    public static String getRadioSex(int id){
        String type="";
        switch (id)
        {
            case R.id.radioButtonMan:
                type= SEX_MAN;
                break;
            case R.id.radioButtonWoman:
                type= SEX_WOMAN;
                break;
            default:
                break;
        }
        return type;
    }

    public static String getRadioType(int id){
        String type="";
        switch (id)
        {
            case R.id.radioButtonStudent:
                type= TYPE_STUDENT;
                break;
            case R.id.radioButtonTeacher:
                type= TYPE_TEACHER;
                break;
            case R.id.radioButtonAdmin:
                type= TYPE_ADMIN;
                break;
            default:
                break;
        }
        return type;
    }

    public static void Logout(Context ctx){
        Map<String,String> info=new HashMap<>();
        info.put("type","");
        info.put("phone","");
        info.put("password","");
        info.put("legalTime",(new Date().getTime()-1000)+"");
        SharedPreferencesHelper.saveTo(ctx,"remlogin",info);
    }

    public static boolean saveBitmap(Bitmap bmpPhoto, File saveFile){
        boolean ret=false;
        if(bmpPhoto!=null){
                try {
                    FileOutputStream sos= new FileOutputStream(saveFile.getAbsolutePath());
                    bmpPhoto.compress(Bitmap.CompressFormat.PNG,90,sos);
                    sos.flush();
                    sos.close();
                    ret=true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return ret;
    }
}
