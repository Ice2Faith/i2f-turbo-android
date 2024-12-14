package com.ugex.savelar.aktoastdialog;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //给自己注册上下文菜单
        this.registerForContextMenu(findViewById(R.id.activity_main_id));
        dlg=new ProgressDialog(this);;
    }
    public void ToastSimpleText(View view){
        Toast.makeText(this,"这是一个简单的文本Toast",Toast.LENGTH_LONG).show();
    }
    public void ToastNewText(View view){
        Toast ts=new Toast(this);
        ImageView iv=new ImageView(this);
        iv.setImageResource(R.drawable.ic_launcher_foreground);
        ts.setView(iv);
        ts.setDuration(Toast.LENGTH_LONG);
        ts.show();
    }
    public void ToastWithIcon(View view){
        Toast ts=new Toast(this);
        View iv= LayoutInflater.from(this).inflate(R.layout.toast_layout,null);
        ts.setView(iv);
        ts.setDuration(Toast.LENGTH_LONG);
        ts.show();
    }
    public void DiagSimple(View view){
        AlertDialog ad=new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_launcher_background)
                .setTitle("AlertDlag")
                .setMessage("这是一个简单的对话框")
                .setPositiveButton("确定",null)
                .create();
        ad.show();
    }
    public void DlagBase(View view){
        AlertDialog dlg=new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_launcher_foreground)
                .setTitle("基本对话框")
                .setMessage("点击一下试试看")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this,"你点击了确定",Toast.LENGTH_LONG).show();
                    }
                })
                .setNeutralButton("忽略",null)
                .setNegativeButton("取消",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this,"你点击了取消",Toast.LENGTH_LONG).show();
                    }
                })
                .create();
        dlg.show();
    }
    public void DlagList(View view){
        final String[] items={"李白","露娜","花木兰","韩信"};
        AlertDialog dlg=new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_launcher_background)
                .setTitle("Please Choose")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this,items[which].toString(),Toast.LENGTH_LONG).show();
                    }
                })
                .create();
        dlg.show();
    }
    public void DlagSingleList(View view){
        final String[] items={"Java","C++","Python","PHP"};
        AlertDialog dlg=new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_launcher_background)
                .setTitle("Please Choose")
                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this,items[which].toString(),Toast.LENGTH_LONG).show();
                    }
                })
                .setPositiveButton("确认",null)
                .create();
        dlg.show();
    }
    public void DlagMultiList(View view){
        final String[] items={"Java","C++","Go","Rust"};
        final boolean[] selects={true,false,false,false};
        AlertDialog dlg=new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_launcher_background)
                .setTitle("Please Choose")
                .setMultiChoiceItems(items, selects, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        Toast.makeText(MainActivity.this,items[which].toString()+":"+isChecked,Toast.LENGTH_LONG).show();
                    }
                })
                .setPositiveButton("确认",null)
                .create();
        dlg.show();
    }
   private ProgressDialog dlg=null;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==PRESENT_PROGRESS)
                dlg.setProgress(msg.arg1);
            if(msg.what==FINISH_PROGRESS)
                dlg.dismiss();
        }
    };
    private static final int PRESENT_PROGRESS=0x0005;
    private static final int FINISH_PROGRESS=0x0006;
    public void DlagProgress(View view){
        dlg.setIcon(R.drawable.ic_launcher_background);
        dlg.setMessage("这是一个进度条对话框");
        dlg.setTitle("进度条度画框");
        dlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dlg.show();
        new Thread(){
            @Override
            public void run() {
                for(int i=0;i<=100;i++)
                {
                    Message msg=handler.obtainMessage();
                    msg.what=PRESENT_PROGRESS;
                    msg.arg1=i;
                    handler.sendMessage(msg);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Message msg=handler.obtainMessage();
                msg.what=FINISH_PROGRESS;
                handler.sendMessage(msg);
            }
        }.start();
    }
    public void DlagSelf(View view){
        View sv=LayoutInflater.from(this).inflate(R.layout.self_dlag,null);
        ImageView siv=sv.findViewById(R.id.imageView2);//通过布局下的查找进行获取或者设置值
        new AlertDialog.Builder(this)
                .setTitle("自定义对话框")
                .setView(sv)
                .setPositiveButton("确定",null)
                .show();
    }
    public void DlagSelfSec(View view){
        View sv=LayoutInflater.from(this).inflate(R.layout.self_dlag_sec,null);
        final Dialog dlg= new Dialog(this);
        dlg.setTitle("自定义对话框二");
        dlg.setContentView(sv);
        Button btn=(Button)sv.findViewById(R.id.buttonCloseSec);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });
        dlg.show();

    }
    //弹出窗口
    public void PopWindowShow(View view){
        //构建弹出窗口
        PopupWindow popwnd=new PopupWindow(this);
        //设置窗口的宽高，不然不会显示
        popwnd.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popwnd.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置获得焦点允许点击，这样当点击弹出窗口以外的地方，弹出窗口自然关闭
        popwnd.setFocusable(true);
        popwnd.setTouchable(true);
        //绑定弹出的布局
        View showView=LayoutInflater.from(this).inflate(R.layout.pop_window_view,null);
        popwnd.setContentView(showView);
        //设置父布局并显示窗口,显示居中，偏移量都为0
        View parentView=LayoutInflater.from(this).inflate(R.layout.activity_main,null);
        popwnd.showAtLocation(parentView, Gravity.CENTER,0,0);
    }

    //日期选择对话框
    @TargetApi(24)
    public void DlgDatePicker(View view){
        //创建一个对话框
        DatePickerDialog dlg=new DatePickerDialog(this);
        //设置对话框监听
        dlg.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Toast.makeText(MainActivity.this, year+"/"+(month+1)+"/"+dayOfMonth, Toast.LENGTH_SHORT).show();
            }
        });
        //显示对话框
        dlg.show();
    }

    //时间选择对话框
    public void DlgTimePicker(View view){
        //时间选择的构造和日期有点不一样，需要直接给出监听器，默认的时分，还有是否是24h制
        TimePickerDialog dlg=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Toast.makeText(MainActivity.this, hourOfDay+":"+minute+":0", Toast.LENGTH_SHORT).show();
            }
        },12,0,true);
        //显示
        dlg.show();
    }

    //弹出通知
    public void NotifySystemInfoTest(View view){
        //获得通知管理器
        NotificationManager nm=(NotificationManager) MainActivity.this.getSystemService(NOTIFICATION_SERVICE);
        //由于安卓O及以上，引入了分类通知的概念，因此这里就需要兼容
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel nchannel=new NotificationChannel("ID_TEST_01","test",NotificationManager.IMPORTANCE_DEFAULT);
            /*中间这部分是可选的，并且手机厂商不一样的话可能也会不一样*/
            nchannel.setDescription("用户测试通知组");//渠道描述
            nchannel.canBypassDnd();//是否绕过勿扰模式
            nchannel.setBypassDnd(true);//设置绕过勿扰模式
            nchannel.canShowBadge();//桌面图标的消息角标
            nchannel.setShowBadge(true);//设置桌面图标的消息角标
            nchannel.setSound(null,null);//设置通知声
            nchannel.enableVibration(true);//启用震动
            nchannel.setVibrationPattern(new long[]{0,200,500,1000,1000});//震动参数
            nchannel.enableLights(true);//启用呼吸灯
            nchannel.setLightColor(Color.GREEN);//设置呼吸灯颜色

            nm.createNotificationChannel(nchannel);
        }
        //准备点击通知时跳转到的Activity
        Intent notiIntent=new Intent(MainActivity.this,MainActivity.class);
        PendingIntent clickPI=PendingIntent.getActivity(MainActivity.this,1,notiIntent,PendingIntent.FLAG_CANCEL_CURRENT);
        //构造一条通知，这里的构造比较复杂，只是把常用的都使用了一下，普通情况下酌情即可
        Notification notify=new NotificationCompat.Builder(MainActivity.this)
                .setContentTitle("这是一条通知")//设置标题
                .setContentText("通知正文，什么也没有")//设置内容
                .setWhen(System.currentTimeMillis())//设置时间
                .setSmallIcon(R.drawable.ic_launcher_foreground)//设置小图标
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher_foreground))//设置大图标，注意这里有差异
                .setAutoCancel(true)//设置查看通知之后自动取消
                .setVibrate(new long[]{0,200,500,1000,1000})//通知时震动
                .setLights(Color.GREEN,500,1000)//通知时呼吸灯
                .setSound(Uri.fromFile(new File("/system/media/audio/ringtones/Luna.ogg")))//设置通知声音
                .setContentIntent(clickPI)//设置点击通知之后跳转到的Intent
                .build();//构造
        //由于使用到了震动这些，需要注意Mainfest权限的申请
       //使用管理器显示通知,ID,通知
        nm.notify(1,notify);

    }

    //菜单-布局绑定
    public boolean onCreateOptionMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }
    //菜单-响应
    public boolean onOptionItemSelected(MenuItem item){
        int id=item.getItemId();
        String str="";
        switch(id)
        {
            case R.id.menu_home:
                str="Menu Home";
                break;
            case R.id.menu_set:
                str="Menu Set";
                break;
            case R.id.menu_about:
                str="Menu About";
                break;
            case R.id.menu_connect:
                str="Menu Connect";
                break;
        }
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        return true;
    }
    //上下文菜单

    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
       super.onCreateContextMenu(menu,view,menuInfo);
       menu.add(0,1,0,"复制");
        menu.add(0,2,0,"剪切");
        menu.add(0,3,0,"粘贴");
    }
    public boolean onContextItemSelected(MenuItem item){
        int id=item.getItemId();
        String str="";
        switch(id)
        {
            case 1:
                str="ContextMenu Copy";
                break;
            case 2:
                str="ContextMenu Cut";
                break;
            case 3:
                str="ContextMenu Past";
                break;
        }
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        return super.onContextItemSelected(item);
    }
}
