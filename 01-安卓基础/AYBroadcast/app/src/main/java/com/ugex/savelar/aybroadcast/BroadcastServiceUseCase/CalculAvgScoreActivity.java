package com.ugex.savelar.aybroadcast.BroadcastServiceUseCase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ugex.savelar.aybroadcast.R;
/*
* 使用startService计算运算结果
* 由于startService启动的service需要调用Service子类的onCommandStart方法
* 因此在此方法中进行云端
* 运算之后通过sendBroadcast发送广播，返回结果
* 因此广播作为activity的内部类
* */
public class CalculAvgScoreActivity extends AppCompatActivity {
    //广播监听器内部类
    class MyCalulServiceReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            //获取计算结果显示到界面上
            double res=intent.getDoubleExtra("avgres",0);
            edtAVGS.setText(""+res);
            //Toast.makeText(context, ""+res, Toast.LENGTH_SHORT).show();
        }
    }
    MyCalulServiceReceiver receiver;
    private EditText edtCHS;
    private EditText edtMTS;
    private EditText edtEGS;
    private EditText edtAVGS;
    private Button btnCalcul;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calcul_avg_score);
        //广播接受器动态注册
        receiver=new MyCalulServiceReceiver();
        registerReceiver(receiver,new IntentFilter("aaacalsvc"));
        InitActive();
    }

    private void InitActive() {
        edtCHS=findViewById(R.id.editTextCHS);
        edtMTS=findViewById(R.id.editTextMTS);
        edtEGS=findViewById(R.id.editTextEGS);
        edtAVGS=findViewById(R.id.editTextAVGS);

    }

    public void CalculAvgScoreProc(View view) {
        //按钮点击时获取输入值并启动Service
        double dchs=Double.parseDouble(edtCHS.getText().toString());
        double dmts=Double.parseDouble(edtMTS.getText().toString());
        double degs=Double.parseDouble(edtEGS.getText().toString());
        Intent intent=new Intent(CalculAvgScoreActivity.this,
                CalculAvgScoreService.class);
        intent.putExtra("chs",dchs);
        intent.putExtra("mts",dmts);
        intent.putExtra("egs",degs);

        startService(intent);
    }

    //Activity生命周期结束时结束广播监听
    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
