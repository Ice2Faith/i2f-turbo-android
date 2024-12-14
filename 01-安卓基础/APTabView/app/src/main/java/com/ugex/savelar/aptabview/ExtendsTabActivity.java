package com.ugex.savelar.aptabview;

import android.app.TabActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
/*通过继承的方式使用TabHost*/
public class ExtendsTabActivity extends TabActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TabHost tabHost=getTabHost();

        //绑定布局
        LayoutInflater.from(this).inflate(R.layout.extends_tabactivity,tabHost.getTabContentView(),true);

        //添加标签页并设置标签对应的:ID，标题，内容
        tabHost.addTab(tabHost.newTabSpec("tabMsg").setIndicator("消息").setContent(R.id.textViewExtMsg));
        tabHost.addTab(tabHost.newTabSpec("tabCat").setIndicator("联系").setContent(R.id.textViewExtCat));
        //添加一个带视图的标签
        View tabBarSet= LayoutInflater.from(this).inflate(R.layout.extends_tab_bar,null);
        tabHost.addTab(tabHost.newTabSpec("tabSet").setIndicator(tabBarSet).setContent(R.id.textViewExtSet));

        //设置标签的图标

        setContentView(tabHost);
    }
}
