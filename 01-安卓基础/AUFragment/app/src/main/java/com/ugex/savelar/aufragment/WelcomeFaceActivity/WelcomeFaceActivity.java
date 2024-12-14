package com.ugex.savelar.aufragment.WelcomeFaceActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ugex.savelar.aufragment.R;
import com.ugex.savelar.aufragment.RollGuideFace.FragmentViewpager1;
import com.ugex.savelar.aufragment.RollGuideFace.FragmentViewpager2;
import com.ugex.savelar.aufragment.RollGuideFace.FragmentViewpager3;
/*
* 安卓4.0以上，也可以
* 添加Tab添加到ActionBar
* 设置导航模式为：NAVIGATION_MODE_TABS
* 创建几个ActionBar.Tab实例
* 设置相应的监听ActionBar.TabListener
* 这样也可以实现这样的效果
* onCreate{
*   //具体请查看注释ActionBarImpl部分
* }*/
public class WelcomeFaceActivity extends AppCompatActivity {
    private ImageView[] imgDots;
    private ViewPager viewPager;
    private Fragment[] fragments;
    //自动轮播的时间爱你消息监听和相关控制变量
    private static final int HANDLE_MSG_AUTO_NEXT=0x1001;
    private boolean runAutoNext=true;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==HANDLE_MSG_AUTO_NEXT){
                int index=msg.arg1;
                viewPager.setCurrentItem(index);
                selectedTabChanged(index);
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_face);

        InitApp();
    }

    private void InitApp() {
        //获取ViewPager和其滑动监听器
        viewPager=findViewById(R.id.viewPager1);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            //在页面改变完成之后
            @Override
            public void onPageSelected(int position) {
                selectedTabChanged(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //初始化Fragment
        fragments=new Fragment[3];
        fragments[0]=new FragmentViewpager1();
        fragments[1]=new FragmentViewpager2();
        fragments[2]=new FragmentViewpager3();
        //设置适配器
        viewPager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager()));
        //获取Tab栏和其所有子View，由于都是TextView
        LinearLayout tabs=findViewById(R.id.linearLayout_imgs);
        int count=tabs.getChildCount();
        imgDots=new ImageView[count];
        for(int i=0;i<count;i++){
            //获取所有子控件
            imgDots[i]=(ImageView) tabs.getChildAt(i);
            //可被点击
            imgDots[i].setEnabled(true);
            //添加附加数据
            imgDots[i].setTag(i);
            //添加监听
            imgDots[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //对应修改显示的页面和当前的标签选中状态
                    viewPager.setCurrentItem((Integer) v.getTag());
                    selectedTabChanged((Integer) v.getTag());
                }
            });
        }
        //设置默认选中
        imgDots[0].setEnabled(false);
        //使用线程配合Handler显示自动轮播
        new Thread(new Runnable() {

            @Override
            public void run() {
                int index=0;
                while(runAutoNext){
                    Message msg=new Message();
                    msg.what=HANDLE_MSG_AUTO_NEXT;
                    msg.arg1=index;
                    handler.sendMessage(msg);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    index=(viewPager.getCurrentItem()+1)%imgDots.length;
                }

            }
        }).start();

    }
    private void selectedTabChanged(int selIndex){
        //设置所有处于可用状态
        for(int i=0;i<imgDots.length;i++){
            imgDots[i].setEnabled(true);
        }
        //设置选中的一项为不可用状态
        imgDots[selIndex].setEnabled(false);
    }

    class MyFragmentAdapter extends FragmentPagerAdapter{

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }

    @Override
    protected void onDestroy() {
        runAutoNext=false;
        super.onDestroy();
    }


    //使用ActionBar实现
    private void ActionBarImpl(){
        //由于需要安卓3.0以上才能用，因此如果使用了低版本的Fragment，那么需要
        /*
        * ViewPager控件来辅助完成
        * 给ViewPager设置FragmentAdapter即可*/
        ActionBar actionBar=getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        MyTabClickListener listener=new MyTabClickListener();
        ActionBar.Tab tab1=actionBar.newTab();
        tab1.setText("Tab1");
        tab1.setTabListener(listener);

        ActionBar.Tab tab2=actionBar.newTab();
        tab1.setText("Tab2");
        tab1.setTabListener(listener);

        actionBar.addTab(tab1);
        actionBar.addTab(tab2);
        /*
        * ViewPager pager=findViewById(R.id.viewPager1);
        * pager.setAdapter(new MyFragmentAdapter(getSupportFragment()));*/
    }

    class MyTabClickListener implements ActionBar.TabListener{

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            //注意，这里第一次的时候，ViewPager是可能为空的，需要判定
            //if(viewPager!=null)
           //   viewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }
    }
}
