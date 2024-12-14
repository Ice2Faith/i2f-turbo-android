package com.ugex.savelar.bdactivityunifydesign.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ugex.savelar.bdactivityunifydesign.MainActivity;
import com.ugex.savelar.bdactivityunifydesign.R;
import com.ugex.savelar.bdactivityunifydesign.Util.CheckCode;
import com.ugex.savelar.bdactivityunifydesign.Util.SendMessageUtil;

public class LoginPageActivity extends AppCompatActivity {
    private Resources res;
    private EditText edtAccount;
    private TextView tvSendCheckCode;
    private EditText edtCheckCode;
    private Chronometer chrWaitTimer;
    private String preCheckCode;
    public static final int NEXT_SEND_CHECK_CODE_WAIT_SECOND=60;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        InitActivity();
    }

    private void InitActivity() {
        res=getResources();
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

        tvSendCheckCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tel=edtAccount.getText().toString().trim();
                if(tel.equals("")){
                    Toast.makeText(LoginPageActivity.this, res.getText(R.string.toast_lease_input_tel_number), Toast.LENGTH_SHORT).show();
                    return;
                }
                preCheckCode= CheckCode.generateCheckCode();
                SendMessageUtil.sendMessage(LoginPageActivity.this,
                        preCheckCode,
                        tel);
                resetWaitNextSendCheckCodeTimer();
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
    private void resetWaitNextSendCheckCodeTimer(){
        tvSendCheckCode.setEnabled(false);
        chrWaitTimer.start();
    }

    public void onBtnLoginCliked(View view) {
        if(preCheckCode==null || preCheckCode.equals(edtCheckCode.getText().toString().trim())==false){
            Toast.makeText(this, res.getText(R.string.toast_check_code_not_compared_or_not_input), Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent=new Intent(LoginPageActivity.this,UpdateUserInfoActivity.class);
        intent.putExtra("account",edtAccount.getText().toString().trim());
        startActivity(intent);
        this.finish();

    }

    public void onBtnGotoMainCliked(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
