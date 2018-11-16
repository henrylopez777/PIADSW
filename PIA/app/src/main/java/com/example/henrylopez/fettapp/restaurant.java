package com.example.henrylopez.fettapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Rating;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.example.henrylopez.fettapp.metodos.mRestaurantes;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

public class restaurant extends AppCompatActivity {
    String selection;
    Bundle bundle;
    TextView tvRestaurantName, tvTel, tvDir, tvPrecio, tvHorario; // tvFb, tvWebSite;
    ImageView btnIVBackMenu;
    RatingBar rbPuntos;

    ImageView ivRTel, ivRDir, ivRPrice, ivRTime, ivRFb, ivRWs, ivIRestaurant, ivRestfondo, ivCalendar, ivShare, ivBtnMaps;
    ConstraintLayout relativeLayout;

    private GoogleMap mgoogleMap;
    ImageView transparent;
    ScrollView scroll;

    private DatabaseReference mDatabase;
    mRestaurantes datos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_restaurant);
        bundle = getIntent().getExtras();
        if (bundle != null) {
            selection = bundle.getString("restaurant");
        }
        ivBtnMaps = findViewById(R.id.ivOpenMap);
        tvRestaurantName = findViewById(R.id.tvRestTitle);
        tvRestaurantName.setText(selection);
        //relativeLayout.findViewById(R.id.cly_imgRest);
        tvTel = findViewById(R.id.tvRestTel);
        tvDir = findViewById(R.id.tvRestDir);
        tvPrecio = findViewById(R.id.tvRestPrice);
        tvHorario = findViewById(R.id.tvRestHor);
        //tvFb=findViewById(R.id.tvRestFace);
        //tvWebSite=findViewById(R.id.tvRestWeb);
        btnIVBackMenu = findViewById(R.id.ivBtnBackMenu);
        rbPuntos = findViewById(R.id.rbRestRat);
        ivIRestaurant = findViewById(R.id.imgRest);
        ivRestfondo = findViewById(R.id.imgRestFondo);
        //scroll = (ScrollView) findViewById(R.id.svLayout);

        ivCalendar = findViewById(R.id.btnIVCalendar);
        ivShare = findViewById(R.id.btnIVShare);

        ivRTel = findViewById(R.id.ivRestTel);
        ivRDir = findViewById(R.id.ivRestDir);
        ivRPrice = findViewById(R.id.ivRestPrice);
        ivRTime = findViewById(R.id.ivRestHor);
        ivRFb = findViewById(R.id.ivRestFace);
        ivRWs = findViewById(R.id.ivRestWeb);
        btnIVBackMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ivCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEventToCalendar();
            }
        });
        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickWhatsApp();
            }
        });
        ivBtnMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gmaps();
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                cargar();
            }
        }).start();


    }

    private void gmaps(){
        Toast.makeText(getApplicationContext(),"Cargando...",Toast.LENGTH_SHORT).show();
        //Intent intent = new Intent(getApplicationContext(),maps.class);
        Intent intent = new Intent(this,maps.class);
        intent.putExtra("restaurant",selection);
        this.startActivity(intent);
    }

    String clTitle, clDesc, clDir;
    private void addEventToCalendar(){
        Toast.makeText(this,"Iniciando Calendario...",Toast.LENGTH_SHORT).show();
        //Calendar cal = Calendar.getInstance();
        /*cal.set(Calendar.DAY_OF_MONTH, 29);
        cal.set(Calendar.MONTH, 4);
        cal.set(Calendar.YEAR, 2013);
        cal.set(Calendar.HOUR_OF_DAY, 22);
        cal.set(Calendar.MINUTE, 45);*/

        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");

        /*intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, cal.getTimeInMillis()+60*60*1000);*/

        intent.putExtra(CalendarContract.Events.ALL_DAY, false);
        //intent.putExtra(CalendarContract.Events.RRULE , "FREQ=DAILY");
        intent.putExtra(CalendarContract.Events.TITLE, clTitle);
        intent.putExtra(CalendarContract.Events.DESCRIPTION, clDesc);
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION,clDir);

        startActivity(intent);
    }

    public void onClickWhatsApp() {
       Toast.makeText(getApplicationContext(), "Compartir...", Toast.LENGTH_SHORT).show();
        PackageManager pm=getApplicationContext().getPackageManager();
        try {
            Intent waIntent = new Intent(Intent.ACTION_SEND);
            String text = "*¿Qué opinas de éste restaurante?*\n\n*Nombre:* "+ tvRestaurantName.getText().toString()+"\n"+
                    "*Dirección:* "+ tvDir.getText().toString() +"\n*Tel:* " + tvTel.getText().toString() + "\n*Horario:*\n " + tvHorario.getText() +"\n\n*Enviado desde FettApp* \n\n https://fettapp.000webhostapp.com/";
            PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            waIntent.setPackage("com.whatsapp");
            waIntent.setType("text/plain");
            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(waIntent, "Compartir Con"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getApplicationContext(), "WhatsApp no está instalado", Toast.LENGTH_SHORT)
                    .show();
        }

    }
    String imageWP;
    private void cargar(){
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Restaurants");
        Query query = mDatabase;
        clTitle=selection;
        query.orderByChild("RestaurantName").equalTo(selection).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot item_snapshot:dataSnapshot.getChildren()) {
                    //Log.i("Hola",item_snapshot.child("RestaurantDire").getValue().toString());
                    if(!item_snapshot.child("Image").getValue().toString().isEmpty()){
                        //Glide.with(getApplicationContext()).load(item_snapshot.child("Image").getValue().toString()).into(ivIRestaurant);
                        Glide.with(getApplicationContext()).load(item_snapshot.child("Image").getValue().toString()).thumbnail(0.01f).into(ivRestfondo);
                        imageWP = item_snapshot.child("Image").getValue().toString();
                    }
                    if(item_snapshot.child("RestaurantDire").getValue()!=null) {
                        clDir=item_snapshot.child("RestaurantDire").getValue().toString();
                        tvDir.setText(clDir);
                    }
                    if(item_snapshot.child("RestaurantRating").getValue()!=null){
                        rbPuntos.setRating(Float.parseFloat(item_snapshot.child("RestaurantRating").getValue().toString()));
                    }
                    if(item_snapshot.child("RPrice").getValue()!=null){
                        tvPrecio.setText(item_snapshot.child("RPrice").getValue().toString());
                    }
                    if(item_snapshot.child("RTel").getValue().toString().isEmpty()){
                        tvTel.setVisibility(View.INVISIBLE);
                        ivRTel.setVisibility(View.INVISIBLE);
                    }
                    else{
                        clDesc=item_snapshot.child("RTel").getValue().toString();
                        tvTel.setText(clDesc);
                    }
                    if(item_snapshot.child("RTime").getValue()!=null){
                        tvHorario.setText(item_snapshot.child("RTime").getValue().toString());
                    }
                    if(!item_snapshot.child("RWs").getValue().toString().isEmpty()){
                        ivRWs.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try{
                                    Toast.makeText(getApplicationContext(),"Cargando...",Toast.LENGTH_SHORT).show();
                                    Uri uri = Uri.parse(item_snapshot.child("RWs").getValue().toString());
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);
                                }catch (Exception e){

                                }
                            }
                        });
                        //tvWebSite.setText(item_snapshot.child("RWs").getValue().toString());
                    }else{
                        ivRWs.setVisibility(View.INVISIBLE);
                        //tvWebSite.setVisibility(View.INVISIBLE);
                    }
                    if(!item_snapshot.child("RFb").getValue().toString().isEmpty()){
                        ivRFb.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try{
                                    Toast.makeText(getApplicationContext(),"Cargando...",Toast.LENGTH_SHORT).show();
                                    Uri uri = Uri.parse(item_snapshot.child("RFb").getValue().toString());
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);
                                }catch (Exception e){

                                }
                            }
                        });
                        //tvFb.setText();
                    }else{
                        /*tvFb.setVisibility(View.INVISIBLE);*/
                        ivRFb.setVisibility(View.INVISIBLE);
                    }
                    mRestaurantes mr = item_snapshot.getValue(mRestaurantes.class);
                    if(mr.getLat().equals(0.1)|| mr.getLng().equals(0.1)){
                        ivBtnMaps.setVisibility(View.INVISIBLE);
                    }else{
                        ivBtnMaps.setVisibility(View.VISIBLE);
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}
