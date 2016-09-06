package com.example.juangui.un_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class Principal extends AppCompatActivity implements View.OnClickListener{

    String name;
    String License;
    Button publish;
    Button look;
    private String FIREBASE_URL="https://unapp-c52f0.firebaseio.com";
    Firebase firebase;
    int persona;
    boolean ver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_principal);
        firebase.setAndroidContext(this);
        firebase=new Firebase(FIREBASE_URL);
        Intent intent=getIntent();
        Bundle bundle= intent.getExtras();
        name=(String) bundle.get("name");
        License=(String) bundle.get("License");
        publish=(Button) findViewById(R.id.publishText);
        publish.setOnClickListener(this);
        look=(Button) findViewById(R.id.lookText);
        look.setOnClickListener(this);
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                persona=Integer.parseInt(dataSnapshot.child(name).child("vehicle").getValue().toString());
                ver=dataSnapshot.hasChild("Service");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.perfil:
                Intent intent=new Intent(this,Perfil.class);
                intent.putExtra("name",name);
                startActivity(intent);
                return true;

            case R.id.logout:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.publishText:
                if(persona!=0) {
                    Intent intent = new Intent(this, publish.class);
                    intent.putExtra("name", name);
                    intent.putExtra("License", License);
                    startActivity(intent);
                }
                else {
                    AlertDialog.Builder alerta = new AlertDialog.Builder(v.getContext());
                    alerta.setMessage("Usted no tiene vehiculo, por lo tanto no puede publicar un servicio")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = alerta.create();
                    alert.setTitle("Alerta");
                    alert.show();
                }
                break;
            case  R.id.lookText:
                if(ver) {
                    Intent intentlook = new Intent(this, lookservices.class);
                    intentlook.putExtra("name", name);
                    startActivity(intentlook);
                }
                else{
                    AlertDialog.Builder alerta = new AlertDialog.Builder(v.getContext());
                    alerta.setMessage("No hay servicios para mostrar")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = alerta.create();
                    alert.setTitle("Alerta");
                    alert.show();
                }
            default:
                break;
        }
    }
}
