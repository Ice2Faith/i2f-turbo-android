package com.demo.classroom.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME="classroom";
    public static final int DB_VERSION=4;
    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createTables(sqLiteDatabase);
        insertDefaultValues(sqLiteDatabase);
    }

    void  createTables(SQLiteDatabase db)
    {
        db.execSQL("create table Student\n" +
                "(\n" +
                "phone varchar(20) primary key,\n" +
                "password varchar(20) not null,\n" +
                "uno varchar(20) unique,\n" +
                "name varchar(20),\n" +
                "sex varchar(10),\n" +
                "photo varchar(300),\n" +
                "birth varchar(20),\n" +
                "inyear varchar(10),\n" +
                "address varchar(300),\n" +
                "college varchar(50),\n" +
                "department varchar(50),\n" +
                "profession varchar(50),\n" +
                "classroom varchar(50),\n" +
                "email varchar(50),\n" +
                "introduce varchar(300)\n" +
                ")");
        db.execSQL("create table Teacher\n" +
                "(\n" +
                "phone varchar(20) primary key,\n" +
                "password varchar(20) not null,\n" +
                "uno varchar(20) unique,\n" +
                "name varchar(20),\n" +
                "sex varchar(10),\n" +
                "photo varchar(300),\n" +
                "birth varchar(20),\n" +
                "teayear varchar(10),\n" +
                "address varchar(300),\n" +
                "college varchar(50),\n" +
                "department varchar(50),\n" +
                "profession varchar(50),\n" +
                "classroom varchar(50),\n" +
                "email varchar(50),\n" +
                "introduce varchar(300)\n" +
                ")");
        db.execSQL("create table Admin\n" +
                "(\n" +
                "phone varchar(20) primary key,\n" +
                "password varchar(20) not null,\n" +
                "name varchar(20),\n" +
                "sex varchar(10),\n" +
                "photo varchar(300),\n" +
                "birth varchar(20),\n" +
                "address varchar(300),\n" +
                "email varchar(50),\n" +
                "introduce varchar(300)\n" +
                ")");
        db.execSQL("create table Attendance\n" +
                "(\n" +
                "id integer primary key autoincrement,\n" +
                "sno varchar(20),\n" +
                "tno varchar(20),\n" +
                "session varchar(10),\n" +
                "datetime varchar(20),\n" +
                "leaverequest varchar(300),\n" +
                "duration varchar(20),\n" +
                "result varchar(20),\n" +
                "other varchar(300)\n" +
                ")");
        db.execSQL("create table Homework\n" +
                "(\n" +
                "id  integer primary key autoincrement,\n" +
                "sno varchar(20),\n" +
                "tno varchar(20),\n" +
                "datetime varchar(20),\n" +
                "grade varchar(300),\n" +
                "comment varchar(20),\n" +
                "other varchar(300)\n" +
                ")");
        db.execSQL("create table ClassAccess\n" +
                "(\n" +
                "id integer primary key autoincrement,\n" +
                "sno varchar(20),\n" +
                "tno varchar(20),\n" +
                "datetime varchar(20),\n" +
                "accessgrade varchar(300),\n" +
                "session varchar(20),\n" +
                "other varchar(300)\n" +
                ")");
    }

    void insertDefaultValues(SQLiteDatabase db)
    {
        db.execSQL("insert into Student(phone,password,uno,name,college,department,profession,classroom) values('18011112222','12345','3001000180','张三','安卓开发技术院','计算机与信息科学系','软件工程','1701');");
        db.execSQL("insert into Student(phone,password,uno,name,college,department,profession,classroom) values('18033334444','12345','3001000181','李四','安卓开发技术院','计算机与信息科学系','软件工程','1701');");
        db.execSQL("insert into Teacher(phone,password,uno,name,college,department,profession,classroom) values('13011112222','12345','1012002101','张教授','安卓开发技术院','计算机与信息科学系','软件工程','1701');");
        db.execSQL("insert into Teacher(phone,password,uno,name,college,department,profession,classroom) values('13033334444','12345','1012002102','李讲师','安卓开发技术院','计算机与信息科学系','软件工程','1701');");
        db.execSQL("insert into Admin(phone,password,name)	values('root','root','超级管理员');");
        db.execSQL("insert into Admin(phone,password,name)	values('admin','admin','管理员');");
        db.execSQL("insert into Attendance(sno,tno,session,datetime,duration,result) values('3001000180','1012002101','2','2020-3-12','半天','批准');");
        db.execSQL("insert into ClassAccess(sno,tno,datetime,accessgrade,session) values('3001000180','1012002101','2020-3-14','86','5');");
        db.execSQL("insert into Homework(sno,tno,datetime,grade,comment) values('3001000180','1012002101','2020-3-14','75','属正常表现');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
