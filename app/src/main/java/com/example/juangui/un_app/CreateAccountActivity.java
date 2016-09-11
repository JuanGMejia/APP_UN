package com.example.juangui.un_app;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.support.v7.app.ActionBarDrawerToggle;
public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener {

    EditText pass,pass2,license;
    private String FIREBASE_URL="https://unapp-c52f0.firebaseio.com";
    Firebase firebase;
    Button botoncreate,botoncancelar;
    View v;
    String texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        firebase.setAndroidContext(this);
        firebase =new Firebase(FIREBASE_URL);

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
                Intent intent=new Intent(v.getContext(),LoginActivity.class);
                startActivity(intent);
                break;

            case R.id.crear:
                break;


            default:
                break;
        }
    }




}
