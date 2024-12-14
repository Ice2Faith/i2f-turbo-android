package com.ugex.savelar.hextransfer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.Stack;
import static java.lang.Math.pow;

public class MainActivity extends AppCompatActivity {
    private Spinner srcSpinner;
    private Spinner drtSpinner;
    private int srcCode;
    private int drtCode;
    private Button btnAC;
    private Button btnBack;
    private Button btnDot;
    private Button[] btnNums=new Button[36];
    int[] numIdArr={R.id.button_Num0,R.id.button_Num1,R.id.button_Num2,R.id.button_Num3,R.id.button_Num4,R.id.button_Num5
            ,R.id.button_Num6,R.id.button_Num7,R.id.button_Num8,R.id.button_Num9,R.id.button_NumA,R.id.button_NumB
            ,R.id.button_NumC,R.id.button_NumD,R.id.button_NumE,R.id.button_NumF,R.id.button_NumG,R.id.button_NumH
            ,R.id.button_NumI,R.id.button_NumJ,R.id.button_NumK,R.id.button_NumL,R.id.button_NumM,R.id.button_NumN
            ,R.id.button_NumO,R.id.button_NumP,R.id.button_NumQ,R.id.button_NumR,R.id.button_NumS,R.id.button_NumT
            ,R.id.button_NumU,R.id.button_NumV,R.id.button_NumW,R.id.button_NumX,R.id.button_NumY,R.id.button_NumZ};
    char[] charArr=new char[36];
    private String srcString;
    private String drtString;
    private TextView srcTextView;
    private TextView drtTextView;

