package com.example.henrylopez.fettapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

public class registro_user extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;
    private SignInButton signInButton;
    public static final int SIGN_IN_CODE =777;
    //public static String ID_GOOGLE_USER = "";
    //public static final String STRING_SP_NAME="fett";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_registro_user);

        //
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        //Inicializar API
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        signInButton = (SignInButton) findViewById(R.id.signInButton);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,SIGN_IN_CODE);
            }
        });
        /*INICIO DE ALMACENAMIENTO LOCAL
        Context context = this;
        SharedPreferences sharedPref = context.getSharedPreferences(STRING_SP_NAME,context.MODE_PRIVATE);*/
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGN_IN_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }



    private void handleSignInResult(GoogleSignInResult result) {
        if(result.isSuccess()){
            /*GoogleSignInAccount account = result.getSignInAccount();
            ID_GOOGLE_USER = account.getId();

            SharedPreferences sharedPref = this.getSharedPreferences(STRING_SP_NAME,Context.MODE_PRIVATE );
            SharedPreferences.Editor mEditor = sharedPref.edit();

            mEditor.putString("ID_GOOGLE_USER",ID_GOOGLE_USER);
            mEditor.putString("ID_GOOGLE_USER_PHOTO",account.getPhotoUrl().toString());
            mEditor.commit();
            */
            goMainScreen();
        }else{
            Toast.makeText(this, R.string.not_log_in, Toast.LENGTH_SHORT).show();
        }

    }

    private void goMainScreen() {
        Intent intent = new Intent(this,menuprincipal.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
