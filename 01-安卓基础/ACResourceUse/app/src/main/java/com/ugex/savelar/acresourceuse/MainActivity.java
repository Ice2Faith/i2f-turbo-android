package com.ugex.savelar.acresourceuse;
/**
 * 字符串资源 res/values/string.xml
 * 颜色资源 res/values/colors.xml
 * 图像资源 res/drawable/string.xml
 * 尺寸资源 res/values/diments.xml 单位 推荐 dp布局 sp字体
 * 为方便国际化和资源修改提供便利
 * 而不是修改源代码
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tvtips;
    private ImageView ivpic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取资源
        Resources res=getResources();
        String tv="Get:"+res.getString(R.string.MyStringFile);
        tvtips=(TextView)findViewById(R.id.textView);
        tvtips.setText(tv);
        ivpic=(ImageView)findViewById(R.id.imageView);
        ivpic.setImageResource(R.drawable.img1);
    }
}
