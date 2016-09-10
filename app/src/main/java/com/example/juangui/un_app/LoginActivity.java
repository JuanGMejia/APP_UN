package com.example.juangui.un_app;

import android.*;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button botonLogin;

    EditText passw, username;
    String user,pass,guardado;

    private String FIREBASE_URL = "https://unapp-c52f0.firebaseio.com";
    Firebase firebase;
    FirebaseAuth mAuthListener;


    //This is our root url
    public static final String ROOT_URL = "http://sia.unalmed.edu.co/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebase.setAndroidContext(this);
        firebase = new Firebase(FIREBASE_URL);



        botonLogin = (Button) findViewById(R.id.login);
        username = (EditText) findViewById(R.id.username);
        passw = (EditText) findViewById(R.id.passw);
        botonLogin.setOnClickListener(this);


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
                            //Toast.makeText(LoginActivity.this, "Usuario y/o contraseña incorrectos", Toast.LENGTH_LONG).show();


                        } else {
                            //Acá se ponen las acciones al logearse
                            //Toast.makeText(LoginActivity.this, "Logeado!", Toast.LENGTH_LONG).show();
                            firebase.createUser(usernamesia, password, new Firebase.ResultHandler() {
                                @Override
                                public void onSuccess() {
                                    // user was created
                                    Toast.makeText(getApplicationContext(), "Pass",
                                            Toast.LENGTH_LONG).show();
                                }
                                @Override
                                public void onError(FirebaseError firebaseError) {
                                    // there was an error]
                                    Toast.makeText(getApplicationContext(), "Fail",
                                            Toast.LENGTH_LONG).show();
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

    @Override
    public void onClick(final View v) {
        switch (v.getId()){
            case R.id.login:
                user=username.getText().toString();
                pass=passw.getText().toString();
                firebase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(user)){
                            guardado=dataSnapshot.child(user).child("password").getValue().toString();
                            if(guardado.equals(pass)){

                            }
                        }
                        else{
                            postSia(user,pass);
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

                break;



        }


    }
}
