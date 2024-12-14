package com.ugex.savelar.aufragment.RollGuideFace;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ugex.savelar.aufragment.R;
/*实现一个滑动的滚动页面切换
* ViewPager+Fragment
* ViewPager在V4(android.support.v4.view.ViewPager)包下面，使用的时候，需要在布局写完整的名称，
* 也就是包含包名,但是这里可以用另一个：androidx.viewpager.widget.ViewPager
*
* */
public class ScrollGuidePageActivity extends AppCompatActivity {
    private View[] views;
    private ViewPager viewPager;
    private PagerTabStrip pagerTabScrip;
    private Fragment[] fragments;
    private String[] pageTitle={"page1","page2","page3"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_guide_page);

        InitActivity();

    }

    private void InitActivity() {
        viewPager=findViewById(R.id.viewpagerGuide);
        /*//官方不推荐直接采用View的方式进行使用
        views=new View[3];
        LayoutInflater inflater=LayoutInflater.from(this);
        int[] viewIds={R.layout.viewpager_view1,R.layout.viewpager_view2,R.layout.viewpager_view3};
        for(int i=0;i<viewIds.length;i++){
            views[i]=inflater.inflate(viewIds[i],null);
        }
        //设置适配器View->PagerAdapter,
        viewPager.setAdapter(new MyPagerAdpter());
        */

        //使用Fragment的方式实现
        fragments=new Fragment[3];
        fragments[0]=new FragmentViewpager1();
        fragments[1]=new FragmentViewpager2();
        fragments[2]=new FragmentViewpager3();
        //设置适配器Fragment->FragmentPagerAdapter,这里是因为使用androidx下面的，所以需要使用getSupportFragmentManager
        viewPager.setAdapter(new MyFragPageAdpter(getSupportFragmentManager()));

        //美化一下标题栏
        pagerTabScrip=findViewById(R.id.pageTabStripTitle);
        pagerTabScrip.setBackgroundColor(Color.WHITE);
        pagerTabScrip.setTextColor(Color.RED);
        pagerTabScrip.setTabIndicatorColor(Color.BLUE);
    }
    class MyPagerAdpter extends PagerAdapter{

        @Override
        public int getCount() {
            return views.length;//推荐是使用Fragment的
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;//判断是否要显示的页面
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(views[position]);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(views[position]);
            return views[position];
        }
        //显示标题
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return pageTitle[position];
        }
    }
    class MyFragPageAdpter extends FragmentPagerAdapter{

        public MyFragPageAdpter(FragmentManager fm){
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

        //显示标题
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return pageTitle[position];
        }
    }
}
