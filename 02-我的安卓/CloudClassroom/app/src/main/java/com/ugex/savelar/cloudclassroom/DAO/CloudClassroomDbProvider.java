package com.ugex.savelar.cloudclassroom.DAO;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CloudClassroomDbProvider extends ContentProvider {
    public static class TableNames{
        public static final String Student="Student";
        public static final String Admin="Admin";
        public static final String Teacher="Teacher";
        public static final String Perform="Perform";
        public static final String Homework="Homework";
        public static final String ClassAccess="ClassAccess";
        public static final String Class="Class";
    }
    public static  class URICode{
        public static final int GET_STUDENT=0x001;
        public static final int GET_TEACHER=0x002;
        public static final int GET_ADMIN=0x003;
        public static final int GET_PERFORM=0x004;
        public static final int GET_HOMEWORK=0x005;
        public static final int GET_CLASSACCESS=0x006;
        public static final int GET_CLASS=0x007;
    }
    private SQLiteDatabase db;
    private static UriMatcher matcher=new UriMatcher(UriMatcher.NO_MATCH);
    private static final String PROVIDER_AUTHORITY_STRING="ugex.provider.cloudclassroom";
    static{
        //用0来表示student这张表，对应调用时的URI：
        //content://ugex.provider/student
        //也就是把这个URI用一个代码来表示
        //参数：注册时的authrities,路径，对应的代码
        matcher.addURI(PROVIDER_AUTHORITY_STRING,"get/student",URICode.GET_STUDENT);
        matcher.addURI(PROVIDER_AUTHORITY_STRING,"get/teacher",URICode.GET_TEACHER);
        matcher.addURI(PROVIDER_AUTHORITY_STRING,"get/admin",URICode.GET_ADMIN);
        matcher.addURI(PROVIDER_AUTHORITY_STRING,"get/class",URICode.GET_CLASS);
        matcher.addURI(PROVIDER_AUTHORITY_STRING,"get/classaccess",URICode.GET_CLASSACCESS);
        matcher.addURI(PROVIDER_AUTHORITY_STRING,"get/homework",URICode.GET_HOMEWORK);
        matcher.addURI(PROVIDER_AUTHORITY_STRING,"get/perform",URICode.GET_PERFORM);
    }
    @Override
    public boolean onCreate() {
        SQLCloudClassroomOpenHelper helper=new SQLCloudClassroomOpenHelper(getContext());
        db=helper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cur=null;
        switch (matcher.match(uri))
        {
            case URICode.GET_ADMIN:
                cur=db.query(TableNames.Admin,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case URICode.GET_CLASS:
                cur=db.query(TableNames.Class,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case URICode.GET_CLASSACCESS:
                cur=db.query(TableNames.ClassAccess,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case URICode.GET_HOMEWORK:
                cur=db.query(TableNames.Homework,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case URICode.GET_PERFORM:
                cur=db.query(TableNames.Perform,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case URICode.GET_STUDENT:
                cur=db.query(TableNames.Student,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case URICode.GET_TEACHER:
                cur=db.query(TableNames.Teacher,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                break;
        }
        return cur;
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
            case URICode.GET_ADMIN:
                ret=db.insert(TableNames.Admin,null,values);
                break;
            case URICode.GET_CLASS:
                ret=db.insert(TableNames.Class,null,values);
                break;
            case URICode.GET_CLASSACCESS:
                ret=db.insert(TableNames.ClassAccess,null,values);
                break;
            case URICode.GET_HOMEWORK:
                ret=db.insert(TableNames.Homework,null,values);
                break;
            case URICode.GET_PERFORM:
                ret=db.insert(TableNames.Perform,null,values);
                break;
            case URICode.GET_STUDENT:
                ret=db.insert(TableNames.Student,null,values);
                break;
            case URICode.GET_TEACHER:
                ret=db.insert(TableNames.Teacher,null,values);
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
            case URICode.GET_ADMIN:
                ret=db.delete(TableNames.Admin,selection,selectionArgs);
                break;
            case URICode.GET_CLASS:
                ret=db.delete(TableNames.Class,selection,selectionArgs);
                break;
            case URICode.GET_CLASSACCESS:
                ret=db.delete(TableNames.ClassAccess,selection,selectionArgs);
                break;
            case URICode.GET_HOMEWORK:
                ret=db.delete(TableNames.Homework,selection,selectionArgs);
                break;
            case URICode.GET_PERFORM:
                ret=db.delete(TableNames.Perform,selection,selectionArgs);
                break;
            case URICode.GET_STUDENT:
                ret=db.delete(TableNames.Student,selection,selectionArgs);
                break;
            case URICode.GET_TEACHER:
                ret=db.delete(TableNames.Teacher,selection,selectionArgs);
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
            case URICode.GET_ADMIN:
                ret=db.update(TableNames.Admin,values,selection,selectionArgs);
                break;
            case URICode.GET_CLASS:
                ret=db.update(TableNames.Class,values,selection,selectionArgs);
                break;
            case URICode.GET_CLASSACCESS:
                ret=db.update(TableNames.ClassAccess,values,selection,selectionArgs);
                break;
            case URICode.GET_HOMEWORK:
                ret=db.update(TableNames.Homework,values,selection,selectionArgs);
                break;
            case URICode.GET_PERFORM:
                ret=db.update(TableNames.Perform,values,selection,selectionArgs);
                break;
            case URICode.GET_STUDENT:
                ret=db.update(TableNames.Student,values,selection,selectionArgs);
                break;
            case URICode.GET_TEACHER:
                ret=db.update(TableNames.Teacher,values,selection,selectionArgs);
                break;
            default:
                break;
        }
        return ret;
    }
}
