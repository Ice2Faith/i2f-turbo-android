package com.ugex.savelar.avusercontrolview.UserControlCombineView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.ugex.savelar.avusercontrolview.R;

/*
* 写一个继承某个布局的自定义布局*/
public class MyTitleLinearLayout extends LinearLayout {

    /**/
    public MyTitleLinearLayout(Context context){
        super(context);
        InitView(context);
    }


    public MyTitleLinearLayout(Context context, AttributeSet attrs){
        super(context,attrs);
        InitView(context);
    }


    public MyTitleLinearLayout(Context context,AttributeSet attrs,int defStyleAttr){
        super(context,attrs,defStyleAttr);
        InitView(context);
    }

    private void InitView(Context context){
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.layout_my_title_linear,null);
        this.addView(view);
    }
}
