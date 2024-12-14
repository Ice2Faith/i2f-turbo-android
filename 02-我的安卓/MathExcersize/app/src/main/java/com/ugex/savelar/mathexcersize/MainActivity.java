package com.ugex.savelar.mathexcersize;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ListView listMaths;
    private List<MathQuestion> mathQus=new ArrayList<>();
    private List<Map<String,String>> DataSource=new ArrayList<Map<String,String>>();;
    private SimpleAdapter adapter;
    private static final int QUSCOUNT=20;
    private int maxNumber=100;
    private EditText maxInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitApp();
    }
    private void InitApp(){
        maxInput=findViewById(R.id.editTextMaxNum);
        listMaths=findViewById(R.id.ListViewDisplay);
        maxInput.setText(maxNumber+"");
        //上下文，数据源，布局，数据源Key，显示项ID
        adapter=new SimpleAdapter(this,DataSource,R.layout.math_qus_list_item,
                new String[]{"num1","ope","num2","res"},
                new int[]{R.id.textViewNum1,R.id.textViewOpe,R.id.textViewNum2,R.id.textViewAnswer});
        listMaths.setAdapter(adapter);
        CreateMathDataSource(QUSCOUNT,false);
        findViewById(R.id.buttonFlash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateMathDataSource(QUSCOUNT,false);
            }
        });
        findViewById(R.id.buttonSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateMathDataSource(QUSCOUNT,true);
            }
        });
        findViewById(R.id.buttonRange).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maxNumber= Integer.parseInt(maxInput.getText().toString());
            }
        });
    }

    private void CreateMathDataSource(int count,boolean disAnwser){
        if(!disAnwser) {
            mathQus.clear();
            for (int i = 0; i < count; i++) {
                mathQus.add(MathQuestion.makeQustion(maxNumber));
            }
        }
        DataSource.clear();
        for(MathQuestion ms : mathQus){
            Map<String,String> item=new HashMap<>();
            item.put("num1",(int)ms.getNumber1()+"");
            item.put("ope",ms.getOperator()+"");
            item.put("num2",(int)ms.getNumber2()+"");
            if(disAnwser){
                item.put("res",ms.getResult()+"");
            }else{
                item.put("res","");
            }
            DataSource.add(item);
        }
        adapter.notifyDataSetChanged();

    }
}

class MathQuestion
{
    private double Number1;
    private char Operator;
    private double Number2;
    private double Result;

    private final static char[] ArrOperators={'+','-','*','/','%'};
    private void CalculResult(){
        switch (Operator){
            case '+':
                Result=Number1+Number2;
                break;
            case '-':
                Result=Number1-Number2;
                break;
            case '*':
                Result=Number1*Number2;
                break;
            case '/':
                Result=Number1/Number2;
                break;
            case '%':
                Result=(int)Number1%(int)Number2;
                break;
            default:
                Result=0;
                break;
        }
    }
    public MathQuestion(){
        Number1=0;
        Number2=1;
        Operator='+';
        CalculResult();
    }
    public MathQuestion(double Num1,double Num2,char Ope){
        Number1=Num1;
        Number2=Num2;
        Operator=Ope;
        CalculResult();
    }

    public double getNumber1() {
        return Number1;
    }
    public double getNumber2(){
        return Number2;
    }
    public char getOperator(){
        return Operator;
    }
    public  double getResult(){
        CalculResult();
        return Result;
    }
    public void setNumber1(double Num1) {
        Number1=Num1;
    }
    public void setNumber2(double Num2){
        Number2=Num2;
    }
    public void setOperator(char Ope){
        Operator=Ope;
    }
    private static Random rand=new Random();
    public static MathQuestion makeQustion(int maxNum){
        char ope=ArrOperators[rand.nextInt(ArrOperators.length)];
        int num2=rand.nextInt(maxNum);
        while((ope=='/'||ope=='%') && num2==0){
            num2=rand.nextInt(maxNum);
        }
        return  new MathQuestion(rand.nextInt(maxNum),num2, ope);
    }
}