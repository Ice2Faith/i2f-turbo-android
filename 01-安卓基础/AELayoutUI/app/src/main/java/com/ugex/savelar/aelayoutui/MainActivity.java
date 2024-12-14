package com.ugex.savelar.aelayoutui;
/**
 * LinnerLayout:线性布局，不会自动换行，有垂直水平方向性
 * RelativyLayout:相对布局，控件之间，父容器之间，无方向性
 */

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
