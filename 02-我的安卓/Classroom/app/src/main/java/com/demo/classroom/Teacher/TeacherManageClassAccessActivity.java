package com.demo.classroom.Teacher;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.classroom.R;
import com.demo.classroom.Service.IEntity;
import com.demo.classroom.Service.ServiceImpl.ClassAccess;
import com.demo.classroom.Service.ServiceImpl.Student;
import com.demo.classroom.Service.ServiceImpl.Teacher;
import com.demo.classroom.Student.StudentViewAttendenceActivity;
import com.demo.classroom.Student.StudentViewClassAccessActivity;
import com.demo.classroom.Template.LayoutDatetime;
import com.demo.classroom.Util.ActivityHelper;
import com.demo.classroom.Util.ObjectAdapter;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TeacherManageClassAccessActivity extends AppCompatActivity {
    private Teacher teacher;
    private Spinner spnStudent;
    private List<Student> data;
    private ObjectAdapter adapter;
    private LayoutDatetime lytDate;
    private EditText tvSession;
    private EditText tvAccessgrade;
    private EditText tvOther;
    private ClassAccess currentClassAccess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_manage_classaccess);

        InitActivity();
    }

    private void InitActivity() {
        teacher=new Teacher(getIntent().getStringExtra(ActivityHelper.KEY_PHONE));
        teacher.readFromDb(getContentResolver());

        lytDate=findViewById(R.id.layoutDatetime);
        lytDate.setTitle("时间");
        tvSession=findViewById(R.id.textViewSession);
        tvAccessgrade=findViewById(R.id.textViewAccessGrade);
        tvOther=findViewById(R.id.textViewOther);

        spnStudent=findViewById(R.id.spinnerStudent);

        data=(List<Student>)new Student().getAllByWhere(getContentResolver(),null);//"classrrom='"+teacher.classroom+"'"
        adapter=new ObjectAdapter(this, data, new ObjectAdapter.OnRequireItemView() {
            @Override
            public View setView(IEntity ent, Context context) {
                TextView tv=new TextView(context);
                Student stu=(Student)ent;
                tv.setText(stu.name+" "+stu.uno);
                return tv;
            }
        });

        spnStudent.setAdapter(adapter);
    }


    public void OnClickedAdd(View view) {
        currentClassAccess =new ClassAccess();
        currentClassAccess.sno=((Student)spnStudent.getSelectedItem()).uno;
        currentClassAccess.tno=teacher.uno;
        currentClassAccess.datetime=lytDate.getDate("-");
        currentClassAccess.session=tvSession.getText().toString().trim();
        currentClassAccess.accessgrade=tvAccessgrade.getText().toString().trim();
        currentClassAccess.other=tvOther.getText().toString().trim();
        if(currentClassAccess.addToDb(getContentResolver())){
            Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "添加失败，原因未知", Toast.LENGTH_SHORT).show();
        }
    }

    public void OnClickedDelete(View view) {
        if(currentClassAccess ==null){
            Toast.makeText(this, "删除失败，请先选择或者添加", Toast.LENGTH_SHORT).show();
            return;
        }
        if(currentClassAccess.deleteFromDb(getContentResolver())){
            Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "删除失败，原因未知", Toast.LENGTH_SHORT).show();
        }
    }

    public void OnCLickedUpdate(View view) {
        if(currentClassAccess ==null){
            Toast.makeText(this, "更新失败，请先选择或者添加", Toast.LENGTH_SHORT).show();
            return;
        }
        currentClassAccess.datetime=lytDate.getDate("-");
        currentClassAccess.session=tvSession.getText().toString().trim();
        currentClassAccess.accessgrade=tvAccessgrade.getText().toString().trim();
        currentClassAccess.other=tvOther.getText().toString().trim();
        if(currentClassAccess.updateToDb(getContentResolver())){
            Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "更新失败，原因未知", Toast.LENGTH_SHORT).show();
        }
    }


    public void OnClickedQuery(View view) {
        Intent intent=new Intent(this, StudentViewClassAccessActivity.class);
        intent.putExtra(ActivityHelper.KEY_PHONE,((Student)spnStudent.getSelectedItem()).phone);
        intent.putExtra(ActivityHelper.KEY_REQUEST_ITEM_ID,"require");
        startActivityForResult(intent,0x101);
    }

    private void resultAttendance(int id){
        currentClassAccess =new ClassAccess();
        currentClassAccess.id=id;
        if(currentClassAccess.id==-1) {
            currentClassAccess = null;
            return;
        }
        currentClassAccess.readFromDb(getContentResolver());
        lytDate.setDatetime(currentClassAccess.datetime);
        tvSession.setText(currentClassAccess.session);
        tvAccessgrade.setText(currentClassAccess.accessgrade);
        tvOther.setText(currentClassAccess.other);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==0x101 && resultCode==0x101){
            resultAttendance(data.getIntExtra(ActivityHelper.KEY_ID,-1));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
