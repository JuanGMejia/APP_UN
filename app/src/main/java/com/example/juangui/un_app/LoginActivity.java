package com.example.juangui.un_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button botonLogin;
    TextView createAccount;
    EditText passw,username;
    String usernameR;
    String passR;
    int vehiR;
    String pass;
    String name;
    Firebase mRef;

    @Override
    protected void onStart() {
        super.onStart();
        botonLogin=(Button) findViewById(R.id.login);
        createAccount=(TextView) findViewById(R.id.Create);
        username=(EditText) findViewById(R.id.username);
        passw=(EditText) findViewById(R.id.passw);
        mRef=new Firebase("https://unapp-c52f0.firebaseio.com/condition");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object data = dataSnapshot.getValue();
                mTextFieldCondition.setText
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        })
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
                    alerta.setMessage("Falta el campo username")
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
                else if(pass.equals("")){
                    AlertDialog.Builder alerta=new AlertDialog.Builder(LoginActivity.this);
                    alerta.setMessage("Falta el campo password")
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
                    DB base= new DB(this,"Base",null,2);
                    SQLiteDatabase db= base.getWritableDatabase();
                    if(db!=null){
                        String[] args =new String[]{name,pass};
                        Cursor c=db.rawQuery("SELECT * FROM users WHERE username=? and password=?",args);

                        if(c.moveToFirst()){
                            usernameR=c.getString(0);
                            passR=c.getString(1);
                            vehiR=c.getInt(2);
                        }
                        else{
                            AlertDialog.Builder alerta=new AlertDialog.Builder(LoginActivity.this);
                            alerta.setMessage("Nombre de usuario y/o contraseña incorrectos")
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
                }


                break;

            default:
                break;
        }
    }


}
