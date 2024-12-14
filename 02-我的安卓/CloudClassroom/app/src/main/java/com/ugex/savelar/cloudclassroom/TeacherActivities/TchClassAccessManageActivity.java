package com.ugex.savelar.cloudclassroom.TeacherActivities;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ugex.savelar.cloudclassroom.Entities.EntityClassAccess;
import com.ugex.savelar.cloudclassroom.R;
import com.ugex.savelar.cloudclassroom.Tools.UtilHelper;
import com.ugex.savelar.cloudclassroom.pubActivities.SelectOrViewClassAccessActivity;

public class TchClassAccessManageActivity extends TchManageModuleActivity {
    private Resources res;
    private static final int CHIOCE_CLASSACCESS_REQUIRE_CODE =0x101;
    private TextView tvDate;
    private EditText edtScore;
    private  EditText edtSession;
    private EditText edtOther;
    private EntityClassAccess selEntity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSetContentViewProc() {
        setContentView(R.layout.activity_tch_class_access_manage);
    }

    @Override
    protected void onSetContentValueProc() {
        this.context=this;
    }

    @Override
    protected void onBindClassAndStudentSpinnerProc() {
        spnClass=(Spinner)findViewById(R.id.spinnerClass);
        spnStudent=(Spinner)findViewById(R.id.spinnerStudent);
    }

    @Override
    protected void onSpnClassSelectedProc(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected void onSpnStudentSelectedProc(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected void onInitActivity() {
        res=getResources();
        tvDate =(TextView)findViewById(R.id.textViewSelectTime);
        edtScore =(EditText)findViewById(R.id.editTextScore);
        edtSession =(EditText)findViewById(R.id.editTextSession);
        edtOther =(EditText)findViewById(R.id.editTextOther);

    }
    private void showAccessToView(){
        if(selEntity ==null) {
            tvDate.setText(res.getString(R.string.click_to_select_time));
            edtScore.setText("");
            edtSession.setText("");
            edtOther.setText("");
            return;
        }
        tvDate.setText(UtilHelper.stringToEmptyWhenNull(selEntity.Cdatetime));
        edtScore.setText(UtilHelper.stringToEmptyWhenNull(selEntity.Cscore));
        edtSession.setText(UtilHelper.stringToEmptyWhenNull(selEntity.Csession));
        edtOther.setText(UtilHelper.stringToEmptyWhenNull(selEntity.Cother));
    }

    public void OnClickedSelectClassAccessTimeTextView(View view) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            DatePickerDialog dlg=new DatePickerDialog(this);
            dlg.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker v, int year, int month, int dayOfMonth) {
                    tvDate.setText(year+UtilHelper.DATE_SPLIT_OR_JOIN_FLAG+(1+month)+UtilHelper.DATE_SPLIT_OR_JOIN_FLAG+dayOfMonth);
                }
            });
            dlg.show();
        }
    }

    public void OnClickedChoiceButton(View view){
        selStudent=studentList.get(spnStudent.getSelectedItemPosition());
        Intent intent=new Intent(this, SelectOrViewClassAccessActivity.class);
        intent.putExtra(UtilHelper.ExtraKey.ACCOUNT,selStudent.Caccount);
        intent.putExtra(SelectOrViewClassAccessActivity.EXTRA_KEY_REQUIRE_SELECT_ONE_STU_PNO,selStudent.Cpno);
        startActivityForResult(intent, CHIOCE_CLASSACCESS_REQUIRE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode== CHIOCE_CLASSACCESS_REQUIRE_CODE && resultCode==SelectOrViewClassAccessActivity.RESULT_CODE_SELECT_ONE){
            int acid=data.getIntExtra(SelectOrViewClassAccessActivity.EXTRA_KEY_RESULT_SELECT_ID_INT,-1);
            if(acid!=-1) {
                selEntity = new EntityClassAccess();
                selEntity.Cid = acid;
                selEntity.getDataFromDb(getContentResolver());
                showAccessToView();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void OnClickedDeleteButton(View view){
        if(selEntity ==null)
            return;
        if(selEntity.deleteFromDb(getContentResolver())){
            Toast.makeText(this, res.getString(R.string.delete_record_success), Toast.LENGTH_SHORT).show();
            selEntity =null;
            showAccessToView();
        }else{
            Toast.makeText(this, res.getString(R.string.delete_record_failure_unkown_reason), Toast.LENGTH_SHORT).show();
        }
    }

    public void OnClickedModifyButton(View view){
        WriteAccessToDb(true);
    }

    public void OnClickedSubmitButton(View view) {
        WriteAccessToDb(false);
    }

    private void WriteAccessToDb(boolean onlyModify){
        selStudent=studentList.get(spnStudent.getSelectedItemPosition());
        if(selStudent==null){
            Toast.makeText(this, res.getString(R.string.pls_retry_after_select_student), Toast.LENGTH_SHORT).show();
            return;
        }
        String pdate= tvDate.getText().toString().trim();
        String pscore= edtScore.getText().toString().trim();
        String psession= edtSession.getText().toString().trim();
        String pother= edtOther.getText().toString().trim();
        if(UtilHelper.stringIsNullOrEmpty(pdate)){
            Toast.makeText(this, res.getString(R.string.pls_fill_time), Toast.LENGTH_SHORT).show();
            return;
        }
        if(UtilHelper.stringIsNullOrEmpty(pscore)){
            Toast.makeText(this, res.getString(R.string.pls_fill_score), Toast.LENGTH_SHORT).show();
            return;
        }
        if(UtilHelper.stringIsNullOrEmpty(psession)){
            Toast.makeText(this, res.getString(R.string.pls_fill_session), Toast.LENGTH_SHORT).show();
            return;
        }
        EntityClassAccess ent=new EntityClassAccess();
        if(onlyModify){
            if(selEntity ==null){
                Toast.makeText(this, res.getString(R.string.current_not_select_any_item), Toast.LENGTH_SHORT).show();
                return;
            }
            ent.Cid= selEntity.Cid;
        }
        ent.Cthno=teacher.Cpno;
        ent.Cstno=selStudent.Cpno;
        ent.Cdatetime=pdate;
        ent.Cscore=pscore;
        ent.Csession=psession;
        ent.Cother=pother;
        UtilHelper.Debug.Logout(""+ent.Cthno+":"+ent.Cstno);
        if(ent.updateToDb(getContentResolver())){
            Toast.makeText(this, res.getString(R.string.add_record_success), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, res.getString(R.string.add_record_failure_unkown_reason), Toast.LENGTH_SHORT).show();
        }
    }

}
