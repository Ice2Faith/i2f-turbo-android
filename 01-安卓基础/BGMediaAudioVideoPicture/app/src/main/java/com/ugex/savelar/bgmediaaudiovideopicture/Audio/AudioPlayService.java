package com.ugex.savelar.bgmediaaudiovideopicture.Audio;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;

import java.io.File;
import java.io.IOException;

public class AudioPlayService extends Service {
    private MediaPlayer mediaPlayer;
    private AudioPlayBroadcastReceiver receiver;
    public AudioPlayService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        //获取MediaPlayer和注册广播接受者
        /*
        * MediaPlayer的另一种获取方式
        * mediaPlayer=MediaPlayer.create(context,Uri/ResourceId)
        * 这种方式可以使用资源文件，R.raw.,并且得到的就是已经进入prepared状态，
        * 可以直接使用start()播放音乐，不需要再prepare()了
        * */
        mediaPlayer=new MediaPlayer();
        receiver=new AudioPlayBroadcastReceiver();
        registerReceiver(receiver,new IntentFilter("operator.audio.receiver"));

    }
    //标识播防的文件名（绝对路径）,和标识点击停止按钮的停止状态
    private String fileName="#$";//这里由于后面有调用equals，因此给一个非法的名称，在第一次运行时不报错
    private boolean isStoped=true;
    private void readyPlay(){
        try {
            //设置进入prepared状态，等待播放，这里直接使用String作为参数，当然也可以使用context+Uri作为参数
            mediaPlayer.setDataSource(fileName);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isStoped=false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        //销毁时一定要释放mediaPlayer.release();
        //但是还要坚持是否播放
        if(mediaPlayer!=null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        if(mediaPlayer!=null) {
            mediaPlayer.release();
            mediaPlayer=null;
        }
        //取消注册广播接收
        unregisterReceiver(receiver);

        super.onDestroy();
    }

    class AudioPlayBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            //获取并判断播放命令
            String cmd=intent.getStringExtra("audioCmd");
            if("play".equals(cmd)){
                //如果是播放命令，可能会有新的播放歌曲
                String rfileName=intent.getStringExtra("audioFileName");
                if(rfileName!=fileName && fileName.equals(rfileName)==false){
                    fileName=rfileName;
                    if(mediaPlayer.isPlaying())
                        mediaPlayer.stop();
                    mediaPlayer.reset();
                    readyPlay();
                }
                //如果被主动停止，需要重新准备以播放
                if(isStoped) {
                    readyPlay();
                }
                mediaPlayer.start();
            }else if("pause".equals(cmd)){
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
            }else if("stop".equals(cmd)){
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                //主动停止之后，有可能需要停止，也有可能是上一次播放自动结束，需要手动停止后进行重新准备播放
                mediaPlayer.reset();
                isStoped=true;
            }
        }
    }
}
