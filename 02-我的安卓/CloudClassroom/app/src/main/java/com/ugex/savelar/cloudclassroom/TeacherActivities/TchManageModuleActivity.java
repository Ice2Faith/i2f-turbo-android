package com.ugex.savelar.cloudclassroom.TeacherActivities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.ugex.savelar.cloudclassroom.Entities.EntityClass;
import com.ugex.savelar.cloudclassroom.Entities.EntityStudent;
import com.ugex.savelar.cloudclassroom.Entities.EntityTeacher;
import com.ugex.savelar.cloudclassroom.Tools.UtilHelper;

import java.util.ArrayList;
import java.util.List;

public abstract class TchManageModuleActivity extends Activity {
    protected Context context;
    protected EntityTeacher teacher;
    protected Spinner spnClass;
    protected Spinner spnStudent;
    protected List<EntityClass> classList;
    protected List<EntityStudent> studentList=new ArrayList<EntityStudent>();;
    protected MyClassAdapter classAdapter;
    protected MyStudentAdapter studentAdapter;
    protected EntityStudent selStudent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onSetContentViewProc();
        InitAbsActivity();
        onInitActivity();
    }
    private void InitAbsActivity(){
        onSetContentValueProc();
        teacher=new EntityTeacher(getIntent().getStringExtra(UtilHelper.ExtraKey.ACCOUNT));
        teacher.getDataFromDb(getContentResolver());

        onBindClassAndStudentSpinnerProc();

        classList=EntityClass.getAllClass(getContentResolver());
        classAdapter=new MyClassAdapter();
        spnClass.setAdapter(classAdapter);
        studentAdapter=new MyStudentAdapter();
        spnStudent.setAdapter(studentAdapter);


        spnClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                studentList=EntityStudent.getStudentByClass(context.getContentResolver(),
                        ((EntityClass)(classAdapter.getItem(position))).Cpno);
                studentAdapter.notifyDataSetChanged();
                onSpnClassSelectedProc(parent, view, position, id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnStudent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selStudent=studentList.get(spnStudent.getSelectedItemPosition());
                onSpnStudentSelectedProc(parent, view, position, id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    /**
     * 请在此处设置布局:setContentView()
     */
    protected abstract void onSetContentViewProc();

    /**
     * 在此处进行您的Activity的其他操作
     */
    protected abstract void onInitActivity();
    /**
     * 请在此处设置content值
     */
    protected  abstract void onSetContentValueProc();

    /**
     * 请在此处绑定spnClass和spnStudent
     */
    protected  abstract void onBindClassAndStudentSpinnerProc();

    /**
     * 你可以在此处理当SpnClass选择之后的额外操作，参数同spinner的选择接口参数
     */
    protected  abstract void onSpnClassSelectedProc(AdapterView<?> parent, View view, int position, long id);
    /**
     * 你可以在此处理当SpnStudent选择之后的额外操作，参数同spinner的选择接口参数
     */
    protected  abstract void onSpnStudentSelectedProc(AdapterView<?> parent, View view, int position, long id);

    class MyClassAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return classList.size();
        }

        @Override
        public Object getItem(int position) {
            return classList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view=new TextView(context);
            view.setText(classList.get(position).Cname);
            return view;
        }
    }

    class MyStudentAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return studentList.size();
        }

        @Override
        public Object getItem(int position) {
            return studentList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view=new TextView(context);
            view.setText(studentList.get(position).Cname+" : "+studentList.get(position).Cpno);
            return view;
        }
    }
}
