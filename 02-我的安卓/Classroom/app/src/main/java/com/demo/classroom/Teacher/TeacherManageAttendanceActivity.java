package com.demo.classroom.Teacher;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.demo.classroom.Service.ServiceImpl.Attendance;
import com.demo.classroom.Service.ServiceImpl.Student;
import com.demo.classroom.Service.ServiceImpl.Teacher;
import com.demo.classroom.Student.StudentViewAttendenceActivity;
import com.demo.classroom.Template.LayoutDatetime;
import com.demo.classroom.Util.ActivityHelper;
import com.demo.classroom.Util.ObjectAdapter;

import java.util.List;

public class TeacherManageAttendanceActivity extends AppCompatActivity {
    private Teacher teacher;
    private Spinner spnStudent;
    private List<Student> data;
    private ObjectAdapter adapter;
    private LayoutDatetime lytDate;
    private EditText tvSession;
    private EditText tvDuration;
    private EditText tvResult;
    private EditText tvLeaveRequest;
    private EditText tvOther;
    private Attendance currentAttendance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_manage_attendance);

        InitActivity();
    }

    private void InitActivity() {
        teacher=new Teacher(getIntent().getStringExtra(ActivityHelper.KEY_PHONE));
        teacher.readFromDb(getContentResolver());

        lytDate=findViewById(R.id.layoutDatetime);
        lytDate.setTitle("时间");
        tvSession=findViewById(R.id.textViewSession);
        tvDuration=findViewById(R.id.textViewDuration);
        tvResult=findViewById(R.id.textViewResult);
        tvLeaveRequest=findViewById(R.id.textViewLeaveRequest);
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
        currentAttendance=new Attendance();
        currentAttendance.sno=((Student)spnStudent.getSelectedItem()).uno;
        currentAttendance.tno=teacher.uno;
        currentAttendance.datetime=lytDate.getDate("-");
        currentAttendance.session=tvSession.getText().toString().trim();
        currentAttendance.duration=tvDuration.getText().toString().trim();
        currentAttendance.result=tvResult.getText().toString().trim();
        currentAttendance.leaverequest=tvLeaveRequest.getText().toString().trim();
        currentAttendance.other=tvOther.getText().toString().trim();
        if(currentAttendance.addToDb(getContentResolver())){
            Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "添加失败，原因未知", Toast.LENGTH_SHORT).show();
        }
    }

    public void OnClickedDelete(View view) {
        if(currentAttendance==null){
            Toast.makeText(this, "删除失败，请先选择或者添加", Toast.LENGTH_SHORT).show();
            return;
        }
        if(currentAttendance.deleteFromDb(getContentResolver())){
            Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "删除失败，原因未知", Toast.LENGTH_SHORT).show();
        }
    }

    public void OnCLickedUpdate(View view) {
        if(currentAttendance==null){
            Toast.makeText(this, "更新失败，请先选择或者添加", Toast.LENGTH_SHORT).show();
            return;
        }
        currentAttendance.datetime=lytDate.getDate("-");
        currentAttendance.session=tvSession.getText().toString().trim();
        currentAttendance.duration=tvDuration.getText().toString().trim();
        currentAttendance.result=tvResult.getText().toString().trim();
        currentAttendance.leaverequest=tvLeaveRequest.getText().toString().trim();
        currentAttendance.other=tvOther.getText().toString().trim();
        if(currentAttendance.updateToDb(getContentResolver())){
            Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "更新失败，原因未知", Toast.LENGTH_SHORT).show();
        }
    }


    public void OnClickedQuery(View view) {
        Intent intent=new Intent(this, StudentViewAttendenceActivity.class);
        intent.putExtra(ActivityHelper.KEY_PHONE,((Student)spnStudent.getSelectedItem()).phone);
        intent.putExtra(ActivityHelper.KEY_REQUEST_ITEM_ID,"require");
        startActivityForResult(intent,0x101);
    }

    private void resultAttendance(int id){
        currentAttendance=new Attendance();
        currentAttendance.id=id;
        if(currentAttendance.id==-1) {
            currentAttendance = null;
            return;
        }
        currentAttendance.readFromDb(getContentResolver());
        lytDate.setDatetime(currentAttendance.datetime);
        tvSession.setText(currentAttendance.session);
        tvDuration.setText(currentAttendance.duration);
        tvResult.setText(currentAttendance.result);
        tvLeaveRequest.setText(currentAttendance.leaverequest);
        tvOther.setText(currentAttendance.other);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==0x101 && resultCode==0x101){
            resultAttendance(data.getIntExtra(ActivityHelper.KEY_ID,-1));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
