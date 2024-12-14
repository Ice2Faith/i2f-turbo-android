package com.ugex.savelar.aufragment.unifyfragment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.ugex.savelar.aufragment.R;
//layout-land文件夹下面是对应布局的横屏方式，或者平板模式
/*
* 实现思路：
* 分别建立左半边Guide和右半边Content两个视图的Fragment
*   FragmentPartGuide extends ListFragment,FragmentPartContent extends Fragment
* 主页面
*   如果是横屏那就显示出两个Fragment，Guide和Content都在视图中
*   如果是竖屏那就只显示做半边的Guide视图，Content需要放在另一个Activity中
* 点击Guide视图的响应
*   如果横屏，那必然能够找到Content组件，那就直接进行响应
*   如果竖屏，必然找不到Content组件，那就需要一个新的Activity来显示
* 至于Guide的响应，不应该让Guide直接控制Content
*   因此，使用接口，自定义一个接口的实现类来处理Guide的响应
*   实现解耦合，判断横竖屏状态，如果竖屏，那就通过Intent传递参数并启动Content的Activity
* */
public class SuitableUIFaceActivity extends AppCompatActivity {
    //实现一个监听器，传给对象用
    private SelectGuideCityListener listener=new SelectGuideCityListener() {
        @Override
        public void selectedCity(String city) {
            Toast.makeText(SuitableUIFaceActivity.this, city, Toast.LENGTH_SHORT).show();
            //如果竖屏，跳转页面，横屏就直接显示
            if(isLand==true)
            {
                //横屏
                contentFace.setContentText(city);
            }
            else
            {
                //竖屏
                Intent itent=new Intent();
                itent.setClass(SuitableUIFaceActivity.this,SelectedContentActivity.class);
                itent.putExtra("city",city);
                startActivity(itent);
            }
        }
    };
    private FragmentPartGuide guideFace;
    private FragmentManager fragManager;
    private FragmentPartContent contentFace;
    private boolean isLand;//判断是否横屏
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suitable_uiface);
        //获得FragmentManager
        fragManager =getFragmentManager();
        //不管横屏竖屏，因为设置的ID都是一样的，所有一样的使用即可
        guideFace=(FragmentPartGuide) fragManager.findFragmentById(R.id.fragmentGuide);
        //传递监听器过去
        guideFace.setSelectedGuideCityListenner(listener);

        //尝试获取Content页，如果能获取到，那就是横屏，否则就是竖屏
        contentFace=(FragmentPartContent)fragManager.findFragmentById(R.id.fragmentContent);
        if(contentFace==null)
            isLand=false;
        else
            isLand=true;
    }
}
