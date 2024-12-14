package com.ugex.savelar.aybroadcast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import com.ugex.savelar.aybroadcast.BroadcastServiceUseCase.CalculAvgScoreActivity;
import com.ugex.savelar.aybroadcast.BroadcastTest.MyBroadcastReceiverTest;
import com.ugex.savelar.aybroadcast.BroadcastTest.MyDynamicBroadcastReceiverTest;
import com.ugex.savelar.aybroadcast.SystemBroadcastUseCase.SysBroadcastUseActivity;
/*
* Broadcast广播
* 当系统发生事件时，就会发送广播，广播本身并不知道有多少人关注自己
* 一个应用也可以发送出广播
* 通过广播中的关键字段，系统将寻找所有关注这个广播的应用，并触发他们注册的Receiver
*
* BroadcastReceiver广播监听器
* 用来监听广播的，
* 向系统注册自己关心的广播，当广播发出之后，系统会带起进程去调用指定的方法执行代码
* 是Android四大组件之一
* 本质是一个系统的全局监听者
* 用于监听系统全局的广播消息
* 方便的实现系统中不同组件之间的通信
* */

/*
*
* */
public class MainActivity extends AppCompatActivity {
    private BroadcastReceiver receriver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //注册监听器
        receriver=new MyDynamicBroadcastReceiverTest();
        IntentFilter filter=new IntentFilter("aaabd");
        registerReceiver(receriver,filter);
    }

    //发送一条广播
    public void SendBroadcastTest(View view) {

        //注意，安卓8以后发生了一些改变，我不知道是啥，但是如果使用一般的Action方式发送广播，
        //那将会无效，广播根本发送不了，还有其他一些也一样，比如Service貌似也是如此
        //这和设置的action有关，比如上面使用的aaabd就可以作为正常的action使用
        //但是这里的mybroadtest就不行
        Intent intent=new Intent(MainActivity.this, MyBroadcastReceiverTest.class);
        sendBroadcast(intent);
    }

    //发送给动态注册的广播监听器
    public void SendDynamicBroadcastTest(View view) {
        /*
        * 动态注册广播使用action去发送广播，使用
        * sendBroadcast发送的广播
        * */
        Intent intent=new Intent("aaabd");
        sendBroadcast(intent);
    }

    @Override
    protected void onDestroy() {
        //反注册广播，反注册之后，将不会在收到这个广播，
        //因此，如果这里的MainActivity如果调用了finish()销毁了Activity
        //那么，再其他的Activity中将不会再接受到广播
        unregisterReceiver(receriver);
        super.onDestroy();
    }

    //使用广播和服务计算平均值
    public void GotoBdSvcCalculActivity(View view) {
        startActivity(new Intent(MainActivity.this,
                CalculAvgScoreActivity.class));
    }

    //发送与接收短信广播
    public void GotoBdSendRcvActivity(View view) {
        startActivity(new Intent(MainActivity.this,
                SysBroadcastUseActivity.class));
    }
}
