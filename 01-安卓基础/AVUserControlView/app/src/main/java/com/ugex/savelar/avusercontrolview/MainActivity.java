package com.ugex.savelar.avusercontrolview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ugex.savelar.avusercontrolview.UserControlDrawView.UserControlDrawViewActivity;
import com.ugex.savelar.avusercontrolview.UserControlCombineView.UserControlCombineViewActivity;
import com.ugex.savelar.avusercontrolview.UserControlExtendsView.UserControlExtendsViewActivity;

/*
* View类是所有View控件的父类
* 自定义控件是从View或者其子类派生来的
* 也可以将控件组合、自绘、继承的方式来实现
* 自绘控件
*   在View上展现的内容全部都是绘制出来的
*   绘制的代码卸载onDraw()方法中
*   自定义的View在界面上显示，只需要像使用普通的控件一样使用即可
*
* */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitApp();
    }

    private void InitApp() {

    }

    //自定义控件1 自绘控件
    public void GotoUserControlDrawView(View view) {
        Intent intent=new Intent();
        intent.setClass(MainActivity.this, UserControlDrawViewActivity.class);
        startActivity(intent);
    }

    //自定义控件2 组合控件
    public void GotoUserControlCombineView(View view) {
        Intent intent=new Intent();
        intent.setClass(MainActivity.this, UserControlCombineViewActivity.class);
        startActivity(intent);
    }

    //自定义控件3 继承控件
    public void GotoUserControlExtendsView(View view) {
        Intent intent=new Intent();
        intent.setClass(MainActivity.this, UserControlExtendsViewActivity.class);
        startActivity(intent);
    }
}
