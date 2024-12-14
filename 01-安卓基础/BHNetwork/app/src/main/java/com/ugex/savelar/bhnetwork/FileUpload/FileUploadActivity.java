package com.ugex.savelar.bhnetwork.FileUpload;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.ugex.savelar.bhnetwork.R;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

/*
* 文件上传
* 上传文件头文件的格式
* 客户端实现文件上传
*
* Http的Get方式，传输数据不能超过8K
*POST方式则不限制传输大小
* */
/*
*文件上传，POST方式
* 前提：服务器端接受上传文件并且能够存储，这样才能上传成功
*
* 上传文件头文件的格式
*   //间隔符：----------------------
*   //边界值：xxxx
*   //Content-Dispositon:from-data,数据来源与post传递过来的
*   //name="key",key是服务器端规定的key值
*   //filename="filename",filename是文件名
*   //application/octet-stream，流类型和编码格式
*   //数据，就是文件的真实数据
*   //其中的\r\n是必须的
*   协议头：Content-Type:multipart/form-data;boundary=-------------------------xxxxx
*   文件格式：----------------------xxxx\r\n
*       Content-Dispositon:from-data;name="key";filename="filename"
*       Content-Type:application/octet-stream;charset=UTF-8\r\n\r\n
*   数据\r\n
*   ------------------xxx-----------------------\r\n
* 客户端实现文件上传
*
* */
public class FileUploadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_upload);
    }

    //实现文件上传HTTP-POST，其他部分知识点看下面两个函数
    private void UploadFile(String fileName){
        String reqUrl="https://www.sina.com.cn/uploadfile";
        //准备协议头
        String BOUNDARY= UUID.randomUUID().toString();//边界标识
        String PREFIX="--";//前缀
        String LINE_END="\r\n";
        String CONTENT_TYPE="multipart/form-data";

        try {
            URL url=new URL(reqUrl);
            HttpsURLConnection conn=(HttpsURLConnection)url.openConnection();

            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Charset","UTF-8");//设置编码格式
            conn.setRequestProperty("connection","keep-alive");//设置持久连接
            conn.setRequestProperty("content-type",CONTENT_TYPE+";boundary="+BOUNDARY);

            //写入文件格式
            File file=new File(fileName);
            DataOutputStream dos=new DataOutputStream(conn.getOutputStream());
            StringBuffer sb=new StringBuffer();
            //第一行
            sb.append(PREFIX+BOUNDARY+LINE_END);

            //第二行,假定服务器端规定的就是filename，注意转义字符
            sb.append("content-disposition:form-data;name=\"filename\";filename=\""+file.getName()+"\""+LINE_END);

            //第三行
            sb.append("content-type:application/octet-stream;charset=UTF-8"+LINE_END+LINE_END);

            //写入协议头文件头
            dos.writeUTF(sb.toString());

            //写入文件数据
            FileInputStream fis= new FileInputStream(file);
            byte[] buffer=new byte[1024];
            int len=0;
            while((len=fis.read(buffer))!=-1){
                dos.write(buffer,0,len);
            }
            fis.close();

            //写入结束标记
            dos.writeUTF(LINE_END);
            dos.writeUTF(PREFIX+BOUNDARY+PREFIX+LINE_END);
            dos.flush();
            //dos.close();

            int rspCode=conn.getResponseCode();
            if(rspCode==200){
                Toast.makeText(this, "文件上传成功", Toast.LENGTH_SHORT).show();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }





    }

    private void RequestNetByGETMethod(){
        //GET 方式,注意，一般网络请求是比较慢的，因此为了避免主线程假死，建议使用多线程进行处理
        //URL格式：协议+路径+参数（https :// + www.baidu.com/search + ? keyword=test&target=CSDN）
        //协议之后跟://,参数之前加上?,参数之间用&分割，路径中可以指明端口号
        //参数部分的拼接可以以借助map，毕竟他就是键值对
        String urlStr="https://www.baidu.com/search?keyword=test&target=CSDN";
        try {
            //创建URL
            URL url = new URL(urlStr);
            //得到HttpsURLConnection，注意，这里比较坑，必须是https://协议的URL才行，否则报错
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            //设置连接属性
            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);
            //输入输出是相对于程序本身来说的
            conn.setDoInput(true);//读取网络发来的信息
            conn.setDoOutput(true);//写出信息到网络
            conn.connect();
            //得到返回码，200即成功
            int responceCode = conn.getResponseCode();
            if(responceCode==200){
                InputStream is=conn.getInputStream();
                //在这里就是熟悉的流操作，这里就不干什么了

                is.close();
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void RequestNetByPostMethod(){
        //POST 方式,注意，一般网络请求是比较慢的，因此为了避免主线程假死，建议使用多线程进行处理
        //URL格式：协议+路径（https :// + www.baidu.com/search）
        //协议之后跟://,参数之前加上?,参数之间用&分割，路径中可以指明端口号
        //参数部分的拼接可以以借助map，参数是写在数据主体里面的，因此需要写入
        //Post方式是不限制传输大小的
        String urlStr="https://www.baidu.com/search";
        try {
            //创建URL
            URL url = new URL(urlStr);
            //得到HttpURLConnection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置连接属性
            conn.setRequestMethod("POST");
            conn.setReadTimeout(5000);
            //输入输出是相对于程序本身来说的
            conn.setDoInput(true);
            conn.setDoOutput(true);
            //参数就直接写入请求的主体部分，不再作为URL的一部分
            DataOutputStream dos=new DataOutputStream(conn.getOutputStream());
            dos.writeUTF("keyword=test&target=CSDN");
            dos.flush();
            //dos.close();

            conn.connect();
            //得到返回码，200即成功
            int responceCode = conn.getResponseCode();
            if(responceCode==200){
                InputStream is=conn.getInputStream();
                //在这里就是熟悉的流操作，这里就不干什么了

                is.close();
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
