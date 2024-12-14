package com.ugex.savelar.onlinewebtv;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    class TvInfo{
        public String name;
        public String link;
        public TvInfo(){}
        public TvInfo(String name,String link){
            this.name=name;
            this.link=link;
        }
        @Override
        public String toString(){
            return name==null?"null":name;
        }
    }
    private WebView wv;
    private Spinner spin;
    private static final List<TvInfo> tvInfos=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitApp();
    }
    private void GetXmlList(Resources res, int id) throws XmlPullParserException, IOException {
        XmlPullParser parser=res.getXml(id);
        int envent=parser.getEventType();
        while(envent!=XmlPullParser.END_DOCUMENT){
            switch (envent)
            {
                case XmlPullParser.START_DOCUMENT:
                    tvInfos.clear();
                    break;
                case XmlPullParser.START_TAG:
                    if(parser.getName().equals("page")) {
                        tvInfos.add(new TvInfo(
                                parser.getAttributeValue(null,"name"),
                                parser.getAttributeValue(null,"link")
                                )
                        );
                    }
                    break;
            }
            envent=parser.next();
        }
    }
    private void InitApp(){
        try {
            GetXmlList(MainActivity.this.getResources(),R.xml.page_list);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        spin=findViewById(R.id.spinnerTVName);
        ArrayAdapter<TvInfo> adapter=new ArrayAdapter<TvInfo>(this,android.R.layout.simple_spinner_item,tvInfos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        spin .setAdapter(adapter);
        spin.setSelection(0,true);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                wv.loadUrl(tvInfos.get(position).link);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        wv=findViewById(R.id.webviewShow);
        wv.getSettings().setDefaultTextEncodingName("utf-8");

        //自动缩放
        wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setSupportZoom(true);
        //电脑Web端标识
        //wv.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36");
        //自适应屏幕
        wv.getSettings().setUseWideViewPort(true);
        wv.getSettings().setLoadWithOverviewMode(true);
        //支持获取手势焦点
        wv.requestFocusFromTouch();
        //支持JS
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        //加载页面
        wv.loadUrl(tvInfos.get(0).link);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && wv.canGoBack()) {
            wv.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
