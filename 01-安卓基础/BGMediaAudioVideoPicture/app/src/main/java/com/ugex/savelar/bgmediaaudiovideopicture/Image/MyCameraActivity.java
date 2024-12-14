package com.ugex.savelar.bgmediaaudiovideopicture.Image;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ugex.savelar.bgmediaaudiovideopicture.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MyCameraActivity extends Activity {
    private Camera camera;
    private SurfaceView svCamera;
    private SurfaceHolder surfaceHolder;
    private ImageView ivPreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_camera);

        InitActivity();
    }

    private void InitActivity() {
        try {
           //如果没有相机设备时可能出错的，虽然不要求处理异常

            camera = Camera.open();
        }catch (Exception e){
            camera=null;
        }
        if(camera==null){
            Toast.makeText(this, "没有相机可用", Toast.LENGTH_SHORT).show();
            this.finish();
            return;
        }

        svCamera=findViewById(R.id.surfaceViewCamera);
        ivPreView=findViewById(R.id.imageViewPreView);
        surfaceHolder=svCamera.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                camera.release();
                camera=null;
            }
        });

        svCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                            if(success){
                                Toast.makeText(MyCameraActivity.this, "自动对焦成功", Toast.LENGTH_SHORT).show();
                            }
                    }
                });
            }
        });

    }


    private void startPreviewCamera(){
        //设置预览，但是给定一个延时
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    camera.setPreviewDisplay(surfaceHolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                camera.startPreview();

            }
        }).start();;
    }
    @Override
    protected void onResume() {
        super.onResume();
        //注意，显示Preview一定要在显示出来界面之后，最好是有一个延时的，否则可能出错
        startPreviewCamera();
    }

    public void onBtnTackPhotoClicked(View view) {
        //参数：马上得到Image，得到Raw数据，图片数据回调,得到jpeg数据
        camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                //预览
                Bitmap bmp= BitmapFactory.decodeByteArray(data,0,data.length);
                ivPreView.setImageBitmap(bmp);
                //保存
                File file=new File(Environment.getExternalStorageDirectory(),"SelfCap"+System.currentTimeMillis()+".png");
                try {
                    FileOutputStream outFile = new FileOutputStream(file);
                    bmp.compress(Bitmap.CompressFormat.PNG, 90, outFile);
                    outFile.flush();
                    outFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if(camera!=null) {
            camera.release();
            camera = null;
        }
        super.onDestroy();
    }
}
