package com.ugex.savelar.bhnetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ugex.savelar.bhnetwork.FileDownlaod.FileDownloadActivity;
import com.ugex.savelar.bhnetwork.FileUpload.FileUploadActivity;
import com.ugex.savelar.bhnetwork.TcpService.TcpCommunicationActivity;
import com.ugex.savelar.bhnetwork.UnifyNetClasses.NetworkUnifyClassesActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitActivity();
    }

    private void InitActivity() {

    }

    //网络编程中常用类的使用
    public void onBtnNetworkClassesClicked(View view) {
        Intent intent=new Intent(this, NetworkUnifyClassesActivity.class);
        startActivity(intent);
    }

    //文件的上传
    public void onBtnFileUploadClicked(View view) {
        Intent intent=new Intent(this, FileUploadActivity.class);
        startActivity(intent);
    }

    //文件的下载
    public void onBtnFileDownloadClicked(View view) {
        Intent intent=new Intent(this, FileDownloadActivity.class);
        startActivity(intent);
    }

    public void onBtnSocketProgramClicked(View view) {
        Intent intent=new Intent(this, TcpCommunicationActivity.class);
        startActivity(intent);
    }
}
