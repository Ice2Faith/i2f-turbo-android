package com.ugex.savelar.aybroadcast.SystemBroadcastUseCase;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ugex.savelar.aybroadcast.R;
/*
* 系统广播的使用
* 安卓的大量系统事件都会对外发送系统广播
* 常见广播Action
*   Intent.ACTION_TIME_CHANGED 系统时间改变
*   Intent.ACTION_DATE_CHANGED 系统日期改变
*   Intent.ACTION_TIMEZONE_CHANGED 系统时区改变
*   Intent.ACTION_BOOT_COMPLETED 系统启动完成
*   Intent.ACTION_BATTERY_CHANGED 系统电池电量改变
*   Intent.ACTION_BATTERY_LOW 系统电池电量低
*   Intent.ACTION_POWER_CONNECTED 系统连接电源
*   Intent.ACTION_POWER_DISCONNECTED 系统电源断开
*   Intent.ACTION_SHUTDOWN 系统关机
* */

/*
* 收到短信
* 广播值：android.provider.Telephony.SMS_RECEIVED
* 携带和短信相关的所有数据
* 可以进行监听，在收到短信之后可以进行解析短信数据
* 但是注意添加权限：
*   android.permission.RECEIVE_SMS*/

/*发送短信
* 权限：SEND_SMS
* 获取SmsManager对象
* 使用方法sendTextMessage
* 如果需要得到短信发送结果
*   定义内部类，继承于BroadCastReceiver
*   注册广播
* */
public class SysBroadcastUseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys_broadcast_use);
        InitActiv();
    }

    private EditText edtTarTel;
    private EditText edtSmsContent;
    private SmsManager smsManager;
    private BroadcastReceiver receiver;
    private void InitActiv() {
        edtSmsContent=findViewById(R.id.editTextSmsContent);
        edtTarTel=findViewById(R.id.editTextTelNumber);

        smsManager=SmsManager.getDefault();
        receiver=new SendSmsBdReceiver();
        registerReceiver(receiver,new IntentFilter("smssend"));
    }


    /*
    * 发送消息，注意写上发送短信的权限，并且在手机上已经授权
    * */
    public void sendMySms(View view) {
        //目标手机号
        String targetAddr=edtTarTel.getText().toString().trim();
        //短信内容
        String content=edtSmsContent.getText().toString();
        //使用广播起来返回消息发送成功与否
        Intent intent=new Intent("smssend");
        PendingIntent sendIntent=PendingIntent.getBroadcast(this,0,intent,0);
       //发送短信，参数：目标手机号，源手机号（不设置就使用默认），短信内容，发送成功之后要做的，对方接受之后要做的
        smsManager.sendTextMessage(targetAddr,"",content,sendIntent,null);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    class SendSmsBdReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "sms send success", Toast.LENGTH_SHORT).show();
        }
    }
}
