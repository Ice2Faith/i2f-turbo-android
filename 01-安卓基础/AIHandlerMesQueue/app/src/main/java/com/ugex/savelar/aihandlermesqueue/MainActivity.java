package com.ugex.savelar.aihandlermesqueue;
/**
 * 多线程与消息处理机制：
 * 如果一个操作超过5s（主线程）,则会弹出活动无响应的弹窗,
 * UI界面会没有更新，出现假死现象，因此必须在子线程中执行
 * Handler 进行线程之间的通讯数据传递，消息队列MessageQueue，消息循环Looper
 */
/**
 * Handler:
 * void handleMessage(Message msg) 接受消息
 * boolean sendEmptyMessage(int what) 发送一个只有what的消息
 * boolean sendMessage(Message msg) 发送一个消息给Handler
 * boolean hasMessage(int what) 判断是否有what消息
 * boolean post(Runnable r) 添加进消息对列
 */


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private TextView tvnum;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //处理消息的，运行在主线程中，创建在主线程中
            if(msg.what==1001){
                tvnum.setText("Sec:"+msg.arg1);
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvnum=(TextView)findViewById(R.id.textViewnum);
    }
    public void SleepLater(View view){
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void SyncClick(View v){
        if(v.getId()==R.id.buttonsync)
        {
            new Thread(){
                public void run(){
                    for(int i=0;i<10;i++){
                       // tvnum.setText(i+"");//子线程不能更改父线程内容
                        Message msg=new Message();
                        msg.what=1001;
                        msg.arg1=i;
                        handler.sendMessage(msg);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }
    }
}
