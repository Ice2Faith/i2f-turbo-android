package com.ugex.savelar.roundmusicplayer;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import androidx.annotation.Nullable;

/**
 * 一个支持用左右声道音量控制实现的环绕音乐播放器
 *  支持设置播放列表
 *  支持上一首、下一首控制（需要设置播放列表不为空）
 *  支持顺序播放、随机播放（需要设置播放列表不为空）
 *  支持多种环绕模式
 *  支持定时停止播放功能(播放完当前)
 * 运行逻辑：
 * 通过Service进行播放，service中的广播接收器尽心接受广，实现参数的传递与基本播放参数控制
 * 外部控制只需要：
 * 1.启动service--startService()
 * 2.使用静态内部类ControlHelper的静态方法进行控制与参数传递即可
 *
 * 你需要关注什么？
 * 1.启动service
 * 2.使用RoundMusicPlayerService.ControlHelper中的方法
 */
public class RoundMusicPlayerService extends Service {
    public static final String ACTION_ROUND_MUSIC_PLAYER_CONTROLLER_RECEIVER_VALUE ="com.ugex.savelar.receiver.roundmusicplayer.controller";
    private ArrayList<String> playList=new ArrayList<>();
    private int currentPlayPos=0;
    private String currentPlayMode=ControlHelper.EXTRA_VALUE_MUSIC_LIST_PLAY_MODE_SEQUENCE;
    private String currentRoundRule=ControlHelper.EXTRA_VALUE_ROUND_RULE_REGULAR;
    private RoundMusicPlayerServiceReceiver receiver;
    private MediaPlayer mediaPlayer;
    private Thread roundThread=new Thread(new RegularRoundThread());
    private Random rand=new Random();
    private float roundVaryRate=0.01f;
    private int roundVaryTime=60;
    private boolean isOpenRound=true;

