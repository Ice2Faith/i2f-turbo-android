package com.ugex.savelar.axnotification;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

/*
* Notifacation
* 这其实也是一个服务getSystemService(NOTIFICATION_SERVICE)
* 包含：
*   大图标，标题，内容，小图标，时间
* */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //发送一条通知
    private NotificationManager notifyManager;
    private  int id=0x101;
    public void sendNewNotifacation(View view) {
        notifyManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification;
       /* //早期版本,2.4
        notification=new Notification();
        //设置图标，提示文字，自动关闭，点击事件响应
        notification.icon=R.mipmap.ic_launcher;//
        notification.tickerText="有新消息";
        notification.flags|=Notification.FLAG_AUTO_CANCEL;
        Intent intent=new Intent(this,MainActivity.class);
        PendingIntent pi=PendingIntent.getActivity(this,0,intent,0);
        //特别这个方法已经过时了，VS中已经删除
       // notification.setLatestEventInfo(this,"title","content",pi);
        */

       //API 16及以上
        Intent intent=new Intent(this,MainActivity.class);
        PendingIntent pi=PendingIntent.getActivity(this,0,intent,0);
        String idchannel = "channel_id";
        String name="渠道名字";
        //由于安卓8引入了通知归类，因此需要加入才能显示出来
        //也就是需要消息管道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(idchannel, name, NotificationManager.IMPORTANCE_HIGH);
            notifyManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(this)
                    .setChannelId(idchannel)
                    .setContentTitle("标题")
                    .setContentText("内容")
                    .setTicker("一条新消息")
                    .setContentIntent(pi)
                    .setVibrate(new long[]{200,200,300,50,50})
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();
        } else {
            notification = new NotificationCompat.Builder(this)
                    .setContentTitle("标题")
                    .setContentText("内容")
                    .setTicker("一条新消息")
                    .setContentIntent(pi)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();
                    //版本11-16使用getNotification()替换build()
        }
        notification.flags|=Notification.FLAG_AUTO_CANCEL;
        notifyManager.notify(id, notification);

    }
}
