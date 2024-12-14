package com.ugex.savelar.ajxmljsonread;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ugex.savelar.ajxmljsonread.XMLSerializerTest.XMLSerializerAndParseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int READ_XML_END=0x0001;
    private static final int READ_JSON_END=0x0002;
    private Button btnXml;
    private TextView tvXml;
    private Button btnJson;
    private TextView tvJson;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==READ_XML_END) {
                tvXml.setText("Xml:\n");
                for (String str:(List<String>)msg.obj) {
                    tvXml.append(str+"\n");
                }

            }
            if(msg.what==READ_JSON_END){
                tvJson.setText("Json:\n");
                for (String str:(List<String>)msg.obj) {
                    tvJson.append(str+"\n");
                }
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitApp();
    }

    private void InitApp() {
        btnXml=findViewById(R.id.buttonXml);
        tvXml=findViewById(R.id.textViewXml);
        btnJson=findViewById(R.id.buttonJson);
        tvJson=findViewById(R.id.textViewJson);
        btnXml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            List<String> ret=GetXmlList(MainActivity.this.getResources(),R.xml.resxml);
                            Message msg=new Message();
                            msg.what=READ_XML_END;
                            msg.obj=ret;
                            handler.sendMessage(msg);
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        btnJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        List<String> ret=GetJsonData(MainActivity.this.getResources(),R.raw.resjson);
                        Message msg=new Message();
                        msg.what=READ_JSON_END;
                        msg.obj=ret;
                        handler.sendMessage(msg);
                    }
                }.start();
            }
        });
    }
    private List<String> GetXmlList(Resources res,int id) throws XmlPullParserException, IOException {
        List<String> ret=null;
        XmlPullParser parser=res.getXml(id);
        int envent=parser.getEventType();
        while(envent!=XmlPullParser.END_DOCUMENT){
            switch (envent)
            {
                case XmlPullParser.START_DOCUMENT:
                    ret=new ArrayList<String>();
                    break;
                case XmlPullParser.START_TAG:
                    if(parser.getName().equals("hero")) {
                        ret.add(parser.getAttributeValue(0));
                    }
                    break;
            }
            envent=parser.next();
        }
        return ret;
    }
    private List<String> GetJsonData(Resources res,int id){
        List<String> ret=new ArrayList<>();
        InputStream is=res.openRawResource(id);
        InputStreamReader ir=new InputStreamReader(is);
        BufferedReader br=new BufferedReader(ir);
        String content="";
        String line="";
        try {
            while((line=br.readLine())!=null) {
                content += line;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JSONObject obj=new JSONObject(content);
            String buffer=obj.getString("name");
            ret.add("name:"+buffer);
            buffer=obj.getString("attri");
            ret.add("attri:"+buffer);
            JSONObject sobj=obj.getJSONObject("jineng");
            buffer=sobj.getString("one");
            ret.add("jineng-one:"+buffer);
            buffer=sobj.getString("two");
            ret.add("jineng-otwo:"+buffer);
            buffer=sobj.getString("three");
            ret.add("jineng-othree:"+buffer);
            JSONArray jarr=obj.getJSONArray("size");
            for(int i=0;i<jarr.length();i++){
                ret.add("size"+i+":"+jarr.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ret;
    }

    //XML的序列化写入和PULL解析
    public void onBtnXmlSerializerParsePullClicked(View view) {
        startActivity(new Intent(this, XMLSerializerAndParseActivity.class));
    }
}