    private Button btnBtoS;
    private Button btnStoB;
    private TextView baseInputTextView;
    private TextView baseOutputTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitAppFace();
        btnBtoS=findViewById(R.id.button_baseTostr);
        btnStoB=findViewById(R.id.button_strTobase);
        baseInputTextView=findViewById(R.id.editText_BaseInput);
        baseOutputTextView=findViewById(R.id.editText_BaseOutput);
        btnStoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Base64Ex bs=new Base64Ex();
                String input=baseInputTextView.getText().toString().trim();
                String output=bs.DataToBase64(input.getBytes());
                baseOutputTextView.setText(output);
            }
        });
        btnBtoS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Base64Ex bs=new Base64Ex();
                String input=baseInputTextView.getText().toString().trim();
                byte[] output=bs.Base64ToData(input);
                baseOutputTextView.setText(new String(output));
            }
        });
    }

    private void InitAppFace(){
        srcTextView=findViewById(R.id.textView_srcNumber);
        drtTextView=findViewById(R.id.textView_drtNumber);
        srcString="";
        drtString="";
        srcTextView.setText(srcString);
        drtTextView.setText(drtString);
        btnAC=findViewById(R.id.button_Clean);
        btnBack=findViewById(R.id.button_back);
        btnDot=findViewById(R.id.button_NumDot);
        btnAC.setOnClickListener(new NumberBtnClickListen());
        btnBack.setOnClickListener(new NumberBtnClickListen());
        btnDot.setOnClickListener(new NumberBtnClickListen());
        for(int i=0;i<36;i++)
        {
            btnNums[i]=findViewById(numIdArr[i]);
            btnNums[i].setOnClickListener(new NumberBtnClickListen());
            if(i<10)
                charArr[i]=(char)('0'+i);
            else
                charArr[i]=(char)('A'+i-10);
        }
        srcSpinner=(Spinner)findViewById(R.id.spinner_srcCode);
        drtSpinner=(Spinner)findViewById(R.id.spinner_drtCode);
        String[] codeList=new String[35];
        for(int i=2;i<=36;i++)
        {
            codeList[i-2]=i+"";
        }
        final ArrayAdapter<String> adapter=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,codeList);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        srcSpinner.setAdapter(adapter);
        drtSpinner.setAdapter(adapter);
        srcSpinner.setSelection(8,true);
        drtSpinner.setSelection(14,true);
        srcCode=Integer.parseInt(srcSpinner.getSelectedItem().toString());
        drtCode=Integer.parseInt(drtSpinner.getSelectedItem().toString());
        btnDot.setTextColor(getResources().getColor(R.color.colorNormalButtonFont));
        UpdataBtnState();
        srcSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                srcCode=Integer.parseInt(adapter.getItem(position).toString());
                new NumberBtnClickListen().UpdataTextView();
                UpdataBtnState();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        drtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                drtCode=Integer.parseInt(adapter.getItem(position).toString());
                new NumberBtnClickListen().UpdataTextView();
                UpdataBtnState();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    private void UpdataBtnState()
    {
        for(int i=0;i<36;i++)
        {
            if(i<srcCode)
            {
               btnNums[i].setEnabled(true);
               btnNums[i].setTextColor(getResources().getColor(R.color.colorNormalButtonFont));
            }
            else
            {
                btnNums[i].setEnabled(false);
                btnNums[i].setTextColor(getResources().getColor(R.color.colorUnuseButtonFont));
            }
        }
    }
    class NumberBtnClickListen implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(v.getId()==btnAC.getId())
            {
                srcString="";
                UpdataTextView();
                return;
            }
            if(v.getId()==btnBack.getId())
            {
                if(srcString.length()>0)
                srcString= srcString.substring(0,srcString.length()-1);
                UpdataTextView();
                return;
            }
            if(v.getId()==btnDot.getId())
            {
                srcString+=".";
                UpdataTextView();
                return;
            }
            int index=-1;
            for(int i=0;i<36;i++)
            {
                if(v.getId()==numIdArr[i])
                {
                    index=i;
                    break;
                }
            }
            if(index!=-1)
            {
                srcString+=charArr[index];
                UpdataTextView();
            }
        }
        public void UpdataTextView()
        {
            Hexer hex=new Hexer();
            double number=hex.OtherToDec(srcString,srcCode);
            drtString=hex.DecToOther(number,drtCode);
            if(srcString.length()>0)
                srcTextView.setText(srcString);
            else
                srcTextView.setText("0.0");
            if(drtTextView.length()>0)
                drtTextView.setText(drtString);
            else
                drtTextView.setText("0.0");
        }
    }
    class Hexer
    {
        private final String table="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwsyz@#";
         public  double OtherToDec(String str,int other)
        {
            double result=0;
            int index=0;
            while(index<str.length() &&  str.charAt(index)!='.')
            {
                result*=other;
                if(str.charAt(index)>='0'&&str.charAt(index)<='9')
                    result+=(str.charAt(index)-'0');
                 else if(str.charAt(index)>='A'&&str.charAt(index)<='Z')
                    result+=(str.charAt(index)-'A'+10);
                 else if(str.charAt(index)>='a'&&str.charAt(index)<='z')
                     result+=(str.charAt(index)-'a'+10);
                index++;
            }
            if(index>=str.length())
                return result;
            index++;
            int i=1;
            while(index<str.length() &&  str.charAt(index)!='.')
            {
                if(str.charAt(index)>='0'&&str.charAt(index)<='9')
                    result+=(str.charAt(index)-'0')*pow(1.0/other,(double)i++);
                else if(str.charAt(index)>='A'&&str.charAt(index)<='Z')
                    result+=(str.charAt(index)-'A'+10)*pow(1.0/other,(double)i++);
                else if(str.charAt(index)>='a'&&str.charAt(index)<='z')
                    result+=(str.charAt(index)-'a'+10)*pow(1.0/other,(double)i++);
                index++;
            }
            return result;
        }
        public String DecToOther(double number,int other)
        {
            if(number<0)
                return "Error!Number out of bounds.";
            String result="";
            long otc=(long)number;
            double flo=number-otc;
            long[] FloNum =new long[1000];
            Stack<Long> p=new Stack<>();
            while(otc!=0)
            {
                p.push(otc%other);
                otc=(long)otc/other;
            }
            int i=0;
            while(i<100&&flo!=0.0)
            {
                FloNum[i]=(long)(flo*other);
                flo=flo*other-FloNum[i];
                i++;
            }
            FloNum[i]=0;
            FloNum[i+1]=-9999;

            while(!p.isEmpty())
            {
                otc=p.pop();
               if(otc>=64 || otc<0)
                    continue;
                result+=table.charAt((int)otc);
            }
            if(result.length()==0)
                result+="0";
            result+=".";
            i=0;
            while(FloNum[i]!=-9999)
            {
                if(FloNum[i]>=64 || FloNum[i]<0)
                    break;
                result+=table.charAt((int)FloNum[i]);
                i++;
            }
            if(result.equals(".0"))
                result="0.0";
            return result;
        }
    }

}