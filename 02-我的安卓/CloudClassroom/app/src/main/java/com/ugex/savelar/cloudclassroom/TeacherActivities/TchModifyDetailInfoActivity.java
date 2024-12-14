package com.ugex.savelar.cloudclassroom.TeacherActivities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ugex.savelar.cloudclassroom.Entities.EntityTeacher;
import com.ugex.savelar.cloudclassroom.R;
import com.ugex.savelar.cloudclassroom.Tools.UtilHelper;

public class TchModifyDetailInfoActivity extends Activity {
    private Resources res;
    private EntityTeacher teacher;
    private EditText edtNo;
    private EditText edtCollege;
    private EditText edtDepartment;
    private EditText edtProfession;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tch_modify_detail_info);

        InitActivity();
    }

    private void InitActivity() {
        res=getResources();
        edtNo=(EditText)findViewById(R.id.editTextNo);
        edtCollege=(EditText)findViewById(R.id.editTextCollege);
        edtDepartment=(EditText)findViewById(R.id.editTextDepartment);
        edtProfession=(EditText)findViewById(R.id.editTextProfession);


        Intent intent=getIntent();
        teacher=new EntityTeacher(intent.getStringExtra(UtilHelper.ExtraKey.ACCOUNT));
        teacher.getDataFromDb(getContentResolver());
        tryRecoveryDataToView();
    }

    private void tryRecoveryDataToView() {
        if(UtilHelper.stringIsNullOrEmpty(teacher.Cpno)==false){
            edtNo.setText(teacher.Cpno);
            edtNo.setEnabled(false);
        }
        edtCollege.setText(UtilHelper.stringToEmptyWhenNull(teacher.Ccollege));
        edtDepartment.setText(UtilHelper.stringToEmptyWhenNull(teacher.Cdepartment));
        edtProfession.setText(UtilHelper.stringToEmptyWhenNull(teacher.Cprofession));
    }

    public void OnClickedModifyInfoButton(View view) {
        if(UtilHelper.stringIsNullOrEmpty(teacher.Cpno)==false){
            String pno=edtNo.getText().toString().trim();
            if(pno.length()>0){
                teacher.Cpno=pno;
            }
        }
        String pcollege=edtCollege.getText().toString().trim();
        String pdepart=edtDepartment.getText().toString().trim();
        String pprofs=edtProfession.getText().toString().trim();
        if(pcollege.equals(teacher.Ccollege)==false){
            teacher.Ccollege=pcollege;
        }
        if(pdepart.equals(teacher.Cdepartment)==false){
            teacher.Cdepartment=pdepart;
        }
        if(pprofs.equals(teacher.Cprofession)==false){
            teacher.Cprofession=pprofs;
        }
        if(teacher.updateToDb(getContentResolver())){
            Toast.makeText(this, res.getString(R.string.info_updata_success), Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Toast.makeText(this, res.getString(R.string.info_update_failure_unkown_reason), Toast.LENGTH_SHORT).show();
        }
    }
}
