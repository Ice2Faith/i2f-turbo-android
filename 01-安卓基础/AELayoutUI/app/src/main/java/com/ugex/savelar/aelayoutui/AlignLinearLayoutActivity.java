package com.ugex.savelar.aelayoutui;
/**
 * 动态修改布局方式
 */

import android.os.Bundle;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.KeyEventDispatcher;

public class AlignLinearLayoutActivity extends AppCompatActivity {
    private CheckBox cbarray[];
    private LinearLayout linearlay;
    private int AcptGravity[]={Gravity.TOP,Gravity.BOTTOM,Gravity.LEFT,Gravity.RIGHT,Gravity.CENTER};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alignlinearlayout_face);
        linearlay=(LinearLayout)findViewById(R.id.alignlnrlay);
        cbarray=new CheckBox[linearlay.getChildCount()];
        for(int i=0;i<linearlay.getChildCount();i++)
        {
            cbarray[i]=(CheckBox)linearlay.getChildAt(i);
            cbarray[i].setOnCheckedChangeListener(new CheckedEvtPrs());
        }

    }
    class CheckedEvtPrs implements CompoundButton.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            //linearlay.setGravity(Gravity.LEFT|Gravity.BOTTOM);
            int pv=0;
            for(int i=0;i<cbarray.length;i++)
            {
                if(cbarray[i].isChecked()==true)
                {
                    pv=pv|AcptGravity[i];
                }
            }
            //没有做互斥布局检测，比如上下都选中
            linearlay.setGravity(pv);
        }
    }
}
