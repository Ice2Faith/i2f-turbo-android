package com.ugex.savelar.guestfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private int[] picIdArr={R.drawable.cut,R.drawable.stone,R.drawable.cloth};
    private ImageView playerPic;
    private ImageView computerPic;
    private TextView resultTV;
    private static int[] BtnIdArr={R.id.imageButtonCut,R.id.imageButtonStone,R.id.imageButtonCloth};
    private ImageButton[] playerChoseBtn=new ImageButton[3];
    private Random rand=new Random();
    private static final String[] resultText={"很遗憾！！你输了","平分秋色！！再来一局","大获全胜！！佩服之至"};
    private Vibrator vibrator;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitApp();
    }
    private void InitApp()
    {

        context=this.getApplicationContext();
        vibrator=(Vibrator)context.getSystemService(context.VIBRATOR_SERVICE);
        playerPic=findViewById(R.id.imageViewPlayer);
        computerPic=findViewById(R.id.imageViewComputer);
        resultTV=findViewById(R.id.textViewResult);
        BtnClickProc lster=new BtnClickProc();
        playerPic.setImageResource(picIdArr[0]);
        computerPic.setImageResource(picIdArr[0]);
        resultTV.setText(resultText[1]);
        for(int i=0;i<BtnIdArr.length;i++){
            playerChoseBtn[i]=findViewById(BtnIdArr[i]);
            playerChoseBtn[i].setOnClickListener(lster);
        }
    }
    class BtnClickProc implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            vibrator.vibrate(200);
            int comId=rand.nextInt(3);
            computerPic.setImageResource(picIdArr[comId]);
            int useId=-1;
            for(int i=0;i<3;i++) {
                if (BtnIdArr[i] == v.getId()) {
                    useId = i;
                    break;
                }
            }
            playerPic.setImageResource(picIdArr[useId]);
            int retId=useId==comId?1:useId==(comId+1)%3?2:0;

            resultTV.setText(resultText[retId]);
        }
    }
}
