package com.ugex.savelar.cloudclassroom.pubActivities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ugex.savelar.cloudclassroom.Entities.EntityAdmin;
import com.ugex.savelar.cloudclassroom.Entities.EntityPerson;
import com.ugex.savelar.cloudclassroom.Entities.EntityStudent;
import com.ugex.savelar.cloudclassroom.Entities.EntityTeacher;
import com.ugex.savelar.cloudclassroom.R;
import com.ugex.savelar.cloudclassroom.Tools.CheckCode;
import com.ugex.savelar.cloudclassroom.Tools.SendMessageUtil;
import com.ugex.savelar.cloudclassroom.Tools.UtilHelper;
import com.ugex.savelar.cloudclassroom.pubActivities.LoginActivity;
import com.ugex.savelar.cloudclassroom.pubActivities.ModifyPubSelfInfoActivity;

public class RegisterActivity extends Activity {
    private Resources res;
    private EntityPerson person;
    private EditText edtUserAccount;
    private EditText edtUserPassword;
    private EditText edtRepeatPassword;
    private RadioGroup rgLoginType;
    private EditText edtChechCode;
    private Chronometer chronometer;
    private TextView tvRequireCheckCode;
    private String sentCode;
    public static final int NEXT_SEND_CHECK_CODE_WAIT_SECOND=60*1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        InitActivity();
    }
    private void InitActivity() {
        res=getResources();
        edtUserAccount=(EditText)findViewById(R.id.editTextUserAccount);
        edtUserPassword=(EditText)findViewById(R.id.editTextUserPassword);
        edtRepeatPassword=(EditText)findViewById(R.id.editTextRepeatUserPassword);
        rgLoginType=(RadioGroup)findViewById(R.id.radioGroupLoginType);
        edtChechCode=(EditText)findViewById(R.id.editTextCheckCode);
        chronometer=(Chronometer)findViewById(R.id.chronometerTimer);
        tvRequireCheckCode =(TextView)findViewById(R.id.textViewRequireCheckCode);
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long preWaitTime=(NEXT_SEND_CHECK_CODE_WAIT_SECOND-(SystemClock.elapsedRealtime()-chronometer.getBase()))/1000;
                if(preWaitTime<=0){
                    tvRequireCheckCode.setEnabled(true);
                    tvRequireCheckCode.setText(res.getString(R.string.require_check_code));
                    chronometer.stop();
                }else {
                    tvRequireCheckCode.setText(preWaitTime +res.getString(R.string.require_again_check_code_after_this_second));
                }
            }
        });
    }

    private void toRegister(String type,String account,String password){
        boolean isSuccess=false;
        if(UtilHelper.LoginTypeValue.TYPE_STUDENT.equals(type)){
            person=new EntityStudent(account.trim());
            person.Cpwd=password.trim();
        }else if(UtilHelper.LoginTypeValue.TYPE_TEACHER.equals(type)){
            person=new EntityTeacher(account.trim());
            person.Cpwd=password.trim();
        }else if(UtilHelper.LoginTypeValue.TYPE_ADMIN.equals(type)){
            person=new EntityAdmin(account.trim());
            person.Cpwd=password.trim();
        }
        if(person!=null && person.registerUser(getContentResolver())){
            Toast.makeText(this, res.getString(R.string.register_info_has_submit_please_wait_admin_gives_permission_after_to_use), Toast.LENGTH_SHORT).show();
            isSuccess=true;
        }else{
            Toast.makeText(this, res.getString(R.string.register_failure_had_regited_or_unkown_reason), Toast.LENGTH_SHORT).show();
        }

        if(isSuccess) {
            Intent intent = new Intent(this, ModifyPubSelfInfoActivity.class);
            intent.putExtra(UtilHelper.ExtraKey.AC_TYPE, type.trim());
            intent.putExtra(UtilHelper.ExtraKey.ACCOUNT, account.trim());
            intent.putExtra(ModifyPubSelfInfoActivity.EXTRA_KEY_REGISTER_ENTRY,"regin");
            startActivity(intent);
            finish();
        }
    }
    public void OnClickedToLoginTextView(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void OnClickedRegisterButton(View view) {
        String account=edtUserAccount.getText().toString().trim();
        String password=edtUserPassword.getText().toString().trim();
        String repassword=edtRepeatPassword.getText().toString().trim();
        if(UtilHelper.stringIsNullOrEmpty(account)){
            Toast.makeText(this, res.getString(R.string.pls_type_in_tel_number), Toast.LENGTH_SHORT).show();
            return;
        }
        if(UtilHelper.stringIsNullOrEmpty(password) || UtilHelper.stringIsNullOrEmpty(repassword)){
            Toast.makeText(this, res.getString(R.string.pls_type_in_pwd), Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.equals(repassword)==false){
            Toast.makeText(this, res.getString(R.string.please_input_repeat_pwd), Toast.LENGTH_SHORT).show();
            return;
        }
        if(edtChechCode.getText().toString().trim().equals(sentCode)==false){
            Toast.makeText(this, res.getString(R.string.pls_fill_check_code), Toast.LENGTH_SHORT).show();
            return;
        }
        String type= UtilHelper.LoginTypeValue.getTypeStringById(rgLoginType.getCheckedRadioButtonId());
        toRegister(type,account,password);
    }

    public void OnClickedRequireCheckCodeTextView(View view) {
        String tel=edtUserAccount.getText().toString().trim();
        if(tel.equals("")){
            Toast.makeText(this, res.getString(R.string.pls_type_in_tel_number), Toast.LENGTH_SHORT).show();
            return;
        }
        sentCode= CheckCode.generateCheckCode();
        SendMessageUtil.sendMessage(this,
                sentCode,
                tel);
        tvRequireCheckCode.setEnabled(false);
        chronometer.start();
    }
}
