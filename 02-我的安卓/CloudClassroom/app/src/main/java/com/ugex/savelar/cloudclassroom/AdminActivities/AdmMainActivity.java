package com.ugex.savelar.cloudclassroom.AdminActivities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.ugex.savelar.cloudclassroom.Entities.EntityAdmin;
import com.ugex.savelar.cloudclassroom.Entities.EntityGridViewJump2;
import com.ugex.savelar.cloudclassroom.Entities.MyMainGridViewAdapter;
import com.ugex.savelar.cloudclassroom.R;
import com.ugex.savelar.cloudclassroom.Tools.UtilHelper;
import com.ugex.savelar.cloudclassroom.pubActivities.LoginActivity;
import com.ugex.savelar.cloudclassroom.pubActivities.ModifyPubSelfInfoActivity;

import java.util.ArrayList;
import java.util.List;

public class AdmMainActivity extends Activity {
    private Resources res;
    private EntityAdmin admin;
    private GridView gdvMain;
    private MyMainGridViewAdapter adapter;
    private  List<EntityGridViewJump2> items=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_main);

        InitActivity();
    }

    private void InitActivity() {
        res=getResources();
        String account=getIntent().getStringExtra(UtilHelper.ExtraKey.ACCOUNT);
        if(UtilHelper.stringIsNullOrEmpty(account)){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }else{
            admin=new EntityAdmin(account);
            admin.getDataFromDb(getContentResolver());
        }

        addItemsToList();
        adapter=new MyMainGridViewAdapter(items,this);
        gdvMain=(GridView)findViewById(R.id.grideViewMain);
        gdvMain.setAdapter(adapter);
        gdvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((EntityGridViewJump2)(adapter.getItem(position))).jumpToActivity(AdmMainActivity.this);
            }
        });

    }
    private void addItemsToList(){
        Intent intent=new Intent(this,AdmClassChargeActivity.class);
        intent.putExtra(UtilHelper.ExtraKey.ACCOUNT,admin.Caccount);
        EntityGridViewJump2 item=new EntityGridViewJump2(intent,res.getString(R.string.class_manage),R.drawable.classes,admin.isUsefulAccount());
        items.add(item);

        intent=new Intent(this,AdmModifyAccountStatusActivity.class);
        intent.putExtra(UtilHelper.ExtraKey.ACCOUNT,admin.Caccount);
        item=new EntityGridViewJump2(intent,res.getString(R.string.person_permission_manage),R.drawable.permission,admin.isUsefulAccount());
        items.add(item);

        intent=new Intent(this, ModifyPubSelfInfoActivity.class);
        intent.putExtra(UtilHelper.ExtraKey.ACCOUNT,admin.Caccount);
        intent.putExtra(UtilHelper.ExtraKey.AC_TYPE,UtilHelper.LoginTypeValue.TYPE_ADMIN);
        item=new EntityGridViewJump2(intent,res.getString(R.string.personal_info_manage),R.drawable.personal,true);
        items.add(item);
    }
}
