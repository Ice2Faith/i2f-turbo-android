package com.ugex.savelar.aelayoutui;
/**
 * 相对于容器或者控件的布局方式
 * 控件默认放置于父容器的左上角
 */

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RelativeLayoutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relativelayout_face);
    }
}
