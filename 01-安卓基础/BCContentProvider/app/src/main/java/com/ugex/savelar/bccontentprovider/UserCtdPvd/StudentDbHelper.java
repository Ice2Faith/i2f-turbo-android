package com.ugex.savelar.bccontentprovider.UserCtdPvd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class StudentDbHelper extends SQLiteOpenHelper {
    public StudentDbHelper(@Nullable Context context) {
        super(context, "student", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table student(_id integer primary key autoincrement,sname,sage);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
