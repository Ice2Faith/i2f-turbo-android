package com.ugex.savelar.aufragment;

import androidx.appcompat.app.AppCompatActivity;
import android.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ugex.savelar.aufragment.DrawerGuideFace.DrawerGuideActivity;
import com.ugex.savelar.aufragment.RollGuideFace.ScrollGuidePageActivity;
import com.ugex.savelar.aufragment.StableGuideFace.StableGuideFaceActivity;
import com.ugex.savelar.aufragment.WelcomeFaceActivity.WelcomeFaceActivity;
import com.ugex.savelar.aufragment.unifyfragment.SuitableUIFaceActivity;

/*
* Fragment生命周期
* Android 3.0之后Api11以上引入的
* 将UI组件实现模块化的管理，实现UI复用
* 将之放入Activity中进行使用，
* 也可以在多个Activity中使用
* 一个Activity中可以包含多个Activity
* Fragment的声明周期受到Activity声明周期影响
*   创建时调用onCreate之后就调用onCreateView
*   用户离开时onPause
*   第一次绘制UI的时候onCreateView,因此这个方法是必不可少的，表示一个根布局
*   他的其他方法，和Activity中是相对应的，但是动态添加Fragment时除外
*
*   onAttach()当Fragment与Activity建立关联的时候
*   onActivityCreated()当Activity的onCreate方法调用时
*   onDestroyView()当Fragment的UI被删除的时候
*   onDetach()当Fragment与Activity取消关联的时候
*
* 使用：
*   创建一个布局给Fragment使用，
*   创建一个类继承自Fragment，并且重写onCreateView方法，
*   然后就可以放入Activity的布局中使用
*
* 交互和管理：
* FragmentManager
*   用来管理Activity的Fragment
*   findFragmentById()//根据ID查找R.id.fragmentId
*   findFragmentByTag()//根据Tag查找，"fragmentTag"
*   popBackStack()
*   addOnBackStackChangedListener
*
* */
/*
* Fragment的添加方式
* 静态的方式添加Fragment，
*   只需要在布局文件中，添加fragment标记即可
*
* 动态的方式添加Fragment，
*   需要FragmentTransaction的使用
*       add()添加一个Fragment
*       remove()删除一个Fragment
*       replace()替换容器中的Fragment
*       hide()隐藏已经显示的Fragment
*       show()显示被隐藏的Fragment
*       addToBackStack()将事务添加到后台栈，这样的话，当点击返回键的时候就会像Activity一样一个个的弹出来，
*           不是一下全结束了
*       commit()提交事务，改变应用到Activity
*
* */

/*
* 系统已经提供的有的Fragment
*   DialogFragment显示一个悬浮对话框
*   ListFragment显示一个由Adapter管理的项目列表
* */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ChangeFragmentText();
        DynamicFragmentTransaction();
        UseSystemFragment();
        GotoAutoSuitFragmentActivity();
        GotoScroolGuidePage();
        GotoStableGuidePage();
        GotoWelcomeFacePage();
        GotoLeftGuidePage();
    }

    //实现更改Fragment中的内容
    private FragmentManager fragManager;
    private FragmentManagerTest fragManagerTest;
    private Button btnChangeFragmentText;
    private boolean isChanged=false;
    private void ChangeFragmentText(){
        //获取FragmentManager对象，这个导入必须和继承Fragment的导入包一样，是同一个包下面的
        fragManager=getFragmentManager();
        fragManagerTest=(FragmentManagerTest) fragManager.findFragmentById(R.id.myFragmentManagerTest);

        btnChangeFragmentText=findViewById(R.id.buttonFmtChange);
        btnChangeFragmentText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isChanged==false)
                    fragManagerTest.setDisplayText("hello fragment manager");
                else
                    fragManagerTest.setDisplayText("text is changed");
                isChanged=!isChanged;
            }
        });
    }
    //Fragment的动态修改
    private Button btnDynamicFragment;
    private void UseSystemFragment(){
        btnDynamicFragment=findViewById(R.id.buttonDynamicFragment);
        btnDynamicFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,FragmentTransactionActivity.class);
                startActivity(intent);
            }
        });
    }

    //使用系统的Fragment
    private Button btnSysFragment;
    private void DynamicFragmentTransaction(){
        btnSysFragment=findViewById(R.id.buttonSystemFragment);
        btnSysFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,SystemFragmentActivity.class);
                startActivity(intent);
            }
        });
    }

    //自动横竖屏界面适配
    private Button btnAutoSuitFragment;
    private void GotoAutoSuitFragmentActivity(){
        btnAutoSuitFragment=findViewById(R.id.buttonAutoSuitFragment);
        btnAutoSuitFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this, SuitableUIFaceActivity.class);
                startActivity(intent);
            }
        });
    }

    //滑动导航页面跳转
    private Button btnScrollGuide;
    private void GotoScroolGuidePage(){
        btnScrollGuide=findViewById(R.id.buttonScollGuideViewPager);
        btnScrollGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this, ScrollGuidePageActivity.class);
                startActivity(intent);
            }
        });
    }

    //固定导航页面跳转
    private Button btnStableGuide;
    private void GotoStableGuidePage(){
        btnStableGuide=findViewById(R.id.buttonStableGuideViewPager);
        btnStableGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this, StableGuideFaceActivity.class);
                startActivity(intent);
            }
        });
    }

    //开始欢迎页面跳转
    private Button btnStartWelcome;
    private void GotoWelcomeFacePage(){
        btnStartWelcome=findViewById(R.id.buttonStartWelcomeViewPager);
        btnStartWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this, WelcomeFaceActivity.class);
                startActivity(intent);
            }
        });
    }

    //开始欢迎页面跳转
    private Button btnLeftGuide;
    private void GotoLeftGuidePage(){
        btnLeftGuide=findViewById(R.id.buttonDrawerGuideViewPager);
        btnLeftGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this, DrawerGuideActivity.class);
                startActivity(intent);
            }
        });
    }
}
