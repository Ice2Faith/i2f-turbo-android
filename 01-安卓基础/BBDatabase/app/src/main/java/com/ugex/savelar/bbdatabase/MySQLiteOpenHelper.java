package com.ugex.savelar.bbdatabase;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    //数据库表创建语句，由于Sqlite是弱类型的，因此可以不用指明列类型
    //特别注意，_id列最好固定写法，加上，不然会出现一些问题
    private static final String CREATE_TABLE_STUDENT="create table Student(_id integer primary key autoincrement,sname,ssex);";
    //自己实现一个默认版本号的构造,另外需要实现的父构造可以考虑实现
    public MySQLiteOpenHelper(@Nullable Context context) {
        super(context, "student.db", null, SQLITE_VERSION);
    }
    //参数：Context,数据库文件名，游标(null为默认游标)，版本（只能改高）
    public static final int SQLITE_VERSION=1;//自己写死的版本号


    //当数据库文件第一次被创建的时候，也就是数据库文件不存在的时候
    //一般在这里创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表，如果数据库文件已经存在了，
        // 可以采取更新版本号的方式间接调用onUpgrade方法,
        // 因此可以考虑在onUpgrade方法中创建表（添加表，修改表等）
        db.execSQL(CREATE_TABLE_STUDENT);//是有可能异常的
    }

    //当数据库版本更新的时候
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion==2 && oldVersion==1){
            String sql="drop table if exists Student;";
            db.execSQL(sql);
            sql="create table Student(_id integer primary key autoincrement,sname,ssex,sage);";
            db.execSQL(sql);
        }
    }
}
