package com.ugex.savelar.bafilestorage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/*
* 使用文件系统
*   Android是使用基于Linux的文件系统，开发者可以建立和访问程序自身的私有文件，
*   也可以访问在资源目录中的原始文件和XML文件，还可以在SD卡等外部存储中保存文件
*
* Android 允许应用程序创建仅能够自身访问的私有文件，文件保存在设备的内部存储器上，
* 在Linux系统上的/data/data/Package_Name/目录中
* */
/*
* 文件系统--文件操作
* 常用FileInputStream，FileOutputStream类结合
* openFileOutput,openFileInput方法完成对文件的读写
* */
/*
* 使用资源文件进行读写
* 在res/raw目录下的资源会被原封不动的保留，不参与编译
* 在程序中通过资源ID访问
* */
/*
* 访问SD卡
* 判断SD卡是否存在
*   Environment.getExternalStorageState()
*   Environment.MEDIA_MOUNTED则说明存在，也就是读写正常
*
* 获得SD卡目录
*   Environment.getExternalStorageDirectory()
* */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtFsContent=findViewById(R.id.editTextFSContent);
    }

    //基于文件系统进行文件读写
    private EditText edtFsContent;
    public void FileSystemWriteContent(View view) {
        FileOutputStream fos=null;
        try {
            fos=this.openFileOutput("test.txt", Context.MODE_PRIVATE);
            BufferedWriter br=new BufferedWriter(new OutputStreamWriter(fos));
            br.write(edtFsContent.getText().toString());
            br.flush();
            br.close();
            edtFsContent.setText("");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public void FileSystemReadContent(View view) {
        FileInputStream fis=null;
        try {
            fis=this.openFileInput("test.txt");
            BufferedReader br=new BufferedReader(new InputStreamReader(fis));
            StringBuilder sb=new StringBuilder();
            String str;
            while((str=br.readLine())!=null){
                sb.append(str);
            }
            edtFsContent.setText(sb.toString());
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //从raw资源文件读取
    public void ResourceRawReadContent(View view) {
        Resources res=getResources();
        InputStream is=res.openRawResource(R.raw.rawtest);
        BufferedReader br= null;
        try {
            br = new BufferedReader(new InputStreamReader(is,"UTF-8"));

            StringBuilder sb=new StringBuilder();
            String str;

            while((str=br.readLine())!=null){
                sb.append(str);
            }
            edtFsContent.setText(sb.toString());
            br.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    //访问SD卡（外部存储）进行读写，直接得到SD卡路径之后，使用Java文件操作即可
    //虽然说大多数时候将/sdcard/作为目录也可以（这玩意是一个软连接，系统会帮我们创建好），这里就当多学一点吧
    //需要权限，外部存储读写权限WRITE_EXTERNAL_STORAGE和READ...
    public  boolean isExternalStorageExist(){
        //注意，这是个String，因此需要使用equals比较
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public void SdCardWriteContent(View view) {
        if(!isExternalStorageExist()){
            Toast.makeText(this, "外部存储不可用", Toast.LENGTH_SHORT).show();
            return;
        }

        File sdpath=Environment.getExternalStorageDirectory();
        try {
            File file=new File(sdpath,"sdtest.txt");
            BufferedWriter bw=new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream( file )));
            bw.write(edtFsContent.getText().toString());
            bw.close();
            edtFsContent.setText("");
            Toast.makeText(this, "Write to:"+file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void SdCardReadContent(View view) {
        if(!isExternalStorageExist()){
            Toast.makeText(this, "外部存储不可用", Toast.LENGTH_SHORT).show();
            return;
        }

        File sdpath=Environment.getExternalStorageDirectory();
        try {
            File file=new File(sdpath,"sdtest.txt");
            BufferedReader bw=new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream( file )));
            StringBuilder sb=new StringBuilder();
            String str;
            while((str=bw.readLine())!=null){
                sb.append(str);
            }
            edtFsContent.setText(sb.toString());
            bw.close();
            Toast.makeText(this, "Read from:"+file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
