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

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    Button botonLogin;
    TextView createAccount;
    EditText passw,username;
    String pass;
    String name;
    private String FIREBASE_URL="https://unapp-c52f0.firebaseio.com";
    Firebase firebase;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d("valor: ", String.valueOf(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)));

        firebase.setAndroidContext(this);
        firebase=new Firebase(FIREBASE_URL);
        createAccount=(TextView) findViewById(R.id.Create);
        botonLogin = (Button) findViewById(R.id.login);
        username=(EditText) findViewById(R.id.username);
        passw=(EditText) findViewById(R.id.passw);
        botonLogin.setOnClickListener(this);
        createAccount.setOnClickListener(this);
    }


    @Override
    public void onClick(final View v) {

        switch (v.getId()){
            case R.id.Create:
                Intent intent=new Intent(v.getContext(),CreateAccountActivity.class);
                v.getContext().startActivity(intent);

                break;

            case R.id.login:
                name=username.getText().toString();
                pass=passw.getText().toString();
                if(name.equals("")){
                    AlertDialog.Builder alerta=new AlertDialog.Builder(v.getContext());
                    alerta.setMessage("username is required")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert=alerta.create();
                    alert.setTitle("Alert");
                    alert.show();
                }
                else if(pass.equals("")){
                    AlertDialog.Builder alerta=new AlertDialog.Builder(v.getContext());
                    alerta.setMessage("password is required")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert=alerta.create();
                    alert.setTitle("Alert");
                    alert.show();
                }
                else{
                    firebase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if(snapshot.hasChild(name)) {
                                if (snapshot.child(name).child("password").getValue().toString().equals(pass)) {
                                    Intent intent=new Intent(v.getContext(),Principal.class);
                                    intent.putExtra("name",name);
                                    v.getContext().startActivity(intent);
                                }
                                else {
                                    AlertDialog.Builder alerta=new AlertDialog.Builder(v.getContext());
                                    alerta.setMessage("Incorrect password")
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            });
                                    AlertDialog alert=alerta.create();
                                    alert.setTitle("Alert");
                                    alert.show();
                                }
                            }
                            else{
                                AlertDialog.Builder alerta=new AlertDialog.Builder(v.getContext());
                                alerta.setMessage("User not exist in database")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alert=alerta.create();
                                alert.setTitle("Alert");
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
        }
    }


}
