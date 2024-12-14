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

import com.ugex.savelar.cloudclassroom.Entities.EntityPerform;
import com.ugex.savelar.cloudclassroom.Entities.EntityStudent;
import com.ugex.savelar.cloudclassroom.R;
import com.ugex.savelar.cloudclassroom.Tools.UtilHelper;

import java.util.ArrayList;
import java.util.List;

public class SelectOrViewPerformActivity extends Activity {
    public static final String EXTRA_KEY_REQUIRE_SELECT_ONE_STU_PNO="require_one";
    public static final String EXTRA_KEY_RESULT_SELECT_ID_INT="result_id";
    public static final int RESULT_CODE_SELECT_ONE=0x101;
    private ListView lsvShow;
    private List<EntityPerform> dataList =new ArrayList<EntityPerform>();
    private MyPerformAdapter dataAdapter;
    private boolean isNeedSetResult=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_or_view_perform);

        InitActivity();
    }


    private void InitActivity() {
        String stno=getIntent().getStringExtra(EXTRA_KEY_REQUIRE_SELECT_ONE_STU_PNO);
        if(UtilHelper.stringIsNullOrEmpty(stno)==false){
            isNeedSetResult=true;
            dataList =EntityPerform.getStudentPerform(getContentResolver(),stno);
        }
        lsvShow =(ListView)findViewById(R.id.listViewShow);
        dataAdapter =new MyPerformAdapter();
        lsvShow.setAdapter(dataAdapter);

        lsvShow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(isNeedSetResult){
                    EntityPerform selEntity=(EntityPerform) (dataAdapter.getItem(position));
                    setActivityResultAndClose(selEntity);
                }
            }
        });
    }

    private void setActivityResultAndClose(EntityPerform selEntity){
        Intent intent=new Intent();
        intent.putExtra(EXTRA_KEY_RESULT_SELECT_ID_INT,selEntity.Cid);
        this.setResult(RESULT_CODE_SELECT_ONE,intent);
        this.finish();
    }

    class MyPerformAdapter extends BaseAdapter {

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
            View view= LayoutInflater.from(SelectOrViewPerformActivity.this)
                    .inflate(R.layout.lsv_item_perform_show,null);

            TextView ptvName=(TextView)(view.findViewById(R.id.textViewSelectSname));
            TextView ptvNo=(TextView)(view.findViewById(R.id.textViewSelectSno));
            TextView ptvDate=(TextView)(view.findViewById(R.id.textViewSelectTime));
            TextView ptvDuration=(TextView)(view.findViewById(R.id.textViewSelectDuration));
            TextView ptvState=(TextView)(view.findViewById(R.id.textViewSelectState));
            TextView ptvReason=(TextView)(view.findViewById(R.id.textViewSelectReason));
            TextView ptvSession=(TextView)(view.findViewById(R.id.textViewSelectSession));
            TextView ptvOther=(TextView)(view.findViewById(R.id.textViewSelectOther));
            EntityPerform acc= dataList.get(position);
            EntityStudent stu=new EntityStudent();
            stu.Cpno=acc.Cstno;
            if(stu.getPersonAccountByNo(getContentResolver())){
                stu.getDataFromDb(getContentResolver());
            }
            ptvName.setText(UtilHelper.stringToEmptyWhenNull(stu.Cname));
            ptvNo.setText(UtilHelper.stringToEmptyWhenNull(stu.Cpno));
            ptvDate.setText(UtilHelper.stringToEmptyWhenNull(acc.Cdatetime));
            ptvDuration.setText(UtilHelper.stringToEmptyWhenNull(acc.Cduration));
            ptvState.setText(UtilHelper.stringToEmptyWhenNull(acc.Cstate));
            ptvReason.setText(UtilHelper.stringToEmptyWhenNull(acc.Creason));
            ptvSession.setText(UtilHelper.stringToEmptyWhenNull(acc.Csession));
            ptvOther.setText(UtilHelper.stringToEmptyWhenNull(acc.Cother));
            return view;
        }
    }
}
