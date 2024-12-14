package com.ugex.savelar.atactivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
/*使用自己的Activity
* 将自己的Activity设置为程序的启动Activity，
* 只需要在AndroidMainifest.xml中修改自己的活动为
* <category android:name="android.intent.category.LAUNCHER" />
* 启动类型，其他Activity设置为DEFAULT，
* 或者将带有LAUNCHER的Activity的NAME换成自己的Activity的类名即可*/

/*
* Activity的生命周期与重构
* 过程：
*   启动Activity
*   onCreate()创建时运行
*   onStart()显示布局时运行,现在还不可见
*   onResume()获取焦点时运行，现在就可见了
*   Activity处于运行状态
*   另一个Activity插入时，onPause()还是可见，回到前台继续返回onResume()或者其他应用程序需要内存就被杀死进程重新回到onCreate
*   Activity变成不可见状态
*   onStop()进入后台执行，不可见，回到前台再次可见时onRestart()onStart(),或者又被杀死回到onCreate
*   onDestory()销毁时执行
*   Activity结束
* 以上的函数都可以进行重写，来实现一个自己的特性，右键-Generate-Override Method
*   点击home键onPause,onStop
*   点击back键onStop,onDestory
* 生命过程转换图：
*   onCreate->onStart
*   onStart->OnResume
*   onResume->onPause
*   onPause->onResume
*   onPause->onStop
*   onStop->onRestart
*   onRestart->onResume
*   onStop->onDestory
* 过程配对：
*   onCreate==onDestory 不可见
*   onStart==onStop 不可见
*   onResume==onPause 可见
*  */

/*
* Activity启动模式
* 安卓任务栈，负责对Activity的栈管理
* 可以在AndroidManifest.xml中的Activity的属性android:launchMode中指定启动模式
*
* 拥有四种启动模式
*   standard标准模式，默认的方式，每次启动都会创建一个新的实例对象，不管栈里面是否已经存在
*       会比较浪费栈空间，内存开销大
*       Intent intent=new Intent(this,ChildActivity.class);
*       startActivity(intent);
*       可以使用this.toString()查看类名和HashCode，会发现HashCode是不一样的，也就是一个新对象
*   singleTop单栈顶模式，会检查是否该Activity已经在栈顶，如果是则直接使用，不进行创建
*       算是标准模式的一种改进
*       会调用onNewIntent方法，可以在这里进行一些设置
*   singleTask单任务模式，保证任务栈中只有一个该Activity，会把它上面的Activity移除销毁，
*       虽然节省空间，但是出栈会比较耗费时间，注意上面的被移除，要考虑是否合适
*       如果已存在，会调用onNewIntent
*   singleInstance单例模式，会使用一个新的任务栈来保存一个新的Activity，这算是比较差劲的一种方式，
*       一般来说不常用
*       使用this.getTaskId()可以获得栈ID，可以看出，是一个新的栈，栈ID不一样
*       如果主任务栈中已经全部弹出，如果还有其他任务栈，那就跳转到其他任务栈，如果都没有了，程序退出
*       也就是说，如果已经用singleinstance启动了Activity，那么当其他栈已经空斩结束时，会进入剩下的这个单例栈
* 
* */

/*
* Activity的切换
* 通过Intent进行
* Intent：
*   是各个组件间进行交互的一种重要方式，不仅可以指定要执行的动作，还可以进行数据传递
*   一般用于启动Activity、服务、发送广播等，承担安卓通信功能
* Intent启动服务的方法：
*   startActivity(Intent)
*   startActivityForResult(Intent,int code)
*   显式意图：
*       明确指定激活组件的名称，在本应用中启动其他Activity时，可以使用显式意图来启动
*       Intent intent=new Intent(this,TargetActivity.class);
*       startActivity(intent);
*       或者
*       Intent intent=new Intent();
*       intent.setClass(MainActivity.this,MYActivity.class);
*       startActivity(intent);
*       也可以根据包名，全路径来指定，可以实现调起其他应用的活动
*       intent.setClassName("com.google.map","com.google.map.MainActivity");
*       startActivity(intent);
*   隐式意图：
*       AndroidMainnifest.xml中，Activity的子标签<action>指明了能够响应的动作为com.google.map.MainActivity
*       <category>标签则包含了类别信息，只有这两者的内容匹配时，才会被启动
*       使用方法，设置action和清单中一样
*       Intent intent=new Intent();
*       intent.serAction("com.google.map.MainActivity");
*       startActivity(intent);
* Intent的筛选过程：
*   加载所有IntentFilter到一个列表中
*   去除Action匹配失败的IntentFilter
*   去除URI数据匹配失败的IntentFilter
*   去除Category匹配失败的IntentFilter
*   判断剩余的IntentFilter数量是否为0
*       不为零，将匹配成功的IntentFilter按优先级排序，返回最高优先级
*       为0，查找失败，发生异常
* */

