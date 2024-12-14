package com.demo.classroom.Template;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.classroom.R;

import androidx.annotation.Nullable;

public class LayoutDatetime extends LinearLayout {
    private Context context;
    private TextView tvTtile;
    private TextView tvDatetime;
    private int year=-1;
    private int month=-1;
    private int day=-1;
    public LayoutDatetime(Context context) {
        super(context);
        InitLayout(context);
    }

    public LayoutDatetime(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        InitLayout(context);
    }

    private void InitLayout(Context ctx) {
        this.context=ctx;
        LayoutInflater.from(context).inflate(R.layout.layout_datetime,this);
        tvTtile=findViewById(R.id.textViewTitle);
        tvDatetime=findViewById(R.id.textViewDatetime);

        tvDatetime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                year=-1;
                month=-1;
                day=-1;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    DatePickerDialog dlg=new DatePickerDialog(context);
                    dlg.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker v, int yeararg, int montharg, int dayOfMonth) {
                            year=yeararg;
                            month=montharg;
                            day=dayOfMonth;
                            tvDatetime.setText(year+"-"+(1+month)+"-"+day);
                        }
                    });
                    dlg.show();
                }
            }
        });
    }
    public void setTitle(String title){
        tvTtile.setText(title);
    }
    public String getDate(String split){
        if(year==-1){
            return "";
        }
        return year+split+(1+month)+split+day;
    }
    public void setDatetime(String date){
        tvDatetime.setText(date);
    }
}
