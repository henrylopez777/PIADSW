package com.example.henrylopez.fettapp;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.henrylopez.fettapp.metodos.mPreferences;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fragment_start.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragment_start#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_start extends Fragment {
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

    public fragment_start() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_start.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_start newInstance(String param1, String param2) {
        fragment_start fragment = new fragment_start();
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
    public void onStart() {
        super.onStart();

    }

    //Variable para administrar fragments
    static FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       final View view = inflater.inflate(R.layout.fragment_fragment_start, container, false);
       fragmentManager = getActivity().getSupportFragmentManager();

       /**CardView card_view = (CardView) view.findViewById(R.id.cvItalianFood); // creating a CardView and assigning a value.
        card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.flFragment,new fragment_italian_food()).commit();
            }
        });*/

        //CONEXION FIREBASE CONSULTA Y MOSTRAR DATOS

        mDatabase= FirebaseDatabase.getInstance().getReference();
        Query filtro=mDatabase.child("Preferences");

        //Los siguientes filtros son para devolver un unico valor será usado a la hora de traer restaurantes
        //Query filtro=mDatabase.child("Preferences").orderByKey().equalTo("100");
        //Query filtro=mDatabase.child("Preferences").orderByChild("Desc").equalTo("Snacks");
        mDatabase.keepSynced(true);


        //Declarar RecyclerView y asignarlo a una variable
        rvPreferences= (RecyclerView)view.findViewById(R.id.rvPreferences);
        //Habilitar que RecyclerView para que se modifiqué segun el contenido del adaptador
        rvPreferences.setHasFixedSize(true);
        rvPreferences.setLayoutManager(new LinearLayoutManager(getContext()));

        //ADAPTADOR FIREBASE PARA POSTERIORMENTE ASIGNARLO A RECYCLERVIEW
        FirebaseRecyclerAdapter<mPreferences,mPreferencesViewHolder>firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<mPreferences, mPreferencesViewHolder>
                        (mPreferences.class,R.layout.preferences_row,mPreferencesViewHolder.class,filtro) {
                    //mPreferences.class(Constructor), ASIGNAR LAS PREFERENCIAS DE COMO SERÁ DESPLEGADO, CLASE ASIGNADORA DE EVENTOS, BaseDatos
                    @Override
                    protected void populateViewHolder(mPreferencesViewHolder viewHolder, mPreferences model, int position) {
                        viewHolder.setImage(getContext().getApplicationContext(),model.getImage());
                        viewHolder.setDesc(model.getDesc());
                        //viewHolder.setOnClickListeners();
                    }

                    @Override
                    public void onBindViewHolder(mPreferencesViewHolder viewHolder, int position) {
                        super.onBindViewHolder(viewHolder, position);
                        viewHolder.setOnClickListeners();
                    }
                };
        //ASIGNARTODO AL RECYCLER VIEW
        rvlayoutManager=new GridLayoutManager(getContext(), 2);
        rvPreferences.setLayoutManager(rvlayoutManager);
        rvPreferences.setAdapter(firebaseRecyclerAdapter);


        return view;
    }

    RecyclerView.LayoutManager rvlayoutManager;
    public static class mPreferencesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener { //implements View.OnClickListener
        View mView;
        Context context;
        ImageView ivPreferences;

        //PROCEDIMIENTO PARA ASIGNAR
        public mPreferencesViewHolder(View itemView){
            super(itemView);
            mView=itemView;
            context = itemView.getContext();
        }

        //PROCEDIMIENTO PARA ASIGNAR IMAGEN
        public void setImage(Context ctx, String image){
            ImageView pref_img= (ImageView) mView.findViewById(R.id.ivPreferences);
            Picasso.with(ctx).load(image).into(pref_img);
            //pref_img.setId('1');
        }

        public void setDesc(String Desc){
            ImageView pref_img= (ImageView) mView.findViewById(R.id.ivPreferences);
            pref_img.setContentDescription(Desc);
        }

        //ASIGNAR CLICK A CADA IMAGEVIEW
        void setOnClickListeners(){
            ivPreferences = (ImageView) mView.findViewById(R.id.ivPreferences);
            ivPreferences.setOnClickListener(this);
        }

        //ASIGNAR EVENTO ONCLICK
        //@Override
        public void onClick(View view) {
            Log.i("hola",String.valueOf(ivPreferences.getContentDescription()));
            switch (view.getId()){
                case R.id.ivPreferences:
                    //CARGAR FRAGMENTO CON LA POSIBILIDAD DE REGRESO Y ENVIANDO PARÁMETROS
                    Fragment fragment = new fragment_italian_food();
                    Fragment fragment1 = new fragment_start();
                    Bundle args = new Bundle();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    args.putString("Selection", (String) ivPreferences.getContentDescription());
                    fragment.setArguments(args);
                    //transaction.addToBackStack("start");
                    transaction.disallowAddToBackStack();
                    transaction.replace(R.id.flFragment,fragment);
                    /*if (fragment.isAdded()) {
                        transaction
                                .hide(fragment1);
                                .show(fragment);
                    } else {
                        transaction
                                .hide(currentFragment)
                                .add(R.id.container, fragment, tag)
                    }
                    transaction.hide(fragment1);
                    transaction.show(R)*/

                    transaction.commit();
                    break;
            }
        }
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
            Toast.makeText(context,R.string.start, Toast.LENGTH_SHORT);
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
