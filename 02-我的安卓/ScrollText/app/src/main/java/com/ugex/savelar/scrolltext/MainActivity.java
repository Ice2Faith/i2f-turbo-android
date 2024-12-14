package com.ugex.savelar.scrolltext;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ugex.savelar.scrolltext.Controls.ScrollTextView;

public class MainActivity extends Activity {
    private ScrollTextView viewText;
    private LinearLayout lytSet;
    private EditText edtDisplayText;
    private EditText edtLineTextLength;
    private EditText edtScrllSpeed;
    private EditText edtRefreshMillSecond;
    private EditText edtTextColor;
    private EditText edtBackColor;
    private EditText edtTextStrockWidth;
    private CheckBox ckbOpenToVoice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewText=findViewById(R.id.scrollTextView);

        viewText.setDisplayText("点击设置输入要显示的文字，再次点击设置退出哦");

        lytSet=findViewById(R.id.linearLayoutSetting);
        edtDisplayText=findViewById(R.id.editTextDisplayText);
        edtLineTextLength=findViewById(R.id.editTextLineTextLength);
        edtScrllSpeed=findViewById(R.id.editTextScrollSpeed);
        edtRefreshMillSecond=findViewById(R.id.editTextRefreshMillTime);
        edtTextColor=findViewById(R.id.editTextTextColor);
        edtBackColor=findViewById(R.id.editTextBackColor);
        edtTextStrockWidth=findViewById(R.id.editTextTextStrockWidth);
        ckbOpenToVoice=findViewById(R.id.checkBoxOpentoVoice);

        ckbOpenToVoice.setChecked(false);
        viewText.setNeedToVoice(false);
    }

    public void onBtnSettingClicked(View view) {
        if(lytSet.getVisibility()==View.INVISIBLE){
            lytSet.setVisibility(View.VISIBLE);
        }else{
            lytSet.setVisibility(View.INVISIBLE);
            ApplySettingToView();
        }
    }
    private void ApplySettingToView(){
        try{
            viewText.setDisplayText(edtDisplayText.getText().toString().trim());
            viewText.setLineTextLength(Integer.parseInt(edtLineTextLength.getText().toString().trim()));
            viewText.setScrollSpeed(Integer.parseInt(edtScrllSpeed.getText().toString().trim()));
            viewText.setRefreshMillTime(Integer.parseInt(edtRefreshMillSecond.getText().toString().trim()));
            viewText.setFontColor(Color.parseColor(edtTextColor.getText().toString().trim()));
            viewText.setBgcolor(Color.parseColor(edtBackColor.getText().toString().trim()));
            viewText.setTextStrokeWidth(Float.parseFloat(edtTextStrockWidth.getText().toString().trim()));
            viewText.setNeedToVoice(ckbOpenToVoice.isChecked());
        }catch (Exception e){}
    }

}
