package com.demo.classroom.DAO;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.net.Uri;
public class ClassroomDbProvider extends ContentProvider {
    public static final String PROVIDER_AUTHORITY_STRING="com.demo.classroom.provider.classroomdbprovider";
    public interface TableNames{
        String Student="Student"; //这里的表名，在DBHelper里面有的基本都要有
        String Teacher="Teacher";
        String Admin="Admin";
        String Attendance="Attendance";
        String Homework="Homework";
        String ClassAccess="ClassAccess";
    }
    public interface URICode{
        int STUDENT=1;
        int TEACHER     =2;
        int ADMIN       =3;
        int ATTENDANCE  =4;
        int HOMEWORK    =5;
        int CLASSACCESS =6;
    }
    public interface UriHelper{
        Uri STUDENT=Uri.parse("content://"+PROVIDER_AUTHORITY_STRING+"/"+"student");
        Uri TEACHER    =Uri.parse("content://"+PROVIDER_AUTHORITY_STRING+"/"+"teacher");
        Uri ADMIN      =Uri.parse("content://"+PROVIDER_AUTHORITY_STRING+"/"+"admin");
        Uri ATTENDANCE =Uri.parse("content://"+PROVIDER_AUTHORITY_STRING+"/"+"attendance");
        Uri HOMEWORK   =Uri.parse("content://"+PROVIDER_AUTHORITY_STRING+"/"+"homework");
        Uri CLASSACCESS=Uri.parse("content://"+PROVIDER_AUTHORITY_STRING+"/"+"classaccess");
    }
    private static UriMatcher matcher=new UriMatcher(UriMatcher.NO_MATCH);
    static{
        //用整型值URICode.STUDENT来表示字符串值："content://"+PROVIDER_AUTHORITY_STRING+"/"+"student"
        //注意student之前的那个"/"是必须的，"/"+"student"就是构成了URI的路径Path
        matcher.addURI(PROVIDER_AUTHORITY_STRING,"student",URICode.STUDENT); //这里每添加一个URI，就要在UriHelper里面添加对应的常量
        matcher.addURI(PROVIDER_AUTHORITY_STRING,"teacher",URICode.TEACHER);
        matcher.addURI(PROVIDER_AUTHORITY_STRING,"admin",URICode.ADMIN);
        matcher.addURI(PROVIDER_AUTHORITY_STRING,"attendance",URICode.ATTENDANCE);
        matcher.addURI(PROVIDER_AUTHORITY_STRING,"homework",URICode.HOMEWORK);
        matcher.addURI(PROVIDER_AUTHORITY_STRING,"classaccess",URICode.CLASSACCESS);
    }
    private SQLiteDatabase db;
    @Override
    public boolean onCreate() {
        DBHelper helper=new DBHelper(getContext());
        db=helper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor=null;
        switch (matcher.match(uri))
        {
            case URICode.STUDENT:
                cursor=db.query(TableNames.Student,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case URICode.TEACHER:
                cursor=db.query(TableNames.Teacher,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case URICode.ADMIN:
                cursor=db.query(TableNames.Admin,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case URICode.ATTENDANCE:
                cursor=db.query(TableNames.Attendance,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case URICode.HOMEWORK:
                cursor=db.query(TableNames.Homework,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case URICode.CLASSACCESS:
                cursor=db.query(TableNames.ClassAccess,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long ret=-1;
        switch (matcher.match(uri))
        {
            case URICode.STUDENT:
                ret=db.insert(TableNames.Student,null,values);
                break;
            case URICode.TEACHER:
                ret=db.insert(TableNames.Teacher,null,values);
                break;
            case URICode.ADMIN:
                ret=db.insert(TableNames.Admin,null,values);
                break;
            case URICode.ATTENDANCE:
                ret=db.insert(TableNames.Attendance,null,values);
                break;
            case URICode.HOMEWORK:
                ret=db.insert(TableNames.Homework,null,values);
                break;
            case URICode.CLASSACCESS:
                ret=db.insert(TableNames.ClassAccess,null,values);
                break;
            default:
                break;
        }
        return Uri.parse("content://"+PROVIDER_AUTHORITY_STRING+"/"+(ret==-1?"false":"true"));
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int ret=0;
        switch (matcher.match(uri))
        {
            case URICode.STUDENT:
                ret=db.delete(TableNames.Student,selection,selectionArgs);
                break;
            case URICode.TEACHER:
                ret=db.delete(TableNames.Teacher,selection,selectionArgs);
                break;
            case URICode.ADMIN:
                ret=db.delete(TableNames.Admin,selection,selectionArgs);
                break;
            case URICode.HOMEWORK:
                ret=db.delete(TableNames.Homework,selection,selectionArgs);
                break;
            case URICode.ATTENDANCE:
                ret=db.delete(TableNames.Attendance,selection,selectionArgs);
                break;
            case URICode.CLASSACCESS:
                ret=db.delete(TableNames.ClassAccess,selection,selectionArgs);
                break;
            default:
                break;
        }
        return ret;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int ret=0;
        switch (matcher.match(uri))
        {
            case URICode.STUDENT:
                ret=db.update(TableNames.Student,values,selection,selectionArgs);
                break;
            case URICode.TEACHER:
                ret=db.update(TableNames.Teacher,values,selection,selectionArgs);
                break;
            case URICode.ADMIN:
                ret=db.update(TableNames.Admin,values,selection,selectionArgs);
                break;
            case URICode.ATTENDANCE:
                ret=db.update(TableNames.Attendance,values,selection,selectionArgs);
                break;
            case URICode.HOMEWORK:
                ret=db.update(TableNames.Homework,values,selection,selectionArgs);
                break;
            case URICode.CLASSACCESS:
                ret=db.update(TableNames.ClassAccess,values,selection,selectionArgs);
                break;
            default:
                break;
        }
        return ret;
    }
}
