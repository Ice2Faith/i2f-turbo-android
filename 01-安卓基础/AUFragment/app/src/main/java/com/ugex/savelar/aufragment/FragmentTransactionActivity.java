package com.ugex.savelar.aufragment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

public class FragmentTransactionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_transaction);
    }
    private boolean isFristAdd=true;
    public void addFragment(View view){
        //获得FragmentManager对象
        FragmentManager fragManager=getFragmentManager();
        //开始一个事务
        FragmentTransaction fragTransaction=fragManager.beginTransaction();
        //添加一个Fragment
        FragmentManagerTest fragment=new FragmentManagerTest();
        if(isFristAdd){
            //参数：添加到的目标布局，fragment，标签
            //参数同replace
            fragTransaction.add(R.id.layout,fragment,"testTag");
            isFristAdd=false;
        }else{
            /*Fragment不允许有带参数构造*/
            fragTransaction.replace(R.id.layout,fragment,"testTag");
        }
        //提交事务
        fragTransaction.commit();
    }
    public void removeFragment(View view){
        //获得FragmentManager对象
        FragmentManager fragManager=getFragmentManager();
        //开始一个事务
        FragmentTransaction fragTransaction=fragManager.beginTransaction();
        //移除一个Fragment
        FragmentManagerTest fragment=new FragmentManagerTest();
        fragTransaction.remove(fragManager.findFragmentByTag("testTag"));
        //提交事务
        fragTransaction.commit();
    }

    public void ShowStateFragment(View view){
        //获得FragmentManager对象
        FragmentManager fragManager=getFragmentManager();
        //开始一个事务
        FragmentTransaction fragTransaction=fragManager.beginTransaction();
        //显示或者隐藏一个Fragment
        FragmentManagerTest fragment=(FragmentManagerTest) fragManager.findFragmentByTag("testTag");
        if(fragment.isHidden())
            fragTransaction.show(fragment);
        else
            fragTransaction.hide(fragment);
        //提交事务
        fragTransaction.commit();
    }
}
