package com.example.juangui.un_app;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener {

    EditText user,pass,pass2;
    CheckBox car,moto;
    private String FIREBASE_URL="https://unapp-c52f0.firebaseio.com";
    Firebase firebase=new Firebase(FIREBASE_URL);
    int vehi;
    Button botoncreate;
    String name;
    String passw;
    String passw2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        user=(EditText) findViewById(R.id.user);
        pass=(EditText) findViewById(R.id.pass);
        pass2=(EditText) findViewById(R.id.pass2);
        car=(CheckBox) findViewById(R.id.car);
        moto=(CheckBox) findViewById(R.id.moto);
        Firebase.setAndroidContext(this);
        botoncreate=(Button) findViewById(R.id.button2);
        botoncreate.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.button2:
                name=user.getText().toString();
                passw=pass.getText().toString();
                passw2=pass2.getText().toString();
                if(passw.equals(passw2)) {
                    vehi = 0;
                    if (car.isChecked()) {
                        vehi++;
                    }
                    if (moto.isChecked()) {
                        vehi += 2;
                    }

                    firebase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.hasChild(name)) {
                                AlertDialog.Builder alerta = new AlertDialog.Builder(CreateAccountActivity.this);
                                alerta.setMessage("user already exists")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alert = alerta.create();
                                alert.setTitle("Alert");
                                alert.show();
                            }
                            else {
                                firebase = new Firebase(FIREBASE_URL).child(name);
                                firebase.child("password").setValue(passw);
                                firebase.child("vehicle").setValue(vehi);
                                Toast.makeText(CreateAccountActivity.this, "Account created!!", Toast.LENGTH_SHORT).show();
                                int x=1;
                                while(x<10000){
                                    x++;
                                }
                                Intent intent=new Intent(CreateAccountActivity.this,LoginActivity.class);
                                startActivity(intent);

                            }

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                }
                else{
                    AlertDialog.Builder alerta = new AlertDialog.Builder(CreateAccountActivity.this);
                    alerta.setMessage("password does not match")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = alerta.create();
                    alert.setTitle("Alert");
                    alert.show();
                }

                break;


            default:
                break;
        }
    }




}
