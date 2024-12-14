package com.ugex.savelar.excompositedesign;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.ugex.savelar.excompositedesign.Util.SRHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private TabHost tabHost;
    Resources res;

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
        res=getResources();
        InitTabHost();
        InitGuideFace();
        InitMsgFace();
        InitImgFace();
        InitSetFace();
    }

    private void InitSetFace() {

        int[] ImgsIDEXLSV={R.drawable.user1,R.drawable.user2,R.drawable.user3,R.drawable.user4,R.drawable.set};
        String[] GroupTextsEXLSV=res.getStringArray(R.array.setting_items);
        String[][] ChildensTextsEXLSV={
                res.getStringArray(R.array.set_net),
                res.getStringArray(R.array.set_account),
                res.getStringArray(R.array.set_display),
                res.getStringArray(R.array.set_about),
                res.getStringArray(R.array.set_set),
        };

        setFaceData=new ArrayList<SetEXLSVEntity>();
        for(int i=0;i<ImgsIDEXLSV.length;i++)
        {
            List<String> childen=new ArrayList<String>();
            for(int j=0;j<ChildensTextsEXLSV[i].length;j++){
                childen.add(ChildensTextsEXLSV[i][j]);
            }
            setFaceData.add(new SetEXLSVEntity(ImgsIDEXLSV[i],GroupTextsEXLSV[i],childen));
        }

        setFaceEXLSV=(ExpandableListView) findViewById(R.id.FaceSet);
        setFaceEXLSV.setAdapter(new SetEXLSVAdapter(MainActivity.this,setFaceData));
        setFaceEXLSV.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(MainActivity.this, setFaceData.get(groupPosition).GroupString+"==>"+setFaceData.get(groupPosition).ChildensString.get(childPosition), Toast.LENGTH_SHORT).show();
                if(groupPosition==4 && childPosition==0){
                    SRHelper.putDataToSharedPreference(MainActivity.this,
                            res.getString(R.string.logininfo_sharedprefe_name),
                            "account",
                            "");
                    Toast.makeText(MainActivity.this, res.getString(R.string.unlogin_success), Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    private void InitImgFace() {
        int[] ImgsIDGLY={R.drawable.user1,R.drawable.user2,R.drawable.user3,R.drawable.user4,R.drawable.user5};
        imgFaceData=new ArrayList<ImgGLYEntity>();
        for(int i=0;i<ImgsIDGLY.length;i++)
        {
            imgFaceData.add(new ImgGLYEntity(ImgsIDGLY[i]));
        }

        imgFaceGLY=(Gallery) findViewById(R.id.FaceImg);
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

        int[] ImgsIDLSV={R.drawable.user1,R.drawable.user2,R.drawable.user3,R.drawable.user4,R.drawable.user5};
        String[] LargeTextsLSV=res.getStringArray(R.array.user_names);
        String[] SmallTextLSV=res.getStringArray(R.array.user_msgs);

        msgFaceData=new ArrayList<MsgLSVEntity>();
        for(int i=0;i<ImgsIDLSV.length;i++)
        {
            msgFaceData.add(new MsgLSVEntity(ImgsIDLSV[i],LargeTextsLSV[i],SmallTextLSV[i]));
        }
        msgFaceLSV=(ListView) findViewById(R.id.FaceMsg);
        msgFaceLSV.setAdapter(new MsgLSVAdapter(MainActivity.this,msgFaceData));
        msgFaceLSV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, msgFaceData.get(position).LargeText+"\nDetail:"+msgFaceData.get(position).SmallText, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void InitGuideFace() {

        int[] ImgsIDGDV={R.drawable.user1,R.drawable.user2,R.drawable.user3,R.drawable.user4,R.drawable.user5};
        String[] DescripsGDV=res.getStringArray(R.array.user_names);


        guideFaceData=new ArrayList<GuideGDVEntity>();
        for(int i=0;i<ImgsIDGDV.length;i++)
        {
            guideFaceData.add(new GuideGDVEntity(ImgsIDGDV[i],DescripsGDV[i]));
        }
        guideFaceGDV=(GridView) findViewById(R.id.FaceGuide);
        guideFaceGDV.setAdapter(new GuideGDVAdapter(MainActivity.this,guideFaceData));
        guideFaceGDV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, guideFaceData.get(position).Descript, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void InitTabHost() {
        tabHost=(TabHost) findViewById(R.id.MainTabHost);
        tabHost.setup();

        View tabBarGuide= LayoutInflater.from(this).inflate(R.layout.tab_view_bar,null);
        ImageView iv=(ImageView) tabBarGuide.findViewById(R.id.imageViewPhoto);
        TextView tv=(TextView) tabBarGuide.findViewById(R.id.textView);
        iv.setImageDrawable(res.getDrawable(R.drawable.TabWeigetItemGuideImg));
        tv.setText(res.getText(R.string.tab_bar_guide));
        tabHost.addTab(tabHost.newTabSpec("tabGuide").setIndicator(tabBarGuide).setContent(R.id.FaceGuide));

        View tabBarMsg=LayoutInflater.from(this).inflate(R.layout.tab_view_bar,null);
        iv=(ImageView) tabBarMsg.findViewById(R.id.imageViewPhoto);
        tv=(TextView) tabBarMsg.findViewById(R.id.textView);
        iv.setImageDrawable(res.getDrawable(R.drawable.TabWeigetItemMsgImg));
        tv.setText(res.getText(R.string.tab_bar_msg));
        tabHost.addTab(tabHost.newTabSpec("tabMsg").setIndicator(tabBarMsg).setContent(R.id.FaceMsg));

        View tabBarImg=LayoutInflater.from(this).inflate(R.layout.tab_view_bar,null);
        iv=(ImageView) tabBarImg.findViewById(R.id.imageViewPhoto);
        tv=(TextView) tabBarImg.findViewById(R.id.textView);
        iv.setImageDrawable(res.getDrawable(R.drawable.TabWeigetItemImgImg));
        tv.setText(res.getText(R.string.tab_bar_img));
        tabHost.addTab(tabHost.newTabSpec("tabImg").setIndicator(tabBarImg).setContent(R.id.FaceImg));

        View tabBarSet=LayoutInflater.from(this).inflate(R.layout.tab_view_bar,null);
        iv=(ImageView) tabBarSet.findViewById(R.id.imageViewPhoto);
        tv=(TextView) tabBarSet.findViewById(R.id.textView);
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
    public void onBtnAnimatePlayEggsClicked(View view){
        startActivity(new Intent(this,EggsActivity.class));
    }
}
