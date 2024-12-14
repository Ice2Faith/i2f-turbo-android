package com.ugex.savelar.atactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MyActivity extends AppCompatActivity {

    /*创建一个Activity只需要继承Activity或者其子类，重写onCreate方法*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //接收传递过来的值
        Intent intent=getIntent();
        String hello=intent.getStringExtra("keyName:hello");
        Toast.makeText(this, "Accept Value:"+hello, Toast.LENGTH_SHORT).show();
        //接收这个键的值，如果没有那就复制-1
        int key2=intent.getIntExtra("key2",-1);
        //获取一个绑定的数据包，再拆包拿去数据
        Bundle bundle=intent.getExtras();
        String key3=bundle.getString("key3");
        /*并在onCreate中绑定布局*/
        setContentView(R.layout.activity_my);
        BackDataTest();
        BtnThreadStartPause();
    }

    //返回数据回去
    private Button btnBackData;
    private void BackDataTest()
    {
        btnBackData=findViewById(R.id.buttonBackData);
        btnBackData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=MyActivity.this.getIntent();
                String str=intent.getStringExtra("keyback");
                intent.putExtra("keyresult",str);
                //获取发送过来的值，并处理之后进行设置返回结果，这样就完成了数据的返回
                //参数，响应码，传递值的intent,可以是新的，也可以用原来的
                setResult(0x0002,intent);
                //这里返回结果之后，就结束本Activity了
                finish();
            }
        });
    }
////////////////////////////////////////////////////////////////////////////
    //线程的启动与暂停
    private Button btnStart;
    private boolean runFlag=true;
    private int count=0;
    private Handler handler=new Handler();
    private TextView tvdisplay;
    private boolean pauseFlag=true;
    private void BtnThreadStartPause()
    {
        tvdisplay=findViewById(R.id.textViewDisplay);
        new Thread(){
            @Override
            public void run() {

                runFlag=true;
                while(runFlag)
                {
                    //判断是否暂停
                    if(!pauseFlag)
                    {
                        count++;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                tvdisplay.setText(""+count);
                            }
                        });
                    }
                }
            }
        }.start();
        btnStart=findViewById(R.id.buttonStartThread);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseFlag=!pauseFlag;
                if(pauseFlag)
                    btnStart.setText("启动");
                else
                    btnStart.setText("暂停");
            }
        });
    }
    //在Activity销毁时，结束线程
    @Override
    protected void onDestroy() {
        super.onDestroy();
        runFlag=false;
    }
    //如果切换到后台，就暂停运行，重新返回前台自动运行
    @Override
    protected void onPause() {
        super.onPause();
        pauseFlag=true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        pauseFlag=false;
    }
}
