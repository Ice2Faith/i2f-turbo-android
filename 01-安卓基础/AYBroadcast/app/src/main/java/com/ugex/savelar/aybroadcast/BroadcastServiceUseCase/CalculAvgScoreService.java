package com.ugex.savelar.aybroadcast.BroadcastServiceUseCase;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class CalculAvgScoreService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //获取服务所需要的的值
        double chs=intent.getDoubleExtra("chs",0);
        double mts=intent.getDoubleExtra("mts",0);
        double egs=intent.getDoubleExtra("egs",0);
        //Toast.makeText(this, "Service calcul avg start", Toast.LENGTH_SHORT).show();
        //计算并通过广播返回计算结果
        Intent rintent=new Intent("aaacalsvc");
        rintent.putExtra("avgres",(chs+mts+egs)/3);
        sendBroadcast(rintent);
        return super.onStartCommand(intent, flags, startId);
    }

    //必须实现的方法，这里不用
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
