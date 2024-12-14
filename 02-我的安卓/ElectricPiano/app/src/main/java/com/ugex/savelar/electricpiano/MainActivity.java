package com.ugex.savelar.electricpiano;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;


public class MainActivity extends AppCompatActivity {
    private Context context;
    private View view;
    private int[] rawId={R.raw.piano01,R.raw.piano02,R.raw.piano03,R.raw.piano04,R.raw.piano05,R.raw.piano06,R.raw.piano07,
            R.raw.piano08,R.raw.piano09,R.raw.piano10,R.raw.piano11,R.raw.piano12,R.raw.piano13,R.raw.piano14};
    int playIndex=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view=new DrawView(this);
        setContentView(view);
        context=this.getApplicationContext();
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction())
                {
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_DOWN:
                    //case MotionEvent.ACTION_UP:
                        playIndex=(int)(event.getY()/(view.getHeight()/14));
                        if(playIndex>=0&&playIndex<14)
                        AsynPlaySound(context,rawId[playIndex]);
                        break;
                }
                return true;
            }
        });
    }
    private void AsynPlaySound(final Context playContext, final int rawPlayId)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MediaPlayer mpr=MediaPlayer.create(playContext,rawPlayId);
                mpr.start();
                try {
                    Thread.sleep(mpr.getDuration());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    mpr.reset();
                    mpr.release();
                }
            }
        }).start();
    }
}
class DrawView extends View
{
    public DrawView(Context context) {
        super(context);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint=new Paint();
        paint.setStrokeWidth(0);
        Rect rect=new Rect();
        rect.left=0;
        rect.right=getWidth();
        int splitCount=14;
        for(int i=0;i<splitCount;i++)
        {
            paint.setColor(Color.rgb((splitCount-i)*255/splitCount,
                    Math.abs((i+splitCount/2)%splitCount-splitCount/2)*255/splitCount,
                    i*255/splitCount));
            rect.top=i*getHeight()/splitCount;
            rect.bottom=(i+1)*getHeight()/splitCount;
            canvas.drawRect(rect,paint);
        }
    }
}
