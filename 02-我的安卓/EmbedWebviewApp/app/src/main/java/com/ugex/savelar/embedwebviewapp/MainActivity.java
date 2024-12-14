package com.ugex.savelar.embedwebviewapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;

import i2f.web.app.WebViewUtils;
import i2f.web.app.core.FileChooserSupportWebChromeClient;

public class MainActivity extends Activity {

    private WebView webView;
    private FileChooserSupportWebChromeClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView=findViewById(R.id.webViewMain);
        client=WebViewUtils.initWebView(webView,this);

        webView.loadUrl("http://39.105.33.11:8080");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(WebViewUtils.onKeyDownBack(webView,keyCode,event)){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        boolean hasProcessed=WebViewUtils.onActivityResultFileChooser(client,requestCode,resultCode,data);
    }
}
