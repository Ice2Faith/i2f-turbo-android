package com.ugex.savelar.awservice.BindServiceUseCase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ugex.savelar.awservice.R;
/*使用bindService来完成*/
/*
* 如果用startService的方式启动，那么参数结果的返回就成了一个问题
* 就成了单边通信，开始点再Activity，结束点再Service
* 毕竟参数可以通过Intent.putExtra()来传递给服务
* 因为它是调用onSystemCommand,返回值是一个状态
* 也不是不可以实现返回，只是需要使用广播的方式实现，这就和Service没什么关系了*/
public class BindServiceUseActivity extends AppCompatActivity {
    private EditText edtCHS;
    private EditText edtMTS;
    private EditText edtEGS;
    private EditText edtAvgS;
    private Button btnCalculAvgS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_service_use);
        InitActiv();
    }
    private void InitActiv(){
        //直接启动服务，先获取到Binder对象
        Intent intent=new Intent(BindServiceUseActivity.this,CalculService.class);;
        bindService(intent,conn, Context.BIND_AUTO_CREATE);

        edtCHS=findViewById(R.id.editTextCHS);
        edtMTS=findViewById(R.id.editTextMTS);
        edtEGS=findViewById(R.id.editTextEGS);
        edtAvgS=findViewById(R.id.editTextAvgS);
        btnCalculAvgS=findViewById(R.id.buttonCalcuAvgS);
        btnCalculAvgS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*其实，启动服务，是没有必要在按钮事件里面的
                *
                * 直接在onCreate的时候就启动就行
                * 而按钮点击的时候就直接就算结果就可以了，也就是onServiceConnected中的计算部分，但是需要binder判空*/
                if(binder==null)
                    return;
                /*计算结果的代码可以直接放在点击按钮里面*/
                double chs=Double.parseDouble(edtCHS.getText().toString());
                double mts=Double.parseDouble(edtMTS.getText().toString());
                double egs=Double.parseDouble(edtEGS.getText().toString());
                double result=binder.calculAvg(chs,mts,egs);
                edtAvgS.setText(""+result);
            }
        });
    }
    private CalculService.CalculBinder binder;
    private ServiceConnection conn=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder=(CalculService.CalculBinder)service;
            //就可以不用在这里进行计算了，而且如果这里进行计算，
            //那么如果不取消绑定服务，只能使用一次
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }
}
