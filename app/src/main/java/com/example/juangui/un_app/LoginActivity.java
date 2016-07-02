package com.example.juangui.un_app;

import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    Firebase firebase=new Firebase(FIREBASE_URL);




    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        botonLogin=(Button) findViewById(R.id.login);
        createAccount=(TextView) findViewById(R.id.Create);
        username=(EditText) findViewById(R.id.username);
        passw=(EditText) findViewById(R.id.passw);

        botonLogin.setOnClickListener(this);
        createAccount.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.Create:
                Intent intent=new Intent(this,CreateAccountActivity.class);
                startActivity(intent);
                break;

            case R.id.login:
                name=username.getText().toString();
                pass=passw.getText().toString();
                if(name.equals("")){
                    AlertDialog.Builder alerta=new AlertDialog.Builder(LoginActivity.this);
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
                    AlertDialog.Builder alerta=new AlertDialog.Builder(LoginActivity.this);
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

                                }
                                else {
                                    AlertDialog.Builder alerta=new AlertDialog.Builder(LoginActivity.this);
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
                                AlertDialog.Builder alerta=new AlertDialog.Builder(LoginActivity.this);
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
