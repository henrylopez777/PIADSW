package com.example.henrylopez.fettapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;



public class menuprincipal extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        int i=1;
        //METODO PARA LEER EL FRAGMENT
        FragmentManager fragmentManager = getSupportFragmentManager();


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            //DETECTAR QUE ITEM ES SELECCIONADO PARA MOSTRAR EL FRAGMENT ADECUADO
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    transaction.replace(R.id.flFragment,new fragment_search()).commit();
                    i=1;
                    return true;
                case R.id.navigation_preferences: //INICIO O PREFERENCIAS
                    transaction.replace(R.id.flFragment,new fragment_start()).commit();
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
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_preferences);
    }

}
