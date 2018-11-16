package com.example.henrylopez.fettapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.henrylopez.fettapp.metodos.mRestaurantes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class maps extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mgoogleMap;
    //ImageView transparent;
    ScrollView scroll;
    Bundle bundle;
    String selection;
    ImageView btnAtras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_maps);
        bundle = getIntent().getExtras();
        if (bundle != null) {
            selection = bundle.getString("restaurant");
        }
        btnAtras=findViewById(R.id.ivBtnBackMenuMap);
        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        /*transparent=findViewById(R.id.imagetrans);*/
        /*transparent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        scroll.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        scroll.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        scroll.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });*/
        setupMap();
    }
    private LatLngBounds Coordenadas;
    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        mgoogleMap = gMap;
        setupParametrosMapa();
        cargar();
    }

    private Marker marker;
    Double Lat=0.0;
    Double Lng=0.0;
    String RestTitle;
    private void agregarMarcadores(double lat, double lng, String RestName) {
        LatLng coordenadas=new LatLng(lat,lng);
        CameraUpdate miUbicacion=CameraUpdateFactory.newLatLngZoom(coordenadas,16);
        if( (marker!=null)) marker.remove();
        marker=mgoogleMap.addMarker(new MarkerOptions()
        .position(coordenadas)
        .title(RestName));
        //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_on_black_24dp)));
        mgoogleMap.animateCamera(miUbicacion);
    }

    private void setupParametrosMapa() {
        mgoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mgoogleMap.getUiSettings().setCompassEnabled(true);
        mgoogleMap.getUiSettings().setMapToolbarEnabled(true);
        mgoogleMap.getUiSettings().setRotateGesturesEnabled(true);
        mgoogleMap.setMinZoomPreference(3.0f);
        /*LatLngBounds Coordenadas= new LatLngBounds(new LatLng(-44, 113), new LatLng(-10, 154));
        mgoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Coordenadas.getCenter(), 10));*/

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mgoogleMap.setMyLocationEnabled(true);
    }

    private DatabaseReference mDatabase;
    private void cargar(){
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Restaurants");
        Query query = mDatabase;
        query.orderByChild("RestaurantName").equalTo(selection).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot item_snapshot : dataSnapshot.getChildren()) {
                    try
                    {
                        mRestaurantes mr =item_snapshot.getValue(mRestaurantes.class);
                        //if (!item_snapshot.child("Lat").getValue().toString().isEmpty()) {
                        //Lat = Double.parseDouble(item_snapshot.child("Lat").getValue().toString());
                        Lat = mr.getLat();
                        RestTitle=mr.getRestaurantName();
                        //}
                        //if (!item_snapshot.child("Lng").getValue().toString().isEmpty()) {
                        //Lat = Double.parseDouble(item_snapshot.child("Lng").getValue().toString());
                        Lng = mr.getLng();
                        agregarMarcadores(Lat,Lng,RestTitle);
                        //}

                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),"Cargando...",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
