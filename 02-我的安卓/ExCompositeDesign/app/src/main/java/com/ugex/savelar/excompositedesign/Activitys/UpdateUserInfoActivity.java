package com.ugex.savelar.excompositedesign.Activitys;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ugex.savelar.excompositedesign.Dao.DbManager;
import com.ugex.savelar.excompositedesign.Dao.UserEntity;
import com.ugex.savelar.excompositedesign.MainActivity;
import com.ugex.savelar.excompositedesign.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateUserInfoActivity extends AppCompatActivity {
    private Resources res;
    private ImageView ivPhoto;
    private EditText edtUserName;
    private Spinner spinnerProvice;
    private Spinner spinnerCity;
    private Spinner spinnerArea;
    private EditText edtAddress;
    private EditText edtAccount;
    private EditText edtBrith;
    private EditText edtEmail;
    private RadioGroup rgSex;
    private Bitmap bmpPhoto;
    public static final int REQUEST_CAMERA_DATA_CODE=0x101;
    private UserEntity userEntity=new UserEntity();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);

        InitActivity();
    }

    private void InitActivity() {
        res=getResources();
        spinnerProvice=findViewById(R.id.spinnerProvince);
        spinnerCity=findViewById(R.id.spinnerCity);
        spinnerArea=findViewById(R.id.spinnerArea);
        ivPhoto=findViewById(R.id.imageViewPhoto);
        edtUserName=findViewById(R.id.editTextUserName);
        edtAddress=findViewById(R.id.editTextAddress);
        edtAccount=findViewById(R.id.editTextAccount);
        edtBrith=findViewById(R.id.editTextBrithDay);
        edtEmail=findViewById(R.id.editTextEmail);
        rgSex=findViewById(R.id.radioGroupSex);
        rgSex.check(R.id.radioButtonMan);
        userEntity.setSex(((RadioButton)findViewById(R.id.radioButtonMan)).getText().toString());

        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),REQUEST_CAMERA_DATA_CODE);
            }
        });

        rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId)
                {
                    case R.id.radioButtonMan:
                        userEntity.setSex(((RadioButton)findViewById(R.id.radioButtonMan)).getText().toString());
                        break;
                    case R.id.radioButtonWoman:
                        userEntity.setSex(((RadioButton)findViewById(R.id.radioButtonWoman)).getText().toString());
                        break;
                    case R.id.radioButtonSecret:
                        userEntity.setSex(((RadioButton)findViewById(R.id.radioButtonSecret)).getText().toString());
                        break;
                }
            }
        });



        edtBrith.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        DatePickerDialog dlg=new DatePickerDialog(UpdateUserInfoActivity.this);
                        dlg.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                userEntity.setBrithday(year+"-"+(1+month)+"-"+dayOfMonth);
                                edtBrith.setText(userEntity.brithday);
                            }
                        });
                        dlg.show();
                    }

                }
            }
        });

        userEntity.setIDAccount(getIntent().getStringExtra("account"));
        edtAccount.setEnabled(false);
        edtAccount.setText(userEntity.IDAccount);

        InitAreaXMLInfoToSpinneres();

        tryShowDbUserEntity();
    }

    private void tryShowDbUserEntity() {
        if(userEntity.hasPrimaryID()==false){
            return;
        }
        String selectSql="select * from "+DbManager.objTable+" where account='"+userEntity.IDAccount+"';";
        Cursor cur=DbManager.selectSql(this,selectSql);
        if(cur.moveToFirst()){
            Log.i("TestLog","has got db data");
            userEntity.setName(cur.getString(cur.getColumnIndex("name")));
            userEntity.setSex(cur.getString(cur.getColumnIndex("sex")));
            userEntity.setPhoto(cur.getString(cur.getColumnIndex("photo")));
            userEntity.setAddress(cur.getString(cur.getColumnIndex("address")));
            userEntity.setBrithday(cur.getString(cur.getColumnIndex("brithday")));
            userEntity.setEmail(cur.getString(cur.getColumnIndex("email")));
            userEntity.setOther(cur.getString(cur.getColumnIndex("other")));
            userEntity.setPassword(cur.getString(cur.getColumnIndex("password")));

            edtAddress.setText(userEntity.address);
            edtBrith.setText(userEntity.brithday);
            edtEmail.setText(userEntity.email);
            edtUserName.setText(userEntity.name);

            try {
                if(new File(userEntity.photo).exists()) {
                    bmpPhoto = BitmapFactory.decodeFile(userEntity.photo);
                    ivPhoto.setImageBitmap(bmpPhoto);
                }

                if(((RadioButton)findViewById(R.id.radioButtonMan)).getText().toString().trim().equals(userEntity.sex)){
                    rgSex.check(R.id.radioButtonMan);
                }
                else if(((RadioButton)findViewById(R.id.radioButtonWoman)).getText().toString().trim().equals(userEntity.sex)){
                    rgSex.check(R.id.radioButtonWoman);
                }
                else if(((RadioButton)findViewById(R.id.radioButtonSecret)).getText().toString().trim().equals(userEntity.sex)){
                    rgSex.check(R.id.radioButtonSecret);
                }

                MyAreaSpinnerBaseAdapter adtPro=(MyAreaSpinnerBaseAdapter)(spinnerProvice.getAdapter());
                for(int i=0;i<adtPro.getCount();i++){
                    AreaInfo info=(AreaInfo)(adtPro.getItem(i));
                    if(userEntity.address.indexOf(info.name)!=-1){
                        spinnerProvice.setSelection(i);
                        break;
                    }
                }
                /*
                 * 一下代码就算是使用线程睡眠来等待逐个执行，依然会出错
                 * E/Perf: getFolderSize() : Exception_1 = java.lang.NullPointerException: Attempt to get length of null array
                 * */
//
//            MyAreaSpinnerBaseAdapter adtCit=(MyAreaSpinnerBaseAdapter)(spinnerCity.getAdapter());
//            for(int i=0;i<adtCit.getCount();i++){
//                AreaInfo info=(AreaInfo)(adtCit.getItem(i));
//                if(userEntity.address.indexOf(info.name)!=-1){
//                    spinnerCity.setSelection(i);
//                    break;
//                }
//            }
//
//            MyAreaSpinnerBaseAdapter adtAre=(MyAreaSpinnerBaseAdapter)(spinnerArea.getAdapter());
//            for(int i=0;i<adtAre.getCount();i++){
//                AreaInfo info=(AreaInfo)(adtAre.getItem(i));
//                if(userEntity.address.indexOf(info.name)!=-1){
//                    spinnerArea.setSelection(i);//19977-777
//                    break;
//                }
//            }
            } catch (Exception e){
                //TODO:Picture not file And location Info not found.
            }
        }

        cur.close();
    }

    private UserEntity fillUserEntity(){
        userEntity.setName(edtUserName.getText().toString());
        userEntity.setAddress(((AreaInfo)(spinnerProvice.getSelectedItem())).name
                +((AreaInfo)(spinnerCity.getSelectedItem())).name
                +((AreaInfo)(spinnerArea.getSelectedItem())).name
                +edtAddress.getText().toString());
        userEntity.setEmail(edtEmail.getText().toString());
        userEntity.setPhoto(new File(Environment.getExternalStorageDirectory(),userEntity.IDAccount+"_user_photo.png").getAbsolutePath());
        try {
            OutputStream sos= new FileOutputStream(userEntity.photo);
            bmpPhoto.compress(Bitmap.CompressFormat.PNG,90,sos);
            sos.flush();
            sos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        userEntity.other="";
        return userEntity;
    }
    public void onBtnUpdateUserInfoClicked(View view) {
        if(edtAccount.getText().toString().trim().equals("")){
            Toast.makeText(this, res.getText(R.string.toast_please_input_user_name), Toast.LENGTH_SHORT).show();
            return;
        }

        fillUserEntity();
        updateUserInfoToDB();

        showUserInfoDiglog();
    }

    public void onBtnDeleteUserInfoClicked(View view) {
        String uaccount=edtAccount.getText().toString().trim();
        long delID=DbManager.delete(this,"account=?",new String[]{uaccount});
        if(delID>=0){
            Toast.makeText(this, "删除用户信息成功", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "删除用户信息失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void showUserInfoDiglog() {
        View sv= LayoutInflater.from(this).inflate(R.layout.user_info_dlg,null);
        ImageView ivpho=sv.findViewById(R.id.imageViewUserPhoto);
        TextView tvAcco=sv.findViewById(R.id.textViewUserAccount);
        TextView tvName=sv.findViewById(R.id.textViewUserName);
        TextView tvSex=sv.findViewById(R.id.textViewUserSex);
        TextView tvBrith=sv.findViewById(R.id.textViewUserBrith);
        TextView tvAddr=sv.findViewById(R.id.textViewUserAddr);
        TextView tvEmail=sv.findViewById(R.id.textViewUserEmail);
        ivpho.setImageBitmap(bmpPhoto);


        tvAcco.setText("账户："+userEntity.IDAccount);
        tvName.setText("用户名："+userEntity.name);
        tvSex.setText("性别："+userEntity.sex);
        tvBrith.setText("生日："+userEntity.brithday);
        tvAddr.setText("地址："+ userEntity.address);
        tvEmail.setText("邮箱："+userEntity.email);


        new AlertDialog.Builder(this)
                .setTitle("登录信息")
                .setView(sv)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(UpdateUserInfoActivity.this, MainActivity.class).putExtra("account",userEntity.IDAccount));
                        UpdateUserInfoActivity.this.finish();
                    }
                })
                .show();
    }

    private void updateUserInfoToDB() {
        //account ,name ,photo ,sex ,address ,
        // brithday ,email ,other
        ContentValues values=userEntity.toContentValues();
        //尝试执行插入，如果插入不成功就执行修改
        long idins= DbManager.insert(this,values);
        if(idins>=0){
            Toast.makeText(this, "保存用户信息成功", Toast.LENGTH_SHORT).show();
        }else{
            values.remove("account");
            long idupd=DbManager.update(this,values,"account=?",new String[]{userEntity.IDAccount});
            if(idupd>=0) {
                Toast.makeText(this, "保存用户信息成功", Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(this, "保存用户信息意外失败", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQUEST_CAMERA_DATA_CODE){
            bmpPhoto=(Bitmap)data.getExtras().get("data");
            if(bmpPhoto!=null){
                ivPhoto.setImageBitmap(bmpPhoto);
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    ///////////////////////////////
    private void InitAreaXMLInfoToSpinneres() {
        AreaInfo root=parseAreaInfo(getResources(),R.xml.nations);

        MyAreaSpinnerBaseAdapter adapterRoot=new MyAreaSpinnerBaseAdapter(root);
        spinnerProvice.setAdapter(adapterRoot);
        spinnerProvice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AreaInfo info=(AreaInfo) spinnerProvice.getSelectedItem();
                MyAreaSpinnerBaseAdapter adapterCity=new MyAreaSpinnerBaseAdapter(info);
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
                MyAreaSpinnerBaseAdapter adapterArea=new MyAreaSpinnerBaseAdapter(info);
                spinnerArea.setAdapter(adapterArea);
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
            TextView tv=new TextView(UpdateUserInfoActivity.this);
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
