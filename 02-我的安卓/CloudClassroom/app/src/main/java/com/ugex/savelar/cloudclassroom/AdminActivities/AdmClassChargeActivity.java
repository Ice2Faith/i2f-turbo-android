package com.ugex.savelar.cloudclassroom.AdminActivities;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ugex.savelar.cloudclassroom.Entities.EntityClass;
import com.ugex.savelar.cloudclassroom.Entities.EntityStudent;
import com.ugex.savelar.cloudclassroom.R;
import com.ugex.savelar.cloudclassroom.Tools.UtilHelper;

import java.util.List;

public class AdmClassChargeActivity extends Activity {
    private Resources res;
    private EditText edtClassNo;
    private EditText edtClassName;
    private ListView lsvClasses;
    private List<EntityClass> classList;
    private MyClassesAdapter adapter;
    private EntityClass selClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_class_charge);

        InitActivity();
    }

    private void InitActivity() {
        res=getResources();
        edtClassNo=(EditText)findViewById(R.id.editTextClassNo);
        edtClassName=(EditText)findViewById(R.id.editTextClassName);
        lsvClasses=(ListView)findViewById(R.id.listViewClasses);

        classList=EntityClass.getAllClass(getContentResolver());
        adapter=new MyClassesAdapter();
        lsvClasses.setAdapter(adapter);

        lsvClasses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selClass=(EntityClass)adapter.getItem(position);
                updateShowView();
            }
        });
    }
    private void updateShowView(){
        if(selClass==null){
            edtClassNo.setText("");
            edtClassName.setText("");
            return;
        }
        edtClassNo.setText(selClass.Cpno);
        edtClassName.setText(selClass.Cname);
    }
    private void updateShowList(){
        classList=EntityClass.getAllClass(getContentResolver());
        adapter.notifyDataSetChanged();
    }
    public void OnClickedAddOrModifyClassButton(View view) {
        String pno=edtClassNo.getText().toString().trim();
        String pname=edtClassName.getText().toString().trim();
        if(UtilHelper.stringIsNullOrEmpty(pno)){
            Toast.makeText(this, res.getString(R.string.pls_fill_class_code), Toast.LENGTH_SHORT).show();
            return;
        }
        if(UtilHelper.stringIsNullOrEmpty(pname)){
            Toast.makeText(this, res.getString(R.string.pls_fill_class_name), Toast.LENGTH_SHORT).show();
            return;
        }
        EntityClass cls=new EntityClass(pno);
        cls.Cname=pname;
        boolean isExsit=false;
        if(cls.hasContiansInDb(getContentResolver()))
            isExsit=true;
        if(cls.updateToDb(getContentResolver())){
            if(isExsit)
                Toast.makeText(this, res.getString(R.string.info_updata_success), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, res.getString(R.string.new_class_was_added), Toast.LENGTH_SHORT).show();
            updateShowList();
        }else{
            Toast.makeText(this, res.getString(R.string.info_update_failure_unkown_reason), Toast.LENGTH_SHORT).show();
        }
    }

    public void OnClickedDelClassButton(View view) {
        String pno=edtClassNo.getText().toString().trim();
        if(UtilHelper.stringIsNullOrEmpty(pno)){
            Toast.makeText(this, res.getString(R.string.pls_fill_class_code), Toast.LENGTH_SHORT).show();
            return;
        }
        EntityClass cls=new EntityClass(pno);
        List<EntityStudent> stulist=EntityStudent.getStudentByClass(getContentResolver(),cls.Cpno);
        if(stulist.size()>0){
            Toast.makeText(this, res.getString(R.string.delete_class_failure_for_it_exist_student), Toast.LENGTH_SHORT).show();
            return;
        }
        if(cls.deleteFromDb(getContentResolver())){
            Toast.makeText(this, res.getString(R.string.delete_record_success), Toast.LENGTH_SHORT).show();
            updateShowList();
        }else{
            Toast.makeText(this, res.getString(R.string.delete_record_failure_unkown_reason), Toast.LENGTH_SHORT).show();
        }
    }

    class MyClassesAdapter extends BaseAdapter{

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
            View view= LayoutInflater.from(AdmClassChargeActivity.this)
                    .inflate(R.layout.lsv_item_adm_class_charge,null);
            TextView ptvName=((TextView)view.findViewById(R.id.textViewName));
            TextView ptvNo=((TextView)view.findViewById(R.id.textViewNo));
            ptvName.setText(classList.get(position).Cname);
            ptvNo.setText(classList.get(position).Cpno);
            return view;
        }
    }

}
