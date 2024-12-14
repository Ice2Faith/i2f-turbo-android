package com.ugex.savelar.angrideviewspinner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private GridView gdMain;
    private String[] data={"王昭君","兰陵王","武则天","达摩"};
    private GridView gdSec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitApp();
        InitDynamicContent();
        InitSpinner();
    }

    private void InitApp() {
        gdMain=findViewById(R.id.GrideViewMain);
        gdSec=findViewById(R.id.GrideViewSec);
        OnlyText();
        ImageAndText();
    }

    private void OnlyText(){
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,android.R.id.text1,data);
        gdMain.setAdapter(adapter);
        gdMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,data[position],Toast.LENGTH_LONG).show();
            }
        });
    }
    private void ImageAndText(){
        gdSec.setAdapter(new MyAdapter());
    }
    class MyAdapter extends BaseAdapter{
        private int[] PicIds={R.drawable.picres_001,R.drawable.picres_002,R.drawable.picres_003,R.drawable.picres_004,R.drawable.picres_005};
        private String[] Names={"Picture1","Picture2","Picture3","Picture4","Picture5"};
        @Override
        public int getCount() {
            return Names.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=null;
            ViewItemHolder holder=null;
            if(convertView==null){
                view= LayoutInflater.from(MainActivity.this).inflate(R.layout.gridview_item,null);
                holder=new ViewItemHolder(view);
                view.setTag(holder);
            }else{
                view=convertView;
                holder=(ViewItemHolder) view.getTag();
            }
            holder.getImageView().setImageResource(PicIds[position]);
            holder.getTextView().setText(Names[position]);
            return view;
        }
    }
    class ViewItemHolder{
        private ImageView iv;
        private TextView tv;
        private View view;
        public ViewItemHolder(View view){
            this.view=view;
        }
        public ImageView getImageView(){
            if(iv==null){
                iv=view.findViewById(R.id.imageViewPic);
            }
            return iv;
        }
        public TextView getTextView(){
            if(tv==null){
                tv=view.findViewById(R.id.textViewText);
            }
            return tv;
        }
    }
//动态更新数据部分
    private EditText edtLine;
    private Button btnAdd;
    private GridView gdDisplay;
    private List<String> dataSource=new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private void InitDynamicContent(){
        edtLine=findViewById(R.id.editTextInfo);
        btnAdd=findViewById(R.id.buttonAdd);
        gdDisplay=findViewById(R.id.gridViewDisplay);
        adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,android.R.id.text1,dataSource);
        gdDisplay.setAdapter(adapter);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSource.add(edtLine.getText().toString());
                adapter.notifyDataSetChanged();//通过此方法，通知控件更新视图
                edtLine.setText("");
            }
        });
    }
    //Spinner部分
    private Spinner spinnerCls;
    private Spinner spinnerStu;
    //班级，学生信息
    private List<String> spnClass=new ArrayList<>();
    private Map<String,List<String>> spnStudent=new HashMap<>();
    //选中的学生信息
    private List<String> selectedStudent=new ArrayList<>();
    private ArrayAdapter<String> stuAdapter;
    private void FillSpinnerData() {
        spnClass.add("Android");
        spnClass.add("C#ASP");

        List<String> stuAnd=new ArrayList<>();
        stuAnd.add("Ajimi");
        stuAnd.add("Ajack");

        List<String> stuAsp=new ArrayList<>();
        stuAsp.add("Cmoli");
        stuAsp.add("Cmoguya");

        spnStudent.put(spnClass.get(0),stuAnd);
        spnStudent.put(spnClass.get(1),stuAsp);

    }
    private void InitSpinner(){
        FillSpinnerData();
        //两个spinner实现主副选项，在某个班级下有某个同学
        spinnerCls=findViewById(R.id.spinnerClass);
        spinnerStu=findViewById(R.id.spinnerStudent);

        spinnerCls.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,
                android.R.id.text1,
                spnClass));

        //这是第一种使用的方式，使用数据适配，当然也可以使用BaseAdapter
        //第二种是在布局文件中指定使用的资源，在string.xml中，添加一个string-array节点，给一个name属性，
        //然后在这个节点下添加项item节点，这些item节点就是适配的资源数据，将会显示在spinner上
        //显示是通过spinner的xml属性entries指定的，比如：@array/spinnerDis

        //根据班级显示学生
        stuAdapter=new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line,
                android.R.id.text1,
                selectedStudent);
        spinnerStu.setAdapter(stuAdapter);
        //设置数据源的更新监听
        spinnerCls.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStudent.clear();
                selectedStudent.addAll(spnStudent.get(spnClass.get(position)));
                stuAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
