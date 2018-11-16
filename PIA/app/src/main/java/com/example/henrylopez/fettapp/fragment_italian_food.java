package com.example.henrylopez.fettapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;
import com.example.henrylopez.fettapp.metodos.mRestaurantes;

import com.google.firebase.database.DatabaseReference;

import org.w3c.dom.Text;

public class fragment_italian_food extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //RecyclerView
    private RecyclerView rvPreferences;
    //Referencia de Base de datos
    private DatabaseReference mDatabase;
    Bundle bundle;

    public fragment_italian_food() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static fragment_italian_food newInstance(String param1, String param2) {
        fragment_italian_food fragment = new fragment_italian_food();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    ImageView btnRatingF, btnLocationF;
    TextView tvDescFilt;
    String selection;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_fragment_italian_food, container, false);

        bundle=getArguments();
        selection=bundle.getString("Selection");
        tvDescFilt=view.findViewById(R.id.tvFilterSelection);
        tvDescFilt.setText(selection);

        btnLocationF=view.findViewById(R.id.ivBtnLocationFilter);
        btnRatingF=view.findViewById(R.id.ivBtnRatingFilter);
        btnRatingF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //filtro_rating();
                createRadioListDialog();
            }
        });

        btnLocationF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filtroMunicipio();
            }
        });


        /**BOTON ATRAS*/
        ImageView btnAtras = (ImageView) view.findViewById(R.id.ivBtnBack);
        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.flFragment,new fragment_start()).commit();
            }
        });

        //CONEXION FIREBASE CONSULTA Y MOSTRAR DATOS

        /*CAMBIAR LA VARIABLE DE SELECCION A CONSTANTE LLAMADA RESTAURANTE
        * EN FIREBASE CAMBIAR SNACK POR RESTAURANTE
        * AÑADIR NODO DE CATEGORIA
        * MODIFICAR MRESTAURANTE.CLASS PARA ACEPTAR CATEGORIA getters and setters
        * MODIFICAR EQUALS A SELECCIÓN POR SER LA VARIABLE QUE ME DETECTA QUE IMAGEVIEW FUE SELECCIONADO
        *
        * --FILTRADOS
        * AÑADIR NODO DE MUNICIPIO PARA POSTERIOR FILTRADO
        * Y AÑADIR CODIGO PARA FILTRADO POR PUNTUACIÓN
        *
        * */
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Restaurants");
        Query query = mDatabase.orderByChild("Category").equalTo(selection);
        mDatabase.keepSynced(true);
        //Declarar RecyclerView y asignarlo a una variable
        rvPreferences= (RecyclerView)view.findViewById(R.id.rvRestaurants);
        //Habilitar que RecyclerView para que se modifiqué segun el contenido del adaptador
        rvPreferences.setHasFixedSize(true);
        rvPreferences.setLayoutManager(new LinearLayoutManager(getContext()));

        //ADAPTADOR FIREBASE PARA POSTERIORMENTE ASIGNARLO A RECYCLERVIEW
        FirebaseRecyclerAdapter<mRestaurantes,mRestaurantesViewHolder> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<mRestaurantes, mRestaurantesViewHolder>
                        (mRestaurantes.class,R.layout.restaurantes_row,mRestaurantesViewHolder.class,query) {
                    @Override
                    protected void populateViewHolder(mRestaurantesViewHolder viewHolder, mRestaurantes model, int position) {
                        Log.i("dato", "populateViewHolder: " + model.getRestaurantDire());
                        viewHolder.setRestaurantDire(model.getRestaurantDire());
                        viewHolder.setRestaurantImage(getContext(),model.getImage());
                        viewHolder.setRestaurantName(model.getRestaurantName());
                        viewHolder.setRestaurantRating(model.getRestaurantRating());
                    }

                    @Override
                    public void onBindViewHolder(mRestaurantesViewHolder viewHolder, int position) {
                        super.onBindViewHolder(viewHolder, position);
                        viewHolder.setOnClickListeners();
                    }
                };
        //ASIGNARTODO AL RECYCLER VIEW
        rvPreferences.setAdapter(firebaseRecyclerAdapter);
        return view;
    }


    private void filtro_rating(String stars){
        View view=getView();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Restaurants");
        Query query;
        Boolean val=false;
        if(stars.equals("Desactivar Filtro")){
            query = mDatabase.orderByChild("Category").equalTo(selection);
            val=false;
        }else {
            query = mDatabase.orderByChild("CatRat").startAt(selection + stars).endAt(selection + stars);
            val=true;
        }
        mDatabase.keepSynced(true);
        //Declarar RecyclerView y asignarlo a una variable
        rvPreferences= (RecyclerView)view.findViewById(R.id.rvRestaurants);
        //Habilitar que RecyclerView para que se modifiqué segun el contenido del adaptador
        rvPreferences.setHasFixedSize(true);
        rvPreferences.setLayoutManager(new LinearLayoutManager(getContext()));

        //ADAPTADOR FIREBASE PARA POSTERIORMENTE ASIGNARLO A RECYCLERVIEW
        FirebaseRecyclerAdapter<mRestaurantes,mRestaurantesViewHolder> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<mRestaurantes, mRestaurantesViewHolder>
                        (mRestaurantes.class,R.layout.restaurantes_row,mRestaurantesViewHolder.class,query) {
                    @Override
                    protected void populateViewHolder(mRestaurantesViewHolder viewHolder, mRestaurantes model, int position) {
                        viewHolder.setRestaurantDire(model.getRestaurantDire());
                        viewHolder.setRestaurantImage(getContext(),model.getImage());
                        viewHolder.setRestaurantName(model.getRestaurantName());
                        viewHolder.setRestaurantRating(model.getRestaurantRating());
                    }
                    @Override
                    public void onBindViewHolder(mRestaurantesViewHolder viewHolder, int position) {
                        super.onBindViewHolder(viewHolder, position);
                        viewHolder.setOnClickListeners();
                    }
                };
        //ASIGNARTODO AL RECYCLER VIEW
        rvPreferences.setAdapter(firebaseRecyclerAdapter);
        LinearLayoutManager mLay= new LinearLayoutManager(getActivity());
        mLay.setReverseLayout(val);
        mLay.setStackFromEnd(val);
        rvPreferences.setLayoutManager(mLay);
    }

    int catseles=5;
    public AlertDialog createRadioListDialog() {
        munseles=6;
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final CharSequence[] items = new CharSequence[6];

        items[0] = "1";
        items[1] = "2";
        items[2] = "3";
        items[3] = "4";
        items[4] = "5";
        items[5] = "Desactivar Filtro";
        builder.setTitle("Selecciona cantidad de estrellas")
                .setSingleChoiceItems(items, catseles, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(
                                getActivity(),
                                "Seleccionaste: " + items[which],
                                Toast.LENGTH_SHORT)
                                .show();
                        switch (items[which].toString()) {
                            case "1":
                                catseles = 0;
                                break;
                            case "2":
                                catseles = 1;
                                break;
                            case "3":
                                catseles = 2;
                                break;
                            case "4":
                                catseles = 3;
                                break;
                            case "5":
                                catseles = 4;
                                break;
                            case "Desactivar Filtro":
                                catseles = 5;
                                break;
                        }
                        String stars= String.valueOf(items[which]);
                        filtro_rating(stars);
                    }
                });
        return builder.show();
    }
    int munseles=6;
    public AlertDialog filtroMunicipio() {
        catseles=5;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final CharSequence[] items = new CharSequence[7];
        items[0] = "San Nicolás";
        items[1] = "San Pedro";
        items[2] = "Monterrey";
        items[3] = "Guadalupe";
        items[4] = "Apodaca";
        items[5] = "Escobedo";
        items[6] = "Desactivar Filtro";
        builder.setTitle("Selecciona el municipio")
                .setSingleChoiceItems(items, munseles, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(
                                getActivity(),
                                "Seleccionaste: " + items[which],
                                Toast.LENGTH_SHORT)
                                .show();
                        switch (items[which].toString()){
                            case "San Nicolás":
                                munseles=0;
                                break;
                            case "San Pedro":
                                munseles=1;
                                break;
                            case "Monterrey":
                                munseles=2;
                                break;
                            case "Guadalupe":
                                munseles=3;
                                break;
                            case "Apodaca":
                                munseles=4;
                                break;
                            case "Escobedo":
                                munseles=5;
                                break;
                            case "Desactivar Filtro":
                                munseles=6;
                                break;
                        }
                        String municipio= String.valueOf(items[which]);
                        filtro_municipio(municipio);
                    }
                });
        return builder.show();
    }

    private void filtro_municipio(String municipio){
        Boolean val;
        View view=getView();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Restaurants");
        Query query;
        if(municipio.equals("Desactivar Filtro")){
            query = mDatabase.orderByChild("Category").equalTo(selection);
            val=false;
        }else {
            query = mDatabase.orderByChild("CatMun").startAt(selection + municipio).endAt(selection + municipio);
            val=false;
        }
        mDatabase.keepSynced(true);
        //Declarar RecyclerView y asignarlo a una variable
        rvPreferences= (RecyclerView)view.findViewById(R.id.rvRestaurants);
        //Habilitar que RecyclerView para que se modifiqué segun el contenido del adaptador
        rvPreferences.setHasFixedSize(true);
        rvPreferences.setLayoutManager(new LinearLayoutManager(getContext()));

        //ADAPTADOR FIREBASE PARA POSTERIORMENTE ASIGNARLO A RECYCLERVIEW
        FirebaseRecyclerAdapter<mRestaurantes,mRestaurantesViewHolder> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<mRestaurantes, mRestaurantesViewHolder>
                        (mRestaurantes.class,R.layout.restaurantes_row,mRestaurantesViewHolder.class,query) {
                    @Override
                    protected void populateViewHolder(mRestaurantesViewHolder viewHolder, mRestaurantes model, int position) {
                        viewHolder.setRestaurantDire(model.getRestaurantDire());
                        viewHolder.setRestaurantImage(getContext(),model.getImage());
                        viewHolder.setRestaurantName(model.getRestaurantName());
                        viewHolder.setRestaurantRating(model.getRestaurantRating());
                    }
                    @Override
                    public void onBindViewHolder(mRestaurantesViewHolder viewHolder, int position) {
                        super.onBindViewHolder(viewHolder, position);
                        viewHolder.setOnClickListeners();
                    }
                };
        //ASIGNARTODO AL RECYCLER VIEW
        rvPreferences.setAdapter(firebaseRecyclerAdapter);
        LinearLayoutManager mLay= new LinearLayoutManager(getActivity());
        mLay.setReverseLayout(val);
        mLay.setStackFromEnd(val);
        rvPreferences.setLayoutManager(mLay);
    }



    public static class mRestaurantesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View mView;
        Context context;
        public mRestaurantesViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
            context=itemView.getContext();
        }

        public void setRestaurantName(String restaurantName){
            TextView restaurant_name=mView.findViewById(R.id.tvRestaurantName);
            if(!restaurantName.equals(""))
            restaurant_name.setText(restaurantName);
        }

        public void setRestaurantDire(String restaurantDir){
            TextView restaurant_Dir=(TextView)mView.findViewById(R.id.tvRestaurantDir);
            if(!restaurantDir.equals(""))
            restaurant_Dir.setText(restaurantDir);
        }

        public void setRestaurantRating(float restaurantRating){
            RatingBar restaurant_Rating= mView.findViewById(R.id.rbRestaurantRating);
            restaurant_Rating.setRating(restaurantRating);
        }

        public void setRestaurantImage(Context ctx,String restaurantImage){
            ImageView restaurant_Image= mView.findViewById(R.id.ivRestaurant);
            if(!restaurantImage.equals(""))
                Glide.with(ctx).load(restaurantImage).thumbnail(0.01f).into(restaurant_Image);
            //Picasso.with(ctx).load(restaurantImage).into(restaurant_Image);
        }

        public void setOnClickListeners()
        {
            CardView cv=(CardView) mView.findViewById(R.id.cvRestaurante);
            cv.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.cvRestaurante:
                    Toast.makeText(context,"Cargando...",Toast.LENGTH_SHORT).show();
                    TextView tv=view.findViewById(R.id.tvRestaurantName);
                    //Log.i("holita",tv.getText().toString());
                    Intent intent = new Intent(context,restaurant.class);
                    intent.putExtra("restaurant",tv.getText().toString());
                    context.startActivity(intent);
                    break;
            }
        } //implements View.OnClickListener
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            /*throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");*/
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
