package com.ugex.savelar.bdactivityunifydesign.Activitys;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ugex.savelar.bdactivityunifydesign.MainActivity;
import com.ugex.savelar.bdactivityunifydesign.R;

public class UpdateUserInfoActivity extends AppCompatActivity {
    private Resources res;
    private ImageView ivPhoto;
    private EditText edtUserName;
    private EditText edtAddress;
    private EditText edtAccount;
    private EditText edtBrith;
    private EditText edtEmail;
    private RadioGroup rgSex;
    private String strSex;
    private Bitmap bmpPhoto;
    public static final int REQUEST_CAMERA_DATA_CODE=0x101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);

        InitActivity();
    }

    private void InitActivity() {
        res=getResources();
        ivPhoto=findViewById(R.id.imageViewPhoto);
        edtUserName=findViewById(R.id.editTextUserName);
        edtAddress=findViewById(R.id.editTextAddress);
        edtAccount=findViewById(R.id.editTextAccount);
        edtBrith=findViewById(R.id.editTextBrithDay);
        edtEmail=findViewById(R.id.editTextEmail);
        rgSex=findViewById(R.id.radioGroupSex);
        rgSex.check(R.id.radioButtonMan);
        strSex= strSex=((RadioButton)findViewById(R.id.radioButtonMan)).getText().toString();

        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),REQUEST_CAMERA_DATA_CODE);
            }
        });

        rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId)
                {
                    case R.id.radioButtonMan:
                        strSex=((RadioButton)findViewById(R.id.radioButtonMan)).getText().toString();
                        break;
                    case R.id.radioButtonWoman:
                        strSex=((RadioButton)findViewById(R.id.radioButtonWoman)).getText().toString();
                        break;
                    case R.id.radioButtonSecret:
                        strSex=((RadioButton)findViewById(R.id.radioButtonSecret)).getText().toString();
                        break;
                }
            }
        });

        edtAccount.setEnabled(false);
        edtAccount.setText(getIntent().getStringExtra("account"));

        edtBrith.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        DatePickerDialog dlg=new DatePickerDialog(UpdateUserInfoActivity.this);
                        dlg.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                edtBrith.setText(year+"-"+month+"-"+dayOfMonth);
                            }
                        });
                        dlg.show();
                    }

                }
            }
        });

    }

    public void onBtnUpdateUserInfoClicked(View view) {
        if(edtAccount.getText().toString().trim().equals("")){
            Toast.makeText(this, res.getText(R.string.toast_please_input_user_name), Toast.LENGTH_SHORT).show();
            return;
        }
        View sv= LayoutInflater.from(this).inflate(R.layout.user_info_dlg,null);
        ImageView ivpho=sv.findViewById(R.id.imageViewUserPhoto);
        TextView tvAcco=sv.findViewById(R.id.textViewUserAccount);
        TextView tvName=sv.findViewById(R.id.textViewUserName);
        TextView tvSex=sv.findViewById(R.id.textViewUserSex);
        TextView tvBrith=sv.findViewById(R.id.textViewUserBrith);
        TextView tvAddr=sv.findViewById(R.id.textViewUserAddr);
        TextView tvEmail=sv.findViewById(R.id.textViewUserEmail);
        ivpho.setImageBitmap(bmpPhoto);

        tvAcco.setText("账户："+edtAccount.getText());
        tvName.setText("用户名："+edtUserName.getText());
        tvSex.setText("性别："+strSex);
        tvBrith.setText("生日："+edtBrith.getText());
        tvAddr.setText("地址："+edtAddress.getText());
        tvEmail.setText("邮箱："+edtEmail.getText());
        new AlertDialog.Builder(this)
                .setTitle("登录信息")
                .setView(sv)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(UpdateUserInfoActivity.this, MainActivity.class).putExtra("account",edtAccount.getText()));
                        UpdateUserInfoActivity.this.finish();
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQUEST_CAMERA_DATA_CODE){
            bmpPhoto=(Bitmap)data.getExtras().get("data");
            if(bmpPhoto!=null){
                ivPhoto.setImageBitmap(bmpPhoto);
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
