package com.example.henrylopez.fettapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.example.henrylopez.fettapp.metodos.mRestaurantes;

import com.google.firebase.database.DatabaseReference;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_fragment_italian_food, container, false);

        /**BOTON ATRAS*/
        Button btnAtras = (Button) view.findViewById(R.id.btnAtras);
        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.flFragment,new fragment_start()).commit();
            }
        });

        //CONEXION FIREBASE CONSULTA Y MOSTRAR DATOS
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Snacks");
        mDatabase.keepSynced(true);
        //Declarar RecyclerView y asignarlo a una variable
        rvPreferences= (RecyclerView)view.findViewById(R.id.rvRestaurants);
        //Habilitar que RecyclerView para que se modifiqué segun el contenido del adaptador
        rvPreferences.setHasFixedSize(true);
        rvPreferences.setLayoutManager(new LinearLayoutManager(getContext()));

        //ADAPTADOR FIREBASE PARA POSTERIORMENTE ASIGNARLO A RECYCLERVIEW
        FirebaseRecyclerAdapter<mRestaurantes,mRestaurantesViewHolder> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<mRestaurantes, mRestaurantesViewHolder>
                        (mRestaurantes.class,R.layout.restaurantes_row,mRestaurantesViewHolder.class,mDatabase) {
                    @Override
                    protected void populateViewHolder(mRestaurantesViewHolder viewHolder, mRestaurantes model, int position) {
                        Log.i("dato", "populateViewHolder: " + model.getRestaurantDire());
                        viewHolder.setRestaurantDire(model.getRestaurantDire());
                        viewHolder.setRestaurantImage(getContext(),model.getImage());
                        viewHolder.setRestaurantName(model.getRestaurantName());
                        viewHolder.setRestaurantRating(model.getRestaurantRating());
                    }
                };
        //ASIGNARTODO AL RECYCLER VIEW
        rvPreferences.setAdapter(firebaseRecyclerAdapter);
        return view;
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
            restaurant_name.setText(restaurantName);
        }

        public void setRestaurantDire(String restaurantDir){
            TextView restaurant_Dir=(TextView)mView.findViewById(R.id.tvRestaurantDir);
            restaurant_Dir.setText(restaurantDir);
        }

        public void setRestaurantRating(float restaurantRating){
            RatingBar restaurant_Rating= mView.findViewById(R.id.rbRestaurantRating);
            restaurant_Rating.setRating(restaurantRating);
        }

        public void setRestaurantImage(Context ctx,String restaurantImage){
            ImageView restaurant_Image= mView.findViewById(R.id.ivRestaurant);
            Picasso.with(ctx).load(restaurantImage).into(restaurant_Image);
        }


        @Override
        public void onClick(View view) {

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
