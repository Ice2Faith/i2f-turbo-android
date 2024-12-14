package com.ugex.savelar.bbdatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

/*数据库的使用
 * SqliteOpenHelper
 *
 * SQLiteDatabase类
 * 等同于JDBC中的Connection和Statement的结合体
 * 即代表与数据的链接，又能由于执行SQL语句
 * */
/*
 * SQLite操作步骤
 * 创建SQliteDatabase对象，代表数据库的链接
 * 创建数据库中的表（Create）
 * 调用SQLiteDatabase对象执行数据库操作
 * 对查询后的结果集Cursor进行处理
 * */
/*SQLiteOpenHelper
 * 推荐从SQLiteOpenHelper继承，实现onCreate方法
 * 在onCreate方法中运行SQL语句创建表格
 * 从SQLiteOpenHelper的接口getReadavleDatabase()或getWritableDatabase()进行操作数据库
 * */
public class MainActivity extends AppCompatActivity {
    private EditText edtSname;
    private EditText edtSsex;
    private ListView lvResult;
    private EditText edtDelName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitApp();
    }

    private MySQLiteOpenHelper helper;
    private SQLiteDatabase db;
    private void InitApp(){
        edtSname=findViewById(R.id.editTextSname);
        edtSsex=findViewById(R.id.editTextSsex);
        lvResult=findViewById(R.id.listViewResult);
        edtDelName=findViewById(R.id.editTextDelName);

        MySQLiteOpenHelper helper=new MySQLiteOpenHelper(MainActivity.this);
        //以读写方式打开，磁盘满了的时候可以只读打开getRead...
        db=helper.getWritableDatabase();

    }
    public void OnInsertStudent(View view){
        /*//可以直接写sql语句进行插入
        String sql="insert into Student(sname,ssex) values('"+
                edtSname.getText().toString()+"','"+
                edtSsex.getText().toString()+"');";
        db.execSQL(sql);
        */

        //也可以使用包装函数
        /*
         * long insert(String table,String nullColumnHack,ContentValues values);
         * 参数：表名，希望插入空值的列名，类似Map的容器
         * */
        ContentValues cvs=new ContentValues();
        cvs.put("sname",edtSname.getText().toString());
        cvs.put("ssex",edtSsex.getText().toString());
        long rowId=db.insert("Student",null,cvs);
        //如果插入没有的列数据，虽然看起来成功了，但是实际上是出错的
        if(rowId!=-1){//返回值是插入的行号，因此不为-1就是成功了
            Toast.makeText(this, "数据插入成功", Toast.LENGTH_SHORT).show();
        }
    }

    //数据库查询操作
    public void OnQueryStudent(View view) {
        /*
        //查询语句也可以直接写SQL语句，但是调用的方法改变：
        Cursor cursor=db.rawQuery("select sname,ssex from Student where ssex=?",new String[]{"男"});
        */
        /*
         * select 列名数组 from 表名 where 条件语句列=条件值 group by ... having ... order by ...
         * 参数：表名，选择的列名数组，where条件语句列，where条件值，group by语句，having语句，order by语句
         * */
        //查询所有数据,返回游标
        Cursor cursor=db.query("Student",
                null,
                null,
                null,
                null,
                null,
                null);

        //实现并设置适配器到ListView进行显示
        CursorAdapter adapter=new CursorAdapter(this,cursor) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                //获取布局
                View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.stu_info_list_item,null);
                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                //绑定数据
                TextView tvname=view.findViewById(R.id.textViewname);
                TextView tvsex=view.findViewById(R.id.textViewsex);
                //通过cursor获取对应的数据，只要给出列索引
                //列索引可以根据列名获取
                tvname.setText(cursor.getString(cursor.getColumnIndex("sname")));
                tvsex.setText(cursor.getString(cursor.getColumnIndex("ssex")));
                Toast.makeText(context, tvname.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        };
        lvResult.setAdapter(adapter);

        /*
        //也可以通过如下方式进行一个简单的适配
        //参数：Context,布局，游标，列名，绑定控件名
        SimpleCursorAdapter adapter1=new SimpleCursorAdapter(
                this,R.layout.stu_info_list_item,
                cursor,
                new String[]{"sname","ssex"},
                new int[]{R.id.textViewname,R.id.textViewsex}
        );
        lvResult.setAdapter(adapter1);
        */

        boolean ret=cursor.moveToFirst();//移动游标到第一条，返回值boolean,是否存在第一条
        if(ret) {
            String fname = cursor.getString(cursor.getColumnIndex("sname"));
            Toast.makeText(this, "第一条："+fname, Toast.LENGTH_SHORT).show();
        }
       // cursor.close(); //使用完游标之后需要进行关闭,如果使用适配器，
        // 那就不用关闭了，如果自己手动关闭，则不会显示出来了
        /*
        //以下的使用方法类似
        cursor.move(12);//基于当前偏移
        cursor.moveToLast();
        cursor.moveToNext();
        cursor.moveToPrevious();
        cursor.moveToPosition(4);//基于绝对下标
        */
        /*//select sname,ssex from Student where ssex='男' group by sname order by sname desc
        db.query("student",
                new String[]{"_id","sname","ssex"},//注意，这里必须带上_id列，否则可能出错
                "ssex=?",//注意，这里是需要占位符的
                new String[]{"男"},
                "sname",
                null,
                "sname desc");
        */
    }

    //数据库的删除与更新
    public void onDeleteStudent(View view){
        String name=edtDelName.getText().toString();
        /*
         * 参数：表名，where语句，where参数
         * 返回值，影响的行数
         * */
        //delete student where sname='name'
        int line=db.delete("student",
                "sname=?",
                new String[]{name});
        if(line>0){
            Toast.makeText(this, "删除数据成功", Toast.LENGTH_SHORT).show();

        }
        /*
        //updata student set 列名=列值，... where 语句=条件
        ContentValues cvs=new ContentValues();
        cvs.put("sname","lisi");
        cvs.put("ssex","man");
        //update student set sname=lisi,ssex=man where sname=李四
        db.update("student",
                cvs,
                "sname=?",
                new String[]{"李四"});
        */
    }

    //关闭数据库，理论上需要判空的
    @Override
    protected void onDestroy() {
        db.close();
        helper.close();
        super.onDestroy();
    }


}
