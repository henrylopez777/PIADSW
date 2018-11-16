package com.example.henrylopez.fettapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;


public class splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splashscreen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs=getSharedPreferences("User",MODE_PRIVATE);
                String id= prefs.getString("id",null);
                if(id==null){
                    Intent intent=new Intent(splashscreen.this, IntroActivity.class);
                    startActivity(intent);
                    finish();
                }
                if(id!=null){
                    Intent intent1=new Intent(getApplicationContext(),registro_user.class);
                    startActivity(intent1);
                    finish();
                }
            }
        },2000);

    }
}
