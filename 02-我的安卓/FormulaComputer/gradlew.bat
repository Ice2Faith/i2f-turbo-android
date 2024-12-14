package com.ugex.savelar.foowordsmemory;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.ugex.savelar.foowordsmemory.service.FooWordsMemoryToastService;

public class MainActivity extends AppCompatActivity {
    private static Bundle setting=new Bundle();

    private CheckBox ckbServiceState;

    private CheckBox ckbGravityUp;
    private CheckBox ckbGravityDown;
    private CheckBox ckbGravityCenter;
    private CheckBox ckbGravityLeft;
    private CheckBox ckbGravityRight;
    private CheckBox ckbGravityVelCenter;
    private CheckBox ckbGravityHorCenter;

    private EditText edtOffsetX;
    private EditText edtOffsetY;

    private EditText edtFontSize;

    private EditText edtWaitMillSecond;
    private EditText edtShowMillSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initActivity();


    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent=new Intent(MainActivity.this,FooWordsMemoryToastService.class);
        startService(intent);

        Toast.makeText(this, "如果你看到这条提示，或者通知栏看到了服务，那么就说明我已经启动了哦", Toast.LENGTH_LONG).show();
    }

    private void initActivity() {
        setting=FooWordsMemoryToastService.getSettingsBundle();

        ckbServiceState=findViewById(R.id.checkBoxServiceState);

        ckbGravityCenter=findViewById(R.id.checkBoxGravityCenter);
        ckbGravityDown=findViewById(R.id.checkBoxGravityDown);
        ckbGravityLeft=findViewById(R.id.checkBoxGravityLeft);
        ckbGravityRight=findViewById(R.id.checkBoxGravityRight);
        ckbGravityUp=findViewById(R.id.checkBoxGravityUp);
        ckbGravityVelCenter=findViewById(R.id.checkBoxGravityVelCenter