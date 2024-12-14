package com.ugex.savelar.cloudclassroom.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLCloudClassroomOpenHelper extends SQLiteOpenHelper {
    public static final String CLOUD_CLASSROOM_DB_FILE_NAME="CloudClassroom.db";
    public static final int DB_VERSION=5;
    public SQLCloudClassroomOpenHelper(@Nullable Context context) {
        super(context, CLOUD_CLASSROOM_DB_FILE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        CreateTables(db);
        InsertDefaultData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void CreateTables(SQLiteDatabase db){
        db.execSQL("CREATE TABLE Admin(\n" +
                "account CHAR(11) PRIMARY KEY,\n" +
                "pwd VARCHAR(20) not null,\n" +
                "lastlogin VARCHAR(20),\n" +
                "name VARCHAR(20),\n" +
                "photo VARCHAR(300),\n" +
                "sex VARCHAR(4),\n" +
                "birth VARCHAR(20),\n" +
                "email VARCHAR(100),\n" +
                "introduce VARCHAR(500),\n" +
                "address VARCHAR(200),\n" +
                "status VARCHAR(10) DEFAULT('false')\n" +
                ")");
        db.execSQL("CREATE TABLE Teacher(\n" +
                "account CHAR(11) PRIMARY KEY,\n" +
                "pwd VARCHAR(20) not null,\n" +
                "lastlogin VARCHAR(20),\n" +
                "pno VARCHAR(20) unique,\n" +
                "name VARCHAR(20),\n" +
                "sex VARCHAR(4),\n" +
                "photo VARCHAR(300),\n" +
                "birth VARCHAR(20),\n" +
                "college VARCHAR(50),\n" +
                "department VARCHAR(50),\n" +
                "profession VARCHAR(50),\n" +
                "address VARCHAR(200),\n" +
                "email VARCHAR(100),\n" +
                "introduce VARCHAR(500),\n" +
                "status VARCHAR(10) DEFAULT('false')\n" +
                ")");
        db.execSQL("CREATE TABLE Class(\n" +
                "pno VARCHAR(20) PRIMARY KEY,\n" +
                "name VARCHAR(50)\n" +
                ")");
        db.execSQL("CREATE TABLE Student(\n" +
                "account CHAR(11) PRIMARY KEY,\n" +
                "pwd VARCHAR(20) not null,\n" +
                "lastlogin VARCHAR(20),\n" +
                "pno VARCHAR(20) unique,\n" +
                "name VARCHAR(20),\n" +
                "sex VARCHAR(4),\n" +
                "photo VARCHAR(300),\n" +
                "birth VARCHAR(20),\n" +
                "inyear VARCHAR(6),\n" +
                "college VARCHAR(50),\n" +
                "department VARCHAR(50),\n" +
                "profession VARCHAR(50),\n" +
                "csno VARCHAR(20),\n" +
                "address VARCHAR(200),\n" +
                "email VARCHAR(100),\n" +
                "introduce VARCHAR(500),\n" +
                "status VARCHAR(10) default('false')\n" +
                ")");
        db.execSQL("CREATE TABLE Perform(\n" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT,\n"+
                "thno VARCHAR(20),\n" +
                "stno VARCHAR(20),\n" +
                "datetime VARCHAR(20),\n" +
                "duration VARCHAR(20),\n" +
                "state VARCHAR(20),\n" +
                "reason VARCHAR(500),\n" +
                "session VARCHAR(10),\n" +
                "other VARCHAR(100)\n" +
                ")");
        db.execSQL("CREATE TABLE Homework(\n" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT,\n"+
                "stno VARCHAR(20),\n" +
                "thno VARCHAR(20),\n" +
                "datetime VARCHAR(20),\n" +
                "problem VARCHAR(500) not null,\n" +
                "score VARCHAR(8),\n" +
                "comment VARCHAR(300),\n" +
                "other VARCHAR(100)\n" +
                ")");
        db.execSQL("CREATE TABLE ClassAccess(\n" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT,\n"+
                "stno VARCHAR(20),\n" +
                "thno VARCHAR(20),\n" +
                "datetime VARCHAR(20),\n" +
                "score VARCHAR(5),\n" +
                "session VARCHAR(10),\n" +
                "other VARCHAR(300)\n" +
                ")");
    }
    private void InsertDefaultData(SQLiteDatabase db){
        db.execSQL("INSERT INTO Admin(account,pwd,lastlogin,name,email,introduce,status) VALUES('ugex','admin','0','超级管理员','ugex_savelar@163.com','本系统的超级管理员兼开发者、设计者','true');");
        db.execSQL("INSERT INTO Admin(account,pwd,name,introduce,status) VALUES('root','root','管理员','系统管理员一枚','true');");
        db.execSQL("INSERT INTO Teacher(account,pwd,name,pno,college,department,profession,email,introduce,status) VALUES('13033334444','123456','李教授','32414412','安卓开发技术院','计算机与信息科学系','计算机科学与技术','tch_li@google.mail.com','来自美国的外教','true');");
        db.execSQL("INSERT INTO Teacher(account,pwd,name,pno,college,department,profession,email,introduce,status) VALUES('18088886666','123456','顾博士','32414413','安卓开发技术院','计算机与信息科学系','计算机科学与技术','tch_ligu@google.mail.com','来自福建的博士','true');");
        db.execSQL("INSERT INTO Class(pno,name) VALUES('8001','软件工程1701');");
        db.execSQL("INSERT INTO Class(pno,name) VALUES('8002','软件工程1702');");
        db.execSQL("INSERT INTO Student(account,pwd,name,pno,college,department,profession,csno,inyear,email,introduce,status) VALUES('18011112222','123456','张三','3001000169','安卓开发技术院','计算机与信息科学系','计算机科学与技术','8001','2017','stu_zhangsan@google.mail.com','虚拟学生一枚','true');");
        db.execSQL("INSERT INTO Student(account,pwd,name,pno,college,department,profession,csno,inyear,email,introduce,status) VALUES('18011113333','123456','李四','3001000170','安卓开发技术院','计算机与信息科学系','计算机科学与技术','8001','2017','stu_lisi@google.mail.com','虚拟学生一枚','true');");
        db.execSQL("INSERT INTO Perform(stno,thno,duration,state,reason,datetime,session) VALUES('3001000169','32414412','半天','病假','感冒','2020-5-15','1');");
        db.execSQL("INSERT INTO Homework(stno,thno,problem,score,comment,datetime) VALUES('3001000169','32414412','第一次作业：1+1=?','0','这么简单都不会做，可以去*了','2020-5-16');");
        db.execSQL("INSERT INTO ClassAccess(stno,thno,score,other,datetime,session) VALUES('3001000169','32414412','75','稍微有点分神打瞌睡','2020-5-17','3');");
    }
}
