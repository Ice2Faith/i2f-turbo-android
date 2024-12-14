package com.ugex.savelar.cloudclassroom.pubActivities;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.ugex.savelar.cloudclassroom.R;


public class FirstBrandActivity extends Activity {
    public static String ADS_DOWNLAOD_RECEIVER_ACTION="com.ugex.savelar.receiver.adsdownload";
    public static final int LOGO_SHOW_MIN_TIME=500;
    private DownloadAdsBroadcastReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_brand);

        InitActivity();

    }

    private void InitActivity() {
        receiver=new DownloadAdsBroadcastReceiver();
        registerReceiver(receiver,new IntentFilter(ADS_DOWNLAOD_RECEIVER_ACTION));
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(LOGO_SHOW_MIN_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                requestAdInfo();
            }
        }).start();
    }

    private void requestAdInfo(){
        Intent intent=new Intent(this,DownloadAdsService.class);
        startService(intent);
    }

    private void gotoNextPage(String picPath,String link){
        Intent intent=new Intent(this, FirstAdsActivity.class);
        intent.putExtra(FirstAdsActivity.EXTRA_KEY_ADS_BITMAP_PATH,picPath);
        intent.putExtra(FirstAdsActivity.EXTRA_KEY_ADS_LINK,link);
        startActivity(intent);
        finish();
    }

    public class DownloadAdsBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String picPath=intent.getStringExtra(DownloadAdsService.EXTRA_RETURN_AD_PICTURE_PATH);
            String link=intent.getStringExtra(DownloadAdsService.EXTRA_RETURN_AD_CLICK_LINK);
            gotoNextPage(picPath,link);
        }
    }
}
