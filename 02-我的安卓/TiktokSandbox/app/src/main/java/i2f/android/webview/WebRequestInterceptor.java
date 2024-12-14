package i2f.android.webview;

import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

public interface WebRequestInterceptor {
    WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request);
}
