package com.ugex.savelar.excompositedesign.Activitys;

import android.Manifest;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ugex.savelar.excompositedesign.MainActivity;
import com.ugex.savelar.excompositedesign.R;
import com.ugex.savelar.excompositedesign.Util.CheckCode;
import com.ugex.savelar.excompositedesign.Util.SRHelper;
import com.ugex.savelar.excompositedesign.Util.SendMessageUtil;

import androidx.appcompat.app.AppCompatActivity;

public class TelLoginPageActivity extends AppCompatActivity {
    private Resources res;
    private EditText edtAccount;
    private TextView tvSendCheckCode;
    private EditText edtCheckCode;
    private Chronometer chrWaitTimer;
    private TextView tvAccountLogin;
    private String preCheckCode;
    public static final int NEXT_SEND_CHECK_CODE_WAIT_SECOND=60;
    private CheckBox ckAutoLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tel_login_page);

        InitActivity();
    }

    private void InitActivity() {
        res=getResources();
        tryDirectLoginFromSharedPreference();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.READ_PHONE_STATE
            },1);
        }
        edtAccount=findViewById(R.id.editTextTelNumber);
        edtCheckCode=findViewById(R.id.editTextCheckCode);
        tvSendCheckCode=findViewById(R.id.textViewSendCheckCode);
        chrWaitTimer=findViewById(R.id.chronometerWaitTimer);
        tvAccountLogin=findViewById(R.id.textViewAccountLoginType);
        ckAutoLogin=findViewById(R.id.checkBoxAutoLogin);

        tvSendCheckCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tel=edtAccount.getText().toString().trim();
                if(tel.equals("")){
                    Toast.makeText(TelLoginPageActivity.this, res.getText(R.string.toast_lease_input_tel_number), Toast.LENGTH_SHORT).show();
                    return;
                }
                preCheckCode= CheckCode.generateCheckCode();
                SendMessageUtil.sendMessage(TelLoginPageActivity.this,
                        preCheckCode,
                        tel);
                resetWaitNextSendCheckCodeTimer();
            }
        });

        tvAccountLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TelLoginPageActivity.this,AccountLoginPageActivity.class));
            }
        });

        chrWaitTimer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long preWaitTime=NEXT_SEND_CHECK_CODE_WAIT_SECOND-(SystemClock.elapsedRealtime()-chrWaitTimer.getBase())/1000;
                if(preWaitTime<=0){
                    tvSendCheckCode.setEnabled(true);
                    tvSendCheckCode.setText(res.getText(R.string.login_page_tv_send_code_text));
                    chrWaitTimer.stop();
                }else {
                    tvSendCheckCode.setText(preWaitTime +""+ res.getText(R.string.login_page_tv_send_code_wait_time_resend));
                }
            }
        });
    }

    private void tryDirectLoginFromSharedPreference(){
        String account= SRHelper.getDataFromSharedPreference(this,
                "logininfo",
                "account",
                "");
        if("".equals(account)==false){
            Intent intent=new Intent(TelLoginPageActivity.this, MainActivity.class);
            intent.putExtra("logintype","auto");
            intent.putExtra("account",account);
            startActivity(intent);
            this.finish();
        }
    }

    private void resetWaitNextSendCheckCodeTimer(){
        tvSendCheckCode.setEnabled(false);
        chrWaitTimer.start();
    }

    public void onBtnLoginCliked(View view) {
        String account=edtAccount.getText().toString().trim();
        if(preCheckCode==null || preCheckCode.equals(edtCheckCode.getText().toString().trim())==false){
            Toast.makeText(this, res.getText(R.string.toast_check_code_not_compared_or_not_input), Toast.LENGTH_SHORT).show();
            return;
        }
        if(ckAutoLogin.isChecked()) {
            SRHelper.putDataToSharedPreference(this,
                    res.getString(R.string.logininfo_sharedprefe_name),
                    "account",
                    account);
        }
        Intent intent=new Intent(TelLoginPageActivity.this,UpdateUserInfoActivity.class);
        intent.putExtra("logintype","tel");
        intent.putExtra("account",account);
        startActivity(intent);
        this.finish();

    }

    public void onTvToRegisterClicked(View view) {
        startActivity(new Intent(this,TelRegisterPageActivity.class));
    }
}
