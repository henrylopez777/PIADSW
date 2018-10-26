package com.example.henrylopez.fettapp;

import android.content.Intent;
import android.media.Rating;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.example.henrylopez.fettapp.metodos.mRestaurantes;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class restaurant extends AppCompatActivity {
String selection;
Bundle bundle;
TextView tvRestaurantName, tvTel, tvDir, tvPrecio, tvHorario, tvFb, tvWebSite;
ImageView btnIVBackMenu;
RatingBar rbPuntos;

ImageView ivRTel, ivRDir, ivRPrice, ivRTime, ivRFb, ivRWs;

private DatabaseReference mDatabase;
mRestaurantes datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_restaurant);
        bundle=getIntent().getExtras();
        if(bundle!=null){
            selection=bundle.getString("restaurant");
        }
        tvRestaurantName=findViewById(R.id.tvRestTitle);
        tvRestaurantName.setText(selection);

        tvTel=findViewById(R.id.tvRestTel);
        tvDir=findViewById(R.id.tvRestDir);
        tvPrecio=findViewById(R.id.tvRestPrice);
        tvHorario=findViewById(R.id.tvRestHor);
        tvFb=findViewById(R.id.tvRestFace);
        tvWebSite=findViewById(R.id.tvRestWeb);
        btnIVBackMenu=findViewById(R.id.ivBtnBackMenu);
        rbPuntos=findViewById(R.id.rbRestRat);

        ivRTel=findViewById(R.id.ivRestTel);
        ivRDir=findViewById(R.id.ivRestDir);
        ivRPrice=findViewById(R.id.ivRestPrice);
        ivRTime=findViewById(R.id.ivRestHor);
        ivRFb=findViewById(R.id.ivRestFace);
        ivRWs=findViewById(R.id.ivRestWeb);

        btnIVBackMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Restaurants");
        Query query = mDatabase;
        query.orderByChild("RestaurantName").equalTo(selection).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot item_snapshot:dataSnapshot.getChildren()) {
                    //Log.i("Hola",item_snapshot.child("RestaurantDire").getValue().toString());

                    if(item_snapshot.child("RestaurantDire").getValue()!=null) {
                        tvDir.setText(item_snapshot.child("RestaurantDire").getValue().toString());
                    }
                    if(item_snapshot.child("RestaurantRating").getValue()!=null){
                        rbPuntos.setRating(Float.parseFloat(item_snapshot.child("RestaurantRating").getValue().toString()));
                    }
                    if(item_snapshot.child("RPrice").getValue()!=null){
                        tvPrecio.setText(item_snapshot.child("RPrice").getValue().toString());
                    }
                    if(item_snapshot.child("RTel").getValue()!=null){
                        tvTel.setText(item_snapshot.child("RTel").getValue().toString());
                    }
                    else{
                        tvTel.setVisibility(View.INVISIBLE);
                        ivRTel.setVisibility(View.INVISIBLE);
                    }
                    if(item_snapshot.child("RTime").getValue()!=null){
                        tvHorario.setText(item_snapshot.child("RTime").getValue().toString());
                    }
                    if(!item_snapshot.child("RWs").getValue().toString().isEmpty()){
                        tvWebSite.setText(item_snapshot.child("RWs").getValue().toString());
                    }else{
                        ivRWs.setVisibility(View.INVISIBLE);
                        tvWebSite.setVisibility(View.INVISIBLE);
                    }
                    if(!item_snapshot.child("RFb").getValue().toString().isEmpty()){
                        tvFb.setText(item_snapshot.child("RFb").getValue().toString());
                    }else{
                        tvFb.setVisibility(View.INVISIBLE);
                        ivRFb.setVisibility(View.INVISIBLE);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}
