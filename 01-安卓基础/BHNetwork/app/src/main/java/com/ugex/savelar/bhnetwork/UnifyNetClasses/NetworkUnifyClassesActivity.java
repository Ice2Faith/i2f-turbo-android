package com.ugex.savelar.bhnetwork.UnifyNetClasses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ugex.savelar.bhnetwork.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/*
* 网络编程中的常用类
* */
/*
* HttpUrlConnection使用，注意，是Http,因为还有一个很像的Https开头的
* 使用上的话Http兼容Https,但是如果你使用Https再给Http地址的话，则会报错
*   使用：
* 创建URL
* URL url=new URL(url);
* 创建HttpURLConnection连接
* connection=(HttpURLConnection)url.openConnection();
* 读取数据
* 访问网络权限：
*     <uses-permission android:name="android.permission.INTERNET"/>
* 特别注意，谷歌在安卓9之后默认使用了加密连接（HTTPS），对于一些数据的明文传输是不允许的
* 也就是说，可能一些HttpUrlConnection操作会失效，错误Logcat:Cleartext HTTP traffic to xxx not permitted
* 解决方法有三种：
*   原文章：https://blog.csdn.net/qq_39959838/article/details/82918676
*   1.Manifest中在application节点属性说明：android:usesCleartextTraffic="true"
*   2.改为使用HttpsUrlConnection，但是某些域名地址会成为不可信地址，需要解决,错误：javax.net.ssl.SSLPeerUnverifiedException: Hostname ip not verified
*       原文章：https://blog.csdn.net/u011068702/article/details/80628529?utm_source=blogxgwz8
*   3.targetApi改到24以下
 *网络连接：3/4G,Wifi等
* 查看网络状态：
* ConnectivityManager by getSystemService()
* NetworkInfo
* */
public class NetworkUnifyClassesActivity extends AppCompatActivity {

    private EditText edtNetInfo;
    private EditText edtDownUrl;
    private EditText edtDownSaveAs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_unify_classes);

        InitActivity();
    }

    private MyNetStateChangeBDReceiver receiver;
    private void InitActivity() {
        edtNetInfo=findViewById(R.id.editTextNewInfo);
        edtDownUrl=findViewById(R.id.editTextNetworkUrl);
        edtDownSaveAs=findViewById(R.id.editTextDownSaveAsFileName);
        edtDownUrl.setText("http://www.");
        edtDownSaveAs.setText(".html");

        connManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        receiver=new MyNetStateChangeBDReceiver();
        registerReceiver(receiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    //查看网络状态权限：
    //    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
    private ConnectivityManager connManager;
    public void onBtnViewNetInfoClicked(View view) {

        NetworkInfo[] netInfos=connManager.getAllNetworkInfo();
        if(netInfos==null){
            return;
        }

        StringBuffer sb=new StringBuffer();

        sb.append("活动网络：\n");
        NetworkInfo activeInfo=connManager.getActiveNetworkInfo();
        if(activeInfo!=null) {
            sb.append("类型："+activeInfo.getTypeName()
                    + ",已连接：" + activeInfo.isConnected()
                    +"，可用："+activeInfo.isAvailable()
                    +"，完整信息："+activeInfo.toString()
                    + "\n");
        }
        sb.append("所有网络：\n");
        for(NetworkInfo info : netInfos){
            sb.append(info.toString()+"\n");
        }
        edtNetInfo.setText(sb.toString());

    }

    private int netStateNotifyId=0x101;
    public void onBtnCheckNetworkClicked(View view) {
        if(!NetHelper.checkNetworkStateAvaliable(this)){
            //发出网络不可用通知
            NotificationManager notifyManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification=new Notification();
            //跳转到设置网络连接的设置页面
            //更多网络连接设置页：Settings.ACTION_WIRELESS_SETTINGS
            //网络设置页
            Intent intent=new Intent( Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
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
                        .setContentTitle("网络连接")
                        .setContentText("网络不可用")
                        .setTicker("无可用网络")
                        .setContentIntent(pi)
                        .setVibrate(new long[]{200,200,300,50,50})
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .build();
            } else {
                notification = new NotificationCompat.Builder(this)
                        .setContentTitle("网络连接")
                        .setContentText("网络不可用")
                        .setTicker("无可用网络")
                        .setContentIntent(pi)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .build();
                //版本11-16使用getNotification()替换build()
            }
            notification.flags|=Notification.FLAG_AUTO_CANCEL;
            notifyManager.notify(netStateNotifyId, notification);
        }else{
            Toast.makeText(this, "网络可用，可以开始下载等操作", Toast.LENGTH_LONG).show();
        }

    }

    public void onBtnDownloadUrlDataClicked(View view) {
        //开始保存，必须是http://协议的URL才行，否则报错
        String urlStr=edtDownUrl.getText().toString().trim();
        edtDownUrl.setText(urlStr);
        new MyDownloadUrlDataTask().execute(urlStr);
    }
    class MyDownloadUrlDataTask extends AsyncTask<String,Void, File>{

        @Override
        protected File doInBackground(String... strings) {
            String urlStr=strings[0];
            try {
                //创建URL
                URL url=new URL(urlStr);
                //得到HttpURLConnection，注意，这里比较坑，如果你使用的是HttpsURLConnection再给定Http地址则会出错
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                //设置连接属性
                conn.setRequestMethod("GET");
                conn.setReadTimeout(5000);
                conn.setDoInput(true);
                conn.connect();
                //得到返回码，200即成功
                int responceCode=conn.getResponseCode();
                if(responceCode==200){
                    //保存文件
                    InputStream is=conn.getInputStream();
                    BufferedInputStream bis=new BufferedInputStream(is);
                    File file=new File(Environment.getExternalStorageDirectory(),edtDownSaveAs.getText().toString().trim());
                    BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(file));
                    int temp;
                    while((temp=bis.read())!=-1){
                        bos.write(temp);
                    }
                    bos.close();
                    bis.close();
                    //注意，这个处于子线程中的任务，是不能操作UI线程的，包括发送一个Toast也不行
                    return file;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            edtDownSaveAs.setText(file.getAbsolutePath());
            Toast.makeText(NetworkUnifyClassesActivity.this, "资源下载完毕", Toast.LENGTH_SHORT).show();
        }
    }
}
