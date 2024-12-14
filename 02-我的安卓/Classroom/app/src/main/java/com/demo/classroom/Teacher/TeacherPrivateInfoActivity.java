package com.demo.classroom.Teacher;

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
import com.demo.classroom.Service.ServiceImpl.Student;
import com.demo.classroom.Service.ServiceImpl.Teacher;
import com.demo.classroom.Template.LayoutAddress;
import com.demo.classroom.Template.LayoutDatetime;
import com.demo.classroom.Template.LayoutSex;
import com.demo.classroom.Util.ActivityHelper;
import com.demo.classroom.Util.UtilHelper;

import java.io.File;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TeacherPrivateInfoActivity extends AppCompatActivity {
    private Teacher teacher;
    private ImageView ivPhoto;
    private EditText edtPwd;
    private EditText edtRePwd;
    private EditText edtNo;
    private EditText edtName;
    private EditText edtteayear;
    private EditText edtCollege;
    private EditText edtDepartment;
    private EditText edtProfession;
    private EditText edtClass;
    private EditText edtEmail;
    private EditText edtIntroduce;

    private LayoutAddress lytAddress;
    private LayoutSex lytSex;
    private LayoutDatetime lytBrith;

    private Bitmap bmpPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_private_info);

        InitActivity();
    }

    private void InitActivity() {
        teacher =new Teacher(getIntent().getStringExtra(ActivityHelper.KEY_PHONE));
        teacher.readFromDb(getContentResolver());

        ivPhoto=findViewById(R.id.imageViewPhoto);
        edtPwd=findViewById(R.id.editTextPwd);
        edtRePwd=findViewById(R.id.editTextCorrectPwd);
        edtNo=findViewById(R.id.editTextNo);
        edtName=findViewById(R.id.editTextName);

        edtteayear =findViewById(R.id.editTextTeayear);

        edtCollege=findViewById(R.id.editTextCollege);
        edtDepartment=findViewById(R.id.editTextDeparment);
        edtProfession=findViewById(R.id.editTextProfession);
        edtClass=findViewById(R.id.editTextClass);
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
        if(UtilHelper.isEmptyString(teacher.photo)==false) {
            bmpPhoto = BitmapFactory.decodeFile(teacher.photo);
            if(bmpPhoto!=null)
                ivPhoto.setImageBitmap(bmpPhoto);
            Log.i("debuginfo","read:"+(bmpPhoto==null)+":"+ teacher.photo);
        }
        if(!UtilHelper.isEmptyString(teacher.password)){
            edtPwd.setText(teacher.password);
            edtRePwd.setText(teacher.password);
        }
        if(!UtilHelper.isEmptyString(teacher.name)){
            edtName.setText(teacher.name);
        }
        if(!UtilHelper.isEmptyString(teacher.uno)){
            edtNo.setText(teacher.uno);
            edtNo.setEnabled(false);
        }
        if(!UtilHelper.isEmptyString(teacher.sex)){
            lytSex.setSex(teacher.sex);
        }
        if(!UtilHelper.isEmptyString(teacher.birth)){
            lytBrith.setDatetime(teacher.birth);
        }
        if(!UtilHelper.isEmptyString(teacher.teayear)){
            edtteayear.setText(teacher.teayear);
            edtteayear.setEnabled(false);
        }
        if(!UtilHelper.isEmptyString(teacher.college)){
            edtCollege.setText(teacher.college);
        }
        if(!UtilHelper.isEmptyString(teacher.department)){
            edtDepartment.setText(teacher.department);
        }
        if(!UtilHelper.isEmptyString(teacher.profession)){
            edtProfession.setText(teacher.profession);
        }
        if(!UtilHelper.isEmptyString(teacher.classroom)){
            edtClass.setText(teacher.classroom);
        }
        if(!UtilHelper.isEmptyString(teacher.email)){
            edtEmail.setText(teacher.email);
        }
        if(!UtilHelper.isEmptyString(teacher.introduce)){
            edtIntroduce.setText(teacher.introduce);
        }

        lytAddress.setAddress(teacher.address,"#");
    }

    public void OnClickedUpdate(View view) {
         File pphotoFile=new File(Environment.getExternalStorageDirectory(),"user-"+ teacher.phone+".png");
         boolean saveSuccess=ActivityHelper.saveBitmap(bmpPhoto,pphotoFile);
         teacher.photo = pphotoFile.getAbsolutePath();

        String pwd=edtPwd.getText().toString().trim();
        String repwd=edtRePwd.getText().toString().trim();
        if(pwd.equals(repwd) && pwd.equals("")==false){
            teacher.password=pwd;
        }

        String no=edtNo.getText().toString().trim();
        if(UtilHelper.isEmptyString(teacher.uno) && UtilHelper.isEmptyString(no)==false){
            teacher.uno=no;
        }

        String name=edtName.getText().toString().trim();
        if(UtilHelper.isEmptyString(name)==false){
            teacher.name=name;
        }

        teacher.sex=lytSex.getSex();

        String brith=lytBrith.getDate("-");
        if(UtilHelper.isEmptyString(brith)==false){
            teacher.birth=brith;
        }

        String inyear= edtteayear.getText().toString().trim();
        if(UtilHelper.isEmptyString(teacher.teayear) && UtilHelper.isEmptyString(inyear)==false){
            teacher.teayear=inyear;
        }

        teacher.address=lytAddress.getAddress("#");

        teacher.college=edtCollege.getText().toString().trim();

        teacher.department=edtDepartment.getText().toString().trim();

        teacher.profession=edtProfession.getText().toString().trim();

        teacher.classroom=edtClass.getText().toString().trim();

        teacher.email=edtEmail.getText().toString().trim();

        teacher.introduce=edtIntroduce.getText().toString().trim();

        if(teacher.saveToDb(getContentResolver())){
            Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "更新失败，原因未知", Toast.LENGTH_SHORT).show();
        }
    }

}
