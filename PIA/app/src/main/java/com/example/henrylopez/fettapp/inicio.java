package com.example.henrylopez.fettapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class inicio extends AppCompatActivity implements fragmentImg.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_inicio);
    }

    public void registro_user(View v){
        Intent registroUser = new Intent(inicio.this,registro_user.class);
        startActivity(registroUser);
    }

    public void inicio_sesion(View v){
        Intent registroUser = new Intent(inicio.this,registro_user.class);
        startActivity(registroUser);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
