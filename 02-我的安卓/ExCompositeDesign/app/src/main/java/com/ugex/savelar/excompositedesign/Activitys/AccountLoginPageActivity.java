package com.ugex.savelar.excompositedesign.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ugex.savelar.excompositedesign.Dao.DbManager;
import com.ugex.savelar.excompositedesign.MainActivity;
import com.ugex.savelar.excompositedesign.R;
import com.ugex.savelar.excompositedesign.Util.SRHelper;
import com.ugex.savelar.excompositedesign.Util.Utools;

public class AccountLoginPageActivity extends AppCompatActivity {
    private EditText edtAccount;
    private EditText edtPassword;
    private TextView tvTelLogin;
    private Resources res;
    private CheckBox ckAutoLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_login_page);

        InitActivity();
    }

    private void InitActivity() {
        res=getResources();
        tryDirectLoginFromSharedPreference();
        edtAccount=findViewById(R.id.editTextAccount);
        edtPassword=findViewById(R.id.editTextPassword);
        tvTelLogin=findViewById(R.id.textViewTelLoginType);
        ckAutoLogin=findViewById(R.id.checkBoxAutoLogin);

        tvTelLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountLoginPageActivity.this,TelLoginPageActivity.class));
            }
        });
    }

    private void tryDirectLoginFromSharedPreference(){
        String account= SRHelper.getDataFromSharedPreference(this,
                res.getString(R.string.logininfo_sharedprefe_name),
                "account",
                "");
        if("".equals(account)==false){
            Intent intent=new Intent(AccountLoginPageActivity.this, MainActivity.class);
            intent.putExtra("logintype","auto");
            intent.putExtra("account",account);
            startActivity(intent);
            this.finish();
        }
    }

    public void onBtnAccountLoginClicked(View view) {
        String account= Utools.regularStrFromObj(edtAccount.getText());
        String password=Utools.regularStrFromObj(edtPassword.getText());
        if("".equals(account) || "".equals(password)){
            Toast.makeText(this, res.getString(R.string.toast_please_input_account_and_password), Toast.LENGTH_SHORT).show();
            return;
        }

        String savedPass=getAccountPassword(account);
        if(savedPass==null){
            Toast.makeText(this, res.getString(R.string.toast_please_check_account_or_password_correct), Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.equals(savedPass)==false){
            Toast.makeText(this, res.getString(R.string.toast_password_of_this_account_is_not_matched), Toast.LENGTH_SHORT).show();
            return;
        }

        if(ckAutoLogin.isChecked()) {
            SRHelper.putDataToSharedPreference(this,
                    "logininfo",
                    "account",
                    account);
        }
        Intent intent=new Intent(AccountLoginPageActivity.this,UpdateUserInfoActivity.class);
        intent.putExtra("logintype","account");
        intent.putExtra("account",account);
        startActivity(intent);
        this.finish();
    }

    private String getAccountPassword(String account) {
        Cursor cur= DbManager.selectPrepare(this,"select password from "+DbManager.objTable+" where account=?;",new String[]{account});
        String pass=null;
        if(cur.moveToFirst()){
            pass=cur.getString(0);

        }
        cur.close();
        return pass;
    }
}
