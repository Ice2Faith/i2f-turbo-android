package com.ugex.savelar.awservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
/*
* start 方式调用
* 启动Service
* onCreate
* onStartCommand
* 停止Service
* onDestroy
* 没有停止再次启动
* onStartCommand
* */
/*
* bind 方式调用
* 绑定Service
* onCreate
* onBind
* （onServiceConnected 需要在onBind返回对象才会调用到）
* 取消绑定Service
* onDestroy
* 注：onBind在声明周期中只存在一次
* */
public class MyServiceTest extends Service {
    //只有在声明周期第一次
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Test","onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Test","onDestroy");
    }
    //每次都会调用
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Test","onStartCommand");
        Toast.makeText(this, "启动了MyServiceTest", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }
    //必须实现的
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("Test","onBind");
        return new MyBinder();
        //这里需要返回一个Binder对象，onServiceConnected才会被调用
    }

    class MyBinder extends Binder{

    }
}
