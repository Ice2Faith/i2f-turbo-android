package com.demo.classroom.Template;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.demo.classroom.R;
import com.demo.classroom.Util.ParserRegionInfo;
import com.demo.classroom.Util.RegionInfo;
import com.demo.classroom.Util.RegionSpinnerAdapter;
import com.demo.classroom.Util.UtilHelper;

import androidx.annotation.Nullable;

public class LayoutAddress extends LinearLayout {
    private Context context;
    private Spinner spnProvince;
    private Spinner spnCity;
    private Spinner spnRegion;
    private EditText edtDetailAddr;

    private RegionInfo rootRegion;
    private RegionSpinnerAdapter provinceAdapter;
    private RegionSpinnerAdapter cityAdapter;
    private RegionSpinnerAdapter regionAdaper;

    public LayoutAddress(Context context) {
        super(context);
        InitLayout(context);
    }

    public LayoutAddress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        InitLayout(context);
    }

    private void InitLayout(Context ctx) {
        this.context=ctx;
        LayoutInflater.from(context).inflate(R.layout.layout_address,this);

        spnProvince=findViewById(R.id.spinnerProvince);
        spnCity=findViewById(R.id.spinnerCity);
        spnRegion=findViewById(R.id.spinnerRegion);
        edtDetailAddr=findViewById(R.id.editTextDetailAddr);

        rootRegion= ParserRegionInfo.parserRegion(getResources(),R.xml.nations);

        provinceAdapter=new RegionSpinnerAdapter(context,rootRegion);
        spnProvince.setAdapter(provinceAdapter);

        cityAdapter=new RegionSpinnerAdapter(context,(RegionInfo) provinceAdapter.getItem(0));
        spnCity.setAdapter(cityAdapter);

        regionAdaper=new RegionSpinnerAdapter(context,(RegionInfo) cityAdapter.getItem(0));
        spnRegion.setAdapter(regionAdaper);

        spnProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                cityAdapter=new RegionSpinnerAdapter(context,(RegionInfo) spnProvince.getSelectedItem());
                spnCity.setAdapter(cityAdapter);
                cityAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spnCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                regionAdaper=new RegionSpinnerAdapter(context,(RegionInfo) spnCity.getSelectedItem());
                spnRegion.setAdapter(regionAdaper);
                regionAdaper.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void setAddress(String address,String split){
        adjustRegionSpinneresToCorrect(address,split);
    }

    public String getAddress(String split){
        String address=((RegionInfo)spnProvince.getSelectedItem()).name+split
                +((RegionInfo)spnCity.getSelectedItem()).name+split
                +((RegionInfo)spnRegion.getSelectedItem()).name+split
                +edtDetailAddr.getText().toString().trim();
        return address;
    }

    private static final int MSG_WHAT_SPINNER_PROVINCE=1;
    private static final int MSG_WHAT_SPINNER_CITY=2;
    private static final int MSG_WHAT_SPINNER_AREA=3;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what== MSG_WHAT_SPINNER_PROVINCE)
                SetSpinnerSelect(spnProvince,msg.arg1);
            else if(msg.what== MSG_WHAT_SPINNER_CITY)
                SetSpinnerSelect(spnCity,msg.arg1);
            else if(msg.what== MSG_WHAT_SPINNER_AREA)
                SetSpinnerSelect(spnRegion,msg.arg1);
            super.handleMessage(msg);
        }
    };
    public void SetSpinnerSelect(Spinner spn,int i){
        spn.setSelection(i);
    }
    private void adjustRegionSpinneresToCorrect(String address,String split) {
        if(UtilHelper.isEmptyString(address))
            return;
        final String[] addInfos =address.split(split,4);
        if(addInfos.length>=4) {
            String detailAdd="";
            for(int i=4-1;i<addInfos.length;i++){
                detailAdd=detailAdd+addInfos[i];
            }
            edtDetailAddr.setText(detailAdd);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(addInfos.length>=1) {
                    for (int i = 0; i < provinceAdapter.getCount(); i++) {
                        RegionInfo info = (RegionInfo) (provinceAdapter.getItem(i));
                        if (addInfos[0].equals(info.name)) {
                            Message msg=new Message();
                            msg.what= MSG_WHAT_SPINNER_PROVINCE;
                            msg.arg1=i;
                            handler.sendMessage(msg);
                            break;
                        }
                    }
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(addInfos.length>=2) {
                    for (int i = 0; i < cityAdapter.getCount(); i++) {
                        RegionInfo info = (RegionInfo) (cityAdapter.getItem(i));
                        if (addInfos[1].equals(info.name)) {
                            Message msg=new Message();
                            msg.what= MSG_WHAT_SPINNER_CITY;
                            msg.arg1=i;
                            handler.sendMessage(msg);
                            break;
                        }
                    }
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(addInfos.length>=3) {
                    for (int i = 0; i < regionAdaper.getCount(); i++) {
                        RegionInfo info = (RegionInfo) (regionAdaper.getItem(i));
                        if (addInfos[2].equals(info.name)) {
                            Message msg=new Message();
                            msg.what= MSG_WHAT_SPINNER_AREA;
                            msg.arg1=i;
                            handler.sendMessage(msg);
                            break;
                        }
                    }
                }
            }
        }).start();
    }

}
