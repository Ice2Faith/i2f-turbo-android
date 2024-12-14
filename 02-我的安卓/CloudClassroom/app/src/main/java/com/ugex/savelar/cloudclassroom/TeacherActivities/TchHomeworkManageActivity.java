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

import com.ugex.savelar.cloudclassroom.Entities.EntityHomework;
import com.ugex.savelar.cloudclassroom.R;
import com.ugex.savelar.cloudclassroom.Tools.UtilHelper;
import com.ugex.savelar.cloudclassroom.pubActivities.SelectOrViewHomeworkActivity;

public class TchHomeworkManageActivity extends TchManageModuleActivity {
    private Resources res;
    private static final int CHOICE_HOMEWORK_REQUIRE_CODE =0x101;
    private TextView tvDate;
    private EditText edtProblem;
    private EditText edtScore;
    private  EditText edtComment;
    private EditText edtOther;
    private EntityHomework selEntity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSetContentViewProc() {
        setContentView(R.layout.activity_tch_homework_manage);
    }

    @Override
    protected void onInitActivity() {
        res=getResources();
        tvDate =(TextView)findViewById(R.id.textViewSelectTime);
        edtProblem =(EditText)findViewById(R.id.editTextProblem);
        edtScore =(EditText)findViewById(R.id.editTextScore);
        edtComment =(EditText)findViewById(R.id.editTextComment);
        edtOther =(EditText)findViewById(R.id.editTextOther);
    }

    @Override
    protected void onSetContentValueProc() {
        context=this;
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

    private void showAccessToView(){
        if(selEntity ==null) {
            tvDate.setText(res.getString(R.string.click_to_select_time));
            edtProblem.setText("");
            edtScore.setText("");
            edtComment.setText("");
            edtOther.setText("");
            return;
        }
        tvDate.setText(UtilHelper.stringToEmptyWhenNull(selEntity.Cdatetime));
        edtProblem.setText(UtilHelper.stringToEmptyWhenNull(selEntity.Cproblem));
        edtScore.setText(UtilHelper.stringToEmptyWhenNull(selEntity.Cscore));
        edtComment.setText(UtilHelper.stringToEmptyWhenNull(selEntity.Ccomment));
        edtOther.setText(UtilHelper.stringToEmptyWhenNull(selEntity.Cother));
    }


    public void OnClickedSelectHomeworkTimeTextView(View view) {
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

    public void OnClickedChoiceButton(View view) {
        selStudent=studentList.get(spnStudent.getSelectedItemPosition());
        Intent intent=new Intent(this, SelectOrViewHomeworkActivity.class);
        intent.putExtra(UtilHelper.ExtraKey.ACCOUNT,selStudent.Caccount);
        intent.putExtra(SelectOrViewHomeworkActivity.EXTRA_KEY_REQUIRE_SELECT_ONE_STU_PNO,selStudent.Cpno);
        startActivityForResult(intent, CHOICE_HOMEWORK_REQUIRE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode== CHOICE_HOMEWORK_REQUIRE_CODE && resultCode==SelectOrViewHomeworkActivity.RESULT_CODE_SELECT_ONE){
            int acid=data.getIntExtra(SelectOrViewHomeworkActivity.EXTRA_KEY_RESULT_SELECT_ID_INT,-1);
            if(acid!=-1) {
                selEntity = new EntityHomework();
                selEntity.Cid = acid;
                selEntity.getDataFromDb(getContentResolver());
                showAccessToView();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void OnClickedModifyButton(View view) {
        WriteAccessToDb(true);
    }

    public void OnClickedSubmitButton(View view) {
        WriteAccessToDb(false);
    }

    public void OnClickedDeleteButton(View view) {
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

    private void WriteAccessToDb(boolean onlyModify){
        selStudent=studentList.get(spnStudent.getSelectedItemPosition());
        if(selStudent==null){
            Toast.makeText(this, res.getString(R.string.pls_retry_after_select_student), Toast.LENGTH_SHORT).show();
            return;
        }
        String pdate= tvDate.getText().toString().trim();
        String pproblem= edtProblem.getText().toString().trim();
        String pscore= edtScore.getText().toString().trim();
        String pcomment= edtComment.getText().toString().trim();
        String pother= edtOther.getText().toString().trim();
        if(UtilHelper.stringIsNullOrEmpty(pdate)){
            Toast.makeText(this, res.getString(R.string.pls_fill_time), Toast.LENGTH_SHORT).show();
            return;
        }
        if(UtilHelper.stringIsNullOrEmpty(pproblem)){
            Toast.makeText(this, res.getString(R.string.pls_fill_problem), Toast.LENGTH_SHORT).show();
            return;
        }
        if(UtilHelper.stringIsNullOrEmpty(pscore)){
            Toast.makeText(this, res.getString(R.string.pls_fill_score), Toast.LENGTH_SHORT).show();
            return;
        }

        EntityHomework ent=new EntityHomework();
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
        ent.Cproblem=pproblem;
        ent.Cscore=pscore;
        ent.Ccomment=pcomment;
        ent.Cother=pother;
        UtilHelper.Debug.Logout(""+ent.Cthno+":"+ent.Cstno);
        if(ent.updateToDb(getContentResolver())){
            Toast.makeText(this, res.getString(R.string.add_record_success), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, res.getString(R.string.add_record_failure_unkown_reason), Toast.LENGTH_SHORT).show();
        }
    }
}
