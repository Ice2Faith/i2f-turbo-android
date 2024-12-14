package com.demo.classroom.Student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.demo.classroom.R;
import com.demo.classroom.Service.IEntity;
import com.demo.classroom.Service.ServiceImpl.Attendance;
import com.demo.classroom.Service.ServiceImpl.Student;
import com.demo.classroom.Util.ActivityHelper;
import com.demo.classroom.Util.ObjectAdapter;
import com.demo.classroom.Util.UtilHelper;

import java.util.List;

public class StudentViewAttendenceActivity extends AppCompatActivity {
    private Student student;
    private ListView lsvInfo;
    private List<Attendance> data;
    private ObjectAdapter adapter;
    private boolean needReturn=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view_attendence);

        InitActivity();
    }

    private void InitActivity() {
        Intent intent=getIntent();
        if(UtilHelper.isEmptyString(intent.getStringExtra(ActivityHelper.KEY_REQUEST_ITEM_ID))==false){
            needReturn=true;
        }
        student=new Student(getIntent().getStringExtra(ActivityHelper.KEY_PHONE));
        student.readFromDb(getContentResolver());

        lsvInfo=findViewById(R.id.listViewInfo);

        data=(List<Attendance>)new Attendance().getAllByWhere(getContentResolver(),"sno='"+student.uno+"'");
        adapter=new ObjectAdapter(this, data, new ObjectAdapter.OnRequireItemView() {
            @Override
            public View setView(IEntity ent, Context context) {
                View view= LayoutInflater.from(context).inflate(R.layout.item_list_view_attendance,null);
                TextView tvDatetime=view.findViewById(R.id.textViewDatetime);
                TextView tvSession=view.findViewById(R.id.textViewSession);
                TextView tvDuration=view.findViewById(R.id.textViewDuration);
                TextView tvResult=view.findViewById(R.id.textViewResult);
                TextView tvLeaveRequest=view.findViewById(R.id.textViewLeaveRequest);
                TextView tvOther=view.findViewById(R.id.textViewOther);
                Attendance obj=(Attendance)ent;
                tvDatetime.setText(obj.datetime);
                tvSession.setText(obj.session);
                tvDuration.setText(obj.duration);
                tvResult.setText(obj.result);
                tvLeaveRequest.setText(obj.leaverequest);
                tvOther.setText(obj.other);
                return view;
            }
        });
        lsvInfo.setAdapter(adapter);

        lsvInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if(needReturn)
                    ReturnItemId(((Attendance)adapter.data.get(position)).id);
            }
        });
    }

    private  void ReturnItemId(int id){
        Intent intent=new Intent();
        intent.putExtra(ActivityHelper.KEY_ID,id);
        setResult(0x101,intent);
        finish();
    }
}
