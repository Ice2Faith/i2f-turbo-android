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

import com.ugex.savelar.cloudclassroom.Entities.EntityHomework;
import com.ugex.savelar.cloudclassroom.Entities.EntityStudent;
import com.ugex.savelar.cloudclassroom.R;
import com.ugex.savelar.cloudclassroom.Tools.UtilHelper;

import java.util.ArrayList;
import java.util.List;

public class SelectOrViewHomeworkActivity extends Activity {
    public static final String EXTRA_KEY_REQUIRE_SELECT_ONE_STU_PNO="require_one";
    public static final String EXTRA_KEY_RESULT_SELECT_ID_INT="result_id";
    public static final int RESULT_CODE_SELECT_ONE=0x101;
    private ListView lsvShow;
    private List<EntityHomework> dataList =new ArrayList<EntityHomework>();
    private MyHomeworkAdapter dataAdapter;
    private boolean isNeedSetResult=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_or_view_homework);

        InitActivity();
    }

    private void InitActivity() {
        String stno=getIntent().getStringExtra(EXTRA_KEY_REQUIRE_SELECT_ONE_STU_PNO);
        if(UtilHelper.stringIsNullOrEmpty(stno)==false){
            isNeedSetResult=true;
            dataList =EntityHomework.getStudentHomework(getContentResolver(),stno);
        }
        lsvShow =(ListView)findViewById(R.id.listViewShow);
        dataAdapter =new MyHomeworkAdapter();
        lsvShow.setAdapter(dataAdapter);

        lsvShow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(isNeedSetResult){
                    EntityHomework selEntity=(EntityHomework) (dataAdapter.getItem(position));
                    setActivityResultAndClose(selEntity);
                }
            }
        });
    }

    private void setActivityResultAndClose(EntityHomework selEntity){
        Intent intent=new Intent();
        intent.putExtra(EXTRA_KEY_RESULT_SELECT_ID_INT,selEntity.Cid);
        this.setResult(RESULT_CODE_SELECT_ONE,intent);
        this.finish();
    }

    class MyHomeworkAdapter extends BaseAdapter{

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
            View view= LayoutInflater.from(SelectOrViewHomeworkActivity.this)
                    .inflate(R.layout.lsv_item_homework_show,null);

            TextView ptvName=(TextView)(view.findViewById(R.id.textViewSelectSname));
            TextView ptvNo=(TextView)(view.findViewById(R.id.textViewSelectSno));
            TextView ptvDate=(TextView)(view.findViewById(R.id.textViewSelectTime));
            TextView ptvProblem=(TextView)(view.findViewById(R.id.textViewSelectProblem));
            TextView ptvScore=(TextView)(view.findViewById(R.id.textViewSelectScore));
            TextView ptvComment=(TextView)(view.findViewById(R.id.textViewSelectComment));
            TextView ptvOther=(TextView)(view.findViewById(R.id.textViewSelectOther));
            EntityHomework acc= dataList.get(position);
            EntityStudent stu=new EntityStudent();
            stu.Cpno=acc.Cstno;
            if(stu.getPersonAccountByNo(getContentResolver())){
                stu.getDataFromDb(getContentResolver());
            }
            ptvName.setText(UtilHelper.stringToEmptyWhenNull(stu.Cname));
            ptvNo.setText(UtilHelper.stringToEmptyWhenNull(stu.Cpno));
            ptvDate.setText(UtilHelper.stringToEmptyWhenNull(acc.Cdatetime));
            ptvProblem.setText(UtilHelper.stringToEmptyWhenNull(acc.Cproblem));
            ptvScore.setText(UtilHelper.stringToEmptyWhenNull(acc.Cscore));
            ptvComment.setText(UtilHelper.stringToEmptyWhenNull(acc.Ccomment));
            ptvOther.setText(UtilHelper.stringToEmptyWhenNull(acc.Cother));
            return view;
        }
    }
}
