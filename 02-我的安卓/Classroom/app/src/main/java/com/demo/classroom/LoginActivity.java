package com.demo.classroom;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.demo.classroom.Admin.AdminMainActivity;
import com.demo.classroom.Service.ServiceImpl.Admin;
import com.demo.classroom.Service.ServiceImpl.Student;
import com.demo.classroom.Service.ServiceImpl.Teacher;
import com.demo.classroom.Student.StudentMainActivity;
import com.demo.classroom.Teacher.TeacherMainActivity;
import com.demo.classroom.Util.ActivityHelper;
import com.demo.classroom.Util.SharedPreferencesHelper;
import com.demo.classroom.Util.UtilHelper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {
    private EditText edtAccount;
    private EditText edtPassword;
    private RadioGroup rgpType;
    private CheckBox ckbRememberLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        InitActivity();
    }

    private void InitActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.READ_SMS,
                            Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.READ_PHONE_STATE
                    },
                    0x101);
        }
        edtAccount=findViewById(R.id.editTextPhone);
        edtPassword=findViewById(R.id.editTextPwd);
        rgpType=findViewById(R.id.radioGroupType);
        ckbRememberLogin=findViewById(R.id.checkBoxRememberLogin);

        Intent intent=getIntent();
        String pho=intent.getStringExtra(ActivityHelper.KEY_PHONE);
        String type=intent.getStringExtra(ActivityHelper.KEY_TYPE);
        if(UtilHelper.isEmptyString(pho)==false){
            edtAccount.setText(pho);
        }
        if(UtilHelper.isEmptyString(type)==false){
            if(ActivityHelper.TYPE_STUDENT.equals(type))
                rgpType.check(R.id.radioButtonStudent);
            else if(ActivityHelper.TYPE_TEACHER.equals(type))
                rgpType.check(R.id.radioButtonTeacher);
            else if(ActivityHelper.TYPE_ADMIN.equals(type))
                rgpType.check(R.id.radioButtonAdmin);
        }

        tryAutoLogin();
    }

    public void OnClickedLogin(View view) {
        String pho=edtAccount.getText().toString().trim();
        String pwd=edtPassword.getText().toString().trim();
        if("".equals(pho) || "".equals(pwd))
        {
            Toast.makeText(this, "请输入完整的注册信息", Toast.LENGTH_SHORT).show();
            return;
        }
        String type=ActivityHelper.getRadioType(rgpType.getCheckedRadioButtonId());
        if(UtilHelper.isEmptyString(type)==false)
            directLogin(type,pho,pwd);

    }

    private void directLogin(String type,String phone,String password)
    {
        Intent intent=new Intent();
        boolean loginSuccess=false;
        if(ActivityHelper.TYPE_STUDENT.equals(type)){
            Student stu=new Student(phone);
            stu.password=password;
            if(stu.login(getContentResolver())){
                loginSuccess=true;
                intent.setClass(this, StudentMainActivity.class);
            }
        }else if(ActivityHelper.TYPE_TEACHER.equals(type)){
            Teacher stu=new Teacher(phone);
            stu.password=password;
            if(stu.login(getContentResolver())){
                loginSuccess=true;
                intent.setClass(this, TeacherMainActivity.class);
            }
        }else if(ActivityHelper.TYPE_ADMIN.equals(type)){
            Admin stu=new Admin(phone);
            stu.password=password;
            if(stu.login(getContentResolver())){
                loginSuccess=true;
                intent.setClass(this, AdminMainActivity.class);
            }
        }

        if(loginSuccess){
            if(ckbRememberLogin.isChecked()){
                saveRememberInfo(type, phone, password);
            }else{
                ActivityHelper.Logout(this);
            }
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            intent.putExtra(ActivityHelper.KEY_PHONE,phone);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this, "登录失败，原因未知", Toast.LENGTH_SHORT).show();
        }

    };

    private void saveRememberInfo(String type,String phone,String password){
        Map<String,String> info=new HashMap<>();
        info.put("type",type);
        info.put("phone",phone);
        info.put("password",password);
        info.put("legalTime",(new Date().getTime()+3*24*60*60*1000)+"");
        SharedPreferencesHelper.saveTo(this,"remlogin",info);
    }
    private void tryAutoLogin(){
        Map<String,String> info=new HashMap<>();
        info.put("type","");
        info.put("phone","");
        info.put("password","");
        info.put("legalTime","");
        SharedPreferencesHelper.readFrom(this,"remlogin",info);

        if(UtilHelper.isEmptyString(info.get("type"))==false){
            if(new Date().getTime() > Long.parseLong(info.get("legalTime")))
                return;
            directLogin(info.get("type"),info.get("phone"),info.get("password"));
        }
    }

    public void OnClickedGoRegister(View view) {
        startActivity(new Intent(this,RegisterActivity.class));
        finish();
    }
}
