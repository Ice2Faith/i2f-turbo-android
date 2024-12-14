package com.ugex.savelar.bhnetwork.UnifyNetClasses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/*监听系统的网络状态更改广播
* 注册广播：
* action:
*   <action android:name="android.net.conn.CONNECTIVITY_CHANGE"/>
* 并且安卓7之后，需要动态注册广播：
* receiver=new MyNetStateChangeBDReceiver();
* registerReceiver(receiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
*
* */
public class MyNetStateChangeBDReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NetHelper.checkNetworkStateAvaliable(context);
    }
}
