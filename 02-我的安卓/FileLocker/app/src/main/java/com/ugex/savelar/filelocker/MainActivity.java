package com.ugex.savelar.filelocker;

import androidx.appcompat.app.AppCompatActivity;


import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private Button mOpeButton;
    private EditText mSrcEditText;
    private EditText mDrtEditText;
    private TextView mLogTextView;
    private EditText mPwdEditText;
    private RadioButton mlockRadioButton;
    private RadioButton munlockRadioButton;
    private CheckBox mOpeDir;
    private Button mCleanLog;
    private boolean isOpeDir=false;
    Resources res;
    private final int UPDATE_LOG_TV=0x1001;
    private final int UPDATE_OPE_BTN=0x1002;
    //定义消息处理过程
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==UPDATE_LOG_TV)
            {
                mLogTextView.setText(msg.obj.toString());
            }else if(msg.what==UPDATE_OPE_BTN){
                mOpeButton.setText(msg.obj.toString());
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res=getResources();
        setContentView(R.layout.activity_main);
        mOpeButton=(Button)findViewById(R.id.button_ope);
        mDrtEditText=(EditText)findViewById(R.id.editText_drtpath);
        mLogTextView=(TextView)findViewById(R.id.textView_log);
        mSrcEditText=(EditText)findViewById(R.id.editText_srcpath);
        mPwdEditText=(EditText)findViewById(R.id.editText_pwd);
        mlockRadioButton=(RadioButton)findViewById(R.id.radioButton_lock);
        munlockRadioButton=(RadioButton)findViewById(R.id.radioButton_unlock);
        mCleanLog=(Button)findViewById(R.id.button_cleanlog);
        mOpeDir=(CheckBox)findViewById(R.id.checkBox_dir);
        mOpeButton.setOnClickListener(this);
        mlockRadioButton.setOnCheckedChangeListener(this);
        munlockRadioButton.setOnCheckedChangeListener(this);
        mlockRadioButton.setChecked(true);
        munlockRadioButton.setChecked(false);
        mOpeDir.setChecked(false);
        mCleanLog.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mLogTextView.setText(R.string.DefaultLog);
            }
        });
        mOpeDir.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isOpeDir=isChecked;
                if(isOpeDir==true)
                {
                    ((TextView)findViewById(R.id.textView_srcpath)).setText(R.string.SrcDir);
                    ((TextView)findViewById(R.id.textView_drtpath)).setText(R.string.DrtDir);
                }else{
                    ((TextView)findViewById(R.id.textView_srcpath)).setText(R.string.SrcFile);
                    ((TextView)findViewById(R.id.textView_drtpath)).setText(R.string.DrtFile);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        new Thread(){
            @Override
            public void run() {
                SendMsgToMainThread(UPDATE_LOG_TV,R.string.Running);
                SendMsgToMainThread(UPDATE_OPE_BTN,R.string.operating);
                String srcpath= String.valueOf(mSrcEditText.getText()).trim();
                String drtpath= String.valueOf(mDrtEditText.getText()).trim();
                String pwd= String.valueOf(mPwdEditText.getText()).trim();
                boolean lock=true;
                if(mlockRadioButton.isChecked()==true){
                    lock=true;
                }else {
                    lock=false;
                }
                if(pwd.length()==0){
                    SendMsgToMainThread(UPDATE_LOG_TV,R.string.ErrorOfZeroPassLen);
                    SendMsgToMainThread(UPDATE_OPE_BTN,R.string.Operate);
                    return;
                }
                AnyFileLocker locker=new AnyFileLocker();
                boolean result=true;
                if(isOpeDir==true){
                    result=locker.DirLock(lock,srcpath,drtpath,pwd);
                    if(result==true){
                        SendMsgToMainThread(UPDATE_LOG_TV,R.string.SuccessOfOperateSuccess);
                    }else{
                        SendMsgToMainThread(UPDATE_LOG_TV,R.string.ErrorOrWarningOfNotPermissonOrPasswordError);
                    }
                }else{
                    result=locker.FileLock(lock,srcpath,drtpath,pwd);
                    if(result==true){
                        SendMsgToMainThread(UPDATE_LOG_TV,R.string.SuccessOfOperateSuccess);
                    }else{
                        SendMsgToMainThread(UPDATE_LOG_TV,R.string.ErrorOfSourceFileNotExistOrNotPermission);
                    }
                }
                SendMsgToMainThread(UPDATE_OPE_BTN,R.string.Operate);
            }
        }.start();
    }
    private void SendMsgToMainThread(int whatID,int strID){
        Message msg=new Message();
        msg.what=whatID;
        msg.obj= res.getString(strID);
        handler.sendMessage(msg);
    }
    private void SendMsgToMainThread(int whatID,String str){
        Message msg=new Message();
        msg.what=whatID;
        msg.obj= str;
        handler.sendMessage(msg);
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.getId()==R.id.radioButton_lock){
            if(isChecked==true)
            {
                munlockRadioButton.setChecked(false);
            }
            else{
                munlockRadioButton.setChecked(true);
            }
        }else
        {
            if(isChecked==true)
            {
                mlockRadioButton.setChecked(false);
            }
            else{
                mlockRadioButton.setChecked(true);
            }
        }
    }
    }
