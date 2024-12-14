package com.i2f.tiktok.sandbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import i2f.android.permission.PermissionUtil;
import i2f.android.webview.WebRequestInterceptor;
import i2f.android.webview.WebViewUtils;
import i2f.android.webview.core.FileChooserSupportWebChromeClient;

public class MainActivity extends Activity {

    private EditText edtWebUrl;
    private Button btnLoadUrl;
    private WebView webViewMain;
    private FileChooserSupportWebChromeClient client;

    private WebRequestInterceptor webRequestInterceptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initApp();
    }

    private void initApp() {
        PermissionUtil.requireExternalReadWriteManagePermission(this);
        bindComponents();
    }

    private void bindComponents() {
        this.edtWebUrl = findViewById(R.id.edtWebUrl);
        this.btnLoadUrl = findViewById(R.id.btnLoadUrl);
        this.webViewMain = findViewById(R.id.webViewMain);

        client = WebViewUtils.initWebView(webViewMain, this, webRequestInterceptor);

        String url = "https://www.douyin.com/";
        loadUrl(url);
    }

    private void loadUrl(String url) {
        this.edtWebUrl.setText(url);
        this.webViewMain.loadUrl(this.edtWebUrl.getText().toString());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (WebViewUtils.onKeyDownBack(webViewMain, keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onExternalReadWritePermissionResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PermissionUtil.onExternalManagePermissionActivityResult(this, requestCode, resultCode, data);
        boolean hasProcessed = WebViewUtils.onActivityResultFileChooser(client, requestCode, resultCode, data);
    }

    public void onBtnLoadUrlClicked(View view) {
        String url = this.edtWebUrl.getText().toString();
        loadUrl(url);
    }
}
