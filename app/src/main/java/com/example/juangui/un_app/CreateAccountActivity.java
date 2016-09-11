package com.example.juangui.un_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.support.v7.app.ActionBarDrawerToggle;
public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener {

    EditText pass,pass2;
    private String FIREBASE_URL="https://unapp-c52f0.firebaseio.com";
    Firebase firebase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    Button botoncreate,botoncancelar;
    private FirebaseAuth mAuth;
    ProgressDialog loading;

    String name,passw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        firebase.setAndroidContext(this);
        firebase =new Firebase(FIREBASE_URL);
        mAuth = FirebaseAuth.getInstance();
        Intent intent=getIntent();
        Bundle bundle= intent.getExtras();
        name=(String) bundle.get("name");
        Toolbar mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("Cambiar contraseña");
        //mToolbar.setSubtitle("Sub");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        pass=(EditText) findViewById(R.id.pass);
        pass2=(EditText) findViewById(R.id.pass2);
        botoncreate=(Button) findViewById(R.id.crear);
        botoncancelar=(Button) findViewById(R.id.cancelar);
        botoncreate.setOnClickListener(this);
        botoncancelar.setOnClickListener(this);

    }



    @Override
    public void onClick(final View v) {
        switch (v.getId()){
            case R.id.cancelar:
                Intent intent=new Intent(CreateAccountActivity.this,LoginActivity.class);
                startActivity(intent);
                break;

            case R.id.crear:
                if(!pass.getText().toString().equals("") && !pass2.getText().toString().equals("")) {
                    if(pass.getText().toString().equals(pass2.getText().toString())) {
                        if(pass.getText().toString().length()>=6 && pass.getText().toString().length()<=10) {
                            loading = ProgressDialog.show(CreateAccountActivity.this, "Cargando", "Espera ...");
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.updatePassword(pass.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    loading.dismiss();
                                    Toast.makeText(CreateAccountActivity.this, "¡Contraseña actualizada!", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        else{
                            Toast.makeText(CreateAccountActivity.this, "¡Contraseña entre 6 y 10 caracteres!", Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(CreateAccountActivity.this, "¡Contraseñas diferentes!", Toast.LENGTH_LONG).show();
                    }
                }
                else if(pass.getText().toString().equals("") && pass2.getText().toString().equals("")){
                    Toast.makeText(CreateAccountActivity.this, "¡Campos requeridos!", Toast.LENGTH_LONG).show();
                }
                else if(pass.getText().toString().equals("")){
                    Toast.makeText(CreateAccountActivity.this, "¡Contraseña requerida!", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(CreateAccountActivity.this, "¡Repetir contraseña requerida!", Toast.LENGTH_LONG).show();
                }
                break;


            default:
                break;
        }
    }




}