public class MainActivity extends AppCompatActivity {
    private Button btnDirectActivity;
    private Button btnHideActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("Activity Life","onCreate()");

        //显式意图
        btnDirectActivity=findViewById(R.id.buttonDetailActivity);
        btnDirectActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                //显式意图的三种方式
                //intent.setClass(MainActivity.this,MyActivity.class);
               //intent.setClassName(MainActivity.this,"com.ugex.savelar.atactivity.MyActivity");
                intent.setClassName("com.ugex.savelar.atactivity","com.ugex.savelar.atactivity.MyActivity");
                startActivity(intent);

            }
        });

        //隐式意图，推荐使用的，官方也建议使用的方式
        btnHideActivity=findViewById(R.id.buttonHideActivity);
        btnHideActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                //需要和Activity中的Action值一样，才能调起来，但是这相当于一个键，不能重复即可，一般设置为包名+类名
                //这样的话，就通过一个中间的键来代替类名，起到了一定的隐藏作用，你就是起一个无意义的名字，只要唯一都可以，
                //虽然说是唯一，但是Mainifest中是允许多个的，只是代码里面只能有一个，实际运行会调起多个给用户选择
                intent.setAction("com.ugex.savelar.atactivity.MyActivity");
                startActivity(intent);
                /*
                * 特别注意的是，如果AndroidMainifest.xml中存在多个Activity的Action值一样的话，
                * 并不会调用失败，而是出现一个可选的Activity，让用户自己选择要哪一个
                * 典型的就是程序选择图片的时候，出现使用哪个图片浏览器打开的情况*/
            }
        });

        startSystemActivity();
        startActivityForSendDataTest();
        startActivityForAcceptDataTest();
    }
    //调用系统已存在的界面
    private Button btnActionView;
    private void startSystemActivity() {
        btnActionView=findViewById(R.id.buttonActionView);
        btnActionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*//这样就可以调用系统中使用ACTION_VIEW的所有Activity给用户选择
                Intent intent=new Intent(Intent.ACTION_VIEW);
                //下面这一句就实现了，显示联系人1的信息页面
               //intent.setData(Uri.parse("content://contacts/people/1"));
                startActivity(intent);*/
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,CallSystemActivity.class);
                startActivity(intent);
            }
        });
    }
    //启动一个Activity并发数据过去
    private Button startAcSendData;
    private void startActivityForSendDataTest(){
        startAcSendData=findViewById(R.id.buttonSendDataActiv);
        startAcSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction("com.ugex.savelar.atactivity.MyActivity");
                //传递一个值过去给启动的Activity,是以键值对的方式发送的，下面只是为了方便阅读，所以传值才这样写的，
                //并不是有什么格式
                intent.putExtra("keyName:hello","keyValue:hello");
                intent.putExtra("key2",12);
                //bundle就相当于一个键值对集合，它可以批量放一堆数据进行传递
                Bundle bundle=new Bundle();
                bundle.putString("key3","hello");
                intent.putExtra("bundle",bundle);

                startActivity(intent);
            }
        });
    }

    //启动一个Activity并接收数据返回
    private Button startAcAcceptData;
    private void startActivityForAcceptDataTest(){
        startAcAcceptData=findViewById(R.id.buttonAcceptActiviData);
        startAcAcceptData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction("com.ugex.savelar.atactivity.MyActivity");
                //让Activity返回数据
                intent.putExtra("keyback","back data test");
                //请求码第二个参数，还需要重写onActivityResult方法接收返回值
                startActivityForResult(intent,0x0001);
            }
        });
    }
//参数，请求码，响应码，相应的intent，就是从intent获取数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //判断是否是需要处理的返回结果，并处理
        if(requestCode==0x0001 && resultCode==0x0002)
            Toast.makeText(this, "返回结果："+data.getStringExtra("keyresult"), Toast.LENGTH_SHORT).show();
    }

    //Activity的生命周期展示
    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Activity Life","onStart()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Activity Life","onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Activity Life","onDestroy()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Activity Life","onPause()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Activity Life","onResume()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("Activity Life","onRestart()");
    }
}
