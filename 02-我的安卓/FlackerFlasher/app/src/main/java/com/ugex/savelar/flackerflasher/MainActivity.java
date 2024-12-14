package com.ugex.savelar.flackerflasher;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView flackerRateTextView;
    private TextView flasherRateTextView;
    private int flackerRateVal=90;
    private int flasherRateVal=90;
    private boolean beClosed=false;
    private CheckBox cbFlash;
    private CheckBox cbFlack;
    private boolean openFlash=false;
    private boolean openFlack=false;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=getApplicationContext();
        flackerRateTextView=findViewById(R.id.textView_flackerVal);
        flasherRateTextView=findViewById(R.id.textView_flasherVal);
        findViewById(R.id.button_run).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beClosed=false;
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        if(!openFlack)
                            return;
                        FlackerManager fm=new FlackerManager(context);
                        while(!beClosed)
                        {
                            fm.Flacker(flackerRateVal>100?flackerRateVal/3:30);
                            try {
                                Thread.sleep(flackerRateVal>100?flackerRateVal*2/3:flackerRateVal);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if(!openFlack)
                                break;
                        }
                    }
                }).start();
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        if(!openFlash)
                            return;
                        FlasherManager sm=new FlasherManager(context);
                        while(!beClosed)
                        {
                            sm.FlashOn();
                            try {
                                Thread.sleep(flasherRateVal/2);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            sm.FlashOff();
                            try {
                                Thread.sleep(flasherRateVal/2);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if(!openFlash)
                                break;
                        }
                    }
                }).start();

            }
        });
        findViewById(R.id.button_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beClosed=true;
            }
        });
        findViewById(R.id.button_falshUp).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int befnum=flasherRateVal;
                if(flasherRateVal<10000)
                    flasherRateVal*=1.07;
                if(flasherRateVal==befnum)
                    flasherRateVal++;
                flasherRateTextView.setText(flasherRateVal+"");
                return false;
            }
        });
        findViewById(R.id.button_falshDown).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(flasherRateVal>12)
                    flasherRateVal*=0.93;
                flasherRateTextView.setText(flasherRateVal+"");
                return false;
            }
        });
        findViewById(R.id.button_flackDown).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(flackerRateVal>12)
                    flackerRateVal*=0.93;
                flackerRateTextView.setText(flackerRateVal+"");
                return false;
            }
        });
        findViewById(R.id.button_flackUp).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int befnum=flackerRateVal;
                if(flackerRateVal<10000)
                    flackerRateVal*=1.07;
                if(flackerRateVal==befnum)
                    flackerRateVal++;
                flackerRateTextView.setText(flackerRateVal+"");
                return false;
            }
        });
        cbFlack=findViewById(R.id.checkBox_openFlack);
        cbFlash=findViewById(R.id.checkBox_openFlash);
        cbFlash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                openFlash=isChecked;
            }
        });
        cbFlack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                openFlack=isChecked;
            }
        });
    }
    @TargetApi(26)
    private void InitControlView() {

    }
}
class FlackerManager
{
    private Context context;
    private Vibrator vibrator;
    public FlackerManager(Context context){
        this.context=context;
        vibrator=(Vibrator)context.getSystemService(context.VIBRATOR_SERVICE);
    }
    public void Flacker(long milliSecound){
        vibrator.vibrate(milliSecound);
    }
    public void Flacker(long[] milliSeconds){
        vibrator.vibrate(milliSeconds,-1);
    }
    public void LoopFlacker(long[] milliSeconds) {
        vibrator.vibrate(milliSeconds,0);
    }
    public void StopLoopFlacker(){
        vibrator.cancel();
    }
}
class FlasherManager
{
    private Context context;
    private Camera camera;
    private CameraManager cameramanager;

    public FlasherManager(Context context)
    {
        this.context=context;
    }
    @TargetApi(Build.VERSION_CODES.M)
    public void Flasher(final long[] milliSeconds) {
        if(!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            Toast.makeText(context,"没有检测到闪光灯呢！！",Toast.LENGTH_LONG);
            return;
        }
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Toast.makeText(context,"不支持安卓6.0以下使用哦！！",Toast.LENGTH_LONG);
            return;
        }
        try {
            cameramanager=(CameraManager)context.getSystemService(context.CAMERA_SERVICE);
            cameramanager.setTorchMode("0",false);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        Thread td=new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<milliSeconds.length;i++) {
                    try {
                        if(i%2==0) {
                            cameramanager.setTorchMode("0",true);
                        }else{
                            cameramanager.setTorchMode("0",false);
                        }
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(milliSeconds[i]);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        td.start();
        try {
            cameramanager.setTorchMode("0",false);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    public void FlashOn(){
        if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                FlashOnApiGatherThan22();
            }
            else{
                Toast.makeText(context,"不支持安卓6.0以下使用哦！！",Toast.LENGTH_LONG);
            }
        }else{
            Toast.makeText(context,"没有检测到闪光灯呢！！",Toast.LENGTH_LONG);
        }
    }
    public void FlashOff(){
        if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                FlashOffApiGatherThan22();
            }
            else{
                Toast.makeText(context,"不支持安卓6.0以下使用哦！！",Toast.LENGTH_LONG);
            }
        }
        else{
            Toast.makeText(context,"没有检测到闪光灯呢！！",Toast.LENGTH_LONG);
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    private void FlashOnApiGatherThan22(){
        try {
            cameramanager=(CameraManager)context.getSystemService(context.CAMERA_SERVICE);
            cameramanager.setTorchMode("0",true);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    private void FlashOffApiGatherThan22(){
        if(cameramanager==null)
            return;
        try {
            cameramanager.setTorchMode("0",false);
            cameramanager=null;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}