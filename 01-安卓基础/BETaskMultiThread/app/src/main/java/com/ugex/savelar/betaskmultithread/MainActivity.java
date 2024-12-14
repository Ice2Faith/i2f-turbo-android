package com.ugex.savelar.betaskmultithread;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

/*
* Task
* 多线程二
* AsyncTask异步任务
*
* Handler使用比较复杂
* Handler和AsyncTask都是不阻塞主线程的，UI的更新只能在主线程中完成，异步处理时不可避免的
* AsyncTask是由系统创建和删除的执行任务线程，由系统有效管理
*
* 使用：
* 只需要穿件AsyncTask的子类，实现方法，使用其对象的execute()方法开启线程即可
*
* */
public class MainActivity extends AppCompatActivity {
    private TextView tvTips;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitActivity();
    }

    private void InitActivity() {
        tvTips=findViewById(R.id.textViewTips);
        progressBar=findViewById(R.id.progressBar);
    }

    public void onBtnStartClicked(View view) {
        new MyAsynTask().execute();//自动调用完成线程，由于没有参数，直接空参数调用即可
    }


    /**
     * 拥有三个泛型参数,最好全部使用类，即使会自动装箱拆箱
     * Params:传入的参数类型
     * Progress:进度指示的类型
     * Result:运行结束后返回的类型
     */
    public class MyAsynTask extends AsyncTask<Void,Integer,Void> {
        /*
         * 后台线程，运行在子线程中的
         * */
        @Override
        protected Void doInBackground(Void... voids) {
            for(int i=0;i<100;i++){
                publishProgress(i);//通知进度更新
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;//这里进行返回结果，由于没有返回值，为null即可
        }

        //进度更改时调用的，运行在主线程中的，也就是可以直接更新控件UI
        @Override
        protected void onProgressUpdate(Integer... values) {
            int num=values[0];//由于只是传递一个值过来
            tvTips.setText("Num:"+num);
            //进度是一个百分比
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                progressBar.setProgress(num,true);
            }
            super.onProgressUpdate(values);
        }

        //执行结束时调用，运行在主线程中，也可以直接更改UI
        @Override
        protected void onPostExecute(Void aVoid) {
            tvTips.setText("计数完成");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                progressBar.setProgress(100,true);
            }
            super.onPostExecute(aVoid);
        }
    }
}
