package com.example.juangui.un_app;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.utilities.Base64;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

public class Perfil extends AppCompatActivity implements View.OnClickListener{
    String name;
    TextView nameProfile,detalle,inicio,fin,hora,cliente,cupo;

    private String FIREBASE_URL="https://unapp-c52f0.firebaseio.com";
    private String FIRESTORAGE_URL="gs://unapp-c52f0.appspot.com";
    Firebase firebase;
    Button service;
    private ImageView foto;
    String custom,start,finish,quota,hour;
    boolean x;
    View v;

    Bitmap bmp;
    Button button;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl(FIRESTORAGE_URL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        Intent intent= getIntent();
        Bundle bundle= intent.getExtras();
        detalle=(TextView) findViewById(R.id.textViewDetalle);
        inicio=(TextView) findViewById(R.id.textViewInicio);
        fin=(TextView) findViewById(R.id.textViewLlegada);
        hora=(TextView) findViewById(R.id.textViewHora);
        cliente=(TextView) findViewById(R.id.textViewClientes);
        cupo=(TextView) findViewById(R.id.textViewCupos);

        detalle.setVisibility(v.INVISIBLE);
        inicio.setVisibility(v.INVISIBLE);
        fin.setVisibility(v.INVISIBLE);
        hora.setVisibility(v.INVISIBLE);
        cliente.setVisibility(v.INVISIBLE);
        cupo.setVisibility(v.INVISIBLE);

        name=(String) bundle.get("name");
        service= (Button) findViewById(R.id.services);
        service.setOnClickListener(this);
        service.setText("VER MI SERVICIO");
        nameProfile= (TextView) findViewById(R.id.nameProfile);
        nameProfile.setText(name);
        foto =(ImageView) findViewById(R.id.setpicture);
        button= (Button) findViewById(R.id.picture);
        button.setOnClickListener(this);


        StorageReference PerfilRef = storageRef.child("Images/"+name+"/Perfil.jpg");
        final long ONE_MEGABYTE = 1024 * 1024;
        PerfilRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes , 0, bytes .length);
                foto.setImageBitmap(bitmap);
                button.setText("Cambiar Imagen");
            }
        });



        firebase.setAndroidContext(this);
        firebase=new Firebase(FIREBASE_URL);

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Service").hasChild(name)) {
                    x=true;
                    start = dataSnapshot.child("Service").child(name).child("Location").child("Start").getValue().toString();
                    finish = dataSnapshot.child("Service").child(name).child("Location").child("Finish").getValue().toString();
                    hour = dataSnapshot.child("Service").child(name).child("Hour").getValue().toString();
                    custom = dataSnapshot.child("Service").child(name).child("Customers").getValue().toString();
                    quota = dataSnapshot.child("Service").child(name).child("quotas").getValue().toString();

                    inicio.setText("Lugar de salida: "+start);
                    fin.setText("Lugar de llegada: "+finish);
                    hora.setText("Hora: "+hour);
                    cliente.setText("Clientes: "+custom);
                    cupo.setText("Cupos: "+quota);
                }
                else {
                    x=false;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()){
            case R.id.picture:
                final int cons=0;
                Intent i= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i,cons);

                break;
            case R.id.services:
                if(x && service.getText().toString().equals("VER MI SERVICIO")){
                    detalle.setVisibility(v.VISIBLE);
                    inicio.setVisibility(v.VISIBLE);
                    fin.setVisibility(v.VISIBLE);
                    hora.setVisibility(v.VISIBLE);
                    cliente.setVisibility(v.VISIBLE);
                    cupo.setVisibility(v.VISIBLE);
                    service.setText("DEJAR DE VER MI SERVICIO");
                }
                else if(x && service.getText().toString().equals("DEJAR DE VER MI SERVICIO")){
                    detalle.setVisibility(v.INVISIBLE);
                    inicio.setVisibility(v.INVISIBLE);
                    fin.setVisibility(v.INVISIBLE);
                    hora.setVisibility(v.INVISIBLE);
                    cliente.setVisibility(v.INVISIBLE);
                    cupo.setVisibility(v.INVISIBLE);
                    service.setText("VER MI SERVICIO");
                }
                else {
                    AlertDialog.Builder alerta = new AlertDialog.Builder(v.getContext());
                    alerta.setMessage("Usted no tiene servicios")
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

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==Activity.RESULT_OK)
        {
            Bundle ext = data.getExtras();
            bmp=(Bitmap) ext.get("data");
            final Bitmap temporal=bmp;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG,100,baos);
            byte[] datas = baos.toByteArray();

            UploadTask uploadTask = storageRef.child("Images/").child(name+"/").child("Perfil.jpg").putBytes(datas);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    foto.setImageBitmap(temporal);
                    button.setText("Cambiar Imagen");

                }
            });


        }
    }

}
