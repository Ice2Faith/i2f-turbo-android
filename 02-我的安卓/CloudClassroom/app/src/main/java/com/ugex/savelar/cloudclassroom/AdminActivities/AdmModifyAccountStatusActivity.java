package com.ugex.savelar.cloudclassroom.AdminActivities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ugex.savelar.cloudclassroom.Entities.EntityAdmin;
import com.ugex.savelar.cloudclassroom.Entities.EntityPerson;
import com.ugex.savelar.cloudclassroom.Entities.EntityStudent;
import com.ugex.savelar.cloudclassroom.Entities.EntityTeacher;
import com.ugex.savelar.cloudclassroom.R;
import com.ugex.savelar.cloudclassroom.Tools.UtilHelper;
import com.ugex.savelar.cloudclassroom.pubActivities.ModifyPubSelfInfoActivity;

import java.util.List;

public class AdmModifyAccountStatusActivity extends Activity {
    private List<EntityPerson> showPersonList;
    private List<EntityAdmin> adminList;
    private List<EntityTeacher> teacherList;
    private List<EntityStudent> studentList;
    private RadioGroup rgType;
    private TextView tvName;
    private TextView tvNo;
    private TextView tvAccount;
    private CheckBox ckbStatus;
    private ListView lsvPerson;
    private EntityPerson selPerson;
    private MyPersonListViewAdapter adapter;
    private String curType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_modify_account_status);

        InitActivity();
    }

    private void InitActivity() {
        rgType=(RadioGroup)findViewById(R.id.radioGroupType);
        tvName=(TextView)findViewById(R.id.textViewName);
        tvNo=(TextView)findViewById(R.id.textViewNo);
        tvAccount=(TextView)findViewById(R.id.textViewAccount);
        ckbStatus=(CheckBox)findViewById(R.id.checkBoxStatus);
        lsvPerson=(ListView)findViewById(R.id.listViewPersones);


        ckbStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    selPerson.Cstatus="true";
                }else{
                    selPerson.Cstatus="false";
                }
                selPerson.updateToDb(AdmModifyAccountStatusActivity.this.getContentResolver());
                updateSelectDetailShow();
            }
        });

        adminList=EntityAdmin.getAllAdmin(getContentResolver());
        studentList=EntityStudent.getAllStudent(getContentResolver());
        teacherList=EntityTeacher.getAllTeacher(getContentResolver());

        rgType.check(UtilHelper.LoginTypeValue.getTypeIdByType(UtilHelper.LoginTypeValue.TYPE_ADMIN));
        adapter=new MyPersonListViewAdapter(adminList);
        lsvPerson.setAdapter(adapter);

        lsvPerson.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selPerson=(EntityPerson) adapter.getItem(position);
                updateSelectDetailShow();
            }
        });

        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setShowData(UtilHelper.LoginTypeValue.getTypeStringById(checkedId));
            }
        });
    }

    private void setShowData(String type){
        if(UtilHelper.LoginTypeValue.TYPE_ADMIN.equals(type)){
            adapter.setDataList(adminList);
        }else if(UtilHelper.LoginTypeValue.TYPE_TEACHER.equals(type)){
            adapter.setDataList(teacherList);
        }else if(UtilHelper.LoginTypeValue.TYPE_STUDENT.equals(type)){
            adapter.setDataList(studentList);
        }
        curType=type;
        selPerson=null;
        updateSelectDetailShow();
    }

    private void updateSelectDetailShow(){
        if(selPerson==null){
            tvName.setText("姓名");
            tvAccount.setText("账号");
            tvNo.setText("学号、工号");
            return;
        }

        tvName.setText(UtilHelper.stringToEmptyWhenNull(selPerson.Cname));
        tvAccount.setText(UtilHelper.stringToEmptyWhenNull(selPerson.Caccount));
        if(UtilHelper.LoginTypeValue.TYPE_STUDENT.equals(curType)){
            EntityStudent stu=(EntityStudent)selPerson;
            tvNo.setText(UtilHelper.stringToEmptyWhenNull(stu.Cpno));
        }else if(UtilHelper.LoginTypeValue.TYPE_TEACHER.equals(curType)){
            EntityTeacher tch=(EntityTeacher)selPerson;
            tvNo.setText(UtilHelper.stringToEmptyWhenNull(tch.Cpno));
        }
        if("true".equals(selPerson.Cstatus)){
            ckbStatus.setChecked(true);
        }else if("false".equals(selPerson.Cstatus)){
            ckbStatus.setChecked(false);
        }
    }
    public void OnClickedToDetailTextView(View view){
        if(selPerson==null)
            return;
        Intent intent=new Intent(this, ModifyPubSelfInfoActivity.class);
        intent.putExtra(UtilHelper.ExtraKey.ACCOUNT,selPerson.Caccount);
        intent.putExtra(UtilHelper.ExtraKey.AC_TYPE,UtilHelper.LoginTypeValue.getTypeStringById(rgType.getCheckedRadioButtonId()));
        intent.putExtra(ModifyPubSelfInfoActivity.EXTRA_KEY_ONLY_VIEW_INFO,"only_view");
        startActivity(intent);
    }

    class MyPersonListViewAdapter extends BaseAdapter{
        private List<? extends EntityPerson> lstData;
        public MyPersonListViewAdapter(List<? extends EntityPerson> lst){
            this.lstData=lst;
        }
        public void setDataList(List<? extends EntityPerson> lst){
            this.lstData=lst;
            this.notifyDataSetChanged();
        }
        @Override
        public int getCount() {
            return lstData.size();
        }

        @Override
        public Object getItem(int position) {
            return lstData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view= LayoutInflater.from(AdmModifyAccountStatusActivity.this)
                    .inflate(R.layout.lsv_item_adm_modify_account_status,null);
            TextView ptvName=(TextView)(view.findViewById(R.id.textViewName));
            TextView ptvAccount=(TextView)(view.findViewById(R.id.textViewAccount));
            ptvName.setText(lstData.get(position).Cname);
            ptvAccount.setText(lstData.get(position).Caccount);
            return view;
        }
    }
}
