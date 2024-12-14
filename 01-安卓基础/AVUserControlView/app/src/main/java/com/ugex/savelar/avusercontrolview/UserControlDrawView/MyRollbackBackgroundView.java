package com.ugex.savelar.avusercontrolview.UserControlDrawView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.ugex.savelar.avusercontrolview.R;

import androidx.annotation.Nullable;
/*实现自定义绘制图片的滚动绘制
* 因此实现Runnable接口*/
public class MyRollbackBackgroundView extends View implements Runnable{
    /*
     * 以代码的方式添加控件时会使用的*/
    public MyRollbackBackgroundView(Context context) {
        super(context);
        InitView();
    }

    /*
     * 以XML文件布局时，自动调用的*/
    public MyRollbackBackgroundView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        InitView();
    }
    /*
     * 如果还有默认样式的时候调用的*/
    public MyRollbackBackgroundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitView();
    }

    private  int currentX;
    private Bitmap m_bitmap;
    private void InitView(){
        currentX=0;
        m_bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.rollback);
    }

    @Override
    protected void onDraw(Canvas canvas) {


        super.onDraw(canvas);
        moveBackground(canvas);
    }

    private void moveBackground(Canvas canvas){
        if(m_bitmap==null)//第一次绘制可能位图还为空
            return;
        currentX-=3;//每次左移三个像素

        //获取连续绘制的第二张图片位置
        int secX=currentX+m_bitmap.getWidth();
        if(secX<=0){//判定是否一次绘制结束
            currentX=0;
            secX=currentX+m_bitmap.getWidth();
        }
        //缩放图片，适应大小
        m_bitmap=Bitmap.createScaledBitmap(m_bitmap,this.getWidth(),this.getHeight(),true);
        //绘制图片
        canvas.drawBitmap(m_bitmap,currentX,0,null);
        canvas.drawBitmap(m_bitmap,secX,0,null);

    }
    //控制是否绘制播放
    private boolean runFalg=true;
    public void setRunFlag(boolean flag){
        runFalg=flag;
    }
    @Override
    public void run() {
        while(runFalg){
            //不可以直接调用onDraw方法
            postInvalidate();//在线程中刷新绘制
            try {
                Thread.sleep(30);
            }catch (Exception e){}

        }
    }
}
