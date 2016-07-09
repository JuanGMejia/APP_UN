package com.example.juangui.un_app;

import android.content.DialogInterface;
import android.content.Intent;
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
    Firebase firebase;
    int vehi;
    Button botoncreate,botoncancelar;
    String name;
    String passw;
    String passw2;
    View v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        firebase.setAndroidContext(this);
        firebase =new Firebase(FIREBASE_URL);
        user=(EditText) findViewById(R.id.user);
        pass=(EditText) findViewById(R.id.pass);
        pass2=(EditText) findViewById(R.id.pass2);
        car=(CheckBox) findViewById(R.id.car);
        moto=(CheckBox) findViewById(R.id.moto);
        botoncreate=(Button) findViewById(R.id.button2);
        botoncancelar=(Button) findViewById(R.id.cancelar);
        botoncreate.setOnClickListener(this);
        botoncancelar.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {

        switch (v.getId()){
            case R.id.cancelar:
                Intent intent=new Intent(v.getContext(),LoginActivity.class);
                startActivity(intent);



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

                                AlertDialog.Builder alerta = new AlertDialog.Builder(v.getContext());
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
                                Toast.makeText(v.getContext(), "Account created!!", Toast.LENGTH_SHORT).show();
                                int x=1;
                                while(x<10000){
                                    x++;
                                }
                                Intent intent=new Intent(v.getContext(),LoginActivity.class);
                                v.getContext().startActivity(intent);


                            }

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                }
                else{
                    AlertDialog.Builder alerta = new AlertDialog.Builder(v.getContext());
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
