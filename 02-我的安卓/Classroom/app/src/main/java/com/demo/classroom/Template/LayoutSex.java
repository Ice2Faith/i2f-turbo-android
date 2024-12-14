package com.demo.classroom.Template;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.demo.classroom.R;
import com.demo.classroom.Util.ActivityHelper;

import androidx.annotation.Nullable;

public class LayoutSex extends LinearLayout {
    private Context context;
    private RadioGroup rgpSex;
    public LayoutSex(Context context) {
        super(context);
        InitLayout(context);
    }

    public LayoutSex(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        InitLayout(context);
    }

    private void InitLayout(Context ctx) {
        this.context=ctx;
        LayoutInflater.from(context).inflate(R.layout.layout_sex,this);
        rgpSex=findViewById(R.id.radioGroupSex);

    }

    public void setSex(String sex){
        if(ActivityHelper.SEX_MAN.equals(sex)){
            rgpSex.check(R.id.radioButtonMan);
        }else if(ActivityHelper.SEX_WOMAN.equals(sex)){
            rgpSex.check(R.id.radioButtonWoman);
        }
    }

    public String getSex(){
        return ActivityHelper.getRadioSex(rgpSex.getCheckedRadioButtonId());
    }
}
