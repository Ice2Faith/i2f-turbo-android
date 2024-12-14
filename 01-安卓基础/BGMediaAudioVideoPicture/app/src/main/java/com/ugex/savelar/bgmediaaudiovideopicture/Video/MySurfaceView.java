package com.ugex.savelar.bgmediaaudiovideopicture.Video;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class MySurfaceView extends SurfaceView {
    private Context context;
    private SurfaceHolder surfaceHolder;
    public MySurfaceView(Context context) {
        super(context);
        InitView(context);
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        InitView(context);
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitView(context);
    }

    private void InitView(Context context) {
        this.context=context;

        InitSurface();


    }

    private void ViewDraw() {
        //在此View上绘制，需要一个同步块，锁定Canvas开始绘制，解锁Canvas结束绘制
        //也就是相当于缓冲绘制
        Canvas canvas=surfaceHolder.lockCanvas();
        if(canvas==null){//锁定Canvas是可能不成功的
            return;
        }

        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    private void InitSurface() {
        surfaceHolder=getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            //Surface被创建的时候回调
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Toast.makeText(context, "Surface:Created", Toast.LENGTH_SHORT).show();

                ViewDraw();
            }
            //Surface发生改变时候回调，屏幕大小改变了
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Toast.makeText(context, "Surface:Changed", Toast.LENGTH_SHORT).show();

            }
            //Surface被销毁的时候回调
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Toast.makeText(context, "Surface:Destroyed", Toast.LENGTH_SHORT).show();

            }
        });
    }

}
