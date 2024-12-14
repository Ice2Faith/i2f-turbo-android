package com.ugex.savelar.ajxmljsonread.XMLSerializerTest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.ugex.savelar.ajxmljsonread.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class XMLSerializerAndParseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xmlserializer_and_parse);

        InitActivity();
    }

    private void InitActivity() {
        spinnerProvice=findViewById(R.id.spinnerProvince);
        spinnerArea=findViewById(R.id.spinnerArea);
        spinnerCity=findViewById(R.id.spinnerCity);
    }

    //XML序列化到文件
    /*其实过程是和自己手写XML文件过程是一样的，一个标志是成对的开始和结束的
    * 首先：文档的标志,说明文档的格式为XML和文档的编码格式，一般用utf-8
    *   使用：serializer.startDocument()和对应的serializer.endDocument();
    * 然后就可以添加标志了
    *   使用：serializer.startTag()和对应的serializer.endTag()，参数都为namespace,tagName
    * 在标志中添加数据,也就是开始结束标记中的内容
    *   使用：serializer.text(),参数为String
    * 也可以设置属性,(注意，设置属性必须在设置内容之前，也就是在serializer.text()之前，否则将会发生错误)
    *   使用：serializer.attribute(),参数：namespace,属性名，属性值
    *
    * 当然，你过你喜欢，可以直接进行流的操作，直接进行写入流，而不通过XmlSerializer
    * */
    public void onBtnXmlSerializeToFileClicked(View view) {
        Student stu=new Student("Jelly",22,89);
        XmlSerializer serializer= Xml.newSerializer();

        try {
            //设置输出流和编码，这是必要的
            serializer.setOutput(
                    new FileOutputStream(new File(Environment.getExternalStorageDirectory(),"serialize.xml")),
                    "utf-8");
            //开始XML文本的文本标志
            serializer.startDocument("utf-8",true);
            //开始一个标志
            /*
            * 最后形成的标志内容如下：
            * <?xml version="1.0" encoding="utf-8"?>
            * <Student>
            *     <name age="22">Jelly</name>
            *     <age>22</age>
            *     <score>89</score>
            * </Student>
            *
            * 结合最后的结果看代码，就很清晰了
            * */
            serializer.startTag(null,"Student");

            serializer.startTag(null,"name");
            serializer.attribute(null,"age",stu.age+"");
            serializer.text(stu.name);
            serializer.endTag(null,"name");

            serializer.startTag(null,"age");
            serializer.text(stu.age+"");
            serializer.endTag(null,"age");

            serializer.startTag(null,"score");
            serializer.text(stu.score+"");
            serializer.endTag(null,"score");

            serializer.endTag(null,"Student");

            serializer.endDocument();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //XML文件的PULL解析
    private Spinner spinnerProvice;
    private Spinner spinnerCity;
    private Spinner spinnerArea;
    public void onBtnXmlPullParseFromFileClicked(View view) {
        AreaInfo root=parseAreaInfo(getResources(),R.xml.nations);

        MyBaseAdapter adapterRoot=new MyBaseAdapter(root);
        spinnerProvice.setAdapter(adapterRoot);
        spinnerProvice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AreaInfo info=(AreaInfo) spinnerProvice.getSelectedItem();
                MyBaseAdapter adapterCity=new MyBaseAdapter(info);
                spinnerCity.setAdapter(adapterCity);
                adapterCity.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AreaInfo info=(AreaInfo) spinnerCity.getSelectedItem();
                MyBaseAdapter adapterArea=new MyBaseAdapter(info);
                spinnerArea.setAdapter(adapterArea);
                adapterArea.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    class MyBaseAdapter extends BaseAdapter{
        private AreaInfo infos;
        public MyBaseAdapter(AreaInfo info){
            this.infos=info;
        }
        public void setAreaInfo(AreaInfo info){
            this.infos=info;
        }
        @Override
        public int getCount() {
            return infos.subAreas.size();
        }

        @Override
        public Object getItem(int position) {
            return infos.subAreas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv=new TextView(XMLSerializerAndParseActivity.this);
            tv.setText(infos.subAreas.get(position).name);
            return tv;
        }
    }
    private AreaInfo parseAreaInfo(Resources res,int xmlId){
        AreaInfo ret=null;
        XmlPullParser parser=res.getXml(xmlId);
        int envent= 0;
        try {
            envent = parser.getEventType();
            AreaInfo provience=null;
            AreaInfo city=null;
            AreaInfo area=null;
            while(envent!=XmlPullParser.END_DOCUMENT){
                switch (envent)
                {
                    case XmlPullParser.START_DOCUMENT:
                        ret=new AreaInfo();
                        break;
                    case XmlPullParser.START_TAG:
                        if(parser.getName().equals("root")) {
                            ret.name=parser.getAttributeValue(null,"name");
                            ret.index="0";
                        }else if(parser.getName().equals("province")){
                            provience=new AreaInfo(
                                    parser.getAttributeValue(null,"name"),
                                    "0"
                            );
                        }else if(parser.getName().equals("city")){
                            city=new AreaInfo(
                                    parser.getAttributeValue(null,"name"),
                                    parser.getAttributeValue(null,"index")
                                    );
                        }else if(parser.getName().equals("area")){
                            area=new AreaInfo(
                                    parser.getAttributeValue(null,"name"),
                                    parser.getAttributeValue(null,"index")
                                    );
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("province")){
                            ret.subAreas.add(provience);
                            provience=null;
                        }else if(parser.getName().equals("city")){
                            provience.subAreas.add(city);
                            city=null;
                        }else if(parser.getName().equals("area")){
                            city.subAreas.add(area);
                            area=null;
                        }
                        break;
                }
                envent=parser.next();
            }
        } catch (XmlPullParserException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

}
