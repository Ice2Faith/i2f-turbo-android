package com.ugex.savelar.aptabview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
/*直接使用TabHost*/
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.direct_tab_view);
        //怒使用继承的方式使用TabHost时，这个ID报错可以不用管，不影响实际使用,下面也一样
        TabHost tabHost=findViewById(R.id.tabHostMain);
        //这里需要进行初始化
        tabHost.setup();
        //添加标签页并设置标签对应的:ID，标题，内容
        tabHost.addTab(tabHost.newTabSpec("tabMsg").setIndicator("消息").setContent(R.id.textViewDrtMsg));
        tabHost.addTab(tabHost.newTabSpec("tabCat").setIndicator("联系").setContent(R.id.textViewDrtCat));
        //添加一个带视图的标签
        View tabBarSet= LayoutInflater.from(this).inflate(R.layout.extends_tab_bar,null);
        tabHost.addTab(tabHost.newTabSpec("tabSet").setIndicator(tabBarSet).setContent(R.id.textViewDrtSet));

        //设置标签的图标

        setContentView(tabHost);
    }
}
