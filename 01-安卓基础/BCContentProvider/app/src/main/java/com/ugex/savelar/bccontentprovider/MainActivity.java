package com.ugex.savelar.bccontentprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ugex.savelar.bccontentprovider.CtdOsv.SmsContentObserverTest;
import com.ugex.savelar.bccontentprovider.SysCtdPvd.SysContentPrividerTestActivity;
import com.ugex.savelar.bccontentprovider.UserCtdPvd.UserContentProviderTestActivity;
/*
* ContentProvider
* 数据库在安卓中是私有的，不能将数据库设置为WORLD_READABLE,每个数据库都只能被创建它的包访问
* 只有创建这个数据库的应用程序才能访问它
* 数据库不能跨越进程和包的边界，直接访问别的应用程序的数据库
* 如果需要在进程间传递数据，可以使用ContentPrivider来实现
*
* 为了在程序之间交换数据，Android提供了ContentProvider这个数据交换的标准API
* 提供给别人ContentProvider(实现)
* 接受数据ContentResolver来操作ContentProvider
* */
/*
* ContentProvider的使用
* Uri:统一资源定位器
* 每个ContentProvider都拥有一个公共的URI
* 一般来说形如：
*   content://包名/路径
*   content:模式
*   包名：命名空间
*   路径：
*   */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void OnSysContentProvider(View view) {
        Intent intent=new Intent(MainActivity.this, SysContentPrividerTestActivity.class);
        startActivity(intent);
    }

    public void OnUserContentProvider(View view) {
        Intent intent=new Intent(MainActivity.this, UserContentProviderTestActivity.class);
        startActivity(intent);
    }

    public void OnUseContenObserver(View view) {
        startActivity(new Intent(this, SmsContentObserverTest.class));
    }
}
