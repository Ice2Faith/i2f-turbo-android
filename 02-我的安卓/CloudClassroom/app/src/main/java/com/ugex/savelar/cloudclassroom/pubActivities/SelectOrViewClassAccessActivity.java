package com.ugex.savelar.cloudclassroom.pubActivities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ugex.savelar.cloudclassroom.Entities.EntityClassAccess;
import com.ugex.savelar.cloudclassroom.Entities.EntityStudent;
import com.ugex.savelar.cloudclassroom.R;
import com.ugex.savelar.cloudclassroom.Tools.UtilHelper;

import java.util.ArrayList;
import java.util.List;

public class SelectOrViewClassAccessActivity extends Activity {
    public static final String EXTRA_KEY_REQUIRE_SELECT_ONE_STU_PNO="require_one";
    public static final String EXTRA_KEY_RESULT_SELECT_ID_INT="result_id";
    public static final int RESULT_CODE_SELECT_ONE=0x101;
    private ListView lsvShow;
    private List<EntityClassAccess> dataList =new ArrayList<EntityClassAccess>();
    private MyAccessAdapter dataAdapter;
    private boolean isNeedSetResult=false;
    private EntityStudent student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_or_view_class_access);

        InitActivity();
    }

    private void InitActivity() {
        student =new EntityStudent(getIntent().getStringExtra(UtilHelper.ExtraKey.ACCOUNT));
        student.getDataFromDb(getContentResolver());
        dataList =EntityClassAccess.getStudentClassAccess(getContentResolver(), student.Cpno);
        if(UtilHelper.stringIsNullOrEmpty(getIntent().getStringExtra(EXTRA_KEY_REQUIRE_SELECT_ONE_STU_PNO))==false){
            isNeedSetResult=true;
        }
        lsvShow =(ListView)findViewById(R.id.listViewShow);
        dataAdapter =new MyAccessAdapter();
        lsvShow.setAdapter(dataAdapter);

        lsvShow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               if(isNeedSetResult){
                   EntityClassAccess selEntity=(EntityClassAccess) (dataAdapter.getItem(position));
                   setActivityResultAndClose(selEntity);
               }
            }
        });
    }

    private void setActivityResultAndClose(EntityClassAccess selEntity){
        Intent intent=new Intent();
        intent.putExtra(EXTRA_KEY_RESULT_SELECT_ID_INT,selEntity.Cid);
        this.setResult(RESULT_CODE_SELECT_ONE,intent);
        this.finish();
    }

    class  MyAccessAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view= LayoutInflater.from(SelectOrViewClassAccessActivity.this)
                    .inflate(R.layout.lsv_item_class_access_show,null);
            TextView ptvName=(TextView)(view.findViewById(R.id.textViewSelectSname));
            TextView ptvNo=(TextView)(view.findViewById(R.id.textViewSelectSno));
            TextView ptvDate=(TextView)(view.findViewById(R.id.textViewSelectTime));
            TextView ptvScore=(TextView)(view.findViewById(R.id.textViewSelectScore));
            TextView ptvSession=(TextView)(view.findViewById(R.id.textViewSelectSession));
            TextView ptvOther=(TextView)(view.findViewById(R.id.textViewSelectOther));
            EntityClassAccess acc= dataList.get(position);
            EntityStudent stu=new EntityStudent();
            stu.Cpno=acc.Cstno;
            if(stu.getPersonAccountByNo(getContentResolver())){
                stu.getDataFromDb(getContentResolver());
            }
            ptvName.setText(UtilHelper.stringToEmptyWhenNull(stu.Cname));
            ptvNo.setText(UtilHelper.stringToEmptyWhenNull(stu.Cpno));
            ptvDate.setText(UtilHelper.stringToEmptyWhenNull(acc.Cdatetime));
            ptvScore.setText(UtilHelper.stringToEmptyWhenNull(acc.Cscore));
            ptvSession.setText(UtilHelper.stringToEmptyWhenNull(acc.Csession));
            ptvOther.setText(UtilHelper.stringToEmptyWhenNull(acc.Cother));
            return view;
        }
    }
}
