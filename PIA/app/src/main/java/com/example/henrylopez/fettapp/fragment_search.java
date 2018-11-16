package com.example.henrylopez.fettapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.henrylopez.fettapp.metodos.mPreferences;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.example.henrylopez.fettapp.metodos.mNews;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fragment_search.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragment_search#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_search extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView rvNews;
    private DatabaseReference mDatabase;

    public fragment_search() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_search.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_search newInstance(String param1, String param2) {
        fragment_search fragment = new fragment_search();
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
        final View view =inflater.inflate(R.layout.fragment_fragment_search, container, false);


        //CONEXION FIREBASE CONSULTA Y MOSTRAR DATOS

        mDatabase= FirebaseDatabase.getInstance().getReference();
        Query filtro=mDatabase.child("News");
        mDatabase.keepSynced(true);
        //Declarar RecyclerView y asignarlo a una variable
        rvNews=(RecyclerView)view.findViewById(R.id.rvNews);
        //Habilitar que RecyclerView para que se modifiqué segun el contenido del adaptador
        rvNews.setHasFixedSize(true);
        rvNews.setLayoutManager(new LinearLayoutManager(getContext()));

        //ADAPTADOR FIREBASE PARA POSTERIORMENTE ASIGNARLO A RECYCLERVIEW
        FirebaseRecyclerAdapter<mNews,mNewsViewHolder> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<mNews, mNewsViewHolder>
                        (mNews.class, R.layout.preferences_news, mNewsViewHolder.class, filtro) {
                    @Override
                    protected void populateViewHolder(mNewsViewHolder viewHolder, mNews model, int position) {
                        viewHolder.setContent(model.getContent());
                        viewHolder.setTitle(model.getTitle());
                        viewHolder.setImage(getContext().getApplicationContext(),model.getImage());
                    }

                    @Override
                    public void onBindViewHolder(mNewsViewHolder viewHolder, int position) {
                        super.onBindViewHolder(viewHolder, position);
                        viewHolder.setOnClickListeners();
                    }
                };
        rvNews.setAdapter(firebaseRecyclerAdapter);
        /*filtro.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notificacion = new NotificationCompat.Builder(getActivity());
                    notificacion.setAutoCancel(true);
                    notificacion.setSmallIcon(R.drawable.fett_iconsm);
                    notificacion.setTicker("Nueva Notificación");
                    notificacion.setWhen(System.currentTimeMillis());
                    notificacion.setContentTitle("Ejemplo");
                    notificacion.setContentText("Ejemplo");
                //Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage("com.example.henrylopez.fettapp.fragment_search");

                //PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                //notificacion.setContentIntent(pendingIntent);
                    NotificationManager nm = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
                    nm.notify(idUnica,notificacion.build());



            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        return view;

    }

    NotificationCompat.Builder notificacion;
    private static final int idUnica=777;
    public static class mNewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        View mView;
        Context context;

        public mNewsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
            context=itemView.getContext();
        }

        public void setImage(Context ctx, String image){
            ImageView ivNews=mView.findViewById(R.id.ivNews);
            Picasso.with(ctx).load(image).into(ivNews);
            ivNews.setContentDescription(image);
        }

        public void setTitle(String Title){
            TextView tvTitle=mView.findViewById(R.id.tvTitle);
            tvTitle.setText(Title);
        }

        public void setContent(String Content){
            TextView tvContent=mView.findViewById(R.id.tvContent);
            tvContent.setText(Content);
        }

        public void setOnClickListeners(){
            ImageView shareNew = mView.findViewById(R.id.btnShareNew);
            shareNew.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnShareNew:
                        onClickWhatsApp();
                    break;
            }
        }

        public void onClickWhatsApp() {
            Toast.makeText(context, "Compartir...", Toast.LENGTH_SHORT).show();
            PackageManager pm=context.getPackageManager();
            String url = (String) mView.findViewById(R.id.ivNews).getContentDescription();
            try {
                Intent waIntent = new Intent(Intent.ACTION_SEND);
                String text = "*¡Ya viste la promoción!*\n¿Vamos o que?\n\n" + url + "\n*Enviado desde FettApp*"; //+"\n\n*Enviado desde FettApp* \n\n https://fettapp.000webhostapp.com/";
                PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                waIntent.setPackage("com.whatsapp");
                waIntent.setType("text/plain");
                waIntent.putExtra(Intent.EXTRA_TEXT, text);
                context.startActivity(Intent.createChooser(waIntent, "Compartir Con"));

            } catch (PackageManager.NameNotFoundException e) {
                Toast.makeText(context, "WhatsApp no está instalado", Toast.LENGTH_SHORT)
                        .show();
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
            Toast.makeText(context, R.string.search, Toast.LENGTH_SHORT);
        }
        mContext=context;
    }
    Context mContext;
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
