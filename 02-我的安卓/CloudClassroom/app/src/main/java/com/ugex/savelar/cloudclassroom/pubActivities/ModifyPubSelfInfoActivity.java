package com.ugex.savelar.cloudclassroom.pubActivities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ugex.savelar.cloudclassroom.Entities.EntityAdmin;
import com.ugex.savelar.cloudclassroom.Entities.EntityPerson;
import com.ugex.savelar.cloudclassroom.Entities.EntityStudent;
import com.ugex.savelar.cloudclassroom.Entities.EntityTeacher;
import com.ugex.savelar.cloudclassroom.R;
import com.ugex.savelar.cloudclassroom.Tools.AreaInfo;
import com.ugex.savelar.cloudclassroom.Tools.UtilHelper;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ModifyPubSelfInfoActivity extends Activity {
    private Resources res;
    public static final String EXTRA_KEY_REGISTER_ENTRY="reg_entry";
    public static final String EXTRA_KEY_ONLY_VIEW_INFO="only_view";
    public static final String ADDRESS_SPLIT_OR_JOIN_FLAG="@";
    private EntityPerson person;
    public EditText edtAccount;
    private ImageView ivPhoto;
    private EditText edtPassword;
    private EditText edtRepeatPassword;
    private EditText edtName;
    private EditText edtEmail;
    private EditText edtIntroduce;
    private RadioGroup rgSex;
    private TextView tvBrith;
    private Spinner spnProvince;
    private Spinner spnCity;
    private Spinner spnArea;
    private EditText edtDetailAddress;
    private Bitmap bmpPhoto;
    public static final int REQUEST_CAMERA_DATA_CODE=0x101;
    private MyAreaSpinnerBaseAdapter adapterProvince;
    private MyAreaSpinnerBaseAdapter adapterCity;
    private MyAreaSpinnerBaseAdapter adapterArea;
    private static final int MSG_WHAT_SPINNER_PROVINCE =0x01;
    private static final int MSG_WHAT_SPINNER_CITY =0x02;
    private static final int MSG_WHAT_SPINNER_AREA =0x03;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what== MSG_WHAT_SPINNER_PROVINCE)
                SetSpinnerSelect(spnProvince,msg.arg1);
            else if(msg.what== MSG_WHAT_SPINNER_CITY)
                SetSpinnerSelect(spnCity,msg.arg1);
            else if(msg.what== MSG_WHAT_SPINNER_AREA)
                SetSpinnerSelect(spnArea,msg.arg1);
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pub_self_info);

        InitActivity();
    }

    private void InitActivity() {
        res=getResources();
        edtAccount=(EditText)findViewById(R.id.editTextAccount);
        ivPhoto=(ImageView)findViewById(R.id.imageViewPhoto);
        edtPassword=(EditText)findViewById(R.id.editTextPassword);
        edtRepeatPassword=(EditText)findViewById(R.id.editTextRepeatPassword);
        edtName=(EditText)findViewById(R.id.editTextName);
        edtEmail=(EditText)findViewById(R.id.editTextEmail);
        edtIntroduce=(EditText)findViewById(R.id.editTextIntroduce);
        spnProvince=(Spinner)findViewById(R.id.spinnerProvince);
        spnCity=(Spinner)findViewById(R.id.spinnerCity);
        spnArea=(Spinner)findViewById(R.id.spinnerArea);
        edtDetailAddress=(EditText)findViewById(R.id.editTextDetailAddress);
        rgSex=(RadioGroup)findViewById(R.id.radioGroupSex);
        tvBrith=(TextView)findViewById(R.id.editTextSelectBrith);

        InitAreaXMLInfoToSpinneres();

        String account=getIntent().getStringExtra(UtilHelper.ExtraKey.ACCOUNT);
        String type=getIntent().getStringExtra(UtilHelper.ExtraKey.AC_TYPE);

        edtAccount.setText(account);
        if (UtilHelper.LoginTypeValue.TYPE_STUDENT.equals(type)) {
            person=new EntityStudent(account);
        } else if (UtilHelper.LoginTypeValue.TYPE_TEACHER.equals(type)) {
            person=new EntityTeacher(account);
        } else if (UtilHelper.LoginTypeValue.TYPE_ADMIN.equals(type)) {
            person=new EntityAdmin(account);
        }
        if(person.getDataFromDb(getContentResolver())){
            showDataToViewes();
        }
    }

    private void showDataToViewes() {

        if(UtilHelper.stringIsNullOrEmpty(person.Cphoto)==false){
            Bitmap bmpPhoto= BitmapFactory.decodeFile(person.Cphoto);
            if(bmpPhoto!=null){
                ivPhoto.setImageBitmap(bmpPhoto);
            }
        }
        if(UtilHelper.stringIsNullOrEmpty(person.Cname)==false){
            edtName.setText(person.Cname);
        }
        if(UtilHelper.stringIsNullOrEmpty(person.Cpwd)==false){
            edtPassword.setText(person.Cpwd);
            edtRepeatPassword.setText(person.Cpwd);
        }
        if(UtilHelper.stringIsNullOrEmpty(person.Cemail)==false){
            edtEmail.setText(person.Cemail);
        }
        if(UtilHelper.stringIsNullOrEmpty(person.Cintroduce)==false){
            edtIntroduce.setText(person.Cintroduce);
        }
        if(UtilHelper.stringIsNullOrEmpty(person.Cbirth)==false){
            tvBrith.setText(person.Cbirth);
        }
        if(UtilHelper.stringIsNullOrEmpty(person.Csex)==false){
            rgSex.check(UtilHelper.SexValue.getSexIdByType(person.Csex));
        }

        adjustAreaSpinneresToCorrect();
    }

    private void adjustAreaSpinneresToCorrect() {
        if(UtilHelper.stringIsNullOrEmpty(person.Caddress))
            return;
        final String[] addInfos =person.Caddress.split(ADDRESS_SPLIT_OR_JOIN_FLAG,4);

        if(addInfos.length>=4) {
            String detailAdd="";
            for(int i=4-1;i<addInfos.length;i++){
                detailAdd=detailAdd+addInfos[i];
            }
            edtDetailAddress.setText(detailAdd);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(addInfos.length>=1) {
                    for (int i = 0; i < adapterProvince.getCount(); i++) {
                        AreaInfo info = (AreaInfo) (adapterProvince.getItem(i));
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
                    for (int i = 0; i < adapterCity.getCount(); i++) {
                        AreaInfo info = (AreaInfo) (adapterCity.getItem(i));
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
                    for (int i = 0; i < adapterArea.getCount(); i++) {
                        AreaInfo info = (AreaInfo) (adapterArea.getItem(i));
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
    public void SetSpinnerSelect(Spinner spn,int i){
        spn.setSelection(i);
    }

    public void OnClickedModifyInfoButton(View view) {
        if(UtilHelper.stringIsNullOrEmpty(getIntent().getStringExtra(EXTRA_KEY_ONLY_VIEW_INFO))==false){
            Toast.makeText(this, res.getString(R.string.cannot_submit_request_of_under_only_view_mode), Toast.LENGTH_SHORT).show();
            return;
        }
        String pname=edtName.getText().toString().trim();
        String ppwd=edtPassword.getText().toString().trim();
        String prepwd=edtRepeatPassword.getText().toString().trim();
        String pemail=edtEmail.getText().toString().trim();
        String pintro=edtIntroduce.getText().toString().trim();
        String paddress=((AreaInfo)(spnProvince.getSelectedItem())).name+ADDRESS_SPLIT_OR_JOIN_FLAG
                +((AreaInfo)(spnCity.getSelectedItem())).name+ADDRESS_SPLIT_OR_JOIN_FLAG
                +((AreaInfo)(spnArea.getSelectedItem())).name+ADDRESS_SPLIT_OR_JOIN_FLAG
                +edtDetailAddress.getText().toString().trim();
        String psex=UtilHelper.SexValue.getSexStringById(rgSex.getCheckedRadioButtonId());
        String pbrith=tvBrith.getText().toString().trim();
        if(psex.equals(person.Csex)==false){
            person.Csex=psex;
        }
        if(pbrith.equals(person.Cbirth)==false){
            person.Cbirth=pbrith;
        }
        if(pname.equals(person.Cname)==false){
            person.Cname=pname;
        }
        if(ppwd.equals(prepwd)){
            if(ppwd.equals(person.Cpwd)==false)
                person.Cpwd=ppwd;
        }else{
            Toast.makeText(this, res.getString(R.string.pwd_and_repeat_pwd_was_not_equals_please_retry_after_modified), Toast.LENGTH_SHORT).show();
            return;
        }
        if(paddress.equals(person.Caddress)==false){
            person.Caddress=paddress;
        }
        if(pemail.equals(person.Cemail)==false){
            person.Cemail=pemail;
        }
        if(pintro.equals(person.Cintroduce)==false){
            person.Cintroduce=pintro;
        }
        if(bmpPhoto!=null){
            File pphotoFile=new File(UtilHelper.ExternalData.EXTERNAL_FILE_STORAGE_ROOT_DIR,person.Caccount+".png");
            if(pphotoFile.getAbsolutePath().equals(person.Cphoto)==false){
                person.Cphoto=pphotoFile.getAbsolutePath();
                try {
                    FileOutputStream sos= new FileOutputStream(person.Cphoto);
                    bmpPhoto.compress(Bitmap.CompressFormat.PNG,90,sos);
                    sos.flush();
                    sos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if(person.updateToDb(getContentResolver())){
            Toast.makeText(this, res.getString(R.string.info_updata_success), Toast.LENGTH_SHORT).show();
            JumpNextPage();
        }else{
            Toast.makeText(this, res.getString(R.string.info_update_failure_unkown_reason), Toast.LENGTH_SHORT).show();
        }
    }

    private void JumpNextPage() {
        String needToRegister=getIntent().getStringExtra(EXTRA_KEY_REGISTER_ENTRY);
        if(UtilHelper.stringIsNullOrEmpty(needToRegister)){
            finish();
        }else{
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra(LoginActivity.EXTRA_KEY_LOGIN_TYPE,getIntent().getStringExtra(UtilHelper.ExtraKey.AC_TYPE) );
            intent.putExtra(LoginActivity.EXTRA_KEY_LOGIN_ACCOUNT, getIntent().getStringExtra(UtilHelper.ExtraKey.ACCOUNT));
            startActivity(intent);
            finish();
        }
    }
    public void OnClickedPhotoImageView(View view) {
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),REQUEST_CAMERA_DATA_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_CAMERA_DATA_CODE){
            bmpPhoto=(Bitmap)data.getExtras().get("data");
            if(bmpPhoto!=null){
                ivPhoto.setImageBitmap(bmpPhoto);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void OnClickedSelectBrithTextView(View view){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            DatePickerDialog dlg=new DatePickerDialog(this);
            dlg.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker v, int year, int month, int dayOfMonth) {
                    tvBrith.setText(year+UtilHelper.DATE_SPLIT_OR_JOIN_FLAG+(1+month)+UtilHelper.DATE_SPLIT_OR_JOIN_FLAG+dayOfMonth);
                }
            });
            dlg.show();
        }
    }
    public void OnClickedLogoutTextView(View view) {
        AutoLoginHelper.saveLoginInfo(this,"","","");
        Intent intent=new Intent(this,LoginActivity.class);
        intent.putExtra(LoginActivity.EXTRA_KEY_LOGIN_TYPE,getIntent().getStringExtra(UtilHelper.ExtraKey.AC_TYPE));
        intent.putExtra(LoginActivity.EXTRA_KEY_LOGIN_ACCOUNT,person.Caccount);
        intent.putExtra(LoginActivity.EXTRA_KEY_LOGIN_PASSWORD,person.Cpwd);
        startActivity(intent);
        finishAffinity();
    }

    /////////////////////////////////////////////////
    private void InitAreaXMLInfoToSpinneres() {
        AreaInfo root=parseAreaInfo(getResources(),R.xml.nations);

        adapterProvince=new MyAreaSpinnerBaseAdapter(root);
        adapterCity=new MyAreaSpinnerBaseAdapter((AreaInfo)adapterProvince.getItem(0));
        spnCity.setAdapter(adapterCity);
        adapterArea=new MyAreaSpinnerBaseAdapter((AreaInfo)adapterCity.getItem(0));
        spnArea.setAdapter(adapterArea);

        spnProvince.setAdapter(adapterProvince);
        spnProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AreaInfo info=(AreaInfo) spnProvince.getSelectedItem();
                adapterCity=new MyAreaSpinnerBaseAdapter(info);
                spnCity.setAdapter(adapterCity);
                adapterCity.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AreaInfo info=(AreaInfo) spnCity.getSelectedItem();
                adapterArea=new MyAreaSpinnerBaseAdapter(info);
                spnArea.setAdapter(adapterArea);
                adapterArea.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    class MyAreaSpinnerBaseAdapter extends BaseAdapter {
        private AreaInfo infos;
        public MyAreaSpinnerBaseAdapter(AreaInfo info){
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
            TextView tv=new TextView(ModifyPubSelfInfoActivity.this);
            tv.setText(infos.subAreas.get(position).name);
            return tv;
        }
    }
    private AreaInfo parseAreaInfo(Resources res, int xmlId){
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
