package com.ugex.savelar.foowordsmemory.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.ugex.savelar.foowordsmemory.MainActivity;
import com.ugex.savelar.foowordsmemory.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

public class FooWordsMemoryToastService extends Service {
    private static TextToSpeech textToSpeech;
    class MyTextToSpeechInitListener implements TextToSpeech.OnInitListener{

        @Override
        public void onInit(int status) {
            if(status==TextToSpeech.SUCCESS)
            {
                int res=textToSpeech.setLanguage(Locale.getDefault());
                if(res==TextToSpeech.LANG_MISSING_DATA||res==TextToSpeech.LANG_NOT_SUPPORTED)
                {
                    Toast.makeText(getApplicationContext(), "语言设置不成功，默认为英文模式！", Toast.LENGTH_SHORT).show();
                }else
                {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        }

    }
    private void InitTextToSpeech(){
        textToSpeech=new TextToSpeech(this, new MyTextToSpeechInitListener());
    }
    private void DestroyTextToSpeech(){
        textToSpeech.stop();
        textToSpeech.shutdown();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        InitTextToSpeech();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DestroyTextToSpeech();
    }

    public static final String USER_WORDS_FILE_NAME="foo_words.txt";
    public static List<String> m_words=new ArrayList<>();

    public static Random rand=new Random();
    private  void loadWords() throws IOException {
        if(m_words.size()!=0){
            return;
        }
        File ufile=new File(Environment.getExternalStorageDirectory(),USER_WORDS_FILE_NAME);
        InputStream is=null;
        if(ufile.exists() && ufile.isFile() && ufile.canRead()){
            try {
                is = new FileInputStream(ufile);
                if(readFileContentToWords(is)==false){
                    is=getApplicationContext().getResources().openRawResource(R.raw.words);
                    readFileContentToWords(is);
                }
            }catch (Exception e){
                is=getApplicationContext().getResources().openRawResource(R.raw.words);
                readFileContentToWords(is);
            }
        }else{
            is=getApplicationContext().getResources().openRawResource(R.raw.words);
            readFileContentToWords(is);
        }
    }

    private boolean readFileContentToWords(InputStream is) throws IOException {
        m_words.clear();
        BufferedReader reader=new BufferedReader(new InputStreamReader(is));
        String line=null;
        while((line=reader.readLine())!=null){
            if(line.trim().equals("")){
                continue;
            }
            m_words.add(line);
        }
        reader.close();
        return m_words.size()!=0;
    }

    public static int LAY_GRAVITY=Gravity.LEFT|Gravity.CENTER_VERTICAL;
    public static int LAY_X_OFFSET=0;
    public static int LAY_Y_OFFSET=0;
    public static float LAY_TEXT_SIZE=16.0f;
    public static int LAY_SHOW_MILLISECOND=2*1000;
    public static  int LAY_WAIT_MILLISECOND =1*1000;

    public static boolean LAY_RUN_STATE=true;

    private static boolean LAY_USE_RANDOM_INDEX =true;
    private static int curIndex =0;

    private static boolean LAY_CONTINUE_LAST_INDEX=false;

    private static boolean LAY_OPEN_SPEECH_MODE=false;

    public static Bundle getSettingsBundle(){
        Bundle setting=new Bundle();
        setting.putInt("LAY_GRAVITY",LAY_GRAVITY);
        setting.putInt("LAY_X_OFFSET",LAY_X_OFFSET);
        setting.putInt("LAY_Y_OFFSET",LAY_Y_OFFSET);
        setting.putFloat("LAY_TEXT_SIZE",LAY_TEXT_SIZE);
        setting.putInt("LAY_SHOW_MILLISECOND",LAY_SHOW_MILLISECOND);
        setting.putInt("LAY_WAIT_MILLISECOND",LAY_WAIT_MILLISECOND);
        setting.putBoolean("LAY_RUN_STATE",LAY_RUN_STATE);

        setting.putBoolean("LAY_USE_RANDOM_INDEX",LAY_USE_RANDOM_INDEX);
        setting.putBoolean("LAY_CONTINUE_LAST_INDEX",LAY_CONTINUE_LAST_INDEX);

        setting.putBoolean("LAY_OPEN_SPEECH_MODE",LAY_OPEN_SPEECH_MODE);

        return setting;
    }

    private void getIntentSettingCmd(Intent intent){
        if(intent==null)return;
        Bundle bundle=intent.getBundleExtra("setting");
        if(bundle==null)return;
        LAY_GRAVITY=bundle.getInt("LAY_GRAVITY");
        LAY_X_OFFSET=bundle.getInt("LAY_X_OFFSET");
        LAY_Y_OFFSET=bundle.getInt("LAY_Y_OFFSET");
        LAY_TEXT_SIZE=bundle.getFloat("LAY_TEXT_SIZE");
        LAY_SHOW_MILLISECOND=bundle.getInt("LAY_SHOW_MILLISECOND");
        LAY_WAIT_MILLISECOND=bundle.getInt("LAY_WAIT_MILLISECOND");
        LAY_RUN_STATE=bundle.getBoolean("LAY_RUN_STATE");

        LAY_USE_RANDOM_INDEX=bundle.getBoolean("LAY_USE_RANDOM_INDEX");
        LAY_CONTINUE_LAST_INDEX=bundle.getBoolean("LAY_CONTINUE_LAST_INDEX");

        if(LAY_USE_RANDOM_INDEX==false){
            if(LAY_CONTINUE_LAST_INDEX==false){
                curIndex =0;
            }

        }

        LAY_OPEN_SPEECH_MODE=bundle.getBoolean("LAY_OPEN_SPEECH_MODE");

    }

    private static int lr=0,lg=0,lb=0;
    private static int cr=255,cg=255,cb=255;
    private static int curTimes=0,maxTimes=100,sleepMillSecond=30;
    private static final int MSG_WHAT_SMOOTH=0x101;

    private void toastOutRandomWords(){
        Handler handler=new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                curTimes=0;
                maxTimes=LAY_SHOW_MILLISECOND/sleepMillSecond;
                lr=cr;
                lg=cg;
                lb=cb;
                cr=rand.nextInt(255);
                cg=rand.nextInt(255);
                cb=rand.nextInt(255);

                if(LAY_USE_RANDOM_INDEX){
                    curIndex =rand.nextInt(m_words.size());
                }
                if(curIndex <0){
                    curIndex =0;
                }
                if(curIndex >=m_words.size()){
                    curIndex =m_words.size()-1;
                }
                String line=m_words.get(curIndex);
                if(LAY_USE_RANDOM_INDEX==false){
                    curIndex =(curIndex +1)%m_words.size();
                }

                if(LAY_OPEN_SPEECH_MODE){
                    //去除词性，取而代之的是一个小停顿，通过.实现
                    String speechLine=line.replaceFirst("[ \\f\\v\\t\\r]+[a-zA-Z/ \\.]+\\.",".");//[ \f\v\t\r] 可以等价替换为 \S 可是这里不知道为什么不管用
                    textToSpeech.speak(speechLine,TextToSpeech.QUEUE_FLUSH,null);
                }

                final View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.foo_words_toast,null);
                final TextView tvWordsLine=view.findViewById(R.id.textViewWordsLine);


                tvWordsLine.setTextColor(Color.rgb(lr,lg,lb));
                tvWordsLine.setText(line);
                tvWordsLine.setTextSize(LAY_TEXT_SIZE);

                final WindowToast toast=WindowToast.make(getApplicationContext(),view,LAY_SHOW_MILLISECOND);
                toast.setGravity(LAY_GRAVITY,LAY_X_OFFSET,LAY_Y_OFFSET);
                toast.show();

                //这一个handler是运行在UI线程中的，因为他的外层handler就是UI线程
                final Handler handler1=new Handler(){
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        if(msg.what==MSG_WHAT_SMOOTH){
                            double rate=curTimes*1.0/maxTimes;
                            double nrate=1.0-rate;
                            int pr=(int)(lr*nrate+cr*rate);
                            int pg=(int)(lg*nrate+cg*rate);
                            int pb=(int)(lb*nrate+cb*rate);
                            tvWordsLine.setTextColor(Color.rgb(pr, pg, pb));
                            GradientDrawable gd=(GradientDrawable) tvWordsLine.getBackground();
                            int gray=(int)(0.299*pr+0.587*pg+0.114*pb);
                            int ngray=255-gray;
                            gd.setColor(Color.argb(40,ngray,ngray,ngray));
                            tvWordsLine.setBackground(gd);
                        }
                    }
                };
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for(;curTimes<maxTimes;curTimes++){
                            handler1.sendEmptyMessage(MSG_WHAT_SMOOTH);
                            try {
                                Thread.sleep(sleepMillSecond);
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });

    }

