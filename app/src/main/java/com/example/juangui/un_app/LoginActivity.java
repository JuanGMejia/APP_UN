package com.example.juangui.un_app;

import android.*;
import android.Manifest;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button botonLogin;

    EditText passw, username;
    String user,pass,guardado;

    private String FIREBASE_URL = "https://unapp-c52f0.firebaseio.com";
    Firebase firebase;

    private FirebaseAuth mAuth;
    ProgressDialog loading;
    FirebaseUser userF;
    Switch recordarUsuario;


    //This is our root url
    public static final String ROOT_URL = "http://sia.unalmed.edu.co/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebase.setAndroidContext(this);
        firebase = new Firebase(FIREBASE_URL);
        mAuth = FirebaseAuth.getInstance();
        botonLogin = (Button) findViewById(R.id.login);
        username = (EditText) findViewById(R.id.username);
        passw = (EditText) findViewById(R.id.passw);
        botonLogin.setOnClickListener(this);
        recordarUsuario=(Switch)findViewById(R.id.recordar);
        CargarPreferencias();
    }

    private void postSia(final String usernamesia, final String password) {

        //Here we will handle the http request to insert user to mysql db
        //Creating a RestAdapter
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL) //Setting the Root URL
                .build(); //Finally building the adapter

        //Creating object for our interface
        RegisterAPI api = adapter.create(RegisterAPI.class);

        //Defining the method insertuser of our interface
        api.logear(

                //Passing the values by getting it from editTexts
                usernamesia,
                password,

                //Creating an anonymous callback
                new Callback<Response>() {
                    @Override
                    public void success(Response result, Response response) {
                        //On success we will read the server's output using bufferedreader
                        //Creating a bufferedreader object
                        loading.getProgress();
                        BufferedReader reader = null;
                        //An string to store output from the server
                        String output = "";

                        //Initializing buffered reader
                        // reader = new BufferedReader(new InputStreamReader(result.getStatus()));

                        //Reading the output in the string
                        //output = reader.readLine();
                        String cadena = new String(((TypedByteArray) response.getBody()).getBytes());
                        String word = "Visitante";
                        if (cadena.contains(word)) {
                            loading.dismiss();
                            Toast.makeText(LoginActivity.this, "Usuario y/o contraseña incorrectos", Toast.LENGTH_LONG).show();

                        } else {
                            //Acá se ponen las acciones al logearse

                            loading.getProgress();
                            mAuth.createUserWithEmailAndPassword(usernamesia + "@unal.edu.co", password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    mAuth.signInWithEmailAndPassword(usernamesia+ "@unal.edu.co", password)
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            loading.dismiss();

                                            GuardarPreferencias();
                                            firebase.child("Users").child(usernamesia).child("Vehicles").child("Carro").child("Placa").setValue("");
                                            firebase.child("Users").child(usernamesia).child("Vehicles").child("Carro").child("Capacidad").setValue("");
                                            firebase.child("Users").child(usernamesia).child("Vehicles").child("Moto").child("Placa").setValue("");
                                            firebase.child("Users").child(usernamesia).child("Vehicles").child("Moto").child("Capacidad").setValue("");
                                            firebase.child("Users").child(usernamesia).child("Nombre").setValue("");
                                            firebase.child("Users").child(usernamesia).child("Sexo").setValue("");
                                            Toast.makeText(LoginActivity.this, "Tirar al tutorial", Toast.LENGTH_LONG).show();
                                        }
                                    });

                                }
                            });

                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        //If any error occured displaying the error as toast
                        Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void CargarPreferencias(){
        SharedPreferences mispreferencias=getSharedPreferences("Preferencias usuario", Context.MODE_PRIVATE);
        if(mispreferencias.getBoolean("switch",false)){
            recordarUsuario.setChecked(true);
        }else {
            recordarUsuario.setChecked(false);
        }
        username.setText(mispreferencias.getString("nombre",""));
    }

    public void GuardarPreferencias(){
        SharedPreferences mispreferencias=getSharedPreferences("Preferencias usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=mispreferencias.edit();
        boolean valorswitch=recordarUsuario.isChecked();
        editor.putBoolean("switch",valorswitch);
        if (valorswitch){
            editor.putString("nombre",username.getText().toString());
        }
        else{
            editor.putString("nombre","");
        }
        editor.commit();

    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()){
            case R.id.login:
                user=username.getText().toString().trim();
                pass=passw.getText().toString().trim();
                if(!user.equals("") && !pass.equals("")) {

                    mAuth.signInWithEmailAndPassword(user + "@unal.edu.co", pass)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            GuardarPreferencias();
                            Toast.makeText(LoginActivity.this, "Existe!", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loading = ProgressDialog.show(LoginActivity.this, "Cargando", "Espera ...");
                            postSia(user, pass);
                        }
                    });
                }
                else if(user.equals("") && pass.equals("")){
                    Toast.makeText(LoginActivity.this, "¡Campos requeridos!", Toast.LENGTH_LONG).show();
                }
                else if(pass.equals("")){
                    Toast.makeText(LoginActivity.this, "¡Contraseña requerida!", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(LoginActivity.this, "¡Usuario requerido!", Toast.LENGTH_LONG).show();
                }
                break;



        }


    }
}
