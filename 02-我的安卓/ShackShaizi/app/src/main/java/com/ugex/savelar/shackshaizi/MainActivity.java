package com.ugex.savelar.shackshaizi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final int[] shaiziImgSrcId = {R.drawable.s1, R.drawable.s2, R.drawable.s3, R.drawable.s4, R.drawable.s5, R.drawable.s6};
    private static final int[] shaiziImgViewId = {R.id.imageViewShaizi1, R.id.imageViewShaizi2, R.id.imageViewShaizi3, R.id.imageViewShaizi4, R.id.imageViewShaizi5};
    private ImageView[] shaiziImgView = new ImageView[5];
    private Button btnShack;
    private Button btnOpen;
    private Button btnClose;
    private TextView maskTV;
    private LinearLayout bodyLL;
    private Random rand = new Random();
    private Vibrator vibrator;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitApp();
    }

    private void DisplayMask() {
        maskTV.setVisibility(View.VISIBLE);
        bodyLL.setVisibility(View.INVISIBLE);
    }

    private void DisplayBody()
    {
        maskTV.setVisibility(View.INVISIBLE);
        bodyLL.setVisibility(View.VISIBLE);
    }
    private void AsynPlaySound(final Context playContext, final int rawPlayId)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MediaPlayer mpr=MediaPlayer.create(playContext,rawPlayId);
                mpr.start();
                try {
                    Thread.sleep(mpr.getDuration());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    mpr.reset();
                    mpr.release();
                }
            }
        }).start();
    }
    private void ShackOnce()
    {
        DisplayMask();
        vibrator.vibrate(500);
        AsynPlaySound(context,R.raw.shackbg);
        for(int i=0;i<5;i++) {
            shaiziImgView[i].setImageResource(shaiziImgSrcId[rand.nextInt(shaiziImgSrcId.length)]);
        }
    }
    private void InitApp()
    {
        for(int i=0;i<5;i++){
            shaiziImgView[i]=findViewById(shaiziImgViewId[i]);
        }
        context=this.getApplicationContext();
        vibrator=(Vibrator)context.getSystemService(context.VIBRATOR_SERVICE);

        btnShack=findViewById(R.id.buttonShack);
        btnShack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShackOnce();
            }
        });
        maskTV=findViewById(R.id.textViewMask);
        bodyLL=findViewById(R.id.LinearLayoutBody);
        DisplayMask();
        for(int i=0;i<5;i++) {
            shaiziImgView[i].setImageResource(shaiziImgSrcId[rand.nextInt(shaiziImgSrcId.length)]);
        }
        btnOpen=findViewById(R.id.buttonOpen);
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayBody();
            }
        });
        btnClose=findViewById(R.id.buttonClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayMask();
            }
        });
    }
}
