package com.ugex.savelar.avusercontrolview.UserControlExtendsView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

public class MyButton extends Button {
    public MyButton(Context context) {
        super(context);
        InitView();
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        InitView();
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitView();
    }

    private void InitView(){
        paint=new Paint();
        SetPaintColor(Color.RED);
        this.setText("");
        this.setBackgroundColor(Color.TRANSPARENT);
    }
    public void SetPaintColor(int color){
        paintColor=color;
        invalidate();
    }
    private int paintColor;
    private Paint paint;
    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(paintColor);
        int wid=this.getWidth();
        int hei=this.getHeight();

        int px=wid/2;
        int py=hei/2;

        int radius=px<py?px-10:py-10;

        canvas.drawCircle(px,py,radius,paint);

        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                SetPaintColor(Color.YELLOW);
                break;
                case MotionEvent.ACTION_UP:
                    SetPaintColor(Color.RED);
                    break;
        }
        return super.onTouchEvent(event);
    }
}
