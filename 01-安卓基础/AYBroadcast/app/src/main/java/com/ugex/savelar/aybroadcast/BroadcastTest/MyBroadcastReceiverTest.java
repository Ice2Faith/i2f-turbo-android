package com.ugex.savelar.aybroadcast.BroadcastTest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/*注册广播，需要在AndroidManifest.xml
*
* 一般来说就是以上方式进行静态注册，应用程序打开时就注册了广播，贯穿整个软件周期
*
* 不过也可以进行动态注册,具体查看MyDynamicBroadcastReceiverTest
* */
public class MyBroadcastReceiverTest extends BroadcastReceiver {
    /*
    * 当接收到广播时触发
    * 方法是运行在主线程中的，处理不要超过10s
    * 否则会应用程序无响应
    * 如果有耗时的操作，应该使用Intent来启动一个Service
    * */
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Receive a Broadccast", Toast.LENGTH_SHORT).show();
    }
}
