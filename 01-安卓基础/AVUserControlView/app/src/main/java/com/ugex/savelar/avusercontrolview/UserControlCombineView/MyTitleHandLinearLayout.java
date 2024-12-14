package com.ugex.savelar.avusercontrolview.UserControlCombineView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ugex.savelar.avusercontrolview.R;

import androidx.annotation.Nullable;
/*直接通过代码的方式实现自定义布局*/
public class MyTitleHandLinearLayout extends LinearLayout {
    private Context context;
    private Button btnBack;
    private TextView tvTitle;

    public MyTitleHandLinearLayout(Context context) {
        super(context);
        InitView(context);
    }

    public MyTitleHandLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        /*在代码中使用自定义组件属性*/
//        TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.mytitle);
//        title=ta.getString(R.styleable.mytitleattr_title);
//        tvTitle.setText(title);
        InitView(context);
    }

    public MyTitleHandLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitView(context);
    }

    private void InitView(Context context){
        this.context=context;
        tvTitle=new TextView(context);
        btnBack=new Button(context);

        LayoutParams params=new LayoutParams(0,
                LayoutParams.WRAP_CONTENT,4);
        tvTitle.setLayoutParams(params);
        tvTitle.setGravity(Gravity.CENTER);

        params=new LayoutParams(0,
                LayoutParams.WRAP_CONTENT,1);
        btnBack.setLayoutParams(params);

        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setBackgroundColor(Color.YELLOW);

        this.addView(btnBack);
        this.addView(tvTitle);

        tvTitle.setText("title");
        btnBack.setText("back");


    }
}
