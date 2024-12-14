package com.ugex.savelar.aufragment.StableGuideFace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ugex.savelar.aufragment.R;
import com.ugex.savelar.aufragment.RollGuideFace.FragmentViewpager1;
import com.ugex.savelar.aufragment.RollGuideFace.FragmentViewpager2;
import com.ugex.savelar.aufragment.RollGuideFace.FragmentViewpager3;

public class StableGuideFaceActivity extends AppCompatActivity {
    private TextView[] tvTitles;
    private String[] titles={"Tab1","Tab2","Tab3"};
    private ViewPager viewPager;
    private Fragment[] fragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stable_guide_face);

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
        LinearLayout tabs=findViewById(R.id.linearLayout_tabs);
        int count=tabs.getChildCount();
        tvTitles=new TextView[count];
        for(int i=0;i<count;i++){
            //获取所有子控件并设置标题文字
            tvTitles[i]=(TextView) tabs.getChildAt(i);
            tvTitles[i].setText(titles[i]);
            //可被点击
            tvTitles[i].setEnabled(true);
            //添加附加数据
            tvTitles[i].setTag(i);
            //添加监听
            tvTitles[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //对应修改显示的页面和当前的标签选中状态
                    viewPager.setCurrentItem((Integer) v.getTag());
                    selectedTabChanged((Integer) v.getTag());
                }
            });
        }
        //设置默认选中
        tvTitles[0].setEnabled(false);
        tvTitles[0].setBackgroundColor(Color.YELLOW);

    }
    private void selectedTabChanged(int selIndex){
        //设置所有处于可用状态
        for(int i=0;i<tvTitles.length;i++){
            tvTitles[i].setEnabled(true);
            tvTitles[i].setBackgroundColor(Color.rgb(0xaa,0xff,0xff));
        }
        //设置选中的一项为不可用状态
        tvTitles[selIndex].setEnabled(false);
        tvTitles[selIndex].setBackgroundColor(Color.YELLOW);
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
}
