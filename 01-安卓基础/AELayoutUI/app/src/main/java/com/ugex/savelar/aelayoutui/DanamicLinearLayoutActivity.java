package com.ugex.savelar.aelayoutui;
/**
 * 动态更改LinearLayout的布局模式
 */

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DanamicLinearLayoutActivity extends AppCompatActivity {
    private LinearLayout linearlyt;
    private Button horbtn,velbtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.danamicliearlayout_face);
        linearlyt=(LinearLayout)findViewById(R.id.danamiclayout);
        horbtn=(Button)findViewById(R.id.btnhor);
        velbtn=(Button)findViewById(R.id.btnvel);
        horbtn.setOnClickListener(new BtnClick());
        velbtn.setOnClickListener(new BtnClick());
    }
    class BtnClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.btnhor)
            {
                linearlyt.setOrientation(LinearLayout.HORIZONTAL);
            }else if(v.getId()==R.id.btnvel)
            {
                linearlyt.setOrientation(LinearLayout.VERTICAL);
            }
        }
    }
}
