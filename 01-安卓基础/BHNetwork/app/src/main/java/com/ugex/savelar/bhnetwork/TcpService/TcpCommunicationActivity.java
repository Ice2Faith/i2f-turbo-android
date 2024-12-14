package com.ugex.savelar.bhnetwork.TcpService;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ugex.savelar.bhnetwork.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class TcpCommunicationActivity extends AppCompatActivity {
    public static final int MSG_SERVER_DONE=0x01;
    public static final int MSG_CLIENT_DONE=0x02;
    public static final int MSG_BIND_SUCCESS=0x03;
    public static final int MSG_CONNECT_SUCCESS=0x04;
    public static final int MSG_ACCEPT_MSG=0x05;

    private EditText edtServerMsg;
    private EditText edtClientMsg;
    private EditText edtCommHistory;

    private  ServerSocket server;
    private Socket client;
    private Socket accClient;
    private boolean isServerDone=false;
    private boolean isClientDone=false;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_SERVER_DONE:
                    isServerDone=true;
                    Toast.makeText(TcpCommunicationActivity.this, "服务器线程准备完成", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_CLIENT_DONE:
                    isClientDone=true;
                    Toast.makeText(TcpCommunicationActivity.this, "客户端线程准备完成", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_BIND_SUCCESS:
                    Toast.makeText(TcpCommunicationActivity.this, "端口绑定成功", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_CONNECT_SUCCESS:
                    Toast.makeText(TcpCommunicationActivity.this, "连接服务器成功", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_ACCEPT_MSG:
                    edtCommHistory.setText(edtCommHistory.getText().toString()+"\n"+msg.obj.toString());
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcp_communication);

        InitActivity();

    }

    public static  final int SERVER_PORT=2202;
    private void InitActivity() {
        edtServerMsg=findViewById(R.id.editTextServerSend);
        edtClientMsg=findViewById(R.id.editTextClientSend);
        edtCommHistory=findViewById(R.id.editTextInfo);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server=new ServerSocket();
                    server.setReuseAddress(true);//允许重用端口
                    server.bind(new InetSocketAddress(SERVER_PORT));//绑定端口
                    if(server.isBound()){
                        handler.sendEmptyMessage(MSG_BIND_SUCCESS);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    accClient=server.accept();
                                    BufferedReader bis=new BufferedReader(new InputStreamReader(accClient.getInputStream()));
                                    while(true){
                                        //在socket中，谨慎使用readLine方法，因为这个是一个阻塞方法，会要求直到有换行符传输过来才返回，
                                        //因此，就要求发出信息的一方需要保证在预计的位置发送换行符，否则将会一直阻塞在这里，虽然数据已经传输过来了
                                        //但是迟迟得不到返回数据
                                        String str=bis.readLine();
                                        Message msg=new Message();
                                        msg.what=MSG_ACCEPT_MSG;
                                        msg.obj="收到客户端："+str;
                                        handler.sendMessage(msg);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }else{
                        server=null;
                        return;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    handler.sendEmptyMessage(MSG_SERVER_DONE);
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    client=new Socket();
                    client.connect(new InetSocketAddress("127.0.0.1",SERVER_PORT),5000);
                    if(client.isConnected()){
                        handler.sendEmptyMessage(MSG_CONNECT_SUCCESS);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                BufferedReader bis= null;
                                try {
                                    bis = new BufferedReader(new InputStreamReader(client.getInputStream()));
                                    while(true){
                                        String str=bis.readLine();
                                        /*if(str==null){
                                            continue;
                                        }
                                        while(bis.readLine()!=null);*/
                                        Message msg=new Message();
                                        msg.what=MSG_ACCEPT_MSG;
                                        msg.obj="收到服务器："+str;
                                        handler.sendMessage(msg);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }).start();
                    }else{
                        client=null;
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    handler.sendEmptyMessage(MSG_CLIENT_DONE);
                }

            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        try {
            if(client!=null && client.isClosed()==false){
                    client.close();
            }
            if(server!=null && server.isClosed()==false){
                server.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
    public void onBtnServerSendMsgClicked(View view) {
        if(server==null || server.isBound()==false){
            return;
        }
        if(accClient==null){
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //这里是不用考虑是否会重复获取到输出流的，因为这是Socket的一个内建对象，拿到的只是引用
                    OutputStream os=accClient.getOutputStream();
                    //这里和换行符进行拼接，只是为了和接收端使用的readLine相呼应而已
                    os.write((edtServerMsg.getText().toString()+"\r\n").getBytes());
                    os.flush();
                    //os.close();//另外，输出流也是不用关闭的，在关闭套接字的时候会自动关闭
                    //如果手动关闭了，那接下来一步就是关闭这个套接字，否则另一端将会出现问题（如果没解决的话）
                    //因此建议不要关闭
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    public void onBtnClientSendMsgClicked(View view) {
        if(client==null || client.isConnected()==false){
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OutputStream os=client.getOutputStream();
                    os.write((edtClientMsg.getText().toString()+"\r\n").getBytes());
                    os.flush();
                    //os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
