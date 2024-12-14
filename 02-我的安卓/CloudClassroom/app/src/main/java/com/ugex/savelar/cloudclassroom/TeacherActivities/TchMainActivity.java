package com.ugex.savelar.cloudclassroom.TeacherActivities;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.ugex.savelar.cloudclassroom.Entities.EntityGridViewJump2;
import com.ugex.savelar.cloudclassroom.Entities.EntityTeacher;
import com.ugex.savelar.cloudclassroom.Entities.MyMainGridViewAdapter;
import com.ugex.savelar.cloudclassroom.R;
import com.ugex.savelar.cloudclassroom.Tools.UtilHelper;
import com.ugex.savelar.cloudclassroom.pubActivities.LoginActivity;
import com.ugex.savelar.cloudclassroom.pubActivities.ModifyPubSelfInfoActivity;

import java.util.ArrayList;
import java.util.List;

public class TchMainActivity extends Activity {
    private Resources res;
    private EntityTeacher teacher;
    private GridView gdvMain;
    private MyMainGridViewAdapter adapter;
    private List<EntityGridViewJump2> items=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tch_main);

        InitActivity();
    }

    private void InitActivity() {
        res=getResources();
        String account=getIntent().getStringExtra(UtilHelper.ExtraKey.ACCOUNT);
        if(UtilHelper.stringIsNullOrEmpty(account)){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }else{
            teacher=new EntityTeacher(account);
            teacher.getDataFromDb(getContentResolver());
            if(UtilHelper.stringIsNullOrEmpty(teacher.Cpno)){
                Toast.makeText(this, res.getString(R.string.pls_use_after_admin_gives_permission_and_fill_teacher_no), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(this,TchModifyDetailInfoActivity.class);
                intent.putExtra(UtilHelper.ExtraKey.ACCOUNT,teacher.Caccount);
                startActivity(intent);
            }
        }
        addItemsToList();
        adapter=new MyMainGridViewAdapter(items,this);
        gdvMain=(GridView)findViewById(R.id.grideViewMain);
        gdvMain.setAdapter(adapter);
        gdvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((EntityGridViewJump2)(adapter.getItem(position))).jumpToActivity(TchMainActivity.this);
            }
        });
    }


    private void addItemsToList(){
        Intent intent=new Intent(this, TchClassAccessManageActivity.class);
        intent.putExtra(UtilHelper.ExtraKey.ACCOUNT,teacher.Caccount);
        EntityGridViewJump2 item=new EntityGridViewJump2(intent,res.getString(R.string.class_access_manage),R.drawable.access,teacher.isUsefulAccount());
        items.add(item);

        intent=new Intent(this, TchHomeworkManageActivity.class);
        intent.putExtra(UtilHelper.ExtraKey.ACCOUNT,teacher.Caccount);
        item=new EntityGridViewJump2(intent,res.getString(R.string.homework_manage),R.drawable.homework,teacher.isUsefulAccount());
        items.add(item);

        intent=new Intent(this, TchPerformManageActivity.class);
        intent.putExtra(UtilHelper.ExtraKey.ACCOUNT,teacher.Caccount);
        item=new EntityGridViewJump2(intent,res.getString(R.string.perform_manage),R.drawable.perform,teacher.isUsefulAccount());
        items.add(item);

        intent=new Intent(this, TchModifyDetailInfoActivity.class);
        intent.putExtra(UtilHelper.ExtraKey.ACCOUNT,teacher.Caccount);
        item=new EntityGridViewJump2(intent,res.getString(R.string.privacy_info_manage),R.drawable.privacy,true);
        items.add(item);


        intent=new Intent(this, ModifyPubSelfInfoActivity.class);
        intent.putExtra(UtilHelper.ExtraKey.ACCOUNT,teacher.Caccount);
        intent.putExtra(UtilHelper.ExtraKey.AC_TYPE,UtilHelper.LoginTypeValue.TYPE_TEACHER);
        item=new EntityGridViewJump2(intent,res.getString(R.string.personal_info_manage),R.drawable.personal,true);
        items.add(item);
    }
}
