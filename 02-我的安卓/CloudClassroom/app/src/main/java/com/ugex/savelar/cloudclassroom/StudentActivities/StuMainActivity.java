package com.ugex.savelar.cloudclassroom.StudentActivities;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.ugex.savelar.cloudclassroom.Entities.EntityGridViewJump2;
import com.ugex.savelar.cloudclassroom.Entities.EntityStudent;
import com.ugex.savelar.cloudclassroom.Entities.MyMainGridViewAdapter;
import com.ugex.savelar.cloudclassroom.R;
import com.ugex.savelar.cloudclassroom.Tools.UtilHelper;
import com.ugex.savelar.cloudclassroom.pubActivities.LoginActivity;
import com.ugex.savelar.cloudclassroom.pubActivities.ModifyPubSelfInfoActivity;
import com.ugex.savelar.cloudclassroom.pubActivities.SelectOrViewClassAccessActivity;
import com.ugex.savelar.cloudclassroom.pubActivities.SelectOrViewHomeworkActivity;
import com.ugex.savelar.cloudclassroom.pubActivities.SelectOrViewPerformActivity;

import java.util.ArrayList;
import java.util.List;

public class StuMainActivity extends Activity {
    private Resources res;
    private EntityStudent student;
    private GridView gdvMain;
    private MyMainGridViewAdapter adapter;
    private List<EntityGridViewJump2> items=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_main);

        InitActivity();
    }

    private void InitActivity() {
        res=getResources();
        String account=getIntent().getStringExtra(UtilHelper.ExtraKey.ACCOUNT);
        if(UtilHelper.stringIsNullOrEmpty(account)){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }else{
            student=new EntityStudent(account);
            student.getDataFromDb(getContentResolver());
            if(UtilHelper.stringIsNullOrEmpty(student.Cpno)){
                Toast.makeText(this, res.getString(R.string.pls_to_use_after_fill_student_no), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(this,StuModifyDetialInfoActivity.class);
                intent.putExtra(UtilHelper.ExtraKey.ACCOUNT,student.Caccount);
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
                ((EntityGridViewJump2)(adapter.getItem(position))).jumpToActivity(StuMainActivity.this);
            }
        });

    }

    private void addItemsToList(){
        Intent intent=new Intent(this, SelectOrViewClassAccessActivity.class);
        intent.putExtra(UtilHelper.ExtraKey.ACCOUNT,student.Caccount);
        EntityGridViewJump2 item=new EntityGridViewJump2(intent,res.getString(R.string.student_access_page),R.drawable.access,student.isUsefulAccount());
        items.add(item);

        intent=new Intent(this, SelectOrViewHomeworkActivity.class);
        intent.putExtra(UtilHelper.ExtraKey.ACCOUNT,student.Caccount);
        item=new EntityGridViewJump2(intent,res.getString(R.string.student_homework_page),R.drawable.homework,student.isUsefulAccount());
        items.add(item);

        intent=new Intent(this, SelectOrViewPerformActivity.class);
        intent.putExtra(UtilHelper.ExtraKey.ACCOUNT,student.Caccount);
        item=new EntityGridViewJump2(intent,res.getString(R.string.student_perform_page),R.drawable.perform,student.isUsefulAccount());
        items.add(item);


        intent=new Intent(this,StuNormalScoreQueryActivity.class);
        intent.putExtra(UtilHelper.ExtraKey.ACCOUNT,student.Caccount);
        item=new EntityGridViewJump2(intent,res.getString(R.string.student_normal_score_query),R.drawable.score,student.isUsefulAccount());
        items.add(item);

        intent=new Intent(this, StuModifyDetialInfoActivity.class);
        intent.putExtra(UtilHelper.ExtraKey.ACCOUNT,student.Caccount);
        item=new EntityGridViewJump2(intent,res.getString(R.string.privacy_info_manage),R.drawable.privacy,true);
        items.add(item);

        intent=new Intent(this, ModifyPubSelfInfoActivity.class);
        intent.putExtra(UtilHelper.ExtraKey.ACCOUNT,student.Caccount);
        intent.putExtra(UtilHelper.ExtraKey.AC_TYPE,UtilHelper.LoginTypeValue.TYPE_STUDENT);
        item=new EntityGridViewJump2(intent,res.getString(R.string.personal_info_manage),R.drawable.personal,true);
        items.add(item);
    }
}
