package com.i2f.fs.win;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int REQ_CODE_PERMISSION_REQUIRE=0x101;

    private File curDir;
    private Spinner spnDir;
    private EditText edtDir;
    private Button btnEnterDir;
    private ArrayList<File> spnDirList=new ArrayList<>();
    private SpnDirAdapter spnDirAdapter;

    private EditText edtSelectPath;
    private Button btnWinSafe;

    private ListView lstFiles;
    private ArrayList<File> filesList=new ArrayList<>();
    private LsvFilesAdapter lsvFilesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requirePermission();
        initComponents();
    }

    private void requirePermission(){
        Toast.makeText(this,"软件需要权限以保证正常运行：\n" +
                "\t2.存储读取权限：用于读取或存储文件",Toast.LENGTH_LONG).show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE
            },REQ_CODE_PERMISSION_REQUIRE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQ_CODE_PERMISSION_REQUIRE){
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i]!= PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"权限：["+permissions[i]+"]没有授权，软件可能无法正常运行！",Toast.LENGTH_LONG).show();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initComponents(){
        spnDir=findViewById(R.id.spnDir);
        edtDir=findViewById(R.id.edtDir);
        btnEnterDir=findViewById(R.id.btnEnterDir);

        edtSelectPath=findViewById(R.id.edtSelectPath);
        btnWinSafe=findViewById(R.id.btnWinSafe);

        lstFiles=findViewById(R.id.lstFiles);

        spnDirAdapter=new SpnDirAdapter();
        spnDir.setAdapter(spnDirAdapter);

        lsvFilesAdapter=new LsvFilesAdapter();
        lstFiles.setAdapter(lsvFilesAdapter);


        spnDir.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object tag = view.getTag();
                File file=(File)tag;

                if(position==0){
                    return;
                }
                refreshDir(file);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lstFiles.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Object tag = view.getTag();
                File file=(File)tag;

                if(position==0){
                    return true;
                }
                refreshDir(file);
                return true;
            }
        });

        lstFiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object tag = view.getTag();
                File file=(File)tag;
                edtSelectPath.setText(file.getAbsolutePath());
            }
        });

        curDir= Environment.getExternalStorageDirectory();
        refreshDir(curDir);
    }

    private void refreshDir(File dir){
        if(dir==null){
            return;
        }
        if(dir.isFile()){
            dir=dir.getParentFile();
        }
        curDir=dir;
        edtDir.setText(curDir.getAbsolutePath());


        List<File> tmpDir=new ArrayList<>();
        List<File> tmpFile=new ArrayList<>();
        File[] list=dir.listFiles();
        for(File item : list){
            if(item.isDirectory()){
                tmpDir.add(item);
            }
            if(item.isFile()){
                tmpFile.add(item);
            }
        }

        Comparator<File> fileNameComparator=new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        };

        Collections.sort(tmpDir,fileNameComparator);
        Collections.sort(tmpFile,fileNameComparator);

        spnDirList.clear();
        filesList.clear();

        filesList.add(curDir);
        filesList.add(curDir.getParentFile());
        filesList.addAll(tmpDir);
        filesList.addAll(tmpFile);

        spnDirList.add(curDir);
        spnDirList.add(curDir.getParentFile());
        spnDirList.addAll(tmpDir);

        spnDir.setSelection(0);
        spnDirAdapter.notifyDataSetChanged();
        lsvFilesAdapter.notifyDataSetChanged();
    }

    public void onBtnEnterDirClicked(View view) {
        File file=new File(edtDir.getText().toString().trim());
        if(!file.exists()){
            Toast.makeText(this, "文件不存在！", Toast.LENGTH_SHORT).show();
            return;
        }
        refreshDir(file);
    }

    public void onBtnWinSafeClicked(View view) {
        File file=new File(edtSelectPath.getText().toString().trim());
        if(!file.exists()){
            Toast.makeText(this, "文件不存在！", Toast.LENGTH_SHORT).show();
            return;
        }
        int cnt=winsafeFileName(file);
        Toast.makeText(this, cnt+"个问题已修复", Toast.LENGTH_SHORT).show();
    }

    private int winsafeFileName(File file){
        int ret=0;
        String[] includesReges={
                "\\\\",
                "\\/",
                "\\*",
                "\\?",
                "\\<",
                "\\>",
                "\\|",
                ":",
                "\""
        };
        if(file.isDirectory()){
            File[] files=file.listFiles();
            for(File item : files){
                ret+=winsafeFileName(item);
            }
        }
        String name = file.getName();
        for(String reg : includesReges){
            name=name.replaceAll(reg," ");
        }
        if(!file.getName().equals(name)){
            name=name.trim();
            File nfile=new File(file.getParentFile(),name);
            file.renameTo(nfile);
            ret++;
        }
        return ret;
    }

    class SpnDirAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return spnDirList.size();
        }

        @Override
        public Object getItem(int position) {
            return spnDirList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv=(TextView)convertView;
            if(tv==null){
                tv=new TextView(MainActivity.this);
            }
            File item=spnDirList.get(position);
            String text=item.getName();
            if(position==0){
                text=".";
            }
            if(position==1){
                text="..";
            }
            tv.setTag(item);
            tv.setText(text);
            tv.setTextSize(14);
            tv.setHeight(62);
            if(item.isDirectory()){
                tv.setTextColor(Color.RED);
            }else{
                tv.setTextColor(Color.DKGRAY);
            }
            return tv;
        }
    }

    class LsvFilesAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return filesList.size();
        }

        @Override
        public Object getItem(int position) {
            return filesList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv=(TextView)convertView;
            if(tv==null){
                tv=new TextView(MainActivity.this);
            }
            File item=filesList.get(position);
            String text=item.getName();
            if(position==0){
                text=".";
            }
            if(position==1){
                text="..";
            }
            tv.setTag(item);
            tv.setText(text);
            tv.setTextSize(16);
            tv.setHeight(84);
            if(item.isDirectory()){
                tv.setTextColor(Color.RED);
            }else{
                tv.setTextColor(Color.DKGRAY);
            }

            return tv;
        }
    }
}
