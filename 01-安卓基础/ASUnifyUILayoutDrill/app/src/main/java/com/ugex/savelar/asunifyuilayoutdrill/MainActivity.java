package com.ugex.savelar.asunifyuilayoutdrill;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowInsets;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private TabHost tabHost;

    private GridView guideFaceGDV;
    List<GuideGDVEntity> guideFaceData;


    private ListView msgFaceLSV;
    List<MsgLSVEntity> msgFaceData;

    private Gallery imgFaceGLY;
    List<ImgGLYEntity> imgFaceData;

    private ExpandableListView setFaceEXLSV;
    List<SetEXLSVEntity> setFaceData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitApp();
    }

    private void InitApp() {
        InitTabHost();
        InitGuideFace();
        InitMsgFace();
        InitImgFace();
        InitSetFace();
    }

    private void InitSetFace() {

        int[] ImgsIDEXLSV={R.drawable.ic_launcher_foreground,R.drawable.ic_launcher_background,R.drawable.ic_launcher_foreground,R.drawable.ic_launcher_background,R.drawable.ic_launcher_foreground};
        String[] GroupTextsEXLSV={"SetGroup1","SetGroup2","SetGroup3","SetGroup4","SetGroup5"};
        String[][] ChildensTextsEXLSV={
                {"1-child1","1-child2","1-child3"},
                {"2-child1","2-child2","2-child3"},
                {"3-child1","3-child2"},
                {"4-child1","4-child2","4-child3","4-child4"},
                {"5-child1"},
        };

        setFaceData=new ArrayList<>();
        for(int i=0;i<ImgsIDEXLSV.length;i++)
        {
            List<String> childen=new ArrayList<>();
            for(int j=0;j<ChildensTextsEXLSV[i].length;j++){
                childen.add(ChildensTextsEXLSV[i][j]);
            }
            setFaceData.add(new SetEXLSVEntity(ImgsIDEXLSV[i],GroupTextsEXLSV[i],childen));
        }

        setFaceEXLSV=findViewById(R.id.FaceSet);
        setFaceEXLSV.setAdapter(new SetEXLSVAdapter(MainActivity.this,setFaceData));
        setFaceEXLSV.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(MainActivity.this, setFaceData.get(groupPosition).GroupString+"==>"+setFaceData.get(groupPosition).ChildensString.get(childPosition), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void InitImgFace() {
        int[] ImgsIDGLY={R.drawable.ic_launcher_foreground,R.drawable.ic_launcher_background,R.drawable.ic_launcher_foreground,R.drawable.ic_launcher_background,R.drawable.ic_launcher_foreground};
        imgFaceData=new ArrayList<>();
        for(int i=0;i<ImgsIDGLY.length;i++)
        {
            imgFaceData.add(new ImgGLYEntity(ImgsIDGLY[i]));
        }

        imgFaceGLY=findViewById(R.id.FaceImg);
        imgFaceGLY.setAdapter(new ImgGLYAdapter(MainActivity.this,imgFaceData));
        imgFaceGLY.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, imgFaceData.get(position).ImgID+"", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void InitMsgFace() {

        int[] ImgsIDLSV={R.drawable.ic_launcher_foreground,R.drawable.ic_launcher_background,R.drawable.ic_launcher_foreground,R.drawable.ic_launcher_background,R.drawable.ic_launcher_foreground};
        String[] LargeTextsLSV={"MsgTitle1","MsgTitle2","MsgTitle3","MsgTitle4","MsgTitle5"};
        String[] SmallTextLSV={"MsgDetail1","MsgDetail2","MsgDetail3","MsgDetail4","MsgDetail5"};

        msgFaceData=new ArrayList<>();
        for(int i=0;i<ImgsIDLSV.length;i++)
        {
            msgFaceData.add(new MsgLSVEntity(ImgsIDLSV[i],LargeTextsLSV[i],SmallTextLSV[i]));
        }
        msgFaceLSV=findViewById(R.id.FaceMsg);
        msgFaceLSV.setAdapter(new MsgLSVAdapter(MainActivity.this,msgFaceData));
        msgFaceLSV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, msgFaceData.get(position).LargeText+"\nDetail:"+msgFaceData.get(position).SmallText, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void InitGuideFace() {

         int[] ImgsIDGDV={R.drawable.ic_launcher_foreground,R.drawable.ic_launcher_background,R.drawable.ic_launcher_foreground,R.drawable.ic_launcher_background,R.drawable.ic_launcher_foreground};
         String[] DescripsGDV={"Guide1","Guide2","Guide3","Guide4","Guide5"};


         guideFaceData=new ArrayList<>();
        for(int i=0;i<ImgsIDGDV.length;i++)
        {
            guideFaceData.add(new GuideGDVEntity(ImgsIDGDV[i],DescripsGDV[i]));
        }
        guideFaceGDV=findViewById(R.id.FaceGuide);
        guideFaceGDV.setAdapter(new GuideGDVAdapter(MainActivity.this,guideFaceData));
        guideFaceGDV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, guideFaceData.get(position).Descript, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void InitTabHost() {
        tabHost=findViewById(R.id.MainTabHost);
        tabHost.setup();
        Resources res=getResources();
        View tabBarGuide= LayoutInflater.from(this).inflate(R.layout.tab_view_bar,null);
        ImageView iv=tabBarGuide.findViewById(R.id.imageViewPhoto);
        TextView tv=tabBarGuide.findViewById(R.id.textView);
        iv.setImageDrawable(res.getDrawable(R.drawable.TabWeigetItemGuideImg));
        tv.setText(res.getText(R.string.tab_bar_guide));
        tabHost.addTab(tabHost.newTabSpec("tabGuide").setIndicator(tabBarGuide).setContent(R.id.FaceGuide));

        View tabBarMsg=LayoutInflater.from(this).inflate(R.layout.tab_view_bar,null);
        iv=tabBarMsg.findViewById(R.id.imageViewPhoto);
        tv=tabBarMsg.findViewById(R.id.textView);
        iv.setImageDrawable(res.getDrawable(R.drawable.TabWeigetItemMsgImg));
        tv.setText(res.getText(R.string.tab_bar_msg));
        tabHost.addTab(tabHost.newTabSpec("tabMsg").setIndicator(tabBarMsg).setContent(R.id.FaceMsg));

        View tabBarImg=LayoutInflater.from(this).inflate(R.layout.tab_view_bar,null);
        iv=tabBarImg.findViewById(R.id.imageViewPhoto);
        tv=tabBarImg.findViewById(R.id.textView);
        iv.setImageDrawable(res.getDrawable(R.drawable.TabWeigetItemImgImg));
        tv.setText(res.getText(R.string.tab_bar_img));
        tabHost.addTab(tabHost.newTabSpec("tabImg").setIndicator(tabBarImg).setContent(R.id.FaceImg));

        View tabBarSet=LayoutInflater.from(this).inflate(R.layout.tab_view_bar,null);
        iv=tabBarSet.findViewById(R.id.imageViewPhoto);
        tv=tabBarSet.findViewById(R.id.textView);
        iv.setImageDrawable(res.getDrawable(R.drawable.TabWeigetItemSetImg));
        tv.setText(res.getText(R.string.tab_bar_set));
        tabHost.addTab(tabHost.newTabSpec("tabSet").setIndicator(tabBarSet).setContent(R.id.FaceSet));

        setContentView(tabHost);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                Toast.makeText(MainActivity.this, tabId, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
