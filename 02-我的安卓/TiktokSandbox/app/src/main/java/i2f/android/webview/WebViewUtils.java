package i2f.android.webview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import i2f.android.webview.core.FileChooserSupportWebChromeClient;
import i2f.android.webview.core.FileSupportReceiverDownloadListener;

public class WebViewUtils {
    public static boolean onKeyDownBack(WebView webView, int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();// 返回前一个页面
            return true;
        }
        return false;
    }
    public static FileChooserSupportWebChromeClient initWebView(WebView webView, Activity activity){
        return initWebView(webView,activity,null);
    }
    public static FileChooserSupportWebChromeClient initWebView(WebView webView, Activity activity, final WebRequestInterceptor interceptor){
        WebSettings webSettings = webView.getSettings();

        //支持JS
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        //设置不使用缓存:LOAD_NO_CACHE
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        //设置默认字符编码
        webSettings.setDefaultTextEncodingName("utf-8");

        //自动缩放
        webSettings.setBuiltInZoomControls(true);

        //不支持缩放，仅适用于WebApp套壳时设置
        webSettings.setSupportZoom(true);

        //电脑Web端标识
        webSettings.setUserAgentString("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36");

        //自适应屏幕
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        //DOM的存储支持，localstorage
        webSettings.setDomStorageEnabled(true);

        FileChooserSupportWebChromeClient webChromeClient=new FileChooserSupportWebChromeClient(activity);
        webView.setWebChromeClient(webChromeClient);
        //支持获取手势焦点
        webView.requestFocusFromTouch();

        webView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                if(interceptor!=null){
                    return interceptor.shouldInterceptRequest(view,request);
                }else{
                    return shouldInterceptRequest(view, request.getUrl().toString());
                }
            }


        });

        FileSupportReceiverDownloadListener listener=new FileSupportReceiverDownloadListener(activity);

        webView.setDownloadListener(listener);

        return webChromeClient;
    }

    public static boolean onActivityResultFileChooser(FileChooserSupportWebChromeClient webChromeClient,
                                                      int requestCode, int resultCode, Intent intent){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            if (requestCode == FileChooserSupportWebChromeClient.REQUEST_SELECT_FILE)
            {
                if (webChromeClient.uploadMessage == null)
                    return true;
                webChromeClient.uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                webChromeClient.uploadMessage = null;
                return true;
            }
        }
        else if (requestCode == FileChooserSupportWebChromeClient.FILECHOOSER_RESULTCODE)
        {
            if (null == webChromeClient.mUploadMessage)
                return true;
            // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
            // Use RESULT_OK only if you're implementing WebView inside an Activity32
            Uri result = intent == null || resultCode != Activity.RESULT_OK ? null : intent.getData();
            webChromeClient.mUploadMessage.onReceiveValue(result);
            webChromeClient.mUploadMessage = null;
            return true;
        }
        return false;
    }
}
