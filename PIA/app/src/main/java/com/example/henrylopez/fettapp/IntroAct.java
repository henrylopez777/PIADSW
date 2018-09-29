package com.example.henrylopez.fettapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class IntroAct extends AppCompatActivity {
    //INECESARIA PERO POR SI LAS DUDAS NO BORRAR XD
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_intro_act);

        Intent intent = new Intent(IntroAct.this,IntroActivity.class);
        startActivity(intent);
    }
}
