package com.ugex.savelar.foowordsmemory.service;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import static android.content.Context.WINDOW_SERVICE;

public class WindowToast {
    public static final long DURATION_SHORT=1*1000;
    public static final long DURATION_LONG=3*1000;
    public static final long DURATION_LONG_LONG=5*1000;
    public static WindowToast make(Context ctx,String text){
        return new WindowToast(ctx,text,DURATION_LONG);
    }
    public static WindowToast make(Context ctx,String text,long duration){
        return new WindowToast(ctx,text,duration);
    }
    public static WindowToast make(Context ctx,View view){
        return new WindowToast(ctx,view,DURATION_LONG);
    }
    public static WindowToast make(Context ctx,View view,long duration){
        return new WindowToast(ctx,view,duration);
    }
    public interface IParamsCallback{
        //callback族函数允许你继承之后实现一些自己的个性化需求
        void callBackTextView(TextView textView);
        void callBackView(View view);
        void callBackParams(WindowManager.LayoutParams params);
    }
    private IParamsCallback callback=new IParamsCallback() {
        @Override
        public void callBackTextView(TextView textView) {
        }

        @Override
        public void callBackView(View view) {
        }

        @Override
        public void callBackParams(WindowManager.LayoutParams params) {
        }
    };
    private String text;
    private View view;
    private WindowManager.LayoutParams params;

    private static WindowManager windowManager;

    private Context context;

    private long duration=0;

    public WindowToast(Context ctx){
        context=ctx;
        defaultValues();
    }
    public WindowToast(Context ctx,String text,long duration){
        context=ctx;
        this.text=text;
        this.duration=duration;
        defaultValues();
    }
    public WindowToast(Context ctx,String text,IParamsCallback callback,long duration){
        context=ctx;
        this.text=text;
        this.callback=callback;
        this.duration=duration;
        defaultValues();
    }
    public WindowToast(Context ctx,View view,long duration){
        context=ctx;
        this.view=view;
        this.duration=duration;
        defaultValues();
    }
    public WindowToast(Context ctx,View view,IParamsCallback callback,long duration){
        context=ctx;
        this.view=view;
        this.callback=callback;
        this.duration=duration;
        defaultValues();
    }
    public WindowToast(Context ctx, View view, WindowManager.LayoutParams params,long duration){
        context=ctx;
        this.view=view;
        this.params=params;
        this.duration=duration;
        defaultValues();
    }

    private void defaultValues(){
        synchronized (WindowToast.class){
            if(windowManager==null){
                windowManager=(WindowManager)context.getSystemService(WINDOW_SERVICE);
            }

            if(text==null){
                text ="";
            }
            if(duration<=0){
                duration=2*1000;
            }
            if(view==null){
                TextView tv=new TextView(context);
                tv.setText(text);
                callback.callBackTextView(tv);
                view=tv;
            }else{
                if(view instanceof TextView){
                    ((TextView)view).setText(text);
                }
            }
            callback.callBackView(view);
            if(params==null) {
                params = new WindowManager.LayoutParams();
                params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
                params.width = WindowManager.LayoutParams.WRAP_CONTENT;
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                params.format = PixelFormat.TRANSLUCENT;
                //params.type = WindowManager.LayoutParams.TYPE_PHONE;
                // 设置窗体显示类型
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                } else {
                    params.type = WindowManager.LayoutParams.TYPE_PHONE;
                }
            }
            callback.callBackParams(params);
        }
    }

    private boolean isCanceled=true;
    private TimeTickCount timer;
    public void show(){
        if(isCanceled==false){
            return;
        }
        isCanceled=false;
        windowManager.addView(view,params);
        timer=new TimeTickCount(duration,100);
        timer.start();
    }
    public void hide(){
        if(isCanceled){
            return;
        }
        timer.cancel();
        windowManager.removeView(view);
        isCanceled=true;
    }

    public WindowToast setGravity(int gravity){
        params.gravity=gravity;
        params.x=0;
        params.y=0;
        return this;
    }
    public WindowToast setOffset(int xoffset,int yoffset){
        params.x=xoffset;
        params.y=yoffset;
        return this;
    }
    public WindowToast setGravity(int gravity,int xoffset,int yoffset){
        params.gravity=gravity;
        params.x=xoffset;
        params.y=yoffset;
        return this;
    }

    public WindowToast setSize(int width,int height){
        params.height=height;
        params.width=width;
        return this;
    }

    public WindowToast setTextColor(int color){
        if(view instanceof TextView){
            ((TextView)view).setTextColor(color);
        }
        return this;
    }

    public WindowToast setFontSize(float size){
        if(view instanceof TextView){
            ((TextView)view).setTextSize(size);
        }
        return this;
    }

    public WindowManager.LayoutParams getParams() {
        return params;
    }

    public WindowToast setParams(WindowManager.LayoutParams params) {
        this.params = params;
        return this;
    }

    public String getText() {
        return text;
    }

    public WindowToast setText(String text) {
        this.text = text;
        return this;
    }

    public View getView() {
        return view;
    }

    public WindowToast setView(View view) {
        this.view = view;
        return this;
    }

    public long getDuration() {
        return duration;
    }

    public WindowToast setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    public WindowToast updateView(View view){
        windowManager.updateViewLayout(view,params);
        return this;
    }
    public WindowToast updateView(View view, WindowManager.LayoutParams params){
        windowManager.updateViewLayout(view,params);
        return this;
    }

    class TimeTickCount extends CountDownTimer{

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public TimeTickCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            hide();
        }
    }
}
