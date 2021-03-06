package com.example.grupo4_ta3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class PerfilUsuario extends AppCompatActivity {
    //Variables
    private TextView txt_id, txt_name, txt_email;
    private ImageView imv_photo;
    private Button btn_logout;
    //private HashMap<String, String> info_user;
   // private String photo;
    public DatabaseReference db_reference; //Variable publica de referencia a la base de datos
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        //Obtener el intent
        Intent intent = getIntent();
        HashMap<String, String> info_user = (HashMap<String, String>)intent.getSerializableExtra("info_user");

        txt_id = findViewById(R.id.txt_userId);
        txt_name = findViewById(R.id.txt_nombre);
        txt_email = findViewById(R.id.txt_correo);
        imv_photo = findViewById(R.id.imv_foto);

        txt_id.setText(info_user.get("user_id"));
        txt_name.setText(info_user.get("user_name"));
        txt_email.setText(info_user.get("user_email"));
        String photo = info_user.get("user_photo");
        Picasso.with(getApplicationContext()).load(photo).into(imv_photo);

        //iniciarVariables();
        iniciarBaseDeDatos();
        //leerTweets();
        //escribirTweets(info_user.get("user_name"));

    }

    /*public void iniciarVariables(){
        txt_id = (TextView)findViewById(R.id.txt_userId);
        txt_name = (TextView)findViewById(R.id.txt_nombre);
        txt_email = (TextView)findViewById(R.id.txt_correo);
        imv_photo = (ImageView)findViewById(R.id.imv_foto);

        txt_id.setText(info_user.get("user_id"));
        txt_name.setText(info_user.get("user_name"));
        txt_email.setText(info_user.get("user_email"));

        photo = info_user.get("user_photo");
        Picasso.with(getApplicationContext()).load(photo).into(imv_photo);
    }*/

    //Iniciar base de datos
    public void iniciarBaseDeDatos(){
        db_reference = FirebaseDatabase.getInstance().getReference().child("Grupo4");
    }

    public void irRegistros(View view){
        Intent intent =  new Intent(this, registros.class);
        startActivity(intent);
    }
    //Leer TWEETS
    public void leerTweets(){
        db_reference.child("Grupo 0").child("tweets").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    System.out.println(snapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError Error) {
                System.out.println(Error.toException());
            }
        });
    }

    //Cerrar secion
    public void cerrarSesion(View view){
        FirebaseAuth.getInstance().signOut();
        finish();
        Intent intent = new Intent(PerfilUsuario.this,MainActivity.class);
        intent.putExtra("msg","cerrarSesion");
        startActivity(intent);
    }



    public void escribirTweets(String autor){
        String tweet = "hola mundo firebase 2";
        String fecha="31/10/2019";
        Map<String,String>hola_tweet =new HashMap<String, String>();
        hola_tweet.put("autor",autor);
        hola_tweet.put("fecha",fecha);
        DatabaseReference tweets =db_reference.child("Grupo").child("tweets");
        tweets.setValue(tweet);
        tweets.child(tweet).child("autor").setValue(autor);
        tweets.child(tweet).child("fecha").setValue(fecha);

    }

    //public void registrosLab5(View view){
        //Intent intent = new Intent(this, Registros.class);
        //finish();
       // startActivity(intent);
    //}



}