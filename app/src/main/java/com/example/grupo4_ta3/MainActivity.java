package com.example.grupo4_ta3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private WeakReference<MainActivity> weakAct = new WeakReference<>(this);
    static final private String TAG = "hole";
    HashMap<String, String> info_user = new HashMap<String, String>();
    //Variables Publicas
    static final int GOOGLE_SIGN_IN = 123;
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestIdToken(getString(R.string.default_web_client_id)).
                requestEmail().
                build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Borrar datos de inicio de sesion
        Intent intent = getIntent();
        String msg = intent.getStringExtra("msg");
        if(msg != null){
            if(msg.equals("cerrarSesion")){
                cerrarSesion();
            }
        }
    }




    //Iniciar sesion  con google

    public void iniciarSesion(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w("TAG", "Fallo el inicio de sesión con google.", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(),
                null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        System.out.println("error");
                        updateUI(null);
                    }
                });
    }



    /*private void updateUI(FirebaseUser user) {
        if(user != null){
        String name = user.getDisplayName();
        String email= user.getEmail();
        String photo = String.valueOf(user.getPhotoUrl());
            System.out.println("nombre");
        System.out.println(name);

        } else {
            System.out.println("Sin registrarse");
        }
    }*/

    //Se crea la funcion updateUI() con el fin de pasar la informacion del usuario
    //por medio de un Intent hacia la nueva actividad
    private void updateUI(FirebaseUser user) {
        if(user != null){
            info_user.put("user_name",user.getDisplayName());
            info_user.put("user_email",user.getEmail());
            info_user.put("user_photo",String.valueOf(user.getPhotoUrl()));
            info_user.put("user_id", user.getUid());


            Intent intent = new Intent(MainActivity.this,PerfilUsuario.class);
            intent.putExtra("info_user",info_user);
            startActivity(intent);
            finish();
        }else {
            System.out.println("Sin registrarse");
        }

    }

    private void cerrarSesion(){

        mGoogleSignInClient.signOut().addOnCompleteListener(this,task -> updateUI(null));

    }






}