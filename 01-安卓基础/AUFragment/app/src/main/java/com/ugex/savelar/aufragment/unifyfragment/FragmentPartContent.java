package com.ugex.savelar.aufragment.unifyfragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ugex.savelar.aufragment.R;

import androidx.annotation.Nullable;

public class FragmentPartContent extends Fragment {
    private TextView tvContent;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //绑定视图
        View view=inflater.inflate(R.layout.fragment_part_content,null);
        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //获取本Fragment的控件
        tvContent= view.findViewById(R.id.textViewContent);
    }
    //设置控件的显示值
    public void setContentText(String str){
        tvContent.setText(str);
    }
}
