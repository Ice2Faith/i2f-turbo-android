package com.ugex.savelar.bgmediaaudiovideopicture.Image;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.ugex.savelar.bgmediaaudiovideopicture.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/*
* 使用系统Camera
* 创建自己的Camera
* */
/*
* 系统Camera
* 是安卓Framework提供的，支持拍照和拍摄视频的设备
* 程序使用时，需要声明权限：
*     <uses-permission android:name="android.permission.CAMERA"/>
* 需要声明照相机特性,并且不一定会使用camera2/camera
*       <uses-feature android:name="android.hardware.camera2" android:required="false"/>
* 如果需要保存图片，那就需要外部存储写入权限
*     <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
* 如果需要录制音频或者视频，需要音频权限
*       <uses-permission android:name="android.permission.RECORD_AUDIO"/>
*
 * */
/*
* 自定义Camera
* 步骤：
* 检测Camera可访问性，写代码检测并请求访问
* 创建一个预览窗口，一般使用SurfaceView，并实现SurfaceHolder接口，实现动态预览Camera内容
* 拍摄和保存
* 释放Camera，以便其他程序使用
*
* */
/*
* NinePatch九宫图
* 解决资源图片的部分不想被拉伸，只拉伸部分背景的情况
* 使用：
* 在SDK的tools/draw9patch.bat
* 运行之后会出现窗口
* 将要做处理的图片拖进去
* 粉红色的部分都会被拉伸，绿色部分左右拉伸，白色部分不会被拉伸，由于布局的原因
* 可以拖动边界线实现这些颜色区域的移动
* 也可以在四边边界上点击，产生一个新的边界
* （部分版本可能不会显示这些颜色，窗口下方有一个show patches 的复选框，勾上就能看到了）
* 使用它可以实现部分拉伸的效果
* 保存为"文件名.9.png"文件，注意一定是.9.png
* 在项目中导入进drawable进来使用即可
* 和使用其他资源图片一样使用
* */
public class ImageTestActivity extends AppCompatActivity {
    private ImageView ivPicture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_test);

        InitActivity();
    }

    private void InitActivity() {
        ivPicture=findViewById(R.id.imageViewPicture);

    }

    ////////////////////////////////////////////
    //使用系统的Camera
    //也就是通过Intent来启动一个startActivityFroResult()的相机拍照活动
    //在onActivityResult()中获取拍照的数据
    private int REQCODE_USE_SYSTEM_CAMERA_PICTURE=0x101;
    public void onBtnUseSystemCameraClicked(View view){
        //录制视频：MediaStore.ACTION_VIDEO_CAPTURE
        //录制视频在onActivityResult中返回的是一个游标
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       /*
       //设置保存位置
       //安卓7之后发生了不少的改变，第一个是动态权限，也就是下面这个
       //另外还有Intent不能携带Uri离开本应用程序了，需要通过Content的方式
       //具体修改情况看这篇文章：
       //https://www.cnblogs.com/kezhuang/p/8706988.html
       //因此这里就使用通用的方法，让它返回的数据进行保存，在onActivityResult中
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        //安卓6及之前可以直接这样用
        File file=new File("/sdcard","SysCap"+System.currentTimeMillis()+".jpg");
        Uri fileUri=Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri );
        */
        //开始活动
        startActivityForResult(intent,REQCODE_USE_SYSTEM_CAMERA_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //判断请求码
        if(requestCode==REQCODE_USE_SYSTEM_CAMERA_PICTURE && resultCode==RESULT_OK){
            //获取图片数据并显示预览
            Bitmap bmp=(Bitmap)data.getExtras().get("data");
            ivPicture.setImageBitmap(bmp);
            //将图片保存到外部存储
            File file=new File(Environment.getExternalStorageDirectory(),"SysCap"+System.currentTimeMillis()+".png");
            try {
                FileOutputStream outFile = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.PNG, 90, outFile);
                outFile.flush();
                outFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onBtnUseSelfCameraClicked(View view) {
        Intent intent=new Intent(this,MyCameraActivity.class);

        startActivity(intent);
    }
}
