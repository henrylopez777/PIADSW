package com.example.henrylopez.fettapp;

//import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
//import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import java.io.File;


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

    //VARIABLES PARA TOMA DE FOTO
    private static final String CARPETA_PRINCIPAL="fett/";
    private static final String CARPETA_IMAGEN="imagenes";
    private static final String DIRECTORIO_IMAGEN=CARPETA_PRINCIPAL+CARPETA_IMAGEN;
    private String pathImg;
    File fileImagen;
    Bitmap bitmapPic;

    private static final int SELECTION_CODE_PHOTO = 20 ;
    private static final int SELECTION_CODE = 10 ;

    //public static final String STRING_SP_NAME = registro_user.STRING_SP_NAME;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //VARIABLES PARA DETECTAR BOTON
    View vista;
    Button btnCloseSesion;
    TextView tvNombre;
    TextView tvEmail;
    TextView tvIdUser;
    ImageView ivUser;
    Button btnChangePhoto;
    Button btnRevokeSesion;

    //INICIALIZAR API DE GOOGLE
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
        //REVISAR
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder (); StrictMode.setVmPolicy (builder.build ());

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //SESION GOOGLE
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

        if(googleApiClient == null || !googleApiClient.isConnected()) {
            googleApiClient = new GoogleApiClient.Builder(getContext())
                    .enableAutoManage(getActivity(), this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }

        //FIN DE GOOGLE
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
    public void onPause() {
        super.onPause();
            googleApiClient.stopAutoManage(getActivity());
            googleApiClient.disconnect();

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
        btnRevokeSesion=vista.findViewById(R.id.btnRemoveAccess);
        btnRevokeSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RevokeAccessGoogle(vista);
            }
        });

        ivUser = (ImageView)vista.findViewById(R.id.ivUser);
        tvNombre = (TextView)vista.findViewById(R.id.tvNombre);
        tvEmail=(TextView)vista.findViewById(R.id.tvCorreo);
        tvIdUser=(TextView)vista.findViewById(R.id.tvIdUsuario);

        btnChangePhoto = vista.findViewById(R.id.btnChangePhoto);
        btnChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogImageOptions();
            }
        });

        // Inflate the layout for this fragment
        return vista;
    }

    private void showDialogImageOptions() {
        final CharSequence[] opciones = {"Tomar Foto","Elegir de Galeria","Cancelar"};
        final AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
        builder.setTitle("Elige una opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(opciones[i].equals("Tomar Foto")){
                    abrirCamara();
                }
                if(opciones[i].equals("Elegir de Galeria")){
                    Intent intent = new Intent(Intent.ACTION_PICK, //ACTION_GET_CONTENT
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/");
                    startActivityForResult(intent.createChooser(intent,"Seleccione"),SELECTION_CODE);
                }
                if(opciones[i].equals("Cancelar")){
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    private void abrirCamara() {
        File myFile= new File(Environment.getExternalStorageDirectory(),DIRECTORIO_IMAGEN);

        boolean isCreated=myFile.exists();

        if(isCreated==false){
            isCreated=myFile.mkdirs();
        }

        if(isCreated==true){
            Long consecutivo=System.currentTimeMillis()/1000;
            String nombre=consecutivo.toString()+".jpg";
            pathImg=Environment.getExternalStorageDirectory()+File.separator+DIRECTORIO_IMAGEN
                    + File.separator+nombre; // ruta de almacenamiento

            //Uri uriSavedImage = Uri.fromFile(new File(pathImg));

            Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,Uri.fromFile(new File(pathImg)));

            try {
                startActivityForResult(intent,SELECTION_CODE_PHOTO);
            }
            catch (Exception e){
                Toast.makeText(getContext(), "Error al abrir la cámara:"+e.toString(), Toast.LENGTH_SHORT)
                        .show();
                Log.e("Cámara ", e.toString());
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case SELECTION_CODE:
                Uri miPath = data.getData();
                /*SharedPreferences sharedPref = getContext().getSharedPreferences(STRING_SP_NAME,Context.MODE_PRIVATE );
                String valor = sharedPref.getString("URI_FOTO", "");
                if(valor!= ""){
                    Glide.clear(ivUser);
                    ivUser.setImageURI(Uri.parse(valor));
                }else
                {
                    SharedPreferences.Editor mEditor = sharedPref.edit();
                    mEditor.putString("URI_FOTO",miPath.toString());
                    mEditor.commit();
                }*/
                Glide.clear(ivUser);
                Glide.with(getContext()).load(miPath).into(ivUser);
                ivUser.setImageURI(miPath);
                break;
            case SELECTION_CODE_PHOTO:
                MediaScannerConnection.scanFile(getContext(), new String[]{pathImg}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String pathImg, Uri uri) {
                                Log.i("Path",""+pathImg);
                            }
                        });
                bitmapPic=BitmapFactory.decodeFile(pathImg);
                ivUser.setImageBitmap(bitmapPic);
                //Glide.clear(ivUser);
                //Glide.with(getContext()).load(pathImg).into(ivUser);
                break;
        }


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

    //ASIGNAR DATOS A LA PANTALLA
    private void handleSignInResult(GoogleSignInResult result) {
        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            tvNombre.setText(account.getDisplayName());
            tvEmail.setText(account.getEmail());
            tvIdUser.setText(account.getId());
            /*SharedPreferences sharedPref = getContext().getSharedPreferences(STRING_SP_NAME,Context.MODE_PRIVATE );
            String id_google_user = sharedPref.getString("ID_GOOGLE_USER","No hay dato");
            String id_google_photo = sharedPref.getString("ID_GOOGLE_USER_PHOTO","No hay dato");
            tvIdUser.setText(id_google_user);*/

            //Glide.with(this).load(account.getPhotoUrl()).into(ivUser);
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
