package com.ugex.savelar.foowordsmemory;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.ugex.savelar.foowordsmemory.service.FooWordsMemoryToastService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity {
    private static Bundle setting=new Bundle();

    private CheckBox ckbServiceState;

    private CheckBox ckbGravityUp;
    private CheckBox ckbGravityDown;
    private CheckBox ckbGravityCenter;
    private CheckBox ckbGravityLeft;
    private CheckBox ckbGravityRight;
    private CheckBox ckbGravityVelCenter;
    private CheckBox ckbGravityHorCenter;

    private EditText edtOffsetX;
    private EditText edtOffsetY;

    private EditText edtFontSize;

    private EditText edtWaitMillSecond;
    private EditText edtShowMillSecond;

    private CheckBox ckbRandomIndex;
    private CheckBox ckbContinueLastIndex;

    private CheckBox ckbOpenSpeechMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            },0x110);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!Settings.canDrawOverlays(getApplicationContext())) {
                //启动Activity让用户授权
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent,100);
            }else{
                openService();
            }
        }else{
            openService();
        }



        initActivity();


    }
    private void openService(){
        Intent intent=new Intent(MainActivity.this,FooWordsMemoryToastService.class);
        startService(intent);

        Toast.makeText(this, "如果你看到这条提示，或者通知栏看到了服务，那么就说明我已经启动了哦", Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    openService();
                } else {
                    Toast.makeText(this, "ACTION_MANAGE_OVERLAY_PERMISSION权限已被拒绝", Toast.LENGTH_SHORT).show();;
                }
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent=getIntent();


    }


    private void initActivity() {
        setting=FooWordsMemoryToastService.getSettingsBundle();

        ckbServiceState=findViewById(R.id.checkBoxServiceState);

        ckbGravityCenter=findViewById(R.id.checkBoxGravityCenter);
        ckbGravityDown=findViewById(R.id.checkBoxGravityDown);
        ckbGravityLeft=findViewById(R.id.checkBoxGravityLeft);
        ckbGravityRight=findViewById(R.id.checkBoxGravityRight);
        ckbGravityUp=findViewById(R.id.checkBoxGravityUp);
        ckbGravityVelCenter=findViewById(R.id.checkBoxGravityVelCenter);
        ckbGravityHorCenter=findViewById(R.id.checkBoxGravityHorCenter);

        edtOffsetX=findViewById(R.id.textViewOffsetX);
        edtOffsetY=findViewById(R.id.textViewOffsetY);

        edtShowMillSecond=findViewById(R.id.textViewShowMillSecond);
        edtWaitMillSecond=findViewById(R.id.textViewWaitMillSecond);

        edtFontSize=findViewById(R.id.textViewFontSize);

        ckbRandomIndex=findViewById(R.id.checkBoxRandomIndex);
        ckbContinueLastIndex =findViewById(R.id.checkBoxContinueLastIndex);

        ckbOpenSpeechMode=findViewById(R.id.checkBoxOpenSpeechMode);

        loadSettingFromFile();

        syncToView();

        submitSettings();
    }

    private void syncToView() {
        ckbServiceState.setChecked(setting.getBoolean("LAY_RUN_STATE"));
        int gravity=setting.getInt("LAY_GRAVITY");
        ckbGravityUp.setChecked((gravity& (Gravity.TOP))==Gravity.TOP);
        ckbGravityRight.setChecked((gravity& (Gravity.RIGHT))==Gravity.RIGHT);
        ckbGravityLeft.setChecked((gravity& (Gravity.LEFT))==Gravity.LEFT);
        ckbGravityDown.setChecked((gravity& (Gravity.BOTTOM))==Gravity.BOTTOM);
        ckbGravityCenter.setChecked((gravity& (Gravity.CENTER))==Gravity.CENTER);
        ckbGravityVelCenter.setChecked((gravity& (Gravity.CENTER_VERTICAL))==Gravity.CENTER_VERTICAL);
        ckbGravityHorCenter.setChecked((gravity& (Gravity.CENTER_HORIZONTAL))==Gravity.CENTER_HORIZONTAL);

        edtFontSize.setText(""+setting.getFloat("LAY_TEXT_SIZE"));
        edtShowMillSecond.setText(""+setting.getInt("LAY_SHOW_MILLISECOND"));
        edtWaitMillSecond.setText(""+setting.getInt("LAY_WAIT_MILLISECOND"));
        edtOffsetY.setText(""+setting.getInt("LAY_Y_OFFSET"));
        edtOffsetX.setText(""+setting.getInt("LAY_X_OFFSET"));

        ckbRandomIndex.setChecked(setting.getBoolean("LAY_USE_RANDOM_INDEX"));
        ckbContinueLastIndex.setChecked(setting.getBoolean("LAY_CONTINUE_LAST_INDEX"));

        ckbOpenSpeechMode.setChecked(setting.getBoolean("LAY_OPEN_SPEECH_MODE"));

    }



    private void submitSettings(){
        Intent intent=new Intent(MainActivity.this,FooWordsMemoryToastService.class);

        syncToData();

        intent.putExtra("setting",setting);
        startService(intent);

        saveSettingToFile();
    }

    private void syncToData() {
        int gravity=0;
        if(ckbGravityHorCenter.isChecked())
            gravity|=Gravity.CENTER_HORIZONTAL;
        if(ckbGravityVelCenter.isChecked())
            gravity|=Gravity.CENTER_VERTICAL;
        if(ckbGravityCenter.isChecked())
            gravity|=Gravity.CENTER;
        if(ckbGravityUp.isChecked())
            gravity|=Gravity.TOP;
        if(ckbGravityDown.isChecked())
            gravity|=Gravity.BOTTOM;
        if(ckbGravityLeft.isChecked())
            gravity|=Gravity.LEFT;
        if(ckbGravityRight.isChecked())
            gravity|=Gravity.RIGHT;
        setting.putInt("LAY_GRAVITY",gravity);

        setting.putBoolean("LAY_RUN_STATE",ckbServiceState.isChecked());

        setting.putInt("LAY_X_OFFSET",Integer.parseInt(edtOffsetX.getText().toString()));
        setting.putInt("LAY_Y_OFFSET",Integer.parseInt(edtOffsetY.getText().toString()));
        setting.putInt("LAY_SHOW_MILLISECOND",Integer.parseInt(edtShowMillSecond.getText().toString()));
        setting.putInt("LAY_WAIT_MILLISECOND",Integer.parseInt(edtWaitMillSecond.getText().toString()));

        setting.putFloat("LAY_TEXT_SIZE",Float.parseFloat(edtFontSize.getText().toString()));

        setting.putBoolean("LAY_USE_RANDOM_INDEX",ckbRandomIndex.isChecked());
        setting.putBoolean("LAY_CONTINUE_LAST_INDEX", ckbContinueLastIndex.isChecked());

        setting.putBoolean("LAY_OPEN_SPEECH_MODE", ckbOpenSpeechMode.isChecked());

    }

    public void OnTextViewAboutClicked(View view) {
        AlertDialog dlg=new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle("帮助/关于")
                .setMessage("1.程序打开服务即开始运行\n" +
                        "2.如果你需要个性化设置，那么你在主页修改信息，然后点击同步按钮即可\n" +
                        "3.虽然已经尽力保证服务能够存活，但是依然需要你将他加入白名单\n" +
                        "4.本程序不使用任何权限，仅仅后台弹出提示窗口\n" +
                        "5.程序支持随机弹出与顺序弹出两种运行模式，在顺序时你可以继续上一次的索引继续\n" +
                        "\t也可以重置为0索引，从头开始\n" +
                        "6.程序支持语音播报，这将使用你系统的TTS引擎播报\n" +
                        "\t如果系统TTS引擎没有，你可以安装使用这些语音引擎\n" +
                        "\t\tGoogle文字转语音引擎、科大讯飞+、度秘语音引擎" +
                        "7.程序支持读取自定义的文件内容，文件需要在内存根目录下\n" +
                        "\t名为："+FooWordsMemoryToastService.USER_WORDS_FILE_NAME+"\n" +
                        "\t文件的每一行将会作为一次弹窗的内容进行显示\n" +
                        "\t并且，如果自定义文件存在并且有数据，将不会使用内置词库数据\n" +
                        "\t你的完整路径为："+ new File(Environment.getExternalStorageDirectory(),FooWordsMemoryToastService.USER_WORDS_FILE_NAME).getAbsolutePath()+"\n" +
                        "\n\tDev: @ Ugex.Savelar")
                .setPositiveButton("我知道了",null)
                .setNegativeButton("删除自定义文件", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File file=new File(Environment.getExternalStorageDirectory(),FooWordsMemoryToastService.USER_WORDS_FILE_NAME);
                        if(file.exists()==false){
                            Toast.makeText(MainActivity.this, "自定义文件还不存在呢！", Toast.LENGTH_SHORT).show();
                        }else{
                            file.delete();
                            Toast.makeText(MainActivity.this, "自定义文件已经被删除了哦！", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNeutralButton("帮我创建自定义文件", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File file=new File(Environment.getExternalStorageDirectory(),FooWordsMemoryToastService.USER_WORDS_FILE_NAME);
                        if(file.exists()==false){
                            try {
                                PrintWriter os=new PrintWriter(new OutputStreamWriter(new FileOutputStream(file)));
                                os.println("0.这是【浮词记忆】的自定义配置文件，下面是小提示：");
                                os.println("1.这个文件的每一行内容都会被读取显示为一个弹窗内容。");
                                os.println("2.因此，一行的内容不宜过多哦！");
                                os.println("3.如果此文件内容发生更改，你需要重启主程序哦！");
                                os.println("4.如果此文件内容为空，将使用内置词库");
                                os.close();
                                Toast.makeText(MainActivity.this, "自定义文件创建好了哦", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                Toast.makeText(MainActivity.this, "自定义文件创建失败了，请检查是否授予文件写入权限", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(MainActivity.this, "文件已经存在了，赶快去填写内容吧", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .create();
        dlg.show();

    }

    public void onButtonSyncClicked(View view) {
        submitSettings();
    }

    private static final String SETTING_FILE_NAME="foo_words.conf";
    private void saveSettingToFile(){
        SharedPreferences sp=this.getSharedPreferences(SETTING_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();

        editor.putInt("LAY_GRAVITY",setting.getInt("LAY_GRAVITY"));
        editor.putInt("LAY_X_OFFSET",setting.getInt("LAY_X_OFFSET"));
        editor.putInt("LAY_Y_OFFSET",setting.getInt("LAY_Y_OFFSET"));
        editor.putFloat("LAY_TEXT_SIZE",setting.getFloat("LAY_TEXT_SIZE"));
        editor.putInt("LAY_SHOW_MILLISECOND",setting.getInt("LAY_SHOW_MILLISECOND"));
        editor.putInt("LAY_WAIT_MILLISECOND",setting.getInt("LAY_WAIT_MILLISECOND"));
        editor.putBoolean("LAY_RUN_STATE",setting.getBoolean("LAY_RUN_STATE"));
        editor.putBoolean("LAY_USE_RANDOM_INDEX",setting.getBoolean("LAY_USE_RANDOM_INDEX"));
        editor.putBoolean("LAY_CONTINUE_LAST_INDEX",setting.getBoolean("LAY_CONTINUE_LAST_INDEX"));

        editor.putBoolean("LAY_OPEN_SPEECH_MODE",setting.getBoolean("LAY_OPEN_SPEECH_MODE"));

        editor.commit();
    }

    private void loadSettingFromFile(){
        SharedPreferences sp=this.getSharedPreferences(SETTING_FILE_NAME, Context.MODE_PRIVATE);

        setting.putInt("LAY_GRAVITY",sp.getInt("LAY_GRAVITY",Gravity.LEFT|Gravity.CENTER_VERTICAL));
        setting.putInt("LAY_X_OFFSET",sp.getInt("LAY_X_OFFSET",20));
        setting.putInt("LAY_Y_OFFSET",sp.getInt("LAY_Y_OFFSET",-200));
        setting.putFloat("LAY_TEXT_SIZE",sp.getFloat("LAY_TEXT_SIZE",20.0f));
        setting.putInt("LAY_SHOW_MILLISECOND",sp.getInt("LAY_SHOW_MILLISECOND",3*1000));
        setting.putInt("LAY_WAIT_MILLISECOND",sp.getInt("LAY_WAIT_MILLISECOND",1*1000));
        setting.putBoolean("LAY_RUN_STATE",sp.getBoolean("LAY_RUN_STATE",true));
        setting.putBoolean("LAY_USE_RANDOM_INDEX",sp.getBoolean("LAY_USE_RANDOM_INDEX",true));
        setting.putBoolean("LAY_CONTINUE_LAST_INDEX",sp.getBoolean("LAY_CONTINUE_LAST_INDEX",true));

        setting.putBoolean("LAY_OPEN_SPEECH_MODE",sp.getBoolean("LAY_OPEN_SPEECH_MODE",true));

    }

    public void OnTextViewTtsSettingClicked(View view) {
        startActivity(new Intent("com.android.settings.TTS_SETTINGS"));
    }
}
