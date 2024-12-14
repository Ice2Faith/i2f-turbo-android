package com.demo.classroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.classroom.Service.ServiceImpl.Admin;
import com.demo.classroom.Service.ServiceImpl.Student;
import com.demo.classroom.Service.ServiceImpl.Teacher;
import com.demo.classroom.Util.ActivityHelper;
import com.demo.classroom.Util.CheckCode;
import com.demo.classroom.Util.SendMessageUtil;
import com.demo.classroom.Util.UtilHelper;

public class RegisterActivity extends AppCompatActivity {
    private EditText edtPhone;
    private EditText edtPwd;
    private EditText edtCorrectPwd;
    private EditText edtCheckCode;
    private TextView tvSendCode;
    private RadioGroup rgpType;
    private Chronometer chronometer;
    private String saveCode;
    public static final int NEXT_SEND_CHECK_CODE_WAIT_SECOND=60*1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        InitActivity();
    }

    private void InitActivity() {
        edtPhone=findViewById(R.id.editTextPhone);
        edtPwd=findViewById(R.id.editTextPwd);
        edtCorrectPwd=findViewById(R.id.editTextCorrectPwd);
        edtCheckCode=findViewById(R.id.editTextCheckCode);
        rgpType=findViewById(R.id.radioGroupType);
        tvSendCode=findViewById(R.id.textViewSendCode);
        chronometer=findViewById(R.id.chronometer);
        tvSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tel=edtPhone.getText().toString().trim();
                if(UtilHelper.isEmptyString(tel)){
                    Toast.makeText(RegisterActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                saveCode= CheckCode.generateCheckCode();
                SendMessageUtil.sendMessage(RegisterActivity.this,
                        saveCode,
                        tel);
                tvSendCode.setEnabled(false);
                chronometer.start();
            }
        });
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long preWaitTime=(NEXT_SEND_CHECK_CODE_WAIT_SECOND-(SystemClock.elapsedRealtime()-chronometer.getBase()))/1000;
                if(preWaitTime<=0){
                    tvSendCode.setEnabled(true);
                    tvSendCode.setText("发送验证码");
                    chronometer.stop();
                }else {
                    tvSendCode.setText(preWaitTime+"S后重发");
                }
            }
        });
    }
    private void directRegister(String type,String phone,String password){
        boolean loginSuccess=false;
        if(ActivityHelper.TYPE_STUDENT.equals(type)){
            Student stu=new Student(phone);
            stu.password=password;
            if(stu.register(getContentResolver())){
                loginSuccess=true;
            }
        }else if(ActivityHelper.TYPE_TEACHER.equals(type)){
            Teacher stu=new Teacher(phone);
            stu.password=password;
            if(stu.register(getContentResolver())){
                loginSuccess=true;
            }
        }else if(ActivityHelper.TYPE_ADMIN.equals(type)){
            Admin stu=new Admin(phone);
            stu.password=password;
            if(stu.register(getContentResolver())){
                loginSuccess=true;
            }
        }

        if(loginSuccess){
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(this,LoginActivity.class);
            intent.putExtra(ActivityHelper.KEY_PHONE,phone);
            intent.putExtra(ActivityHelper.KEY_TYPE,type);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this, "登录失败，原因未知", Toast.LENGTH_SHORT).show();
        }
    }
    public void OnClickedRegister(View view) {
        String pho=edtPhone.getText().toString().trim();
        String pwd=edtPwd.getText().toString().trim();
        String rpwd=edtCorrectPwd.getText().toString().trim();
        String checkCode=edtCheckCode.getText().toString().trim();
        if(UtilHelper.isEmptyString(pho) || UtilHelper.isEmptyString(pwd) || UtilHelper.isEmptyString(rpwd) || UtilHelper.isEmptyString(checkCode)){
            Toast.makeText(this, "请完善注册信息后重试", Toast.LENGTH_SHORT).show();
            return;
        }
        if(pwd.equals(rpwd)==false){
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        if(checkCode.equals(saveCode)==false){
            Toast.makeText(this, "请输入正确验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        String type= ActivityHelper.getRadioType(rgpType.getCheckedRadioButtonId());
        if("".equals(type)==false)
            directRegister(type,pho,pwd);
    }

    public void OnClickedGoLogin(View view) {
        startActivity(new Intent(this,LoginActivity.class));
    }
}
