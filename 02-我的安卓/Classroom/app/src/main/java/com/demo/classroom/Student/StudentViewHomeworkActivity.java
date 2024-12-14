package com.demo.classroom.Student;

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
import com.demo.classroom.Service.ServiceImpl.Homework;
import com.demo.classroom.Service.ServiceImpl.Student;
import com.demo.classroom.Util.ActivityHelper;
import com.demo.classroom.Util.ObjectAdapter;
import com.demo.classroom.Util.UtilHelper;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class StudentViewHomeworkActivity extends AppCompatActivity {
    private Student student;
    private ListView lsvInfo;
    private List<Homework> data;
    private ObjectAdapter adapter;
    private boolean needReturn=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view_homework);

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

        data=(List<Homework>)new Homework().getAllByWhere(getContentResolver(),"sno='"+student.uno+"'");
        adapter=new ObjectAdapter(this, data, new ObjectAdapter.OnRequireItemView() {
            @Override
            public View setView(IEntity ent, Context context) {
                View view= LayoutInflater.from(context).inflate(R.layout.item_list_view_homework,null);
                TextView tvDatetime=view.findViewById(R.id.textViewDatetime);
                TextView tvScore=view.findViewById(R.id.textViewScore);
                TextView tvComment=view.findViewById(R.id.textViewComment);
                TextView tvOther=view.findViewById(R.id.textViewOther);
                Homework obj=(Homework) ent;
                tvDatetime.setText(obj.datetime);
                tvScore.setText(obj.grade);
                tvComment.setText(obj.comment);
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
