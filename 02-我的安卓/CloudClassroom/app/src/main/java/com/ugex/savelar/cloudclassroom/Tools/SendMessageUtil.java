package com.ugex.savelar.cloudclassroom.Tools;

import java.util.ArrayList;

import android.R.bool;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.widget.Toast;

public class SendMessageUtil {
    /** 发送与接收广播 **/
    private static String SENT_SMS_ACTION = "SENT_SMS_ACTION";
    private static String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
    public static boolean sendSuccess=false;
    /**
     * 实现发送短信
     * @param context 
     * @param text 短信内容
     * @param phoneNumber 手机号码
     */
    public static void sendMessage(Context context, String text,
            String phoneNumber) {
    	boolean sendsuccess=false;
        context.registerReceiver(sendMessage, new IntentFilter(SENT_SMS_ACTION));
        context.registerReceiver(receiver, new IntentFilter(
                DELIVERED_SMS_ACTION));
        
        // create the sentIntent parameter  
        Intent sentIntent = new Intent(SENT_SMS_ACTION);  
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, sentIntent,0);
        // create the deilverIntent parameter  
        Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);  
        PendingIntent deliverPI = PendingIntent.getBroadcast(context, 0,deliverIntent, 0); 
        
        SmsManager smsManager = SmsManager.getDefault();
        //如果单条短信字数超过70，需要分多条发送
        if (text.length() > 70 ) {
            ArrayList<String> msgs = smsManager.divideMessage(text);
            for (String msg : msgs) {
                smsManager.sendTextMessage(phoneNumber, null, msg, sentPI, deliverPI);                        
            }
        } else {
            smsManager.sendTextMessage(phoneNumber, null, text, sentPI, deliverPI);
        }
    }

    
    public static BroadcastReceiver sendMessage = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 判断短信是否发送成功
        	switch (getResultCode()) {
            case Activity.RESULT_OK:
                Toast.makeText(context, "验证码发送成功", Toast.LENGTH_SHORT).show();
            	
            	break;
            default:
                Toast.makeText(context, "验证码发送失败", Toast.LENGTH_LONG).show();
                break;
            }
        }
    };
    private static BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 对方收到短信
            //Toast.makeText(context, "对方已接收短信", Toast.LENGTH_LONG).show();
        }
    };
    
    
}
