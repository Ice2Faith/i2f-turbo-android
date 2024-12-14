package com.ugex.savelar.awservice.SystemServiceUseCase;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;

import com.ugex.savelar.awservice.MyServiceTest;
import com.ugex.savelar.awservice.R;
/*System Service
* 属于安卓原生代码，可以通过Android代码对服务进行交互
* Android将Binder封装成了普通Manager类，调用代码是察觉不到使用Service的
* 服务通过Context.getSystemService(String serviceName)来获得
* serviceName决定了获取的不同的Manager类，不同的Manager也有不同的方法
* 来获取系统功能或者系统状态
* */
/*
* 常见系统服务
* POWER_SERVICE "power" PowerManager 电源管理
* NOTIFICATION_SERVICE "notification" NotificationManager 通知管理
* LOCATION_SERVICE "location" LocationManager 定位管理
* CONNECTIVITY_SERVICE "connection" ConnectivityManagere 网络管理
* */

public class SystemServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_service);
    }

    //调节音量
    private AudioManager audioManager;
    public void setVolumnTest(View view){
        audioManager=(AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //参数：声音流类型（来电，闹钟，通知，媒体），增加还是降低，显示的提示状态
        //这里设置音乐声音，增大，显示调整界面
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_RAISE,
                AudioManager.FLAG_SHOW_UI);
        /*audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION,
                30,
                AudioManager.FLAG_SHOW_UI);*/
    }

    //添加一个闹钟，定时触发器
    private AlarmManager alarmManager;
    public void setNewAlarm(View view){
        alarmManager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //参数：类型（唤醒系统闹铃，系统不唤醒不闹铃，相对绝对时间），触发时间，触发的意图操作（PendingIntent）
        /*
        * 类型：
        * AlarmManager.ELAPSED_REALTIME 相对，不唤醒
        * AlarmManager.ELAPSED_REALTIME_WAKEUP 相对，唤醒
        * AlarmManager.RTC 绝对，不唤醒
        * AlarmManager.RTC_WAKEUP 绝对，唤醒
        * */
        Intent intent=new Intent(SystemServiceActivity.this, MyServiceTest.class);
        PendingIntent pi=PendingIntent.getService(this,0,intent,0);
        alarmManager.set(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis()+1000,
                pi);
    }
}
