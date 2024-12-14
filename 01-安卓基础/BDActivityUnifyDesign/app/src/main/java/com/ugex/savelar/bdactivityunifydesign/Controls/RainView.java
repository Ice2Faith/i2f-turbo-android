package com.ugex.savelar.bdactivityunifydesign.Controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.ugex.savelar.bdactivityunifydesign.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;

public class RainView extends View {
    private Context context;
    private int bounceLevel=20;
    private int ballRadius=40;
    private int ballCount=1;
    public RainView(Context context) {
        super(context);
        InitView(context);
    }

    public RainView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        InitView(context);
        ParseAttrs(attrs);
    }


    public RainView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitView(context);
        ParseAttrs(attrs);
    }

    private void ParseAttrs(AttributeSet attrs) {
        TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.RainView);
         this.bounceLevel=ta.getInt(R.styleable.RainView_bounce_level,20);
        this.ballRadius=ta.getInt(R.styleable.RainView_ball_radius,40);
        this.ballCount=ta.getInt(R.styleable.RainView_ball_count,1);
    }

    public static class Rain{
        public int x;
        public int y;
        public int color=Color.RED;
        public int speed;
        public boolean isUp=true;
    }
    private Random rand=new Random();
    private List<Rain> rains=new ArrayList<>();
    private boolean isFristDraw=true;
    private void InitView(Context context) {
        this.context=context;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                for(int i=0;i<rains.size();i++){
                    Rain rain=rains.get(i);
                    rain.y+=rain.speed;
                    if(rain.isUp){
                        rain.speed--;
                        if(rain.speed<=-bounceLevel)
                            rain.isUp=false;
                    }else{
                        rain.speed++;
                        if(rain.speed>=bounceLevel) {
                            rain.isUp = true;
                            rain.color=rand.nextInt();
                        }

                    }
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    RainView.this.postInvalidate();
                }
            }
        },30,30);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int wid=this.getWidth();
        int hei=this.getHeight();
        if(isFristDraw){
            if(ballCount==1){
                Rain rain=new Rain();
                rain.x=wid/2;
                rain.y=(int)(hei*0.8);
                rains.add(rain);
            }else{
                for(int i=0;i<ballCount;i++){
                    Rain rain=new Rain();
                    rain.x=wid/2+rand.nextInt(wid/2)-wid/4;
                    rain.y=(int)(hei*0.8)-rand.nextInt(hei/4);
                    rains.add(rain);
                }
            }

            isFristDraw=false;
        }
        Paint paint=new Paint();
        paint.setStyle(Paint.Style.FILL);

        for(int i=0;i<rains.size();i++){
            paint.setColor(rains.get(i).color);
            canvas.drawCircle(rains.get(i).x,rains.get(i).y,ballRadius,paint);
        }


    }
}
