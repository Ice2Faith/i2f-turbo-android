package com.ugex.savelar.abselectcontrol;
/**
 * 使用RadioGroup可以实现单选框
 * 使用checkbox 可以实现复选框
 */

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tvchoice;
    private RadioGroup rgitem;
    private RadioButton rbjava;
    private RadioButton rbcpp;
    private RadioButton rbpy;
    private CheckBox cbcs,cbc,cbjs;
    private StringBuilder strs=new StringBuilder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rgitem=(RadioGroup)findViewById(R.id.radioGroupDevLang);
        tvchoice=(TextView)findViewById(R.id.textViewChoice);
        rbjava=(RadioButton)findViewById(R.id.radioButtonJava);
        rbcpp=(RadioButton)findViewById(R.id.radioButtonCpp);
        rbpy=(RadioButton)findViewById(R.id.radioButtonPython);
        //设置默认选择
        rbjava.setChecked(true);
        rgitem.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            //参数：发出时间的源 选中的id
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                tvchoice.setText("choice:"+String.valueOf(checkedId)+" "+(((RadioButton)findViewById(checkedId)).getText()).toString());
            }
        });
        cbcs=(CheckBox)findViewById(R.id.checkBox_csharp);
        cbc=(CheckBox)findViewById(R.id.checkBox_c);
        cbjs=(CheckBox)findViewById(R.id.checkBox_js);
        cbcs.setOnCheckedChangeListener(new CheckBoxChecked());
        cbc.setOnCheckedChangeListener(new CheckBoxChecked());
        cbjs.setOnCheckedChangeListener(new CheckBoxChecked());
    }
    //内部类，实现CompoundButton.OnCheckedChangeListener 接口
    class CheckBoxChecked implements CompoundButton.OnCheckedChangeListener{
        //参数:发出时间的源 是否被选中
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            String str=buttonView.getText().toString()+" ";
            if(isChecked)
            {
                strs.append(str);
            }
            else
            {
                int si=strs.indexOf(str);
                if(si!=-1)
                    strs.delete(si,str.length()+si);
            }
            tvchoice.setText(strs);
        }
    }
}
