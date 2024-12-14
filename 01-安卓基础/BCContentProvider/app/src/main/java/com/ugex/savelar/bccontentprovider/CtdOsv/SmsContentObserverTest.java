package com.ugex.savelar.bccontentprovider.CtdOsv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.ugex.savelar.bccontentprovider.R;
/**
 * ContentObserver内容观察者
 * 用来观察指定Uri代表的数据是否发生变化
 * 如果发生变化，就会触发ContentObserver.onChange()方法
 * 可以在这个触发方法中获取到变化的数据
 *
 * 需要在ContentResolver注册ContentObserver
 * ContentResolver.registerContentObserver(uri,true,ContentObserver)
 * 这一句话，就将ContentProvider,ContentResolver,ContentObserver关系到了一起
 *
 * 原理：
 * 一个应用提供了ContentProvider来提供数据
 * 当其他应用使用了ContentResolver进行修改数据之后
 * 便会自动调用到ContentResolver.notifyChange()方法
 * 此时，系统就会触发ContentObserver.onChange()来通知对应的观察者
 */

/**
 * 因此，这里就使用这个原理，来进行接受短信
 */

public class SmsContentObserverTest extends AppCompatActivity {
    private TextView tvAddr;
    private TextView tvBody;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_content_observer_test);

        InitActivity();
    }

    private void InitActivity() {
        tvAddr=findViewById(R.id.textViewOsvAddr);
        tvBody=findViewById(R.id.textViewOsvBody);
        ContentResolver resolver=getContentResolver();
        Uri uri=Uri.parse("content://sms/");
        //参数:uri,是否独立通知，Observer
        resolver.registerContentObserver(uri,true,new MySmsObserver(new Handler()));
    }

    class MySmsObserver extends ContentObserver{

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public MySmsObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);

            Uri uri=Uri.parse("content://sms/");
            ContentResolver cr=getContentResolver();

            Cursor cur=cr.query(uri,new String[]{"type","date","body","address"},null,null,null);
            //由于数据库中保存的数据，就是按照时间排序的，因此，第一条就是新短信
            if(cur.moveToFirst()){
                String addr=cur.getString(cur.getColumnIndex("address"));
                String body=cur.getString(cur.getColumnIndex("body"));
                String smsStr="新短信：来自："+addr+" 内容："+body;
                Toast.makeText(SmsContentObserverTest.this, smsStr, Toast.LENGTH_SHORT).show();

                tvAddr.setText(addr);
                tvBody.setText(body);
            }
            cur.close();
        }
    }

}

