package com.ugex.savelar.aatextcontrol;
/**
 * TextView EditText Button ImageButton ImageView
 *  主活动：MainActivity.java
 *  主布局文件：activity_main.xml
 */
        import androidx.appcompat.app.AppCompatActivity;

        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //1.声明变量
    private TextView mtextview;
    private Button mbutton;
    private ImageButton mimgbutton;
    private EditText medittext;
    private ImageView mimgview;
    private int[] imgsrc={R.drawable.img,R.drawable.img1};
    private int index=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//主活动绑定主布局，自动生成
        //2.关联控件
        mtextview=(TextView)findViewById(R.id.textView);
        mbutton=(Button)findViewById(R.id.button);//通过findid方式关联控件
        mimgbutton=(ImageButton)findViewById(R.id.imageButton);
        medittext=(EditText)findViewById(R.id.editText);
        mimgview=(ImageView)findViewById(R.id.imageView);
        //3.添加按键点击事件处理程序
        /*//3.1.匿名内部类方式实现，多行注释快捷键：Ctrl+shift+/，再按一次取消注释
        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mtextview.setText("点击了普通按钮");//设置控件显示的文字
            }
        });*/

        /*//3.2.内部类实现方式
        mbutton.setOnClickListener(new NormalButtonClick());*/
        //3.3.在xml中绑定事件处理方法

       /* //3.4.使用lambda表达式实现方法引用
        mbutton.setOnClickListener((v)->{
            mtextview.setText("点击了普通按钮");
        });*/
        mimgbutton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mtextview.setText("点击了图像按钮"+medittext.getText().toString());//获取控件上的文字，需要转换为string
                mimgview.setImageResource(imgsrc[index]);//设置图片，通过R.drawable下找到，一切资源都是ID
                index=(index+1)%2;
            }
        });
    }
    /*//3.2.内部类实现方式
    class NormalButtonClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            mtextview.setText("点击了普通按钮");
        }
    }*/
    //3.3.必须是public 的，而且，要和原型一致（有一个view参数）
    public void ClickNormalBtn(View v){
        mtextview.setText("点击了普通按钮");
    }
}
