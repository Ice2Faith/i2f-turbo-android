package com.ugex.savelar.aufragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.app.Fragment;
//如果要支持早期版本的安卓，API13以下（不包含），就需要v4.support中的Fragment
//继承的方式，也会变为FragmentActivity,重写onCreate设置绑定的布局
/*
* V4包下面的控件查找也要对应V4包下面的FragmentManager
* fragmentManager=this.getSupportFragmentManager();
* 这里会有一点差异*/
public class FragmentManagerTest extends Fragment {
    /*由于API规定，Fragment不能带有有参数构造函数，
    * 因此，可以使用静态方法，帮我们构造出一个我们想要的Fragment返回给我们
    * 会用到Bundle来进行传递参数
    * 比如：
    * public static FragmentManagerTest getInstance(int number){
    *   FragmentManagerTest f=new FragmentManagerTest();
    *   Bundle bundle=new Bundle();
    *   bundle.putInt("number",num);
    *   f.setArguments(bundle);
    *   return f;
    * }
    * 在需要使用的地方：
    *   Bundle bundle=getArguments();
    *   int num=bundle.getInt("number");
    * 这样就实现了Fragment的伪带参构造的实现
    * */
    private TextView tvInfo;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_manager_test,null);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tvInfo=view.findViewById(R.id.textViewfmt);
        super.onViewCreated(view, savedInstanceState);
    }
    //提供一个可以更改显示内容的函数
    public void setDisplayText(String str){
        this.tvInfo.setText(str);
    }
}
