package com.example.henrylopez.fettapp;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;


public class menuprincipal extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        int i=1;
        //METODO PARA LEER EL FRAGMENT
        FragmentManager fragmentManager = getSupportFragmentManager();


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            transaction = fragmentManager.beginTransaction();
            //DETECTAR QUE ITEM ES SELECCIONADO PARA MOSTRAR EL FRAGMENT ADECUADO
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    transaction.replace(R.id.flFragment,new fragment_search(),"News").commit();
                    i=1;
                    return true;
                case R.id.navigation_preferences: //INICIO O PREFERENCIAS
                    transaction.replace(R.id.flFragment,new fragment_start(),"Home").commit();
                    i=1;
                    return true;
                case R.id.navigation_config:
                    if(i==1) {
                        i=0;
                        transaction.replace(R.id.flFragment, new fragment_config()).commit();
                        return true;
                    }
            }
            return false;
        }
    };
    FragmentTransaction transaction;
    BottomNavigationView navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //OCULTAR BARRA DE TITULO
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        //CARGAR LAYOUT
        setContentView(R.layout.activity_menuprincipal);

        //CARGAR FRAME POR DEFECTO EL DE INICIO
        //FragmentManager fragmentManager = getSupportFragmentManager();
        //FragmentTransaction transaction = fragmentManager.beginTransaction();
        //transaction.replace(R.id.flFragment,new fragment_start()).commit();

        //CARGAR BARRA DE NAVEGACION INFERIOR
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_preferences);
    }

    //CONTROLAR EVENTO DE BOTON ATRAS EVITANDO CIERRE DE APP
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
       if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            /*// Esto es lo que hace mi botón al pulsar ir a atrás
            Toast.makeText(getApplicationContext(), "hola",
                    Toast.LENGTH_SHORT).show();*/
           Fragment myFragment = getFragmentManager().findFragmentById(R.id.flFragment);
           if (myFragment != null && myFragment.isVisible()) {
               transaction.replace(R.id.flFragment,new fragment_start()).commit();
           }
            return true;
       }
        return super.onKeyDown(keyCode, event);
    }
}
