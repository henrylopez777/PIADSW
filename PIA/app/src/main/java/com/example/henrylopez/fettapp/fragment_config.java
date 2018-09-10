package com.example.henrylopez.fettapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fragment_config.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragment_config#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_config extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View vista;
    Button btnCloseSesion;
    TextView tvNombre;
    TextView tvEmail;
    TextView tvIdUser;
    ImageView ivUser;

    GoogleApiClient googleApiClient;

    private OnFragmentInteractionListener mListener;

    public fragment_config() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_config.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_config newInstance(String param1, String param2) {
        fragment_config fragment = new fragment_config();
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

        //SESION GOOGLE
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity(),  this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
    }

    //PARA NO SOBRE CARGAR LA APP Y EVITAR QUE SE CIERRE
    @Override
    public void onStop() {
        super.onStop();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.stopAutoManage(getActivity());
            googleApiClient.disconnect();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //CARGAR VISTA PARA DETECTAR EVENTOS CLICK
        vista = inflater.inflate(R.layout.fragment_fragment_config, container, false);

        //BOTON CERRAR SESION
        btnCloseSesion=vista.findViewById(R.id.btnCloseSesion);
        btnCloseSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogOutSesionGoogle(vista);
            }
        });

        //BOTON Revocar Accesos
        btnCloseSesion=vista.findViewById(R.id.btnCloseSesion);
        btnCloseSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RevokeAccessGoogle(vista);
            }
        });

        ivUser = (ImageView)vista.findViewById(R.id.ivUser);
        tvNombre = (TextView)vista.findViewById(R.id.tvNombre);
        tvEmail=(TextView)vista.findViewById(R.id.tvCorreo);
        tvIdUser=(TextView)vista.findViewById(R.id.tvIdUsuario);

        // Inflate the layout for this fragment
        return vista;
    }

    //QUITAR ACCESOS
    private void RevokeAccessGoogle(View vista) {
        Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if(status.isSuccess()) {
                    goLogInScreen();
                } else{
                    Toast.makeText(getContext(), R.string.revokeaccess,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void LogOutSesionGoogle(View vista) {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if(status.isSuccess()) {
                    goLogInScreen();
                } else{
                    Toast.makeText(getContext(), R.string.err_close_sesion,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(opr.isDone()){
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        }else{
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    //Asignar datos a la pantalla
    private void handleSignInResult(GoogleSignInResult result) {
        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            tvNombre.setText(account.getDisplayName());
            tvEmail.setText(account.getEmail());
            tvIdUser.setText(account.getId());
            Glide.with(this).load(account.getPhotoUrl()).into(ivUser);
        }else{
            goLogInScreen();
        }
    }

    //Regresar al menu principal
    private void goLogInScreen() {
        Intent intent = new Intent(getContext(),registro_user.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
            Toast.makeText(context, R.string.config, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
