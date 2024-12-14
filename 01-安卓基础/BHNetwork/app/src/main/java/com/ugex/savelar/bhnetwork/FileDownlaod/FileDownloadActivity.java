package com.ugex.savelar.bhnetwork.FileDownlaod;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ugex.savelar.bhnetwork.R;

import java.io.File;

/*文件下载
* 多线程下载原理
* */
/*
* 多线程下载原理
* 获得网络文件总长度
* 本地生成一个与总长度大小相同的文件
* 开启N个线程下载，计算每个线程下载的数据量
* 开启线程，从文件的不同位置下载数据，并写入本地文件的相同位置上
*
*
* */
public class FileDownloadActivity extends AppCompatActivity {

    private EditText edtDownUrl;
    private EditText edtFileSave;
    private EditText edtMaxThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_download);

        InitActivity();
    }

    private void InitActivity() {
        edtDownUrl=findViewById(R.id.editTextDownUrl);
        edtFileSave=findViewById(R.id.editTextSaveName);
        edtMaxThread=findViewById(R.id.editTextMaxDownloadThread);
    }

    public void onBtnDownloadFileClicked(View view){
        String saveName=edtFileSave.getText().toString().trim();
        String urlStr=edtDownUrl.getText().toString().trim();
        String smaxtd=edtMaxThread.getText().toString().trim();
        if(saveName.equals("") ||urlStr.equals("")||smaxtd.equals("")){
            Toast.makeText(this, "下载参数不完整，请填写后重试", Toast.LENGTH_SHORT).show();
            return;
        }
        int maxThread=Integer.parseInt(smaxtd);
        File file=new File(Environment.getExternalStorageDirectory(),saveName);
        DownloadHttpFileThread td=new DownloadHttpFileThread(urlStr,file);
        td.setMaxDownloadThreadCount(maxThread);
        td.start();
    }
    private String getFileName(String urlStr){
        return urlStr.substring(urlStr.lastIndexOf("/"));
    }
}
