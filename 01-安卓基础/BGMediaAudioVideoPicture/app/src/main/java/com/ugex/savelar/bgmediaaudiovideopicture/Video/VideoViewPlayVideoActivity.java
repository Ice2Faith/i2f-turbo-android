package com.ugex.savelar.bgmediaaudiovideopicture.Video;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.ugex.savelar.bgmediaaudiovideopicture.R;

import java.io.File;

public class VideoViewPlayVideoActivity extends AppCompatActivity {
    private VideoView vvideo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view_play_video);
        InitActivity();
    }

    private void InitActivity() {
        vvideo=findViewById(R.id.videoView);
        vvideo.setVideoURI(Uri.fromFile(new File(getIntent().getStringExtra("videoFileName"))));
        MediaController controller=new MediaController(this);
        vvideo.setMediaController(controller);
        vvideo.requestFocus();

        vvideo.start();

        vvideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(VideoViewPlayVideoActivity.this, "视频播放完毕", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
