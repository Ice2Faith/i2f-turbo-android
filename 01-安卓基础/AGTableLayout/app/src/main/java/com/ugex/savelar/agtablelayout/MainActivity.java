package com.ugex.savelar.agtablelayout;
/**
 * TableLayout:表格布局 activity_main.xml shrinkexpandlayout_face.xml
 * 默认控件占据一行
 * 所以需要TableRow安排一行
 * 列数跟随最大列数，不足的空出
 *
 * GridLayout:网格布局 gridlayout_face.xml
 * 更加灵活的表格布局形式
 * 设置行列，自动换行，对于单元格可以设置占用的行和列
 *
 * FrameLayout:帧布局 framelayout_face.xml modelslidelogin_face.xml(模拟实现滑动以验证的效果)
 * 空间都会被挤在左上角，会被覆盖，除非有透明
 */

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private FrameLayout famlay;
    private TextView tvfns,tvstl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modelslidelogin_face);
        //测试其他布局，请注释下面这行代码
        initSlide();
    }
    private void initSlide(){
        famlay=(FrameLayout)findViewById(R.id.framelytface);
        tvfns=(TextView)findViewById(R.id.textViewfinish);
        tvstl=(TextView)findViewById(R.id.textViewtouch);
        famlay.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_MOVE)
                {
                    //设置可见性，处理完成返回true，否则会无效
                    tvfns.setVisibility(View.VISIBLE);
                    tvstl.setVisibility(View.INVISIBLE);
                }
                return true;
            }
        });
    }

}
