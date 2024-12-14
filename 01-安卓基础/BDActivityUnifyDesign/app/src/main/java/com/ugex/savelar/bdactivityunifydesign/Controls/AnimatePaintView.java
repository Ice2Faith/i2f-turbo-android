package com.ugex.savelar.bdactivityunifydesign.Controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ugex.savelar.bdactivityunifydesign.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;

public class AnimatePaintView extends View {
    private Context context;
    private int wid=-1;
    private int hei=-1;
    private Timer timer=new Timer();
    private List<Ball> allBalls=new ArrayList<>();
    private  int ballCount =130;
    public AnimatePaintView(Context context) {
        super(context);
        InitView(context);
    }

    public AnimatePaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        InitView(context);
        ParseAttrs(attrs);
    }

    public AnimatePaintView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitView(context);
        ParseAttrs(attrs);
    }
    private void ParseAttrs(AttributeSet attrs) {
        TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.AnimatePaintView);
        this.ballCount =ta.getInt(R.styleable.AnimatePaintView_ani_ball_count,130);
    }
    public static class Ball{
        public double x;
        public double y;
        public double radius;
        public int color;
        public double speed;
        public double direct;
        private Random rand=new Random();

        public Ball(double x,double y,double radius){
            this.x=x;
            this.y=y;
            this.radius=radius;
            randomArgs();
        }

        public void randomArgs() {
            this.color= Color.argb(rand.nextInt(256),
                    rand.nextInt(256),
                    rand.nextInt(256),
                    rand.nextInt(256));
            this.speed=rand.nextInt(19)+1;
            this.direct=DirectionFromSize(rand.nextInt(360)-180,rand.nextInt(360)-180);
        }

        public Ball setArgs(double x,double y,double radius,int color,double speed,double direct){
            this.x=x;
            this.y=y;
            this.radius=radius;
            this.color=color;
            this.speed=speed;
            this.direct=direct;
            return this;
        }
    }

    private void InitView(Context cxt) {
        this.context=cxt;
        allBalls.clear();

        for(int i = 0; i< ballCount; i++){
            Ball ball=new Ball(rand.nextInt(480),
                    rand.nextInt(720),
                    rand.nextInt(45)+5);
            allBalls.add(ball);
        }

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for(int i = 0; i< ballCount; i++){
                    Ball ball=allBalls.get(i);
                    ball.x+=ball.speed*Math.sin(ball.direct);
                    ball.y+=ball.speed*Math.cos(ball.direct);
                    if(ball.x<=0 || ball.x>=wid
                            || ball.y<=0 || ball.y>=hei){
                        ball.randomArgs();
                    }
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    AnimatePaintView.this.postInvalidate();
                }
            }
        },300,30);//300毫秒之后执行，每30毫秒执行一次

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        for(int i = 0; i< ballCount; i++) {
                            if(rand.nextInt(100)<50){
                                Ball ball = allBalls.get(i);
                                ball.direct=(ball.direct+DirectionFromSize(event.getY()-ball.y,event.getX()-ball.x))/2.0;
                            }

                        }
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

            if(hei!=this.getHeight()
            || wid!=this.getWidth()){
                hei=this.getHeight();
                wid=this.getWidth();
            }
        Paint paint=new Paint();
        paint.setStyle(Paint.Style.FILL);
        for(int i = 0; i< ballCount; i++) {
            Ball ball=allBalls.get(i);
            paint.setColor(ball.color);
            canvas.drawCircle((int)ball.x, (int)ball.y, (float)ball.radius, paint);
        }

    }
    Random rand=new Random();
    public static double DirectionFromSize(double height, double width){
        return Math.atan2(width,height);
    }
    public static double AngleToRadian(double angle){
        return angle/180.0*Math.PI;
    }

}
