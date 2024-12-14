package com.ugex.savelar.azsharedpreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/*
* SharedPrefrence存储
* 保存用户数据和配置
* 需要重启之后还能使用
* 需要存储在外部介质中
*
* 一个轻量级的输出存储配置信息文件
* 只能存储基本类型，string算基本类型
* 以XML文件格式进行保存
* 位于目录：/data/data/Package_name/shared_prefs
* */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //写入SharedPreferences
    private EditText edtKey;
    private EditText edtContent;
    private SharedPreferences preferences;
    public void saveDataToSharedPreferences(View view) {
        edtKey=findViewById(R.id.editTextKey);
        edtContent=findViewById(R.id.editTextContent);
        //获取SharedPreference
        //参数：文件名（不用带后缀），进入模式
        preferences=getSharedPreferences("test", Context.MODE_PRIVATE);
        //获取编辑器才能进行写入
        SharedPreferences.Editor editor=preferences.edit();
        //写入数据，put族方法,键值对,如果键相同，就相当于修改，进行一个覆盖操作
       // editor.putString("strTest","this is a test string");
        editor.putString(edtKey.getText().toString(),
                edtContent.getText().toString());
        //写入完成之后需要提交，这样才会保存
        editor.commit();
        edtContent.setText("");
    }

    //读取值
    public void readDataToSharedPreferences(View view) {
        preferences=getSharedPreferences("test", Context.MODE_PRIVATE);
        edtContent.setText(preferences.getString(
                edtKey.getText().toString(),
                "default value"
        ));
    }
}
