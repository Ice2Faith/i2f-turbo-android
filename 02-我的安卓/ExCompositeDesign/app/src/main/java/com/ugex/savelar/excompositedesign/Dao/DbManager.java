package com.ugex.savelar.excompositedesign.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ugex.savelar.excompositedesign.Dao.UserDBHelper;

public class DbManager {
    public static final String objTable="user_info";
    public static long insert(Context context,ContentValues values){
        SQLiteDatabase db=getSqlDb(context);
        return db.insert(objTable, null, values);
    }
    public static long update(Context context,ContentValues values,String whereClause,String[] whereArgs){
        SQLiteDatabase db=getSqlDb(context);
        return db.update(objTable,  values,whereClause,whereArgs);
    }
    public static long delete(Context context,String whereClause,String[] whereArgs){
        SQLiteDatabase db=getSqlDb(context);
        return db.delete(objTable,  whereClause,whereArgs);
    }
    public static Cursor select(Context context,String[] cols,String whereClause,String[] whereArgs,String groupBy,String having,String orderBy){
        SQLiteDatabase db=getSqlDb(context);
        return db.query(objTable,cols,whereClause,whereArgs,groupBy,having,orderBy);
    }
    public static Cursor selectSql(Context context,String sqlSelect){
        SQLiteDatabase db=getSqlDb(context);
        return db.rawQuery(sqlSelect,null);
    }
    public static Cursor selectPrepare(Context context,String sqlPrepare,String[] prepareArgs){
        SQLiteDatabase db=getSqlDb(context);
        return db.rawQuery(sqlPrepare,prepareArgs);
    }
    public static void execSql(Context context,String sql){
        SQLiteDatabase db=getSqlDb(context);
        db.execSQL(sql);
    }

    public static SQLiteDatabase getSqlDb(Context context){
        UserDBHelper dbHelper=new UserDBHelper(context);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        return db;
    }
}
