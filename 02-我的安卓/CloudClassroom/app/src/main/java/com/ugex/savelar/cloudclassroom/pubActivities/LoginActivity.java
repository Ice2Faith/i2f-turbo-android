package com.ugex.savelar.cloudclassroom.pubActivities;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ugex.savelar.cloudclassroom.AdminActivities.AdmMainActivity;
import com.ugex.savelar.cloudclassroom.Entities.EntityAdmin;
import com.ugex.savelar.cloudclassroom.Entities.EntityPerson;
import com.ugex.savelar.cloudclassroom.Entities.EntityStudent;
import com.ugex.savelar.cloudclassroom.Entities.EntityTeacher;
import com.ugex.savelar.cloudclassroom.R;
import com.ugex.savelar.cloudclassroom.StudentActivities.StuMainActivity;
import com.ugex.savelar.cloudclassroom.TeacherActivities.TchMainActivity;
import com.ugex.savelar.cloudclassroom.Tools.UtilHelper;

import java.util.Map;

public class LoginActivity extends Activity {
    private Resources res;
    public static final String EXTRA_KEY_LOGIN_TYPE="login_type";
    public static final String EXTRA_KEY_LOGIN_ACCOUNT="login_account";
    public static final String EXTRA_KEY_LOGIN_PASSWORD="login_password";
    private EditText edtUserAccount;
    private EditText edtUserPassword;
    private RadioGroup rgLoginType;
    private CheckBox ckbAutoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        InitActivity();
    }

    private void InitActivity() {
        res=getResources();
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

        edtUserAccount=(EditText)findViewById(R.id.editTextUserAccount);
        edtUserPassword=(EditText)findViewById(R.id.editTextUserPassword);
        rgLoginType=(RadioGroup)findViewById(R.id.radioGroupLoginType);
        ckbAutoLogin=(CheckBox)findViewById(R.id.checkBoxAutoLogin);

        tryAutoLogin();

        Map<String,String> lastinfo=LastLoginHelper.getLastLoginType(this);
        tryRecoveryDisplay(lastinfo.get(LastLoginHelper.KEY_LAST_LOGIN_TYPE),
                lastinfo.get(LastLoginHelper.KEY_LAST_LOGIN_ACCOUNT),
                lastinfo.get(LastLoginHelper.KEY_LAST_LOGIN_PASSWORD));

        Intent intent=getIntent();
        String type=intent.getStringExtra(EXTRA_KEY_LOGIN_TYPE);
        String account=intent.getStringExtra(EXTRA_KEY_LOGIN_ACCOUNT);
        String password=intent.getStringExtra(EXTRA_KEY_LOGIN_PASSWORD);
        tryRecoveryDisplay(type, account, password);
    }

    private void tryRecoveryDisplay(String type, String account, String password) {
        if(UtilHelper.stringIsNullOrEmpty(account)==false){
            edtUserAccount.setText(account);
        }
        if(UtilHelper.stringIsNullOrEmpty(password)==false){
            edtUserPassword.setText(password);
        }
        if(UtilHelper.stringIsNullOrEmpty(type)==false) {
            rgLoginType.check(UtilHelper.LoginTypeValue.getTypeIdByType(type));
        }
    }

    private void tryAutoLogin(){
        Map<String,String> info=AutoLoginHelper.readLoginInfo(this);
        String type=info.get(AutoLoginHelper.KEY_TYPE).trim();
        String account=info.get(AutoLoginHelper.KEY_ACCOUNT).trim();
        String password=info.get(AutoLoginHelper.KEY_PASSWORD).trim();
        if(UtilHelper.stringIsNullOrEmpty(type)
        ||UtilHelper.stringIsNullOrEmpty(account)
        ||UtilHelper.stringIsNullOrEmpty(password)){
             return;
        }
        toLogin(type,account,password);
    }

    private void toLogin(String type,String account,String password){
        if(type==null || type.trim().length()==0){
            Toast.makeText(this, res.getString(R.string.unsupported_login_type), Toast.LENGTH_SHORT).show();
            return;
        }
        if(account==null || account.trim().length()==0){
            Toast.makeText(this, res.getString(R.string.pls_type_in_account), Toast.LENGTH_SHORT).show();
            return;
        }
        if(password==null || password.trim().length()==0){
            Toast.makeText(this, res.getString(R.string.pls_type_in_pwd),Toast.LENGTH_SHORT).show();
            return;
        }
        type=type.trim();
        account=account.trim();
        password=password.trim();

        boolean isLoginSuccess=false;
        EntityPerson person=null;
        if(UtilHelper.LoginTypeValue.TYPE_STUDENT.equals(type)){
            person=new EntityStudent(account);
        }else if(UtilHelper.LoginTypeValue.TYPE_TEACHER.equals(type)){
            person=new EntityTeacher(account);
        }else if(UtilHelper.LoginTypeValue.TYPE_ADMIN.equals(type)){
            person=new EntityAdmin(account);
        }
        if(person!=null){
            if(person.getDataFromDb(getContentResolver())){
                if(password.equals(person.Cpwd)){
                    isLoginSuccess=true;
                }
            }
        }

        if(isLoginSuccess) {
            if (ckbAutoLogin.isChecked()) {
                AutoLoginHelper.saveLoginInfo(this, type, account, password);
            }
            LastLoginHelper.setLastLoginInfo(this, type, account, password);

            Intent intent = new Intent();
            intent.putExtra(UtilHelper.ExtraKey.ACCOUNT, account);
            intent.putExtra(UtilHelper.ExtraKey.AC_TYPE,type);
            if (UtilHelper.LoginTypeValue.TYPE_STUDENT.equals(type)) {
                intent.setClass(this, StuMainActivity.class);
            } else if (UtilHelper.LoginTypeValue.TYPE_TEACHER.equals(type)) {
                intent.setClass(this, TchMainActivity.class);
            } else if (UtilHelper.LoginTypeValue.TYPE_ADMIN.equals(type)) {
                intent.setClass(this, AdmMainActivity.class);
            }
            startActivity(intent);
            finish();

        }else{
            Toast.makeText(this, res.getString(R.string.login_failure_please_check_pwd_or_account), Toast.LENGTH_SHORT).show();
        }
    }
    public void OnClickedLoginButton(View view) {
        int tpid=rgLoginType.getCheckedRadioButtonId();
        toLogin(UtilHelper.LoginTypeValue.getTypeStringById(tpid),
                edtUserAccount.getText().toString().trim(),
                edtUserPassword.getText().toString().trim());
    }

    public void OnClickedToRegisterTextView(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }
}
