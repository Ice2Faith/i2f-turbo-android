package com.ugex.savelar.bgmediaaudiovideopicture.Video;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.SeekBar;

import com.ugex.savelar.bgmediaaudiovideopicture.R;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MediaPlayerPlayVideoActivity extends AppCompatActivity {
   // private MySurfaceView svMyVideo;
    private SurfaceView svVideo;
    private MediaPlayer mediaPlayer;
    private String playFileName;

    private SeekBar seekBarPlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player_play_video);

        InitActivity();
    }

    private void InitActivity() {
        playFileName=getIntent().getStringExtra("videoFileName");

        //使用自定义的Surface
//        svMyVideo=new MySurfaceView(this);
//        setContentView(svMyVideo);

        //实现播放进度控制
        seekBarPlay=findViewById(R.id.seekBarPlayVideo);
        seekBarPlay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser && mediaPlayer!=null){//注意，这里需要判定是否来自用户操作，如果不判定将会出现卡碟情况，也就是进度条本身是随着播放进度变化而变化的，会造成连环效应
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        svVideo=findViewById(R.id.surfaceViewVideo);
        mediaPlayer=new MediaPlayer();

        //设置播放结束接听，实现循环播放
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(isStoped)//手动停止的情况不管
                    return;
               mediaPlayer.reset();
               readyPlay();
                StartPlay();
            }
        });


    }
    private Timer timer=new Timer();
    private boolean isFristPlay=true;
    private void StartPlay(){
        mediaPlayer.start();

        //在第一次播放的时候设置定时器，这样随时去记录播放进度
        if(isFristPlay){
            //由于此Activity只是负责播放一个视频，因此只需要设置一次区间即可
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                seekBarPlay.setMin(0);
            }
            seekBarPlay.setMax(mediaPlayer.getDuration());
            //开启定时器，延时200毫秒之后，开始每500毫秒更新一次播放进度
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //这里需要判定是否处于播放状态，因为可能用户暂停或者停止了播放，或者播放结束了
                    if(mediaPlayer.isPlaying())
                        seekBarPlay.setProgress(mediaPlayer.getCurrentPosition());
                }
            },200,500);
            isFristPlay=false;
        }


    }

    //启动Activity就开始播放
    @Override
    protected void onResume() {
        super.onResume();
        //但是虽然界面显示出来了，有些东西还是没有准备好，不能够立即播放
        //因此使用线程，让它等一下再自动播放，对于用户而言，是一个良好的体验
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(isStoped){
                    readyPlay();
                }
                StartPlay();
            }
        }).start();


    }

    private boolean isStoped=true;
    private void readyPlay(){
        try {
            //设置播放源和播放设备之后进入播放状态
            mediaPlayer.setDataSource(this, Uri.fromFile(new File(playFileName)));
            mediaPlayer.setDisplay(svVideo.getHolder());
            mediaPlayer.prepare();//prepared
        } catch (IOException e) {
            e.printStackTrace();
        }

        isStoped=false;
    }
    public void onBtnVideoPlayClicked(View view) {
        if(isStoped){
            readyPlay();
        }
        StartPlay();
    }

    public void onBtnVideoPauseClicked(View view) {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }

    }

    public void onBtnVideoStopClicked(View view) {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        mediaPlayer.reset();//Idle
        isStoped=true;
    }

    @Override
    protected void onDestroy() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        //再次说明，一定要释放，不然其他程序可能无法正常使用MediaPlayer
        if(mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer=null;
        }
        //Activity结束时销毁定时器
        timer.cancel();
        super.onDestroy();
    }
}
