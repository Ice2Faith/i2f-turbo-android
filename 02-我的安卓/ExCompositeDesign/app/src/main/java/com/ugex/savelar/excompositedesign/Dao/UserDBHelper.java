package com.ugex.savelar.excompositedesign.Dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class UserDBHelper extends SQLiteOpenHelper {
    public static final String dbName="user";
    public static final int dbVersion=1;
    public UserDBHelper(@Nullable Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table user_info(" +
                "account varchar(20) primary key," +
                "password varchar(20)," +
                "name varchar(20)," +
                "photo varchar(300)," +
                "sex varchar(5)," +
                "address varchar(100)," +
                "brithday varchar(50)," +
                "email varchar(80)," +
                "other varchar(256));";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
