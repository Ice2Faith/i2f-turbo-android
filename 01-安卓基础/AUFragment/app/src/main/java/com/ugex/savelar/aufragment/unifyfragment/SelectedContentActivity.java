package com.ugex.savelar.aufragment.unifyfragment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ugex.savelar.aufragment.R;

public class SelectedContentActivity extends AppCompatActivity {

    private FragmentPartContent contentFace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_content);
        //获取传递过来的参数
        String city=getIntent().getStringExtra("city");
        //获取空间并设置值
        contentFace=(FragmentPartContent)getFragmentManager().findFragmentById(R.id.fragmentContent);
        contentFace.setContentText(city);
    }
}
