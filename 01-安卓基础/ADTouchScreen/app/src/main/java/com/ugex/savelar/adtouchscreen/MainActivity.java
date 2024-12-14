package com.ugex.savelar.adtouchscreen;
/**
 * 触屏事件
 * 实现：
 * 1.重写OnTouchEvent方法
 * 2.控件上重写方法SetOnTouchEventListener()
 */

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tvtips;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvtips=(TextView)findViewById(R.id.textView);
    }
    //重写方法

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction())//得到当前操作，移动，按下，弹起
        {
            case MotionEvent.ACTION_DOWN:
                tvtips.setText("Down X:"+event.getX()+" Y:"+event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                tvtips.setText("Move X:"+event.getX()+" Y:"+event.getY());
                break;
            case MotionEvent.ACTION_UP:
                tvtips.setText("Up X:"+event.getX()+" Y:"+event.getY());
                break;
        }
        //默认返回尾false，需要向上传递，如果处理完毕，可以返回true
        return super.onTouchEvent(event);
    }
}
