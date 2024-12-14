package com.ugex.savelar.aybroadcast.BroadcastTest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/*注册广播，动态注册
*
* */
public class MyDynamicBroadcastReceiverTest extends BroadcastReceiver {
    /*
    * 当接收到广播时触发
    * */
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Receive a Dynamic Broadccast", Toast.LENGTH_SHORT).show();
    }
}
