package com.ugex.savelar.texttovoice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private EditText edtWords;
    private Button btnVary;
    private Button btnStop;
    private Button btnClear;
    private Button btnPaste;
    private TextToSpeech txtSpeech;
    private ClipboardManager clipManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitEnv();
    }
    class SpeechInit implements OnInitListener
    {
        @Override
        public void onInit(int status)
        {
            if(status==TextToSpeech.SUCCESS)
            {
                int res=txtSpeech.setLanguage(Locale.getDefault());
                if(res==TextToSpeech.LANG_MISSING_DATA||res==TextToSpeech.LANG_NOT_SUPPORTED)
                {
                    Toast.makeText(MainActivity.this, "语言设置不成功，默认为英文模式！", Toast.LENGTH_SHORT).show();
                }else
                {
                    txtSpeech.setLanguage(Locale.US);
                }
            }
        }
    }
    private void InitEnv()
    {
        startService(new Intent(this,BGSpeechWhenCopyService.class));
        clipManager=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        txtSpeech=new TextToSpeech(this,new SpeechInit());
        edtWords=findViewById(R.id.editTextWords);
        btnVary=findViewById(R.id.buttonVary);
        btnVary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtSpeech.speak(edtWords.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
            }
        });
        btnStop=findViewById(R.id.buttonStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtSpeech.stop();
            }
        });
        btnClear=findViewById(R.id.buttonClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtWords.setText("");
            }
        });
        btnPaste=findViewById(R.id.buttonPaste);
        btnPaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData cd=clipManager.getPrimaryClip();
                if(cd.getItemCount()>0)
                {
                    String str=cd.getItemAt(0).getText().toString();
                    edtWords.setText(str);
                }
            }
        });

    }
}
