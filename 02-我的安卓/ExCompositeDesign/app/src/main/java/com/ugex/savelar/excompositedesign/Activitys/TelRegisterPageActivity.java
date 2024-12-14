package com.ugex.savelar.excompositedesign.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ugex.savelar.excompositedesign.Dao.DbManager;
import com.ugex.savelar.excompositedesign.Dao.UserEntity;
import com.ugex.savelar.excompositedesign.R;
import com.ugex.savelar.excompositedesign.Util.Utools;

public class TelRegisterPageActivity extends AppCompatActivity {
    private Resources res;
    private EditText edtAccount;
    private EditText edtPass;
    private EditText edtRePass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tel_register_page);

        InitActivity();
    }

    private void InitActivity() {
        res=getResources();
        edtAccount=findViewById(R.id.editTextTelNumber);
        edtPass=findViewById(R.id.editTextPassword);
        edtRePass=findViewById(R.id.editTextRepeatPassword);
    }

    public void onBtnRegisterCliked(View view) {
        String account= Utools.regularStrFromObj(edtAccount.getText());
        String pass=Utools.regularStrFromObj(edtPass.getText());
        String repass=Utools.regularStrFromObj(edtRePass.getText());
        edtAccount.setText(account);
        edtPass.setText(pass);
        edtRePass.setText(repass);
        if(account.length()<=0 || pass.length()<=0 || repass.length()<=0){
            Toast.makeText(this, res.getString(R.string.toast_tel_or_pass_length_must_gather_than_zero), Toast.LENGTH_SHORT).show();
            return;
        }
        if(pass.equals(repass)==false){
            Toast.makeText(this, res.getString(R.string.toast_both_password_not_matched), Toast.LENGTH_SHORT).show();
            return;
        }
        UserEntity ue=new UserEntity();
        ue.setIDAccount(account)
                .setPassword(pass);
        ContentValues values=ue.toContentValues();
        long row=DbManager.insert(this,values);
        if(row<0){
            Toast.makeText(this, res.getString(R.string.toast_this_accout_had_been_registered_please_to_login), Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent=new Intent(this,UpdateUserInfoActivity.class);
        intent.putExtra("account",account);
        startActivity(intent);
        //18060009000 12315
    }

    public void onTvToLoginClicked(View view) {
        startActivity(new Intent(this,TelLoginPageActivity.class));
    }
}
