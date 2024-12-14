package com.ugex.savelar.cloudclassroom.StudentActivities;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ugex.savelar.cloudclassroom.Entities.EntityClass;
import com.ugex.savelar.cloudclassroom.Entities.EntityStudent;
import com.ugex.savelar.cloudclassroom.R;
import com.ugex.savelar.cloudclassroom.Tools.UtilHelper;

import java.util.List;

public class StuModifyDetialInfoActivity extends Activity {
    private Resources res;
    private EntityStudent student;
    private EditText edtNo;
    private EditText edtInyear;
    private EditText edtCollege;
    private EditText edtDepartment;
    private EditText edtProfession;
    private Spinner spnClass;
    private List<EntityClass> classList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_modify_detial_info);

        InitActivity();
    }

    private void InitActivity() {
        res=getResources();
        edtNo=(EditText)findViewById(R.id.editTextNo);
        edtInyear=(EditText)findViewById(R.id.editTextInyear);
        edtCollege=(EditText)findViewById(R.id.editTextCollege);
        edtDepartment=(EditText)findViewById(R.id.editTextDepartment);
        edtProfession=(EditText)findViewById(R.id.editTextProfession);
        spnClass=(Spinner)findViewById(R.id.spinnerClass);

        classList=EntityClass.getAllClass(getContentResolver());
        spnClass.setAdapter(new MyClassSpinnerAdapter());

        Intent intent=getIntent();
        student=new EntityStudent(intent.getStringExtra(UtilHelper.ExtraKey.ACCOUNT));
        student.getDataFromDb(getContentResolver());
        tryRecoveryDataToView();
    }

    private void tryRecoveryDataToView() {
        if(UtilHelper.stringIsNullOrEmpty(student.Cpno)==false){
            edtNo.setText(student.Cpno);
            edtNo.setEnabled(false);
        }
        if(UtilHelper.stringIsNullOrEmpty(student.Cinyear)==false){
            edtInyear.setText(student.Cinyear);
        }
        if(UtilHelper.stringIsNullOrEmpty(student.Ccollege)==false){
            edtCollege.setText(student.Ccollege);
        }
        if(UtilHelper.stringIsNullOrEmpty(student.Cdepartment)==false){
            edtDepartment.setText(student.Cdepartment);
        }
        if(UtilHelper.stringIsNullOrEmpty(student.Cprofession)==false){
            edtProfession.setText(student.Cprofession);
        }
        if(UtilHelper.stringIsNullOrEmpty(student.Ccsno)==false){
            for(int i=0;i<classList.size();i++){
                if(classList.get(i).Cpno.equals(student.Ccsno)){
                    spnClass.setSelection(i);
                    break;
                }
            }
        }
    }

    public void OnClickedModifyInfoButton(View view) {
        if(UtilHelper.stringIsNullOrEmpty(student.Cpno)){
            String pno=edtNo.getText().toString().trim();
            if(pno.length()>0){
                student.Cpno=pno;
                student.Cstatus="true";
            }
        }
        String pinyear=edtInyear.getText().toString().trim();
        String pcollege=edtCollege.getText().toString().trim();
        String pdepart=edtDepartment.getText().toString().trim();
        String pprofs=edtProfession.getText().toString().trim();
        String pcsno=((EntityClass)spnClass.getSelectedItem()).Cpno;
        if(pinyear.equals(student.Cinyear)==false){
            student.Cinyear=pinyear;
        }
        if(pcollege.equals(student.Ccollege)==false){
            student.Ccollege=pcollege;
        }
        if(pdepart.equals(student.Cdepartment)==false){
            student.Cdepartment=pdepart;
        }
        if(pprofs.equals(student.Cprofession)==false){
            student.Cprofession=pprofs;
        }
        if(pcsno.equals(student.Ccsno)==false){
            student.Ccsno=pcsno;
        }
        if(student.updateToDb(getContentResolver())){
            Toast.makeText(this, res.getString(R.string.info_updata_success), Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Toast.makeText(this, res.getString(R.string.info_update_failure_unkown_reason), Toast.LENGTH_SHORT).show();
        }
    }


    class MyClassSpinnerAdapter extends BaseAdapter {
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
            TextView tv=new TextView(StuModifyDetialInfoActivity.this);
            tv.setText(classList.get(position).Cname);
            return tv;
        }
    }
}
