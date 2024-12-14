package com.demo.classroom.Admin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.demo.classroom.R;
import com.demo.classroom.Service.ServiceImpl.Admin;
import com.demo.classroom.Template.LayoutAddress;
import com.demo.classroom.Template.LayoutDatetime;
import com.demo.classroom.Template.LayoutSex;
import com.demo.classroom.Util.ActivityHelper;
import com.demo.classroom.Util.UtilHelper;

import java.io.File;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AdminPrivateInfoActivity extends AppCompatActivity {
    private Admin admin;
    private ImageView ivPhoto;
    private EditText edtPwd;
    private EditText edtRePwd;
    private EditText edtName;
    private EditText edtEmail;
    private EditText edtIntroduce;

    private LayoutAddress lytAddress;
    private LayoutSex lytSex;
    private LayoutDatetime lytBrith;

    private Bitmap bmpPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_private_info);

        InitActivity();
    }

    private void InitActivity() {
        admin =new Admin(getIntent().getStringExtra(ActivityHelper.KEY_PHONE));
        admin.readFromDb(getContentResolver());

        ivPhoto=findViewById(R.id.imageViewPhoto);
        edtPwd=findViewById(R.id.editTextPwd);
        edtRePwd=findViewById(R.id.editTextCorrectPwd);
        edtName=findViewById(R.id.editTextName);

        edtEmail=findViewById(R.id.editTextEmail);
        edtIntroduce=findViewById(R.id.editTextIntroduce);

        lytAddress=findViewById(R.id.layoutAddress);
        lytSex=findViewById(R.id.layoutSex);
        lytBrith=findViewById(R.id.layoutBrith);
        lytBrith.setTitle("生日");

        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChoicePhoto();
            }
        });

        displayToView();
    }
    private void getChoicePhoto(){
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),0x101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==0x101){
            bmpPhoto=(Bitmap)data.getExtras().get("data");
            if(bmpPhoto!=null){
                ivPhoto.setImageBitmap(bmpPhoto);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void displayToView(){
        if(UtilHelper.isEmptyString(admin.photo)==false) {
            bmpPhoto = BitmapFactory.decodeFile(admin.photo);
            if(bmpPhoto!=null)
                ivPhoto.setImageBitmap(bmpPhoto);
            Log.i("debuginfo","read:"+(bmpPhoto==null)+":"+ admin.photo);
        }
        if(!UtilHelper.isEmptyString(admin.password)){
            edtPwd.setText(admin.password);
            edtRePwd.setText(admin.password);
        }
        if(!UtilHelper.isEmptyString(admin.name)){
            edtName.setText(admin.name);
        }

        if(!UtilHelper.isEmptyString(admin.sex)){
            lytSex.setSex(admin.sex);
        }
        if(!UtilHelper.isEmptyString(admin.birth)){
            lytBrith.setDatetime(admin.birth);
        }
        if(!UtilHelper.isEmptyString(admin.email)){
            edtEmail.setText(admin.email);
        }
        if(!UtilHelper.isEmptyString(admin.introduce)){
            edtIntroduce.setText(admin.introduce);
        }

        lytAddress.setAddress(admin.address,"#");
    }

    public void OnClickedUpdate(View view) {
         File pphotoFile=new File(Environment.getExternalStorageDirectory(),"user-"+ admin.phone+".png");
         boolean saveSuccess=ActivityHelper.saveBitmap(bmpPhoto,pphotoFile);
         admin.photo = pphotoFile.getAbsolutePath();

        String pwd=edtPwd.getText().toString().trim();
        String repwd=edtRePwd.getText().toString().trim();
        if(pwd.equals(repwd) && pwd.equals("")==false){
            admin.password=pwd;
        }

        String name=edtName.getText().toString().trim();
        if(UtilHelper.isEmptyString(name)==false){
            admin.name=name;
        }

        admin.sex=lytSex.getSex();

        String brith=lytBrith.getDate("-");
        if(UtilHelper.isEmptyString(brith)==false){
            admin.birth=brith;
        }


        admin.address=lytAddress.getAddress("#");

        admin.email=edtEmail.getText().toString().trim();

        admin.introduce=edtIntroduce.getText().toString().trim();

        if(admin.saveToDb(getContentResolver())){
            Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "更新失败，原因未知", Toast.LENGTH_SHORT).show();
        }
    }

}
