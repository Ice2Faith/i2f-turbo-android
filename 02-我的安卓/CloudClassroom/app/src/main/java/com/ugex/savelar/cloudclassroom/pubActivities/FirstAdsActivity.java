package com.ugex.savelar.cloudclassroom.pubActivities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.ugex.savelar.cloudclassroom.R;

public class FirstAdsActivity extends Activity {
    private Resources res;
    public static final String EXTRA_KEY_ADS_BITMAP_PATH="ads_bmp_path";
    public static final String EXTRA_KEY_ADS_LINK="ads_link";
    public static  final int MAX_WAIT_MILLSECOND=5*1000;
    private ImageView ivAds;
    private TextView tvJumpAds;
    private Chronometer chronometer;
    private String adLink;
    private Bitmap adBmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frist_ads);

        InitActivity();
    }

    private void InitActivity() {
        res=getResources();
        ivAds=(ImageView)findViewById(R.id.imageViewAds);
        tvJumpAds=(TextView)findViewById(R.id.textViewJumpAds);
        chronometer=(Chronometer)findViewById(R.id.chronometerTimer);

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long preWaitTime=(MAX_WAIT_MILLSECOND-(SystemClock.elapsedRealtime()-chronometer.getBase()))/1000;
                if(preWaitTime<=0){
                   gotoNextPage();
                }else {
                    tvJumpAds.setText(preWaitTime+res.getString(R.string.jump_after_this_second));
                }
            }
        });
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        String adsPath=getIntent().getStringExtra(EXTRA_KEY_ADS_BITMAP_PATH);
        adLink=getIntent().getStringExtra(EXTRA_KEY_ADS_LINK);
        adBmp= BitmapFactory.decodeFile(adsPath);

        if(adBmp!=null)
            ivAds.setImageBitmap(adBmp);

    }
    private void gotoNextPage(){
        startActivity(new Intent(this, LoginActivity.class));
        chronometer.stop();
        this.finish();
    }

    public void OnClickedAdsImageView(View view) {
        if(adLink==null || adLink.equals(""))
            return;
        Intent intent = new Intent("android.intent.action.VIEW");
        Uri content_url = Uri.parse(adLink);
        intent.setData(content_url);
        startActivity(intent);
    }

    public void OnClickedJumpAdsTextView(View view) {
        gotoNextPage();
    }
}
