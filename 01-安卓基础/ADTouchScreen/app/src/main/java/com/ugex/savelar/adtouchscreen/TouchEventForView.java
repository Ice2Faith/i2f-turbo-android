package com.ugex.savelar.adtouchscreen;
/**
 * 在AndroidManifest.xml中修改主活动用以启动 行：  <activity android:name=".TouchEventForView">
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class TouchEventForView extends Activity {
    private TextView mtvs;
    private TextView tvpoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.touchforview);
        mtvs=findViewById(R.id.mtextview);
        tvpoint=(TextView)findViewById(R.id.textView2);
        mtvs.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_MOVE)
                    tvpoint.setText("Move X:"+event.getX()+" Y:"+event.getY());
                return true;
            }
        });
    }
}
