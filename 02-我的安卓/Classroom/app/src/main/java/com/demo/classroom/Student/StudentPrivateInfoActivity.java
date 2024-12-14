package com.demo.classroom.Student;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.demo.classroom.R;
import com.demo.classroom.Service.ServiceImpl.Student;
import com.demo.classroom.Template.LayoutAddress;
import com.demo.classroom.Template.LayoutDatetime;
import com.demo.classroom.Template.LayoutSex;
import com.demo.classroom.Util.ActivityHelper;
import com.demo.classroom.Util.ParserRegionInfo;
import com.demo.classroom.Util.RegionInfo;
import com.demo.classroom.Util.RegionSpinnerAdapter;
import com.demo.classroom.Util.UtilHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class StudentPrivateInfoActivity extends AppCompatActivity {
    private Student student;
    private ImageView ivPhoto;
    private EditText edtPwd;
    private EditText edtRePwd;
    private EditText edtNo;
    private EditText edtName;
    private EditText edtInyear;
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
        setContentView(R.layout.activity_student_private_info);

        InitActivity();
    }

    private void InitActivity() {
        student=new Student(getIntent().getStringExtra(ActivityHelper.KEY_PHONE));
        student.readFromDb(getContentResolver());

        ivPhoto=findViewById(R.id.imageViewPhoto);
        edtPwd=findViewById(R.id.editTextPwd);
        edtRePwd=findViewById(R.id.editTextCorrectPwd);
        edtNo=findViewById(R.id.editTextNo);
        edtName=findViewById(R.id.editTextName);

        edtInyear=findViewById(R.id.editTextInyear);

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
        if(UtilHelper.isEmptyString(student.photo)==false) {
            bmpPhoto = BitmapFactory.decodeFile(student.photo);
            if(bmpPhoto!=null)
                ivPhoto.setImageBitmap(bmpPhoto);
            Log.i("debuginfo","read:"+(bmpPhoto==null)+":"+student.photo);
        }
        if(!UtilHelper.isEmptyString(student.password)){
            edtPwd.setText(student.password);
            edtRePwd.setText(student.password);
        }
        if(!UtilHelper.isEmptyString(student.name)){
            edtName.setText(student.name);
        }
        if(!UtilHelper.isEmptyString(student.uno)){
            edtNo.setText(student.uno);
            edtNo.setEnabled(false);
        }
        if(!UtilHelper.isEmptyString(student.sex)){
            lytSex.setSex(student.sex);
        }
        if(!UtilHelper.isEmptyString(student.birth)){
            lytBrith.setDatetime(student.birth);
        }
        if(!UtilHelper.isEmptyString(student.inyear)){
            edtInyear.setText(student.inyear);
            edtInyear.setEnabled(false);
        }
        if(!UtilHelper.isEmptyString(student.college)){
            edtCollege.setText(student.college);
        }
        if(!UtilHelper.isEmptyString(student.department)){
            edtDepartment.setText(student.department);
        }
        if(!UtilHelper.isEmptyString(student.profession)){
            edtProfession.setText(student.profession);
        }
        if(!UtilHelper.isEmptyString(student.classroom)){
            edtClass.setText(student.classroom);
        }
        if(!UtilHelper.isEmptyString(student.email)){
            edtEmail.setText(student.email);
        }
        if(!UtilHelper.isEmptyString(student.introduce)){
            edtIntroduce.setText(student.introduce);
        }

        lytAddress.setAddress(student.address,"#");
    }

    public void OnClickedUpdate(View view) {
         File pphotoFile=new File(Environment.getExternalStorageDirectory(),"user-"+student.phone+".png");
         boolean saveSuccess=ActivityHelper.saveBitmap(bmpPhoto,pphotoFile);
         student.photo = pphotoFile.getAbsolutePath();

        String pwd=edtPwd.getText().toString().trim();
        String repwd=edtRePwd.getText().toString().trim();
        if(pwd.equals(repwd) && pwd.equals("")==false){
            student.password=pwd;
        }

        String no=edtNo.getText().toString().trim();
        if(UtilHelper.isEmptyString(student.uno) && UtilHelper.isEmptyString(no)==false){
            student.uno=no;
        }

        String name=edtName.getText().toString().trim();
        if(UtilHelper.isEmptyString(name)==false){
            student.name=name;
        }

        student.sex=lytSex.getSex();

        String brith=lytBrith.getDate("-");
        if(UtilHelper.isEmptyString(brith)==false){
            student.birth=brith;
        }

        String inyear=edtInyear.getText().toString().trim();
        if(UtilHelper.isEmptyString(student.inyear) && UtilHelper.isEmptyString(inyear)==false){
            student.inyear=inyear;
        }

        student.address=lytAddress.getAddress("#");

        student.college=edtCollege.getText().toString().trim();

        student.department=edtDepartment.getText().toString().trim();

        student.profession=edtProfession.getText().toString().trim();

        student.classroom=edtClass.getText().toString().trim();

        student.email=edtEmail.getText().toString().trim();

        student.introduce=edtIntroduce.getText().toString().trim();

        if(student.saveToDb(getContentResolver())){
            Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "更新失败，原因未知", Toast.LENGTH_SHORT).show();
        }
    }

}
