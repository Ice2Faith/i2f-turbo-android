package com.ugex.savelar.excompositedesign.Activitys;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Chronometer;
import android.widget.TextView;

import com.ugex.savelar.excompositedesign.R;

import androidx.appcompat.app.AppCompatActivity;

public class HelloPageActivity extends AppCompatActivity {
    private Resources res;
    private Chronometer choTimer;
    private TextView tvTimerTips;
    public static final int MAX_WAIT_MILLSECOND=5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_page);

        InitActivity();
    }

    private void InitActivity() {
        res=getResources();
        choTimer=findViewById(R.id.chronometerTimer);
        tvTimerTips=findViewById(R.id.textViewHelloTimer);

        choTimer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long preWaitTime=(MAX_WAIT_MILLSECOND-(SystemClock.elapsedRealtime()-choTimer.getBase()))/1000;
                if(preWaitTime<=0){
                    startActivity(new Intent(HelloPageActivity.this, TelLoginPageActivity.class));
                    choTimer.stop();
                    HelloPageActivity.this.finish();
                }else {
                    tvTimerTips.setText(preWaitTime + ""+res.getText(R.string.hello_page_tv_wait_enter_time_start));
                }
            }
        });

        choTimer.setBase(SystemClock.elapsedRealtime());
        choTimer.start();
    }
}