    private boolean isOpenStopOnTimeArrive=false;
    private long stopPlayTime;
    class SoftCubicFuncRegularRoundThread implements Runnable{
        @Override
        public void run() {
            boolean isDown=true;
            float currentLeft=-1.0f;
            while(isOpenRound){
                float leftVol=(float) ((Math.pow(currentLeft,3)-(-1.0))/2);
                mediaPlayer.setVolume(leftVol,1.0f-leftVol);
                if(isDown==false) {
                    currentLeft += roundVaryRate;
                    if(currentLeft>=1.0f){
                        isDown=true;
                    }
                } else{
                    currentLeft -= roundVaryRate;
                    if(currentLeft<=-1.0f){
                        isDown=false;
                    }
                }
                if(Thread.currentThread().isInterrupted()){
                    break;
                }
                try {
                    Thread.sleep(roundVaryTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class SoftSinRegularRoundThread implements Runnable{
        @Override
        public void run() {
            boolean isDown=true;
            float currentLeft=0.0f;
            while(isOpenRound){
                float leftVol=(float)(Math.sin(currentLeft)-(-1.0))/2.0f;
                mediaPlayer.setVolume(leftVol,1.0f-leftVol);
                currentLeft+=roundVaryRate;
                if(currentLeft>=Math.PI*2.0f){
                    currentLeft-=Math.PI*2.0f;
                }
                if(Thread.currentThread().isInterrupted()){
                    break;
                }
                try {
                    Thread.sleep(roundVaryTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class RegularRoundThread implements Runnable{
        @Override
        public void run() {
            boolean isDown=true;
            float currentLeft=1.0f;
            while(isOpenRound){
                mediaPlayer.setVolume(currentLeft,1.0f-currentLeft);
                if(isDown){
                    currentLeft-=roundVaryRate;
                    if(currentLeft<=0.0f){
                        isDown=false;
                    }
                }else{
                    currentLeft+=roundVaryRate;
                    if(currentLeft>=1.0f){
                        isDown=true;
                    }
                }
                if(Thread.currentThread().isInterrupted()){
                    break;
                }
                try {
                    Thread.sleep(roundVaryTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class ShuffleRoundThread implements Runnable{
        @Override
        public void run() {
            boolean isDown=true;
            float currentLeft=1.0f;
            float jumpLeft=rand.nextInt(500)/1000.0f;
            while(isOpenRound){
                mediaPlayer.setVolume(currentLeft,1.0f-currentLeft);
                if(isDown){
                    currentLeft-=roundVaryRate;
                    if(currentLeft<=jumpLeft){
                        isDown=false;
                        jumpLeft=(rand.nextInt(500)+500)/1000.0f;
                    }
                }else{
                    currentLeft+=roundVaryRate;
                    if(currentLeft>=jumpLeft){
                        isDown=true;
                        jumpLeft=rand.nextInt(500)/1000.0f;
                    }
                }
                if(Thread.currentThread().isInterrupted()){
                    break;
                }
                try {
                    Thread.sleep(roundVaryTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        receiver=new RoundMusicPlayerServiceReceiver();
        registerReceiver(receiver,new IntentFilter(ACTION_ROUND_MUSIC_PLAYER_CONTROLLER_RECEIVER_VALUE));
        mediaPlayer=new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(isOpenStopOnTimeArrive){
                    if(new Date().getTime()>=stopPlayTime){
                        if(mediaPlayer!=null){
                            mediaPlayer.stop();
                        }
                        isOpenRound=false;
                        roundThread.interrupt();
                        isOpenStopOnTimeArrive=false;
                        return;
                    }
                }

                if(playList.size()>0){
                    if(ControlHelper.EXTRA_VALUE_MUSIC_LIST_PLAY_MODE_SEQUENCE.equals(currentPlayMode)){
                        currentPlayPos=(currentPlayPos+1)%(playList.size());
                    }else if(ControlHelper.EXTRA_VALUE_MUSIC_LIST_PLAY_MODE_RANDOM.equals(currentPlayMode)){
                        currentPlayPos=rand.nextInt(playList.size());
                    }
                    directPlayNewMusic(playList.get(currentPlayPos));
                }else{
                    sendBroadcast(new Intent(MainActivity.ACTION_RECEIVER_MUSIC_PLAY_COMPLETE));
                }


            }
        });
    }

    @Override
    public void onDestroy() {
        if(mediaPlayer!=null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        if(mediaPlayer!=null) {
            mediaPlayer.release();
            mediaPlayer=null;
        }
       unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static class ControlHelper{
        public static final String EXTRA_KEY_SET_STATE ="set_play_state";
        public static final String EXTRA_VALUE_STATE_PLAY="play";
        public static final String EXTRA_VALUE_STATE_PAUSE="pause";
        public static final String EXTRA_VALUE_STATE_STOP="stop";
        public static final String EXTRA_VALUE_STATE_RESET="reset";

        public static final String EXTRA_KEY_SET_LOOP="loop";
        public static final String EXTRA_VALUE_LOOP_OPEN="open_loop";
        public static final String EXTRA_VALUE_LOOP_CLOSE="close_loop";

        public static final String EXTRA_KEY_SET_MUSIC ="set_music";

        public static final String EXTRA_KEY_SET_MUSIC_LIST ="set_music_list";
        public static final String EXTRA_KEY_CLEAR_MUSIC_LIST="clear_music_list";
        public static final String EXTRA_KEY_SET_MUSIC_LIST_PLAY_POS="set_music_list_play_pos";
        public static final String EXTRA_KEY_PLAY_NEXT="play_next";
        public static final String EXTRA_KEY_PLAY_PREVIOUS="play_previous";

        public static final String EXTRA_KEY_SET_MUSIC_LIST_PLAY_MODE ="set_music_list_play_mode";
        public static final String EXTRA_VALUE_MUSIC_LIST_PLAY_MODE_SEQUENCE="play_mode_sequence";
        public static final String EXTRA_VALUE_MUSIC_LIST_PLAY_MODE_RANDOM="play_mode_random";

        public static final String EXTRA_KEY_SET_ROUND_MODE ="set_round_mode";
        public static final String EXTRA_VALUE_ROUND_MODE_OPEN="open_round";
        public static final String EXTRA_VALUE_ROUND_MODE_CLOSE="close_round";

        public static final String EXTRA_KEY_SET_ROUND_RULE ="set_round_rule";
        public static final String EXTRA_VALUE_ROUND_RULE_REGULAR="regular_round";
        public static final String EXTRA_VALUE_ROUND_RULE_SHUFFLE="shuffle_round";
        public static final String EXTRA_VALUE_ROUND_RULE_SOFTSIN="softsin_round";
        public static final String EXTRA_VALUE_ROUND_RULE_CUBICFUNC="cubicfunc_round";

        public static final String EXTRA_KEY_SET_STOP_LONG_TIME="set_stop_long_time";
        public static final String EXTRA_KEY_CANCEL_STOP_LONG_TIME="cancel_stop_long_time";

        public static void setStopLongTime(Context cxt,long time){
            Intent intent=new Intent(ACTION_ROUND_MUSIC_PLAYER_CONTROLLER_RECEIVER_VALUE);
            intent.putExtra(EXTRA_KEY_SET_STOP_LONG_TIME,time);
            cxt.sendBroadcast(intent);
        }

        public static void cancelStopLongTime(Context cxt){
            Intent intent=new Intent(ACTION_ROUND_MUSIC_PLAYER_CONTROLLER_RECEIVER_VALUE);
            intent.putExtra(EXTRA_KEY_CANCEL_STOP_LONG_TIME,"cancel");
            cxt.sendBroadcast(intent);
        }

        public static void setMusic(Context cxt,String fileName){
            Intent intent=new Intent(ACTION_ROUND_MUSIC_PLAYER_CONTROLLER_RECEIVER_VALUE);
            intent.putExtra(EXTRA_KEY_SET_MUSIC,fileName);
            cxt.sendBroadcast(intent);
        }
        public static void play(Context cxt){
            Intent intent=new Intent(ACTION_ROUND_MUSIC_PLAYER_CONTROLLER_RECEIVER_VALUE);
            intent.putExtra(EXTRA_KEY_SET_STATE,EXTRA_VALUE_STATE_PLAY);
            cxt.sendBroadcast(intent);
        }
        public static void pause(Context cxt){
            Intent intent=new Intent(ACTION_ROUND_MUSIC_PLAYER_CONTROLLER_RECEIVER_VALUE);
            intent.putExtra(EXTRA_KEY_SET_STATE,EXTRA_VALUE_STATE_PAUSE);
            cxt.sendBroadcast(intent);
        }
        public static void stop(Context cxt){
            Intent intent=new Intent(ACTION_ROUND_MUSIC_PLAYER_CONTROLLER_RECEIVER_VALUE);
            intent.putExtra(EXTRA_KEY_SET_STATE,EXTRA_VALUE_STATE_STOP);
            cxt.sendBroadcast(intent);
        }
        public static void reset(Context cxt){
            Intent intent=new Intent(ACTION_ROUND_MUSIC_PLAYER_CONTROLLER_RECEIVER_VALUE);
            intent.putExtra(EXTRA_KEY_SET_STATE,EXTRA_VALUE_STATE_RESET);
            cxt.sendBroadcast(intent);
        }
        public static void setLoop(Context cxt,boolean isOpenLoop){
            Intent intent=new Intent(ACTION_ROUND_MUSIC_PLAYER_CONTROLLER_RECEIVER_VALUE);
            if(isOpenLoop)
                intent.putExtra(EXTRA_KEY_SET_LOOP,EXTRA_VALUE_LOOP_OPEN);
            else
                intent.putExtra(EXTRA_KEY_SET_LOOP,EXTRA_VALUE_LOOP_CLOSE);
            cxt.sendBroadcast(intent);
        }
        public static void setRound(Context cxt,boolean isOpenRound){
            Intent intent=new Intent(ACTION_ROUND_MUSIC_PLAYER_CONTROLLER_RECEIVER_VALUE);
            if(isOpenRound)
                intent.putExtra(EXTRA_KEY_SET_ROUND_MODE,EXTRA_VALUE_ROUND_MODE_OPEN);
            else
                intent.putExtra(EXTRA_KEY_SET_ROUND_MODE,EXTRA_VALUE_ROUND_MODE_CLOSE);
            cxt.sendBroadcast(intent);
        }
        public static void setRoundRule(Context cxt,String roundRule){
            Intent intent=new Intent(ACTION_ROUND_MUSIC_PLAYER_CONTROLLER_RECEIVER_VALUE);
            intent.putExtra(EXTRA_KEY_SET_ROUND_RULE,roundRule);
            cxt.sendBroadcast(intent);
        }

        public static void setPlayList(Context cxt,ArrayList<String> list){
            Intent intent=new Intent(ACTION_ROUND_MUSIC_PLAYER_CONTROLLER_RECEIVER_VALUE);
            intent.putStringArrayListExtra(EXTRA_KEY_SET_MUSIC_LIST,list);
            cxt.sendBroadcast(intent);
        }

        public static void setListPlayMode(Context cxt,String mode){
            Intent intent=new Intent(ACTION_ROUND_MUSIC_PLAYER_CONTROLLER_RECEIVER_VALUE);
            intent.putExtra(EXTRA_KEY_SET_MUSIC_LIST_PLAY_MODE,mode);
            cxt.sendBroadcast(intent);
        }

        public static void clearPlayList(Context cxt){
            Intent intent=new Intent(ACTION_ROUND_MUSIC_PLAYER_CONTROLLER_RECEIVER_VALUE);
            intent.putExtra(EXTRA_KEY_CLEAR_MUSIC_LIST,"clear");
            cxt.sendBroadcast(intent);
        }
        public static void setMusicListPlayPos(Context cxt,int pos){
            Intent intent=new Intent(ACTION_ROUND_MUSIC_PLAYER_CONTROLLER_RECEIVER_VALUE);
            intent.putExtra(EXTRA_KEY_SET_MUSIC_LIST_PLAY_POS,pos);
            cxt.sendBroadcast(intent);
        }
        public static void playNext(Context cxt){
            Intent intent=new Intent(ACTION_ROUND_MUSIC_PLAYER_CONTROLLER_RECEIVER_VALUE);
            intent.putExtra(EXTRA_KEY_PLAY_NEXT,"next");
            cxt.sendBroadcast(intent);
        }
        public static void playPrevious(Context cxt){
            Intent intent=new Intent(ACTION_ROUND_MUSIC_PLAYER_CONTROLLER_RECEIVER_VALUE);
            intent.putExtra(EXTRA_KEY_PLAY_PREVIOUS,"previous");
            cxt.sendBroadcast(intent);
        }
    }
    class RoundMusicPlayerServiceReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String playState=intent.getStringExtra(ControlHelper.EXTRA_KEY_SET_STATE);
            if(playState!=null && "".equals(playState.trim())==false){
                if(ControlHelper.EXTRA_VALUE_STATE_STOP.equals(playState)){
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                    }
                }else if(ControlHelper.EXTRA_VALUE_STATE_PAUSE.equals(playState)){
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                    }
                }else if(ControlHelper.EXTRA_VALUE_STATE_PLAY.equals(playState)){
                        mediaPlayer.start();
                }else if(ControlHelper.EXTRA_VALUE_STATE_RESET.equals(playState)){
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                    }
                    mediaPlayer.reset();
                }
            }

            String loop=intent.getStringExtra(ControlHelper.EXTRA_KEY_SET_LOOP);
            if(loop!=null && "".equals(loop.trim())==false){
                if(ControlHelper.EXTRA_VALUE_LOOP_OPEN.equals(loop)){
                    mediaPlayer.setLooping(true);
                }else if(ControlHelper.EXTRA_VALUE_LOOP_CLOSE.equals(loop)){
                    mediaPlayer.setLooping(false);
                }
            }

            String music=intent.getStringExtra(ControlHelper.EXTRA_KEY_SET_MUSIC);
            if(music!=null && "".equals(music.trim())==false){
                directPlayNewMusic(music);
            }

            String roundState=intent.getStringExtra(ControlHelper.EXTRA_KEY_SET_ROUND_MODE);
            if(roundState!=null && "".equals(roundState.trim())==false){
                if(ControlHelper.EXTRA_VALUE_ROUND_MODE_OPEN.equals(roundState)){
                    isOpenRound=true;
                    CreateNewRoundModeThread();
                    roundThread.start();
                }else if(ControlHelper.EXTRA_VALUE_ROUND_MODE_CLOSE.equals(roundState)){
                    isOpenRound=false;
                    roundThread.interrupt();
                }
            }

            String roundRule=intent.getStringExtra(ControlHelper.EXTRA_KEY_SET_ROUND_RULE);
            if(roundRule!=null && "".equals(roundRule.trim())==false){
                currentRoundRule=roundRule;
                roundThread.interrupt();
                CreateNewRoundModeThread();

                if(isOpenRound && mediaPlayer.isPlaying()){
                    roundThread.start();
                }
            }

            ArrayList<String> musicList=intent.getStringArrayListExtra(ControlHelper.EXTRA_KEY_SET_MUSIC_LIST);
            if(musicList!=null && musicList.size()>0) {
                playList=musicList;
            }

            String listPlayMode=intent.getStringExtra(ControlHelper.EXTRA_KEY_SET_MUSIC_LIST_PLAY_MODE);
            if(listPlayMode!=null && "".equals(listPlayMode.trim())==false){
                if(ControlHelper.EXTRA_VALUE_MUSIC_LIST_PLAY_MODE_SEQUENCE.equals(listPlayMode)){
                    currentPlayMode=ControlHelper.EXTRA_VALUE_MUSIC_LIST_PLAY_MODE_SEQUENCE;
                }else if(ControlHelper.EXTRA_VALUE_MUSIC_LIST_PLAY_MODE_RANDOM.equals(listPlayMode)){
                    currentPlayMode=ControlHelper.EXTRA_VALUE_MUSIC_LIST_PLAY_MODE_RANDOM;
                }
            }

            String clearList=intent.getStringExtra(ControlHelper.EXTRA_KEY_CLEAR_MUSIC_LIST);
            if(clearList!=null && "".equals(clearList.trim())==false){
                playList.clear();
            }

            int playPos=intent.getIntExtra(ControlHelper.EXTRA_KEY_SET_MUSIC_LIST_PLAY_POS,-1);
            if(playPos!=-1 && playList.size()>0){
                currentPlayPos=playPos;
                directPlayNewMusic(playList.get(currentPlayPos));
            }

            String nextOne=intent.getStringExtra(ControlHelper.EXTRA_KEY_PLAY_NEXT);
            if(nextOne!=null && "".equals(nextOne.trim())==false){
               if(playList.size()>0){
                   if(ControlHelper.EXTRA_VALUE_MUSIC_LIST_PLAY_MODE_SEQUENCE.equals(currentPlayMode)){
                       currentPlayPos=(currentPlayPos+1)%(playList.size());
                   }else if(ControlHelper.EXTRA_VALUE_MUSIC_LIST_PLAY_MODE_RANDOM.equals(currentPlayMode)){
                       currentPlayPos=rand.nextInt(playList.size());
                   }
                   directPlayNewMusic(playList.get(currentPlayPos));
               }
            }

            String previousOne=intent.getStringExtra(ControlHelper.EXTRA_KEY_PLAY_PREVIOUS);
            if(previousOne!=null && "".equals(previousOne.trim())==false){
                if(playList.size()>0){
                    if(ControlHelper.EXTRA_VALUE_MUSIC_LIST_PLAY_MODE_SEQUENCE.equals(currentPlayMode)){
                        currentPlayPos=currentPlayPos-1;
                        if(currentPlayPos<0){
                            currentPlayPos=playList.size()-1;
                        }
                    }else if(ControlHelper.EXTRA_VALUE_MUSIC_LIST_PLAY_MODE_RANDOM.equals(currentPlayMode)){
                        currentPlayPos=rand.nextInt(playList.size());
                    }
                    directPlayNewMusic(playList.get(currentPlayPos));
                }
            }

            long stopTime=intent.getLongExtra(ControlHelper.EXTRA_KEY_SET_STOP_LONG_TIME,-1);
            if(stopTime!=-1){
                isOpenStopOnTimeArrive=true;
                stopPlayTime=stopTime;
            }

            String cancelStop=intent.getStringExtra(ControlHelper.EXTRA_KEY_CANCEL_STOP_LONG_TIME);
            if(cancelStop!=null && "".equals(cancelStop.trim())==false){
                isOpenStopOnTimeArrive=false;
                stopPlayTime=-1;
            }
        }
    }
    private void directPlayNewMusic(String musicPath){
        if(mediaPlayer.isPlaying())
            mediaPlayer.stop();
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(musicPath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void CreateNewRoundModeThread(){
        if(ControlHelper.EXTRA_VALUE_ROUND_RULE_REGULAR.equals(currentRoundRule)){
            if(roundThread.isInterrupted()==false){
                roundThread.interrupt();
            }
            roundThread=new Thread(new RegularRoundThread());
        }else if(ControlHelper.EXTRA_VALUE_ROUND_RULE_SHUFFLE.equals(currentRoundRule)){
            if(roundThread.isInterrupted()==false){
                roundThread.interrupt();
            }
            roundThread=new Thread(new ShuffleRoundThread());
        }else if(ControlHelper.EXTRA_VALUE_ROUND_RULE_SOFTSIN.equals(currentRoundRule)){
            if(roundThread.isInterrupted()==false){
                roundThread.interrupt();
            }
            roundThread=new Thread(new SoftSinRegularRoundThread());
        }else if(ControlHelper.EXTRA_VALUE_ROUND_RULE_CUBICFUNC.equals(currentRoundRule)){
            if(roundThread.isInterrupted()==false){
                roundThread.interrupt();
            }
            roundThread=new Thread(new SoftCubicFuncRegularRoundThread());
        }
    }
}
