package com.ugex.savelar.aufragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;//AS使用的
//import  android.app.Fragment;//版本为3.0以上的使用
//import android.support.v4.app.Fragment;//使用早期的Android,2.2,2.3

/*
创建时：
onCreate
onCreateView
onStart
onResume
销毁时：
onPause
onStop
onDestroy
转入后台时：
onPause
onStop
onStart
onResume
一次创建到退出的过程看起来是这样的：
onAttach
onCreate
onCreateView
onActivityCreated
onStart
onResume//显示完成
onPause//开始退出
onStop
onDestroyView
onDestroy
onDetach
一次带有进入后台的过程看起来是这样的：
onAttach
onCreate
onCreateView
onActivityCreated
onStart
onResume//显示完成
onPause
onStop//进入后台
onStart
onResume//返回前台
onPause//退出
onStop
onDestroyView
onDestroy
onDetach
* */

public class FragmentDemo extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("Tag","onCreateView");
        //解析布局
        View view=inflater.inflate(R.layout.fragment_demo,null);
        //返回设计视图
        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Tag","onCreate");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("Tag","onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("Tag","onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("Tag","onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("Tag","onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Tag","onDestroy");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("Tag","onActivityCreated");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("Tag","onDestroyView");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("Tag","onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("Tag","onDetach");
    }
}
