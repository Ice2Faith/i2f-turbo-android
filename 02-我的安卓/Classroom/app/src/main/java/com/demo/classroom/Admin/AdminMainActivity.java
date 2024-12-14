package com.demo.classroom.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.demo.classroom.LoginActivity;
import com.demo.classroom.R;
import com.demo.classroom.Service.ServiceImpl.Admin;
import com.demo.classroom.Util.ActivityHelper;
import com.demo.classroom.Util.GuideItemAdapter;
import com.demo.classroom.Util.ItemHelper;

import java.util.ArrayList;
import java.util.List;

public class AdminMainActivity extends AppCompatActivity {
    private Admin admin;
    private ListView lsvGuide;
    private GuideItemAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        InitActivity();
    }

    private void InitActivity() {
        admin =new Admin(getIntent().getStringExtra(ActivityHelper.KEY_PHONE));
        admin.readFromDb(getContentResolver());

        lsvGuide=findViewById(R.id.listViewGuide);

        List<ItemHelper> data=new ArrayList<>();

        Intent intent=new Intent(this,AdminPrivateInfoActivity.class);
        intent.putExtra(ActivityHelper.KEY_PHONE, admin.phone);
        ItemHelper item=new ItemHelper("个人信息","修改维护个人信息",R.mipmap.ic_launcher,this,intent);
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
                    ActivityHelper.Logout(AdminMainActivity.this);
                    AdminMainActivity.this.finishAffinity();
                }
            }
        });
    }
}
