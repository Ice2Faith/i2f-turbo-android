package com.ugex.savelar.filejoinziper;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;

public class MainActivity extends Activity {
    private EditText edtInput;
    private EditText edtOutput;
    private Button btnBegin;
    private Button btnClean;
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("fjzip-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitActivity();
    }

    private void InitActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}
                            ,0x01);
        }

        edtInput=findViewById(R.id.editTextInputCmd);
        edtOutput=findViewById(R.id.editTextOutputCmd);
        btnBegin=findViewById(R.id.buttonBegin);
        btnClean=findViewById(R.id.buttonClean);
    }


    public native boolean hzp(String srcFile,String drtFile);
    public native boolean dhzp(String srcFile,String drtFile);
    public native boolean fjs(String[] srcFiles,String drtFile);
    public native boolean dfjs(String srcFile,String drtPath);
    public native boolean mzp(String[] srcFiles,String drtFile);
    public native boolean dmzp(String srcFile,String drtPath);
    private static final String helpInfo=	"命令行参数：\n"+
            "\t模式 目标路径 源路径1 源路径2 ...\n"+
            "\t模式：\n"+
            "\t\thzp:\t单文件压缩\n"+
            "\t\t\thzp test_save.hzp test.mp3\n"+
            "\t\tdhzp:\t单文件解压\n"+
            "\t\t\tdhzp test.mp3 test_save.hzp\n"+
            "\t\tfjs:\t多文件拼接\n"+
            "\t\t\tfjs test_save.fjs test1.mp3 test2.mp4\n"+
            "\t\tdfjs:\t多文件拆分\n"+
            "\t\t\tdfjs ./savepath/ test_save.fjs\n"+
            "\t\tmzp:\t多文件压缩\n"+
            "\t\t\tmzp test_save.mzp test.png test.mp3 test.txt\n"+
            "\t\tdmzp:\t多文件解压\n"+
            "\t\t\tdmzp ./savepath/ test_save.mzp\n"+
            "\t注意：\n"+
            "\t\t通过以上例子，你应该知道了，多文件拆分和多文件解压时\n"+
            "\t\t目标路径需要是一个文件夹，也就是符号【\\】结尾\n"+
            "\t\t多文件压缩的本质：先合并多文件 再压缩\n" +
            "\t\t如果不使用完整路径，将会以/sdcard/为根路径";

    public void OnClickedHelpInfoTextView(View view) {
        AlertDialog dlg=new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_launcher_foreground)
                .setTitle("帮助信息")
                .setMessage(helpInfo)
                .setNegativeButton("好的",null)
                .create();
        dlg.show();
    }

    public void OnClickedBeginButton(View view) {
       new Thread(new OperaThread()).start();
    }

    public void OnClickedCleanButton(View view) {
        edtOutput.setText("");
        edtInput.setText("");
    }

    class OperaThread implements  Runnable
    {
        @Override
        public void run() {
            edtOutput.setText("操作进行中...");
            task();
        }

        public void task()
        {
            String exsdPath= Environment.getExternalStorageDirectory().getAbsolutePath();
            String cmdline=edtInput.getText().toString().trim();
            if(cmdline.length()<=0)
            {
                edtOutput.setText("请输入命令：\n"+helpInfo);
                return;
            }
            String[] cmds=cmdline.split("\\s+");
            if (cmds.length < 3)
            {
                edtOutput.setText("请阅读命令使用方式：\n"+helpInfo);
                return;
            }

            for(int i=1;i<cmds.length;i++)
            {
                if(cmds[i].length()>0 && cmds[i].charAt(0)!='/')
                {
                    cmds[i]=exsdPath+ File.separator+cmds[i];
                }
            }

            String mode = cmds[0];
            String drtFile = cmds[1];
            int beginInputIndex = 2;
            String srcFile=cmds[beginInputIndex];
            String[] ifiles=new String[cmds.length-beginInputIndex];
            for(int i=0;i<ifiles.length;i++)
            {
                ifiles[i]=cmds[beginInputIndex+i];
            }

            boolean rst=false;
            if ("hzp".equalsIgnoreCase(mode))
            {
                rst=hzp(srcFile,drtFile);
            }
            else if ("dhzp".equalsIgnoreCase(mode))
            {
                rst=dhzp(srcFile,drtFile);
            }
            else if ("fjs".equalsIgnoreCase(mode))
            {
                rst=fjs(ifiles,drtFile);
            }
            else if ("dfjs".equalsIgnoreCase(mode))
            {
                rst = dfjs(srcFile,drtFile);
            }
            else if ("mzp".equalsIgnoreCase(mode))
            {
                rst =mzp(ifiles,drtFile);
            }
            else if ("dmzp".equalsIgnoreCase(mode))
            {
                rst = dmzp(srcFile,drtFile);
            }
            else
            {
                edtOutput.setText("未识别的模式，请注意大小写，以下是使用帮助：\n"+helpInfo);
                return;
            }
            if (rst == true)
            {
                edtOutput.setText("操作成功,模式："+mode);
            }
            else
            {
                edtOutput.setText("操作失败，请检查文件或参数是否符合要求\n");
            }
        }
    }
}
