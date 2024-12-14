package com.ugex.savelar.awservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.ugex.savelar.awservice.BindServiceUseCase.BindServiceUseActivity;
import com.ugex.savelar.awservice.SystemServiceUseCase.SystemServiceActivity;
/*
* Service
* 要使用Service，也是需要在AndroidManifest.xml文件中声明的
* Service是安卓四大组件之一，可在后台运行的，不提供界面的
* 即使跳转APP，Service依旧在后台运行，任何组件都可以绑定Service
* 比如，下载，播放，读取文件等都可以使用Service
* */
/*
* Service启动方式
*   start
*       一旦某个组件start一个Service之后，Service开始独立运行，不再与原来组件产生任何关系
*   bind
*       某个组件bind一个Service之后，Service为组件提供一个接口，会进行交互
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    //start 方式启动Service
    public void serviceTestStart(View view) {
        Intent intent=new Intent(MainActivity.this,MyServiceTest.class);
        startService(intent);
    }

    public void serviceTestStop(View view) {
        Intent intent=new Intent(MainActivity.this,MyServiceTest.class);
        stopService(intent);
    }

    //使用bind方式启动Service
    private ServiceConnection conn=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //已经连接成功
            Log.i("Test","onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //连接无效
            Log.i("Test","onServiceDisconnected");
        }
    };
    public void serviceTestBind(View view) {
        Intent intent=new Intent(MainActivity.this,MyServiceTest.class);
        bindService(intent,conn, Service.BIND_AUTO_CREATE);//如果Service不存在则自动创建
        /*使用bindService必须要有连接存在*/
    }

    public void serviceTestUnbind(View view) {
        unbindService(conn);
    }

    //用服务的方式计算平均分
    public void serviceCalculAvg(View view) {
        Intent intent=new Intent(MainActivity.this, BindServiceUseActivity.class);
        startActivity(intent);
    }

    public void serviceSystemActivi(View view) {
        Intent intent=new Intent(MainActivity.this, SystemServiceActivity.class);
        startActivity(intent);
    }
}