    public static boolean serviceRunning=false;
    private void runShow(){
        if(serviceRunning==true){
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                serviceRunning=true;
                while(LAY_RUN_STATE){
                    toastOutRandomWords();
                    try {
                        Thread.sleep(LAY_SHOW_MILLISECOND+LAY_WAIT_MILLISECOND);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                serviceRunning=false;
            }
        }).start();
    }
    public FooWordsMemoryToastService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //判断是否有通知栏控制信息送来
        String notify=intent.getStringExtra(NOTIFY_CONTROL_EXTRA_KEY);
        if(notify!=null && "".equals(notify)==false){
            if(notify.equals(NOTIFY_CONTROL_SWITCH_POWER_STATE)){
                LAY_RUN_STATE=!LAY_RUN_STATE;
            }
            if(notify.equals(NOTIFY_CONTROL_SWITCH_SPEECH_STATE)){
                LAY_OPEN_SPEECH_MODE=!LAY_OPEN_SPEECH_MODE;
            }
        }
        try {
            loadWords();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getIntentSettingCmd(intent);
        startForeground(0x101,buildNotification());
        runShow();
        return START_STICKY;//super.onStartCommand(intent, flags, startId);
    }


    public static final String NOTIFY_CONTROL_EXTRA_KEY ="notify";
    public static final String NOTIFY_CONTROL_SWITCH_POWER_STATE="powerSwitch";
    public static final String NOTIFY_CONTROL_SWITCH_SPEECH_STATE="speechSwitch";
    private Notification buildNotification(){
        Notification notification=null;

        //API 16及以上
        NotificationManager notifyManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String idchannel = "foo_words";
        String name="foo_words";
        Resources res=getResources();
        String notify_title=res.getString(R.string.notify_title);
        String notify_content=res.getString(R.string.notify_content);
        String notify_trick=res.getString(R.string.notify_trick);
        //由于安卓8引入了通知归类，因此需要加入才能显示出来
        //也就是需要消息管道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(idchannel, name, NotificationManager.IMPORTANCE_HIGH);
            notifyManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(this)
                    .setChannelId(idchannel)
                    .setContentTitle(notify_title)
                    .setContentText(notify_content)
                    .setTicker(notify_trick)
                    .setVibrate(new long[]{200,200,300,50,50})
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();
        } else {
            notification = new NotificationCompat.Builder(this)
                    .setContentTitle(notify_title)
                    .setContentText(notify_content)
                    .setTicker(notify_trick)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();
            //版本11-16使用getNotification()替换build()
        }
        //设置自定义通知栏布局
        RemoteViews remoteNotifyView=new RemoteViews(getPackageName(),R.layout.notification_bar);
        notification.contentView=remoteNotifyView;
        //设置点击通知图标进入设置主页
        Intent intentIcon = new Intent(getApplicationContext(),
                MainActivity.class);//代表fragment所绑定的activity，这个需要写全路径
        remoteNotifyView.setOnClickPendingIntent(R.id.imageViewNotifyIcon,
                PendingIntent.getActivity(
                        getApplicationContext(),
                        0x09,
                        intentIcon,
                        0
                ));
        //设置点击功能按钮和播报按钮进行设置响应，响应在onStartCommand中
        Intent intentPower=new Intent(getApplicationContext(),FooWordsMemoryToastService.class);
        intentPower.putExtra(NOTIFY_CONTROL_EXTRA_KEY,NOTIFY_CONTROL_SWITCH_POWER_STATE);
        remoteNotifyView.setOnClickPendingIntent(R.id.buttonNotifyOpenPower,
                PendingIntent.getService(
                        getApplicationContext(),
                        0x10,
                        intentPower,0)
        );

        Intent intentSpeech=new Intent(getApplicationContext(),FooWordsMemoryToastService.class);
        intentSpeech.putExtra(NOTIFY_CONTROL_EXTRA_KEY,NOTIFY_CONTROL_SWITCH_SPEECH_STATE);
        remoteNotifyView.setOnClickPendingIntent(R.id.buttonNotifyOpenSpeech,
                PendingIntent.getService(
                        getApplicationContext(),
                        0x11,
                        intentSpeech,0)
        );
        return notification;
    }

}
