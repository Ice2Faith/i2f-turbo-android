package com.ugex.savelar.avusercontrolview.UserControlDrawView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ugex.savelar.avusercontrolview.R;

public class UserControlDrawViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_control_draw_view);
        InitApp();
    }

    private MyRollbackBackgroundView view;
    private void InitApp() {
        view=findViewById(R.id.myRollbackView);
        view.setRunFlag(true);
        new Thread(view).start();

        //setContentView(view);
    }
}
