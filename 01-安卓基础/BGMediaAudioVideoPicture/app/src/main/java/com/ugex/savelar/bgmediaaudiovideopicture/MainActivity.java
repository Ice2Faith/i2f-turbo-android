package com.ugex.savelar.bgmediaaudiovideopicture;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ugex.savelar.bgmediaaudiovideopicture.Audio.AudioTestActivity;
import com.ugex.savelar.bgmediaaudiovideopicture.Image.ImageTestActivity;
import com.ugex.savelar.bgmediaaudiovideopicture.Video.VideoTestActivity;

/*
* MediaPlayer
* 控制音视频文件的播放
* 状态：
* new创建或者reset()之后，处于Idle状态
* release()之后，处于End状态
* 这两个状态之间就是MediaPlayer的生命周期
* 也可以设置它的一些监听器，实现一些自己需要的功能
*
* -new->Idle
* Idle-setDataResource()->Initialed
* Initialed-prepare()->prepared
* prepared-start()->started
* started-stop()->stoped
* started-pause()->paused
* -release()->end
*
* */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onBtnAudioClicked(View view){
        startActivity(new Intent(this, AudioTestActivity.class));
    }

    public void onBtnVideoClicked(View view){
        startActivity(new Intent(this, VideoTestActivity.class));

    }

    public void onBtnImageClicked(View view){
        startActivity(new Intent(this, ImageTestActivity.class));

    }
}
