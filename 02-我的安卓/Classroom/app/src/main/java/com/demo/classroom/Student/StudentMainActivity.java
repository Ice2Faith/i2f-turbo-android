package com.demo.classroom.Student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.demo.classroom.LoginActivity;
import com.demo.classroom.R;
import com.demo.classroom.Service.ServiceImpl.Student;
import com.demo.classroom.Util.ActivityHelper;
import com.demo.classroom.Util.GuideItemAdapter;
import com.demo.classroom.Util.ItemHelper;

import java.util.ArrayList;
import java.util.List;

public class StudentMainActivity extends AppCompatActivity {
    private Student student;
    private ListView lsvGuide;
    private GuideItemAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        InitActivity();
    }

    private void InitActivity() {
        student=new Student(getIntent().getStringExtra(ActivityHelper.KEY_PHONE));
        student.readFromDb(getContentResolver());

        lsvGuide=findViewById(R.id.listViewGuide);

        List<ItemHelper> data=new ArrayList<>();

        Intent intent=new Intent(this,StudentPrivateInfoActivity.class);
        intent.putExtra(ActivityHelper.KEY_PHONE,student.phone);
        ItemHelper item=new ItemHelper("个人信息","修改维护个人信息",R.mipmap.ic_launcher,this,intent);
        data.add(item);

        intent=new Intent(this,StudentViewAttendenceActivity.class);
        intent.putExtra(ActivityHelper.KEY_PHONE,student.phone);
        item=new ItemHelper("出勤查询","出勤信息浏览",R.mipmap.ic_launcher,this,intent);
        data.add(item);

        intent=new Intent(this,StudentViewHomeworkActivity.class);
        intent.putExtra(ActivityHelper.KEY_PHONE,student.phone);
        item=new ItemHelper("平时作业查询","平时作业信息浏览",R.mipmap.ic_launcher,this,intent);
        data.add(item);

        intent=new Intent(this,StudentViewClassAccessActivity.class);
        intent.putExtra(ActivityHelper.KEY_PHONE,student.phone);
        item=new ItemHelper("课堂评价查询","课堂评价信息浏览",R.mipmap.ic_launcher,this,intent);
        data.add(item);

        intent=new Intent(this, LoginActivity.class);
        item=new ItemHelper("注销登录","注销登录",R.mipmap.ic_launcher,this,intent);
        data.add(item);

        adapter=new GuideItemAdapter(this,data);
        lsvGuide.setAdapter(adapter);

        lsvGuide.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ItemHelper item=(ItemHelper)adapter.getItem(position);
                item.JumpTo();
                if(item.title.equals("注销登录")){
                    ActivityHelper.Logout(StudentMainActivity.this);
                    StudentMainActivity.this.finishAffinity();
                }
            }
        });
    }
}
