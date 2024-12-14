package com.ugex.savelar.atactivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

/*
访问某些系统Activity需要用户权限（例如呼叫指定电话号码）
 访问系统Activity远比我们想象中的简单，通用格式如下：
        Intent intent =new Intent(action值);
		intent.setData(Uri格式);
        startActivity(intent);
   上述代码前两行也可以简写为：
        Intent intent =new Intent(action值，Uri格式);
         例如拨打电话：
          Intent intent =new Intent(Intent.ACTION_CALL，Uri.parse("tel:10086"));
	      startActivity(intent);
下面是Android中12个常见的系统Activity的Intent的定义格式，请给为你同学遵照上述格式自行调试处这十二个程序、

1，调web浏览器
Uri  myBlogUri=Uri.parse("http://kuikui.javaeye.com");
returnIt = new Intent(Intent.ACTION_VIEW, myBlogUri);

2，地图
Uri mapUri = Uri.parse("geo:38.899533,-77.036476");
returnIt = new Intent(Intent.ACTION_VIEW, mapUri);

3，调拨打电话界面
Uri telUri = Uri.parse("tel:100861");
returnIt = new Intent(Intent.ACTION_DIAL, telUri);

4，直接拨打电话
Uri callUri = Uri.parse("tel:100861");
returnIt = new Intent(Intent.ACTION_CALL, callUri);

5，卸载
Uri uninstallUri = Uri.fromParts("package", "xxx", null);
returnIt = new Intent(Intent.ACTION_DELETE, uninstallUri);

6，安装
Uri installUri = Uri.fromParts("package", "xxx", null);
returnIt = new Intent(Intent.ACTION_PACKAGE_ADDED, installUri);

7，播放
Uri playUri = Uri.parse("file:///sdcard/download/everything.mp3");
returnIt = new Intent(Intent.ACTION_VIEW, playUri);

8，调用发邮件
Uri emailUri = Uri.parse("mailto:shenrenkui@gmail.com");
returnIt = new Intent(Intent.ACTION_SENDTO, emailUri);

9，直接发邮件
returnIt = new Intent(Intent.ACTION_SEND);
String[] tos = { "shenrenkui@gmail.com" };
String[] ccs = { "shenrenkui@gmail.com" };
returnIt.putExtra(Intent.EXTRA_EMAIL, tos);
returnIt.putExtra(Intent.EXTRA_CC, ccs);
returnIt.putExtra(Intent.EXTRA_TEXT, "body");
returnIt.putExtra(Intent.EXTRA_SUBJECT, "subject");
returnIt.setType("message/rfc882");
Intent.createChooser(returnIt, "Choose Email Client");

10，发短信
Uri smsUri = Uri.parse("tel:100861");
returnIt = new Intent(Intent.ACTION_VIEW, smsUri);
returnIt.putExtra("sms_body", "shenrenkui");
returnIt.setType("vnd.android-dir/mms-sms");

11，直接发短信
Uri smsToUri = Uri.parse("smsto://100861");
returnIt = new Intent(Intent.ACTION_SENDTO, smsToUri);
returnIt.putExtra("sms_body", "shenrenkui");

12，发彩信
Uri mmsUri = Uri.parse("content://media/external/images/media/23");
returnIt = new Intent(Intent.ACTION_SEND);
returnIt.putExtra("sms_body", "shenrenkui");
returnIt.putExtra(Intent.EXTRA_STREAM, mmsUri);
returnIt.setType("image/png");
*/
public class CallSystemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_system);
    }

    public void SysCallWebBrowser(View view) {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://www.baidu.com"));
        startActivity(intent);
    }

    public void SysCallMapApp(View view) {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("geo:38.899533,-77.036476"));
        startActivity(intent);
    }

    public void SysCallPhoneFace(View view) {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:10086"));
        startActivity(intent);
    }

    public void SysCallDirectCallTel(View view) {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:10086"));
        startActivity(intent);
    }

    public void SysCallUninstallApp(View view) {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_DELETE);
        intent.setData(Uri.fromParts("package", "com.android.downloader", null));
        startActivity(intent);
    }

    public void SysCallInstallApp(View view) {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_PACKAGE_ADDED);
        intent.setData(Uri.fromParts("package", "/sdcard/com.android.downloader", null));
        startActivity(intent);
    }

    public void SysCallPlayMedia(View view) {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("file:///system/media/audio/ringtones/BirdLoop.ogg"));
        startActivity(intent);
    }

    public void SysCallOpenMail(View view) {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:neteasy@163.com"));
        startActivity(intent);
    }

    public void SysCallDirectSendMail(View view) {
        Intent intent=new Intent();
        intent = new Intent(Intent.ACTION_SEND);
        String[] tos = { "neteasy@163.com" };
        String[] ccs = { "main@qq.com" };
        intent.putExtra(Intent.EXTRA_EMAIL, tos);
        intent.putExtra(Intent.EXTRA_CC, ccs);
        intent.putExtra(Intent.EXTRA_TEXT, "body");
        intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
        intent.setType("message/rfc882");
        Intent.createChooser(intent, "Choose Email Client");
        startActivity(intent);
    }

    public void SysCallOpenSMS(View view) {
        Intent intent=new Intent();
        Uri smsUri = Uri.parse("tel:10086");
        intent = new Intent(Intent.ACTION_VIEW, smsUri);
        intent.putExtra("sms_body", "10086");
        intent.setType("vnd.android-dir/mms-sms");
        startActivity(intent);
    }

    public void SysCallDirectSendSMS(View view) {
        Intent intent=new Intent();
        Uri smsToUri = Uri.parse("smsto:10086");
        intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", "10086");
        startActivity(intent);
    }
    public void SysCallDirectSendRichSMS(View view) {
        Intent intent=new Intent();
        Uri mmsUri = Uri.parse("content://sdcard/download/0.png");
        intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra("sms_body", "test");
        intent.putExtra(Intent.EXTRA_STREAM, mmsUri);
        intent.setType("image/png");
        startActivity(intent);
    }

    //获取相机信息，返回并显示到ImageView上
    private static final int CODE_CAMERA_CAPTURE=0x1002;
    private ImageView ivCamera;
    public void SysCallCaptureCamera(View view) {
        ivCamera=findViewById(R.id.imageViewCamera);
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,CODE_CAMERA_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch(requestCode)
        {
            case CODE_CAMERA_CAPTURE:
                Bitmap bmp=(Bitmap)data.getExtras().get("data");
                ivCamera.setImageBitmap(bmp);
                break;
        }
    }
}
