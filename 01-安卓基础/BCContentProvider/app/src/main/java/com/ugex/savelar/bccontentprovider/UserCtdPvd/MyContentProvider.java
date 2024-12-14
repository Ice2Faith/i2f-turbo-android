package com.ugex.savelar.bccontentprovider.UserCtdPvd;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
/*
* 这里一般和一个SQLiteOpenHelper关联，
* 把传递过来的参数经过自己处理之后，
* 交给db来执行即可*/

/*
* UriMatcher工具类
* 继承ContentProvider后，ContentProvider只有一个onCreate生命周期，
* 其他程序通过ContentResolver第一次访问是，onCreate被回调
* 其他程序使用ContentResolver指定增删等操作时，都需要一个Uri参数
* 为了能顺利的提供这个Uri参数，Android提供了一个UriMatcher工具类
* 比如数据库表比较多的时候
*
* 使用：
* 在ContentProvider中添加一个UriMatcher变量，并使用静态代码块初始化Matcher
* 在接收到CURD调用的时候，使用code=matcher.match(uri);获得相应的代码
* 然后根据代码做对应的操作
* 调用的时候只需要给出对应的URI即可
* */
public class MyContentProvider extends ContentProvider {
    private StudentDbHelper helper;
    private SQLiteDatabase db;
    //使用静态变量加静态代码块的方式进行一开始的初始化
    private static UriMatcher matcher=new UriMatcher(UriMatcher.NO_MATCH);
    static{
        //用0来表示student这张表，对应调用时的URI：
        //content://ugex.provider/student
        //也就是把这个URI用一个代码来表示
        //参数：注册时的authrities,路径，对应的代码
        matcher.addURI("ugex.provider","student",0);
        //用1来表示teacher这张表，对应调用时的URI：
        //URI:协议://域名/路径?参数
        //content://ugex.provider/teacher
        matcher.addURI("ugex.provider","teacher",1);
    }
    @Override
    public boolean onCreate() {
        //注意这里的Context不能使用this,因为它实际运行的上下文可不一定是自己
        //另外Toast的context不能为null,应用程序会崩溃
        Toast.makeText(getContext(), "MYCP:onCreate", Toast.LENGTH_SHORT).show();
        helper=new StudentDbHelper(getContext());//这里不能用有this标识content，而是所在的getcontent
        db=helper.getWritableDatabase();//顺便拿到DB
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Toast.makeText(getContext(), "MYCP:query", Toast.LENGTH_SHORT).show();
        //使用UriMatcher来获取URI的code，进行判断之后进行相应的处理
        /*int code=matcher.match(uri);//获取URI对应的代码（匹配代码）
        if(code==0){
            //查询学生表
        }else if(code==1){
            //查询教师表
        }*/
        //根据参数，进行自己的修改或者参数透传
        Cursor cur=db.query("student",projection,selection,selectionArgs,null,null,sortOrder);
        return cur;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Toast.makeText(getContext(), "MYCP:getType", Toast.LENGTH_SHORT).show();
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Toast.makeText(getContext(), "MYCP:insert", Toast.LENGTH_SHORT).show();
        long rowId=db.insert("student",null,values);
        Uri ruri=ContentUris.withAppendedId(uri,rowId);//也就是在原有的URI上追加一个值，这里就是：/rwoId
        return ruri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Toast.makeText(getContext(), "MYCP:delete", Toast.LENGTH_SHORT).show();

        return 1;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Toast.makeText(getContext(), "MYCP:update", Toast.LENGTH_SHORT).show();

        return 0;
    }
}
