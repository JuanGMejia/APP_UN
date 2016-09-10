package com.example.juangui.un_app;

import android.*;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    Button botonLogin;
    //TextView createAccount;
    EditText passw,username;
    String pass;
    String name;
    String LicenseV;
    private String FIREBASE_URL="https://unapp-c52f0.firebaseio.com";
    Firebase firebase;
    boolean logeado = false;

    //This is our root url
    public static final String ROOT_URL = "http://sia.unalmed.edu.co/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        firebase.setAndroidContext(this);
        firebase=new Firebase(FIREBASE_URL);
        //createAccount=(TextView) findViewById(R.id.Create);
        botonLogin = (Button) findViewById(R.id.login);
        username=(EditText) findViewById(R.id.username);
        passw=(EditText) findViewById(R.id.passw);
        botonLogin.setOnClickListener(this);
        //createAccount.setOnClickListener(this);
    }

    private void logearUsuario(){
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
                username.getText().toString(),
                passw.getText().toString(),

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
                        Log.d("---->", cadena);

                        String word = "Visitante";
                        if(cadena.contains(word)){
                            //Toast.makeText(LoginActivity.this, "Usuario y/o contraseña incorrectos", Toast.LENGTH_LONG).show();
                            logeado = false;
                        } else {
                            //Acá se ponen las acciones al logearse
                            //Toast.makeText(LoginActivity.this, "Logeado!", Toast.LENGTH_LONG).show();
                            logeado = true;
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        //If any error occured displaying the error as toast
                        Toast.makeText(LoginActivity.this, error.toString(),Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    @Override
    public void onClick(final View v) {
        logearUsuario();
        if(logeado)
        {
            Toast.makeText(LoginActivity.this, "Logeado", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(LoginActivity.this, "Usuario y/o contraseña incorrectos", Toast.LENGTH_LONG).show();
        }

        // acá está el logeo de firebase original
        /*
        switch (v.getId()){
            case R.id.Create:
                Intent intent=new Intent(v.getContext(),CreateAccountActivity.class);
                startActivity(intent);

                break;

            case R.id.login:
                name=username.getText().toString();

                pass=passw.getText().toString();
                if(name.equals("") || pass.equals("")){
                    AlertDialog.Builder alerta=new AlertDialog.Builder(v.getContext());
                    alerta.setMessage("Los campos son requeridos")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert=alerta.create();
                    alert.setTitle("Alerta");
                    alert.show();
                }
                else{
                    firebase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if(snapshot.hasChild(name)) {
                                if (snapshot.child(name).child("password").getValue().toString().equals(pass)) {
                                    Intent intent=new Intent(v.getContext(),Principal.class);
                                    LicenseV=snapshot.child(name).child("license").getValue().toString();
                                    intent.putExtra("name",name);
                                    intent.putExtra("License",LicenseV);
                                    v.getContext().startActivity(intent);
                                }
                                else {
                                    AlertDialog.Builder alerta=new AlertDialog.Builder(v.getContext());
                                    alerta.setMessage("Usuario y/o contraseña incorrectas")
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            });
                                    AlertDialog alert=alerta.create();
                                    alert.setTitle("Alerta");
                                    alert.show();
                                }
                            }
                            else{
                                AlertDialog.Builder alerta=new AlertDialog.Builder(v.getContext());
                                alerta.setMessage("Usuario y/o contraseña incorrectas")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alert=alerta.create();
                                alert.setTitle("Alerta");
                                alert.show();
                            }
                            }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }



                break;


            default:
                break;
        } */
    }


}
