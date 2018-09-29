package com.example.henrylopez.fettapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;



public class IntroActivity extends AppIntro {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE); INECESARIO
        //OCULTAR PANEL DE NOTIFICACIONES
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //OCULTAR BARRA DE TITULO

        //AÑADIR SLIDERS
        addSlide(SampleSlide.newInstance(R.layout.slider_1));
        addSlide(SampleSlide.newInstance(R.layout.slider_2));
        addSlide(SampleSlide.newInstance(R.layout.slider_3));
        //addSlide(SampleSlide.newInstance(R.layout.activity_registro_user));
        setDepthAnimation ();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        v.vibrate(60);
    }


    //BOTON DE SALTAR
    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent intent = new Intent(this,inicio.class);
        startActivity(intent);
    }

    //BOTON DE HECHO
    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent = new Intent(this,inicio.class);
        startActivity(intent);
    }
}
