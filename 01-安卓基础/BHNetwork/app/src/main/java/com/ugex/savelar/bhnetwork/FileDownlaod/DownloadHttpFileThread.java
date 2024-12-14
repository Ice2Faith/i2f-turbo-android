package com.ugex.savelar.bhnetwork.FileDownlaod;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
public class DownloadHttpFileThread extends Thread {
    private String urlStr;
    private File fileSave;
    private int maxDownloadThreadCount=5;

    public DownloadHttpFileThread(String urlStr, File fileSave){
        this.urlStr=urlStr;
        this.fileSave=fileSave;
    }
    public DownloadHttpFileThread setMaxDownloadThreadCount(int maxDownloadThreadCount){
        this.maxDownloadThreadCount=maxDownloadThreadCount;
        return this;
    }
    @Override
    public void run() {
        if(urlStr==null) {
            return;
        }
        try {
            //获得URL
            URL url=new URL(urlStr);
            //得到Connection
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            //设置连接属性
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Accept-Encoding", "identity");//新安卓版本更新，需要加上这个属性，否则接受到的文件大小不正确
            conn.connect();

            if(conn.getResponseCode()==200){
                //conn.getContentType();//文件类型
                //conn.getContentLength();//内容长度
                Log.i("MyTestLog","Url="+urlStr);
                int length=conn.getContentLength();
                Log.i("MyTestLog","length="+length);
                Log.i("MyTestLog","saveFileName="+fileSave.getName());
                //生成等大小文件
                RandomAccessFile randFile=new RandomAccessFile(fileSave,"rwd");
                randFile.setLength(length);
                randFile.close();

                //计算每个线程下载的数据大小
                //开启多线程下载
                int blockBaseSize=length/maxDownloadThreadCount;
                int endBlockSize=blockBaseSize;
                Log.i("MyTestLog","baseBlockSize="+blockBaseSize);
                Log.i("MyTestLog","endblockSize="+endBlockSize);
                Log.i("MyTestLog","threadCount="+maxDownloadThreadCount);
                if(length%blockBaseSize!=0){
                    endBlockSize+=length%blockBaseSize;
                }
                for(int i=0;i<maxDownloadThreadCount;i++){
                    if(i==maxDownloadThreadCount-1){
                        blockBaseSize=endBlockSize;
                    }
                    new DownloadHttpFileMultiThread(urlStr,
                            i*blockBaseSize,
                            blockBaseSize,
                            fileSave
                            ).start();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

class DownloadHttpFileMultiThread extends Thread{
        private String urlStr;
    private int downLoadBlockOffset;//下载的数据块开始位置
    private int downLoadBlockSize;//下载的数据块大小
    private File fileSave;//保存的文件
    public DownloadHttpFileMultiThread(String urlStr, int blockOffset, int blockSize, File fileSave){
        this.urlStr=urlStr;
        this.downLoadBlockOffset =blockOffset;
        this.downLoadBlockSize=blockSize;
        this.fileSave=fileSave;
    }

    @Override
    public void run() {
        int startOffset=downLoadBlockOffset;
        int endOffset=startOffset+downLoadBlockSize;

        try {
            //获得URL
            URL url=new URL(urlStr);
            //得到Connection
            HttpURLConnection conn=(HttpURLConnection) url.openConnection();

           // conn=(HttpsURLConnection)url.openConnection();
            //

            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Accept-Encoding", "identity");
            conn.setRequestProperty("range","bytes"+startOffset+"-"+endOffset);//设置部分下载属性
            conn.connect();

            if(conn.getResponseCode()==200){
                int length=conn.getContentLength();
                InputStream is=conn.getInputStream();
                //生成等大小文件
                RandomAccessFile randFile=new RandomAccessFile(fileSave,"rwd");
                randFile.seek(startOffset);

                byte[] buffer=new byte[1024];
                int len=0;
                while((len=is.read(buffer))!=-1){
                    randFile.write(buffer,0,len);
                }

                randFile.close();
                is.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}