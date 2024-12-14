package com.demo.classroom.Util;

import android.database.Cursor;

public class CursorHelper {
    public static String getString(Cursor cur,String colName){
        return cur.getString(cur.getColumnIndex(colName));
    }
    public static String getSafeString(Cursor cur,String colName){
        return UtilHelper.getSafeString(cur.getString(cur.getColumnIndex(colName)));
    }
    public static int getInteger(Cursor cur,String colName){
        return cur.getInt(cur.getColumnIndex(colName));
    }
}
