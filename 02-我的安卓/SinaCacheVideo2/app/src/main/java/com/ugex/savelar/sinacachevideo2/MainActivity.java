package com.ugex.savelar.sinacachevideo2;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    public static class SrcDirAdpater extends BaseAdapter{
        private Context context;
        public static final String[][] dirs={
                {"根文件夹下载","sina/weibo/storage/video_download/video"},
                {"根文件夹缓存","sina/weibo/storage/video_play_cache"},
                {"Android/Data下载","Android/data/com.sina.weibo/files/sina/weibo/storage/video_download/video"},
                {"Android/data缓存","Android/data/com.sina.weibo/files/sina/weibo/storage/video_play_cache"}
        };
        public SrcDirAdpater(Context context){
            this.context=context;
        }
        @Override
        public int getCount() {
            return dirs.length;
        }

        @Override
        public Object getItem(int position) {
            return dirs[position][1];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view=null;
            if(convertView!=null){
                view=(TextView)convertView;
            }else{
                view=new TextView(context);
                view.setHeight(120);
                view.setTextColor(Color.BLACK);
                view.setTextSize(18);
            }
            view.setText(dirs[position][0]+":"+dirs[position][1]);

            return view;
        }
    }

    public static final String DEF_SAVE_DIR="01-sina-videos";

    public Spinner spnSrcDir;
    public EditText edtSavePath;
    public CheckBox ckbDelParent;

    public SrcDirAdpater adpater;

    public static final String VIDEO_FILE_NAME_PATTEN="[A-Z0-9]+--[0-9]+-1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initActivity();
    }

    private void initActivity() {
        requesPermissions();
        spnSrcDir=findViewById(R.id.spinnerSrcDir);

        adpater=new SrcDirAdpater(this);
        spnSrcDir.setAdapter(adpater);

        edtSavePath=findViewById(R.id.editTextSavePath);
        File defPath=new File(Environment.getExternalStorageDirectory(),DEF_SAVE_DIR);
        edtSavePath.setText(defPath.getAbsolutePath());

        ckbDelParent=findViewById(R.id.checkBoxDelSrcParent);
        ckbDelParent.setChecked(true);
    }

    private void requesPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
            },0x101);
        }
    }

    public int moveSinaFiles(File srcPath,File dstPath,boolean delParent){
        if(!dstPath.exists()){
            dstPath.mkdirs();
        }
        List<File> files=getDirVideoFiles(srcPath);
        return moveFiles2(files,dstPath,delParent);
    }

    public List<File> getDirVideoFiles(File path){
        List<File> ret=new ArrayList<>();
        if(path.exists() && path.isDirectory()){
            File[] paths=path.listFiles();
            for(File dir : paths){
                if(dir.isDirectory()){
                    File[] vps=dir.listFiles();
                    for(File file : vps){
                        if(file.getName().matches(VIDEO_FILE_NAME_PATTEN)){
                            ret.add(file);
                        }
                    }
                }
            }
        }

        return ret;
    }

    public int moveFiles2(List<File> files,File dstPath,boolean delParentDir){
        int ret=0;
        Set<File> srcParentPaths=new HashSet<>();
        for(File pf : files){
            String pn=pf.getName();
            if(pn.endsWith("-0")){
                pn=pn.substring(0,pn.lastIndexOf("-0"))+".mp4";
            }else{
                pn=pn+".mp4";
            }

            srcParentPaths.add(pf.getParentFile());

            File dstFile=new File(dstPath,pn);

            pf.renameTo(dstFile);
            ret++;
        }
        if(delParentDir){
            for (File dir : srcParentPaths){
                deleteAbsFile(dir);
            }
        }
        return ret;
    }

    public void sinaSave(View view) {
        String selectDir=(String)spnSrcDir.getSelectedItem();
        File srcPath= new File(Environment.getExternalStorageDirectory(),selectDir);

        File dstPath=null;
        String save=edtSavePath.getText().toString().trim();
        if(save.startsWith("/")==false){
            dstPath=new File(Environment.getExternalStorageDirectory(),save);
        }else{
            dstPath=new File(save);
        }
        if(dstPath.exists()==false){
            dstPath.mkdirs();
        }
        int count=moveSinaFiles(srcPath,dstPath,ckbDelParent.isChecked());
        Toast.makeText(this, count+"个文件转存已完成", Toast.LENGTH_SHORT).show();
    }

    public void deleteAbsFile(File file){
        if(!file.exists()){
            return;
        }
        //如果是文件夹，先递归删除子项
        if(file.isDirectory()){
            File[] files=file.listFiles();
            if(files!=null && files.length>0){
                for(File pf : files){
                    deleteAbsFile(pf);
                }
            }
        }
        file.delete();
    }
}
