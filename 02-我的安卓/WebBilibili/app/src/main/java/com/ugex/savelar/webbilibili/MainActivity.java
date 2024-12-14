package com.ugex.savelar.webbilibili;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.EditText;

import i2f.web.app.WebViewUtils;
import i2f.web.app.core.FileChooserSupportWebChromeClient;


public class MainActivity extends Activity {

    private WebView webViewMain;

    private EditText edtLoadUrl;

    private FileChooserSupportWebChromeClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtLoadUrl=findViewById(R.id.editTextUrl);
        webViewMain=findViewById(R.id.webViewMain);
        client= WebViewUtils.initWebView(webViewMain,this);

        String url=loadLastUrl();
        if(url==null){
            url="https://www.bilibili.com/";
           //url="http://39.104.81.124:8080";
        }
        edtLoadUrl.setText(url);
        //加载页面
        webViewMain.loadUrl(url);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (WebViewUtils.onKeyDownBack(webViewMain,keyCode,event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void OnClickLoadUrlButton(View view) {
        String url=edtLoadUrl.getText().toString().trim();
        if("".equals(url)){
            return;
        }
        saveLastUrl(url);
        webViewMain.loadUrl(url);
    }

    public void saveLastUrl(String url){
        SharedPreferences sp=this.getSharedPreferences("last_settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("lastUrl",url);
        editor.commit();
    }
    public String loadLastUrl(){
        SharedPreferences sp=this.getSharedPreferences("last_settings", Context.MODE_PRIVATE);
        String ret=sp.getString("lastUrl",null);
        return ret;
    }
    /////////////////////////////////////////////
    public static ValueCallback<Uri> mUploadMessage;
    public static ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    public static final int FILECHOOSER_RESULTCODE = 2;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        boolean hasProcess= WebViewUtils.onActivityResultFileChooser(client,requestCode,resultCode,intent);
    }
}
