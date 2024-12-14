package com.ugex.savelar.aladapterlistview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

//类似C# ADO
public class MainActivity extends AppCompatActivity {
    private String[] DataSource={"李白","韩信","王昭君","孙尚香","貂蝉"};
    private ListView listview;
    private List<Map<String,String>> simDataSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitApp();
    }
    private void InitApp(){
        listview=findViewById(R.id.ListViewData);
        //ArrayDataSourceAdapter();
       // SimpleDataSourceAdapter();
        try {
            XmlSimpleDataSourceAdapter();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void ArrayDataSourceAdapter(){
        //自定义布局：上下文，布局，显示项，数据源
        // ArrayAdapter<String> adapter=new ArrayAdapter<>(this,R.layout.listviewitem,R.id.textViewString,DataSource);
        //系统默认的布局
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,android.R.id.text1,DataSource);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,DataSource[position],Toast.LENGTH_LONG).show();
            }
        });
        listview.setAdapter(adapter);
    }
    private void SimpleDataSourceAdapter(){
        simDataSource=new ArrayList<Map<String,String>>();
        Map<String,String> dt=new HashMap<>();
        dt.put("name","韩信");
        dt.put("attri","刺客");
        simDataSource.add(dt);
        Map<String,String> dt2=new HashMap<>();
        dt2.put("name","王昭君");
        dt2.put("attri","法师");
        simDataSource.add(dt2);
        //上下文，数据源，布局，数据源Key，显示项ID
        SimpleAdapter adpter=new SimpleAdapter(this,simDataSource,R.layout.simlistviewitem,new String[]{"name","attri"},new int[]{R.id.textViewName,R.id.textViewAttri});
        listview.setAdapter(adpter);
    }
    static class Student{
        public String name;
        public String age;
        public Student(){}
        public Student(String name,String age){
            this.name=name;
            this.age=age;
        }
    }
    private void XmlSimpleDataSourceAdapter() throws XmlPullParserException, IOException {
        //从XML读取数据
        List<Student> lsd=new ArrayList<>();
        Student stu=new Student();;
        XmlPullParser parser=getResources().getXml(R.xml.stus) ;
        int envent=parser.getEventType();
        while(envent!=XmlPullParser.END_DOCUMENT){
            switch (envent){
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    if(parser.getName().equals("stu"))
                        stu=new Student();
                    if(parser.getName().equals("name"))
                        stu.name=parser.nextText();
                    if(parser.getName().equals("age"))
                        stu.age=parser.nextText();
                    break;
                case XmlPullParser.END_TAG:
                    if(parser.getName().equals("stu"))
                        lsd.add(stu);
                    break;
            }
            envent=parser.next();
        }
        //填充数据
        simDataSource=new ArrayList<Map<String,String>>();
        for(Student ps:lsd){
            Map<String,String> dt=new HashMap<>();
            dt.put("name",ps.name);
            dt.put("age",ps.age);
            simDataSource.add(dt);
        }
        SimpleAdapter adpter=new SimpleAdapter(this,simDataSource,R.layout.simlistviewitem,new String[]{"name","age"},new int[]{R.id.textViewName,R.id.textViewAttri});
        listview.setAdapter(adpter);
    }
}
