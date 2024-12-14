package com.ugex.savelar.bgmediaaudiovideopicture.Video;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.ugex.savelar.bgmediaaudiovideopicture.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*
* 使用VideoView类播放视频
* 使用：
* 在布局文件中添加VideoView组件
* 在Activity中获取该组件，使用VideoView.setVideoPath()
* 或VideoView.setVideoURI()设置需要加载播放的视频
* 调用start()播放，还有stop()和pause()方法
*
*/
/* 使用MediaPlayer播放视频
* 不但可以播放音乐，也可以播放视频，但是没有提供视频输出界面，
* 因此需要配合SurfaceView组件使用
*
* */
/*
* SurfaceView
* 提供独立于主线程的绘制线程
* 提供了Z轴的绘画层次，在后台完成绘制之后再切换回前台
* 通过SurfaceJolder来控制Surface,
* 通过SurfaceHolder来获取Canvas
* SurfaceHolder.Callback接口来获取surface的生命周期和状态变化
*
* 常见应用于游戏开发，缓冲绘制等
* 使用：像自定义控件一样使用即可，可以在XML中添加，也可以直接代码使用出来
*
* */
public class VideoTestActivity extends AppCompatActivity {
    private EditText edtFileName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_test);

        InitActivity();
        getLocalVideoList();
    }

    private void InitActivity() {
        edtFileName=findViewById(R.id.editTextFileNameVideo);
        edtFileName.setText(new File(Environment.getExternalStorageDirectory(),"test.mp4").getAbsolutePath());


    }

    public void onBtnUseVideoViewPlayClicked(View view) {
        Intent intent=new Intent(this,VideoViewPlayVideoActivity.class);
        intent.putExtra("videoFileName",edtFileName.getText().toString());
        startActivity(intent);

    }

    public void onBtnUseMediaPlayerPlayClicked(View view) {
        Intent intent=new Intent(this,MediaPlayerPlayVideoActivity.class);
        intent.putExtra("videoFileName",edtFileName.getText().toString());
        startActivity(intent);
    }

    //获取本地视频
    private ListView lsvLocalVideo;
    //文件名，全路径
    List<String> listVideo =new ArrayList<>();
    List<String> listAbsVideo =new ArrayList<>();
    private void insertLocalVideo(File path){
        //过滤文件夹，加快加载，毕竟这是在主线程中
        if(path.getName().equalsIgnoreCase("android")
                ||path.getName().equalsIgnoreCase("miui")
                ||path.getName().equalsIgnoreCase("tencent")
                ||path.getName().equalsIgnoreCase("dcim")
                ||path.getName().equalsIgnoreCase("xiaomi")){
            return;
        }
        if(path.isFile()){
            if(path.getName().endsWith(".mp4")){
                listVideo.add(path.getName());
                listAbsVideo.add(path.getAbsolutePath());
            }
        }else if(path.isDirectory()){
            for(File fp : path.listFiles()){
                if(fp.isFile()){
                    if(fp.getName().endsWith(".mp4")){
                        listVideo.add(fp.getName());
                        listAbsVideo.add(fp.getAbsolutePath());
                    }
                }else if(fp.isDirectory()){
                    insertLocalVideo(fp);
                }
            }
        }
    }
    private void getLocalVideoList(){
        insertLocalVideo(Environment.getExternalStorageDirectory());
        lsvLocalVideo =findViewById(R.id.listViewSdVideo);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,android.R.id.text1, listVideo);
        lsvLocalVideo.setAdapter(adapter);
        lsvLocalVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edtFileName.setText(listAbsVideo.get(position));
            }
        });
    }
}
