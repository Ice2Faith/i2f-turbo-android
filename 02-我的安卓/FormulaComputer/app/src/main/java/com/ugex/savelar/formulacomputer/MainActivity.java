package com.ugex.savelar.formulacomputer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
    private EditText edtInput,edtOutput;
    private Button btnQuateLeft,btnQuateRight;
    private Button btnSqrt,btnLog,btnAdds,btnMuls;
    private Button btnRecip,btnEpowX,btnLn,btnnGoldSec;
    private Button btnDehex,btnClear,btnBack,btnAdd;
    private Button btnNum7,btnNum8,btnNum9,btnSub;
    private Button btnNum4,btnNum5,btnNum6,btnMul;
    private Button btnNum1,btnNum2,btnNum3,btnDiv;
    private Button btnNum0,btnDot,btnMod,btnEqual;
    private Button btnNumA,btnNumB,btnNumC,btnPow;
    private Button btnNumD,btnNumE,btnNumF,btnNeg;
    private Button btnPercent,btnFactorial,btnAbs,btnAngle;
    private Button btnAnd,btnOr,btnXor,btnRadian;
    private Button btnSin,btnCos,btnTan,btnnPi;
    private Button btnArcSin,btnArcCos,btnArcTan,btnnE;

    private Button btnLMov,btnRMov,btnNot,btnMinSum;
    private Button btnMax,btnMin,btnAvg,btnMaxFac;
    private Button btnRecipAdds,btnRecipMuls,btnPerAdd,btnPerSub;
    private Button btnRand,btnRandZ,btnRandF,btnDayOfMonth;
    private Button btnCeil,btnFloor,btnRound,btnDayOfYear;
    private Button btnDHead,btnFTail,btnKmh2Ms,btnMs2Kmh;
    private Button btnCt2Ft,btnFt2Ct,btnXPowX,btnFeibo;

    private EditText edtSrcHexNumber,edtDstHexNumber;
    private Spinner spnSrcHex,spnDstHex;

    private Button btnInQuate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponent();

    }

    class BtnClickListener implements View.OnClickListener {
        private String text;
        public BtnClickListener(String text){
            this.text=text;
        }
        @Override
        public void onClick(View v) {
            edtInput.append(text);
            computeResult();
        }
    }

    private void initComponent() {
        edtInput=findViewById(R.id.edtInput);
        edtOutput=findViewById(R.id.edtOutput);

        btnQuateLeft=findViewById(R.id.btnQuateLeft);
        btnQuateRight=findViewById(R.id.btnQuateRight);
        btnQuateLeft.setOnClickListener(new BtnClickListener(" ( "));
        btnQuateRight.setOnClickListener(new BtnClickListener(" ) "));

        btnSqrt=findViewById(R.id.btnSqrt);
        btnLog=findViewById(R.id.btnLog);
        btnAdds=findViewById(R.id.btnAdds);
        btnMuls=findViewById(R.id.btnMuls);
        btnSqrt.setOnClickListener(new BtnClickListener(" sqrt "));
        btnLog.setOnClickListener(new BtnClickListener(" log "));
        btnAdds.setOnClickListener(new BtnClickListener(" adds "));
        btnMuls.setOnClickListener(new BtnClickListener(" muls "));

        btnRecip=findViewById(R.id.btnRecip);
        btnEpowX=findViewById(R.id.btnEpowX);
        btnLn=findViewById(R.id.btnLn);
        btnnGoldSec=findViewById(R.id.btnnGoldSec);
        btnRecip.setOnClickListener(new BtnClickListener(" recip "));
        btnEpowX.setOnClickListener(new BtnClickListener(" epow "));
        btnLn.setOnClickListener(new BtnClickListener(" ln "));
        btnnGoldSec.setOnClickListener(new BtnClickListener(" numgsec "));

        btnDehex=findViewById(R.id.btnDehex);
        btnClear=findViewById(R.id.btnClear);
        btnBack=findViewById(R.id.btnBack);
        btnAdd=findViewById(R.id.btnAdd);
        btnDehex.setOnClickListener(new BtnClickListener(" dehex"));
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearInput();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backInput();
            }
        });
        btnAdd.setOnClickListener(new BtnClickListener(" + "));

        btnNum7=findViewById(R.id.btnNum7);
        btnNum8=findViewById(R.id.btnNum8);
        btnNum9=findViewById(R.id.btnNum9);
        btnSub=findViewById(R.id.btnSub);
        btnNum7.setOnClickListener(new BtnClickListener("7"));
        btnNum8.setOnClickListener(new BtnClickListener("8"));
        btnNum9.setOnClickListener(new BtnClickListener("9"));
        btnSub.setOnClickListener(new BtnClickListener(" - "));

        btnNum4=findViewById(R.id.btnNum4);
        btnNum5=findViewById(R.id.btnNum5);
        btnNum6=findViewById(R.id.btnNum6);
        btnMul=findViewById(R.id.btnMul);
        btnNum4.setOnClickListener(new BtnClickListener("4"));
        btnNum5.setOnClickListener(new BtnClickListener("5"));
        btnNum6.setOnClickListener(new BtnClickListener("6"));
        btnMul.setOnClickListener(new BtnClickListener(" * "));

        btnNum1=findViewById(R.id.btnNum1);
        btnNum2=findViewById(R.id.btnNum2);
        btnNum3=findViewById(R.id.btnNum3);
        btnDiv=findViewById(R.id.btnDiv);
        btnNum1.setOnClickListener(new BtnClickListener("1"));
        btnNum2.setOnClickListener(new BtnClickListener("2"));
        btnNum3.setOnClickListener(new BtnClickListener("3"));
        btnDiv.setOnClickListener(new BtnClickListener(" / "));

        btnNum0=findViewById(R.id.btnNum0);
        btnDot=findViewById(R.id.btnDot);
        btnMod=findViewById(R.id.btnMod);
        btnEqual=findViewById(R.id.btnEqual);
        btnNum0.setOnClickListener(new BtnClickListener("0"));
        btnDot.setOnClickListener(new BtnClickListener("."));
        btnMod.setOnClickListener(new BtnClickListener(" % "));
        btnEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                computeResult();
            }
        });

        btnNumA=findViewById(R.id.btnNumA);
        btnNumB=findViewById(R.id.btnNumB);
        btnNumC=findViewById(R.id.btnNumC);
        btnPow=findViewById(R.id.btnPow);
        btnNumA.setOnClickListener(new BtnClickListener("A"));
        btnNumB.setOnClickListener(new BtnClickListener("B"));
        btnNumC.setOnClickListener(new BtnClickListener("C"));
        btnPow.setOnClickListener(new BtnClickListener(" ^ "));

        btnNumD=findViewById(R.id.btnNumD);
        btnNumE=findViewById(R.id.btnNumE);
        btnNumF=findViewById(R.id.btnNumF);
        btnNeg=findViewById(R.id.btnNeg);
        btnNumD.setOnClickListener(new BtnClickListener("D"));
        btnNumE.setOnClickListener(new BtnClickListener("E"));
        btnNumF.setOnClickListener(new BtnClickListener("F"));
        btnNeg.setOnClickListener(new BtnClickListener(" neg "));

        btnPercent=findViewById(R.id.btnPercent);
        btnFactorial=findViewById(R.id.btnFactorial);
        btnAbs=findViewById(R.id.btnAbs);
        btnAngle=findViewById(R.id.btnAngle);
        btnPercent.setOnClickListener(new BtnClickListener(" per "));
        btnFactorial.setOnClickListener(new BtnClickListener(" ! "));
        btnAbs.setOnClickListener(new BtnClickListener(" abs "));
        btnAngle.setOnClickListener(new BtnClickListener(" angle "));

        btnAnd=findViewById(R.id.btnAnd);
        btnOr=findViewById(R.id.btnOr);
        btnXor=findViewById(R.id.btnXor);
        btnRadian=findViewById(R.id.btnRadian);
        btnAnd.setOnClickListener(new BtnClickListener(" and "));
        btnOr.setOnClickListener(new BtnClickListener(" or "));
        btnXor.setOnClickListener(new BtnClickListener(" xor "));
        btnRadian.setOnClickListener(new BtnClickListener(" radian "));

        btnSin=findViewById(R.id.btnSin);
        btnCos=findViewById(R.id.btnCos);
        btnTan=findViewById(R.id.btnTan);
        btnnPi=findViewById(R.id.btnnPi);
        btnSin.setOnClickListener(new BtnClickListener(" sin "));
        btnCos.setOnClickListener(new BtnClickListener(" cos "));
        btnTan.setOnClickListener(new BtnClickListener(" tan "));
        btnnPi.setOnClickListener(new BtnClickListener(" numpi "));

        btnArcSin=findViewById(R.id.btnArcSin);
        btnArcCos=findViewById(R.id.btnArcCos);
        btnArcTan=findViewById(R.id.btnArcTan);
        btnnE=findViewById(R.id.btnnE);
        btnArcSin.setOnClickListener(new BtnClickListener(" arcsin "));
        btnArcCos.setOnClickListener(new BtnClickListener(" arccos "));
        btnArcTan.setOnClickListener(new BtnClickListener(" arctan "));
        btnnE.setOnClickListener(new BtnClickListener(" nume "));


        btnLMov=findViewById(R.id.btnLMov);
        btnRMov=findViewById(R.id.btnRMov);
        btnNot=findViewById(R.id.btnNot);
        btnMinSum=findViewById(R.id.btnnMinSum);
        btnMax=findViewById(R.id.btnMax);
        btnMin=findViewById(R.id.btnMin);
        btnAvg=findViewById(R.id.btnAvg);
        btnMaxFac=findViewById(R.id.btnMaxFac);
        btnRecipAdds=findViewById(R.id.btnRecipAdds);
        btnRecipMuls=findViewById(R.id.btnRecipMuls);
        btnPerAdd=findViewById(R.id.btnPerAdd);
        btnPerSub=findViewById(R.id.btnnPerSub);
        btnRand=findViewById(R.id.btnRand);
        btnRandZ=findViewById(R.id.btnRandZ);
        btnRandF=findViewById(R.id.btnRandF);
        btnDayOfMonth=findViewById(R.id.btnDayOfMonth);
        btnCeil=findViewById(R.id.btnCeil);
        btnFloor=findViewById(R.id.btnFloor);
        btnRound=findViewById(R.id.btnRound);
        btnDayOfYear=findViewById(R.id.btnnDayOfYear);
        btnDHead=findViewById(R.id.btnDHead);
        btnFTail=findViewById(R.id.btnFTail);
        btnKmh2Ms=findViewById(R.id.btnKmhToMs);
        btnMs2Kmh=findViewById(R.id.btnnMsToKmh);
        btnCt2Ft=findViewById(R.id.btnCtToFt);
        btnFt2Ct=findViewById(R.id.btnFtToCt);
        btnXPowX=findViewById(R.id.btnXPowX);
        btnFeibo=findViewById(R.id.btnFeibo);

        btnLMov.setOnClickListener(new BtnClickListener(" lmov "));
        btnRMov.setOnClickListener(new BtnClickListener(" rmov "));
        btnNot.setOnClickListener(new BtnClickListener(" not "));
        btnMinSum.setOnClickListener(new BtnClickListener(" minsum "));
        btnMax.setOnClickListener(new BtnClickListener(" max "));
        btnMin.setOnClickListener(new BtnClickListener(" min "));
        btnAvg.setOnClickListener(new BtnClickListener(" avg "));
        btnMaxFac.setOnClickListener(new BtnClickListener(" maxfac "));
        btnRecipAdds.setOnClickListener(new BtnClickListener(" recipadds "));
        btnRecipMuls.setOnClickListener(new BtnClickListener(" recipmuls "));
        btnPerAdd.setOnClickListener(new BtnClickListener(" peradd "));
        btnPerSub.setOnClickListener(new BtnClickListener(" persub "));
        btnRand.setOnClickListener(new BtnClickListener(" rand "));
        btnRandZ.setOnClickListener(new BtnClickListener(" randz "));
        btnRandF.setOnClickListener(new BtnClickListener(" randf "));
        btnDayOfMonth.setOnClickListener(new BtnClickListener(" dayofmonth "));
        btnCeil.setOnClickListener(new BtnClickListener(" ceil "));
        btnFloor.setOnClickListener(new BtnClickListener(" floor "));
        btnRound.setOnClickListener(new BtnClickListener(" round "));
        btnDayOfYear.setOnClickListener(new BtnClickListener(" dayofyear "));
        btnDHead.setOnClickListener(new BtnClickListener(" dhead "));
        btnFTail.setOnClickListener(new BtnClickListener(" ftail "));
        btnKmh2Ms.setOnClickListener(new BtnClickListener(" kmhtoms "));
        btnMs2Kmh.setOnClickListener(new BtnClickListener(" mstokmh "));
        btnCt2Ft.setOnClickListener(new BtnClickListener(" cttoft "));
        btnFt2Ct.setOnClickListener(new BtnClickListener(" fttoct "));
        btnXPowX.setOnClickListener(new BtnClickListener(" xpowx "));
        btnFeibo.setOnClickListener(new BtnClickListener(" feibo "));

        //////////////////////////////////////////////
        edtSrcHexNumber=findViewById(R.id.edtSrcHexNumber);
        edtDstHexNumber=findViewById(R.id.edtDstHexNumber);
        spnSrcHex=findViewById(R.id.spnSrcHex);
        spnDstHex=findViewById(R.id.spnDstHex);

        ArrayAdapter<Integer> adapter=new ArrayAdapter<Integer>(this,android.R.layout.simple_list_item_1,SUPPORT_HEXES);

        spnSrcHex.setAdapter(adapter);
        spnDstHex.setAdapter(adapter);

        spnSrcHex.setSelection(10-2);
        spnDstHex.setSelection(16-2);

        spnSrcHex.setOnItemSelectedListener(new SpnHexItemSelectListener(true));
        spnDstHex.setOnItemSelectedListener(new SpnHexItemSelectListener(false));

        edtSrcHexNumber.setOnEditorActionListener(new EdtHexActionListener(true));
        edtDstHexNumber.setOnEditorActionListener(new EdtHexActionListener(false));
        edtSrcHexNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                transferHex(true);
            }
        });

        btnInQuate=findViewById(R.id.btnInQuate);
        btnInQuate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str=edtInput.getText().toString();
                str="( "+str+" ) ";
                edtInput.setText(str);
                computeResult();
            }
        });
    }

    private void transferHex(boolean isSrc){
        Integer srcHex=(Integer) spnSrcHex.getSelectedItem();
        Integer dstHex=(Integer) spnDstHex.getSelectedItem();
        String srcHexNum=edtSrcHexNumber.getText().toString().trim();
        String dstHexNum=edtDstHexNumber.getText().toString().trim();
        if(isSrc){
            dstHexNum=transferHexBase(srcHexNum,srcHex,dstHex);
            edtDstHexNumber.setText(dstHexNum);
        }else{
            srcHexNum=transferHexBase(dstHexNum,dstHex,srcHex);
            edtSrcHexNumber.setText(srcHexNum);
        }
    }

    private static final Integer[] SUPPORT_HEXES={2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
    class SpnHexItemSelectListener implements AdapterView.OnItemSelectedListener{
        private boolean isSrc;
        public SpnHexItemSelectListener(boolean isSrc){
            this.isSrc=isSrc;
        }
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            transferHex(isSrc);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
    class EdtHexActionListener implements TextView.OnEditorActionListener{
        private boolean isSrc;
        public EdtHexActionListener(boolean isSrc){
            this.isSrc=isSrc;
        }
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            transferHex(isSrc);
//            if ((keyEvent != null
//                    && KeyEvent.KEYCODE_ENTER == keyEvent.getKeyCode()
//                    && KeyEvent.ACTION_DOWN == keyEvent.getAction())) {
//                transferHex(isSrc);
//                return true;
//            }
            return false;
        }
    }

    public void computeResult(){
        String str=edtInput.getText().toString();
        double result=compute(str);
        String error=getLastError();
        if(error.length()!=0){
            error="\n["+error+"]";
        }
        edtOutput.setText(""+result+error);
    }
    public void backInput(){
        String str=edtInput.getText().toString().trim();
        int index=str.lastIndexOf(" ");
        if(index>=0){
            String nstr=str.substring(0,index).trim()+" ";
            edtInput.setText(nstr);
        }else if(str.length()>0){
            String nstr=str.substring(0,str.length()-1).trim()+" ";
            edtInput.setText(nstr);
        }
    }
    public void clearInput(){
        String str=edtInput.getText().toString();
        if(str.length()==0){
            edtOutput.setText("");
        }
        edtInput.setText("");
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native double compute(String formula);

    public native double getLastResult();

    public native String getLastError();

    public native String transferHexBase(String num,int srcHex,int dstHex);
}
