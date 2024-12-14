package com.ugex.savelar.avusercontrolview.UserControlDrawView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.ugex.savelar.avusercontrolview.R;

import androidx.annotation.Nullable;

/*
* 自绘控件的实现
* 必须实现三个构造方法*/
public class MyDrawView extends View {

    /*
    * 以代码的方式添加控件时会使用的*/
    public MyDrawView(Context context) {
        super(context);
        InitView();
    }

    /*
    * 以XML文件布局时，自动调用的*/
    public MyDrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        InitView();
    }
    /*
    * 如果还有默认样式的时候调用的*/
    public MyDrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitView();
    }

    private Bitmap mBitmap;
    private Paint mPaint;
    private void InitView() {
        mPaint=new Paint();
        mPaint.setColor(Color.RED);//画笔颜色
        mPaint.setStyle(Paint.Style.STROKE);//画笔样式
        mPaint.setStrokeWidth(3);//画笔线宽

        //获取Bitmap并缩放大小
        mBitmap= BitmapFactory.decodeResource(getResources(), R.drawable.rollback);
        mBitmap=Bitmap.createScaledBitmap(mBitmap,250,250,true);
    }

    /*
    * 绘制的回调函数
    * Canvas：相当于画布
    * 使用Canvas的draw族方法，绘制响应的图形
    * Paint:画笔*/

    @Override
    protected void onDraw(Canvas canvas) {
        int vwid=this.getWidth();
        int vhei=this.getHeight();
        //绘制圆形
        canvas.drawCircle(vwid/3,vhei/3,(vhei+vwid)/2/3,mPaint);
        //绘制填充矩形
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0,(vhei+vwid)/3,(vhei+vwid)/3,(vhei+vwid)*2/3,mPaint);
        //绘制位图
        canvas.drawBitmap(mBitmap,vwid/6,vhei/6,null);

        //绘制边框
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0,0,this.getWidth(),this.getHeight(),mPaint);
        super.onDraw(canvas);
    }
}
