package com.ugex.savelar.similarfilemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.ugex.savelar.similarfilemanager.core.FileInfos;
import com.ugex.savelar.similarfilemanager.core.LsvFilesAdapter;
import com.ugex.savelar.similarfilemanager.core.PathUtil;
import com.ugex.savelar.similarfilemanager.core.SpnDirListAdapter;
import com.ugex.savelar.similarfilemanager.core.SysOpenFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Spinner spnDirList;
    private RadioGroup rgpFileFilter;
    private ListView lsvFiles;

    private SpnDirListAdapter dirsAdapter;
    private LsvFilesAdapter filesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requirePermissions();
        initActivity();
    }

    private void requirePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            },0x101);
        }
    }

    private void initActivity() {
        spnDirList=findViewById(R.id.spnDirsList);
        rgpFileFilter=findViewById(R.id.rgpFileFilter);
        lsvFiles=findViewById(R.id.lsvFiles);

        rgpFileFilter.check(R.id.rdoAllFiles);

        dirsAdapter=new SpnDirListAdapter(this);
        spnDirList.setAdapter(dirsAdapter);
        refreshSpnDirs(PathUtil.getSdRoot());

        filesAdapter=new LsvFilesAdapter(this);
        lsvFiles.setAdapter(filesAdapter);

        rgpFileFilter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int position=spnDirList.getSelectedItemPosition();
                if(position>=0){
                    refreshLsvFiles(dirsAdapter.data.get(position));
                }

            }
        });

        spnDirList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                File file=dirsAdapter.data.get(position);
                refreshSpnDirs(file);
                refreshLsvFiles(file);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lsvFiles.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                FileInfos fileInfos=filesAdapter.data.get(position);
                SysOpenFile.openFile(MainActivity.this,new File(fileInfos.filePath));
                return true;
            }
        });

    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what==MSG_WAHT_LSV_FILE_DATA_DONE){
                List<FileInfos> fileInfos=(List<FileInfos>)msg.obj;
                filesAdapter.data=fileInfos;
                filesAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "分析已完成！", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private static final int MSG_WAHT_LSV_FILE_DATA_DONE = 0x101;
    public void refreshLsvFiles(final File dir){
        filesAdapter.data.clear();
        filesAdapter.notifyDataSetChanged();
        Toast.makeText(this, "请稍后...", Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<File> files=PathUtil.listFiles(dir);
                List<FileInfos> fileInfos=convert2FileInfos(files);

                Message msg=new Message();
                msg.obj=fileInfos;
                msg.what=MSG_WAHT_LSV_FILE_DATA_DONE;

                handler.sendMessage(msg);
            }
        }).start();

    }
    public List<FileInfos> convert2FileInfos(List<File> files){
        List<FileInfos> ret=new ArrayList<>();

        int ckId=rgpFileFilter.getCheckedRadioButtonId();
        boolean needCheckSum=(ckId==R.id.rdoEqualsFiles);
        for(File file : files){
            ret.add(new FileInfos(file.getAbsolutePath(),needCheckSum));
        }
        if(ckId==R.id.rdoAllFiles){
            int groupId=1;
            for(FileInfos item : ret){
                item.groupId=groupId;
                groupId++;
            }
        }
        if(ckId==R.id.rdoSizeFiles){
            Collections.sort(ret,new FileInfos.FileSizeComparator());
            int groupId=1;
            int i=0;
            while(i<ret.size()){
                FileInfos it=ret.get(i);
                int j=i+1;
                while(j<ret.size()){
                    FileInfos jt=ret.get(j);
                    if(jt.fileSize==it.fileSize){
                        jt.isRepeat=true;
                    }else{
                        break;
                    }
                    j++;
                }
                for(int k=i;k<j;k++){
                    FileInfos kt=ret.get(k);
                    kt.groupId=groupId;
                }
                groupId++;
                i=j;
            }
        }
        if(ckId==R.id.rdoEqualsFiles){
            Collections.sort(ret,new FileInfos.FileCheckSumComparator());
            int groupId=1;
            int i=0;
            while(i<ret.size()){
                FileInfos it=ret.get(i);
                int j=i+1;
                while(j<ret.size()){
                    FileInfos jt=ret.get(j);
                    if(jt.fileCheckSum==it.fileCheckSum){
                        jt.isRepeat=true;
                    }else{
                        break;
                    }
                    j++;
                }
                for(int k=i;k<j;k++){
                    FileInfos kt=ret.get(k);
                    kt.groupId=groupId;
                }
                groupId++;
                i=j;
            }
        }
        return ret;
    }

    public void refreshSpnDirs(File file){
        List<File> dirs= PathUtil.listDirs(file);
        dirs.add(0,file);
        File parent=file.getParentFile();
        if(file.getAbsolutePath().equals(PathUtil.getSdRoot().getAbsolutePath())){
            dirs.add(1,file);
        }else{
            dirs.add(1,file.getParentFile());
        }

        dirsAdapter.data=dirs;
        dirsAdapter.notifyDataSetChanged();
        spnDirList.setSelection(0);

    }


    public void btnRootDirFilesClicked(View view) {
        File root=PathUtil.getSdRoot();
        refreshSpnDirs(root);
        refreshLsvFiles(root);
    }

    public void btnCleanSimilarClicked(View view) {
        AlertDialog dialog=new AlertDialog.Builder(this)
                .setTitle("重复文件删除")
                .setMessage("确认删除重复的这些文件吗？操作不可取消！")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "操作已取消！", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteRepeatFilesPreProcess();
                    }
                })
                .create();
        dialog.show();
    }

    public void deleteRepeatFilesPreProcess(){
        if(rgpFileFilter.getCheckedRadioButtonId()==R.id.rdoSizeFiles){
            AlertDialog dialog=new AlertDialog.Builder(this)
                    .setTitle("删除大小相同文件警告")
                    .setMessage("确定删除文件大小相同的文件吗？除非你确定这样做，否则不建议您这样做！！！")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "操作已取消！", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteRepeatFiles();
                        }
                    })
                    .create();
            dialog.show();
        }else{
            deleteRepeatFiles();
        }

    }

    public void deleteRepeatFiles(){
        int count=0;
        for(FileInfos item : filesAdapter.data){
            if(item.isRepeat){
                try {
                    new File(item.filePath).delete();
                    count++;
                }catch (Exception e){

                }
            }
        }

        Toast.makeText(this, count+"个重复文件已删除！", Toast.LENGTH_SHORT).show();
        if(count>0) {
            File cur = dirsAdapter.data.get(0);
            refreshLsvFiles(cur);
        }
    }

    public void btnMoveRepeatToRptDirClicked(View view) {
        File tarDir=new File(dirsAdapter.data.get(0),"rpt");
        if(!tarDir.exists()){
            tarDir.mkdirs();
        }

        Toast.makeText(this, "正在移动重复文件...", Toast.LENGTH_SHORT).show();
        int count=0;

        int i=0;
        while(i<filesAdapter.data.size()){
            FileInfos it=filesAdapter.data.get(i);
            int j=i+1;
            while(j<filesAdapter.data.size()){
                FileInfos jt=filesAdapter.data.get(j);
                if(jt.groupId==it.groupId){
                    File curFile=new File(jt.filePath);
                    File mvFile=new File(tarDir,jt.fileName);
                    curFile.renameTo(mvFile);
                    count++;
                }else{
                    break;
                }
                j++;
            }
            if(j!=i+1){
                File curFile=new File(it.filePath);
                File mvFile=new File(tarDir,it.fileName);
                curFile.renameTo(mvFile);
                count++;
            }
            i=j;
        }
        Toast.makeText(this, count+"个文件已移动完成！", Toast.LENGTH_SHORT).show();
        if(count>0){
            refreshSpnDirs(tarDir);
            refreshLsvFiles(tarDir);
        }else{
            tarDir.delete();
        }
    }
}
