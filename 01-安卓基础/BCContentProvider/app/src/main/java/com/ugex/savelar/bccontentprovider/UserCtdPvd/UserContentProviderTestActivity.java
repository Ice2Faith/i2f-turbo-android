package com.ugex.savelar.bccontentprovider.UserCtdPvd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.ugex.savelar.bccontentprovider.R;
/*
* 自定义ContentProvider
*   继承ContentProvider
*   实现抽象方法
*       onCreate(),getType(),query(),insert(),update(),delete()等
*   AndroidMainifest.xml中注册
*       <provider android:name=".MyProvider"
*       android:authorities="ugex.provider.contentprovider"
*       android:exported="true"/>
*
* UriMatcher
* */
public class UserContentProviderTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_content_provider_test);

        InitActivity();
    }

    private void InitActivity() {

    }
    public static final String MY_CONTENTPROVIDER_URL="ugex.provider.contentprovider";
    public void onUserContentProviderTestDeleteClicked(View view) {
        ContentResolver cr=getContentResolver();
        //也就是在注册的authorities属性前面加上一个协议：content://
        //就算是在其他程序中，也是给出URI即可
        cr.delete(Uri.parse("content://"+MY_CONTENTPROVIDER_URL),null,null);

    }
}
