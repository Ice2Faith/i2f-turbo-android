package com.ugex.savelar.bgmediaaudiovideopicture.Audio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ugex.savelar.bgmediaaudiovideopicture.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
* 使用Service实现能够后台音乐播放，
* 使用Broadcast实现播放控制*/
public class AudioTestActivity extends AppCompatActivity {
    private EditText edtFileName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_test);
        InitActivity();
        getLocalMusicList();
    }

    private void InitActivity() {
        //进入就开启服务
        edtFileName=findViewById(R.id.editTextFileNameAudio);
        edtFileName.setText(Environment.getDownloadCacheDirectory().getAbsolutePath()+File.separator+"test.mp3");
        startService(new Intent(this,AudioPlayService.class));
    }


    public void onBtnPlayAudioClicked(View view) {
        //发送播放的广播，此广播需要包含播放的文件名
        Intent intent=new Intent("operator.audio.receiver");
        intent.putExtra("audioCmd","play");
        intent.putExtra("audioFileName",edtFileName.getText().toString().trim());
        sendBroadcast(intent);
    }

    public void onBtnPauseAudioClicked(View view) {
        //发送广播暂停播放音乐
        Intent intent=new Intent("operator.audio.receiver");
        intent.putExtra("audioCmd","pause");
        sendBroadcast(intent);
    }

    public void onBtnStopAudioClicked(View view) {
        //发送广播停止播放音乐，如果出现音乐播放完毕之后，无法再次播放，需要点一次停止
        Intent intent=new Intent("operator.audio.receiver");
        intent.putExtra("audioCmd","stop");
        sendBroadcast(intent);
    }

    //获取本地音乐
    private ListView lsvLocalMusic;
    //文件名，全路径
    List<String> listMusic=new ArrayList<>();
    List<String> listAbsMusic=new ArrayList<>();
    private void insertLocalMusic(File path){
        //过滤文件夹，加快加载，毕竟这是在主线程中
        if(path.getName().startsWith("."))
            return;
        if(path.getName().equalsIgnoreCase("android")
         ||path.getName().equalsIgnoreCase("miui")
        ||path.getName().equalsIgnoreCase("tencent")
                ||path.getName().equalsIgnoreCase("dcim")
                ||path.getName().equalsIgnoreCase("xiaomi")){
            return;
        }
        if(path.isFile()){
            if(path.getName().endsWith(".mp3")){
                listMusic.add(path.getName());
                listAbsMusic.add(path.getAbsolutePath());
            }
        }else if(path.isDirectory()){
            for(File fp : path.listFiles()){
               if(fp.isFile()){
                   if(fp.getName().endsWith(".mp3")){
                       listMusic.add(fp.getName());
                       listAbsMusic.add(fp.getAbsolutePath());
                   }
               }else if(fp.isDirectory()){
                   insertLocalMusic(fp);
               }
            }
        }
    }
    private void getLocalMusicList(){
        insertLocalMusic(Environment.getExternalStorageDirectory());
        lsvLocalMusic=findViewById(R.id.listViewSdMusic);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,android.R.id.text1,listMusic);
        lsvLocalMusic.setAdapter(adapter);
        lsvLocalMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edtFileName.setText(listAbsMusic.get(position));
            }
        });
    }
}
