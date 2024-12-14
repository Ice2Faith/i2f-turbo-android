package com.ugex.savelar.aybroadcast.SystemBroadcastUseCase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ReceiveSMSBDReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "new msm", Toast.LENGTH_SHORT).show();
        String con="";
        Toast.makeText(context, con, Toast.LENGTH_SHORT).show();
    }
}
