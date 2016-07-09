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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.File;

public class Perfil extends AppCompatActivity implements View.OnClickListener{
    String name;
    TextView nameProfile;
    private String APP_DIRECTORY ="myPictureApp/";
    private String MEDIA_DIRECTORY=APP_DIRECTORY +"media";
    private String TEMPORAL_PICTURE_NAME="temporal.jpg";
    private String FIREBASE_URL="https://unapp-c52f0.firebaseio.com";
    Firebase firebase;
    private final int PHOTO_CODE=100;
    private final int SELECT_PICTURE=200;
    Button mapa;
    private ImageView imageView;
    private String pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        Intent intent= getIntent();
        Bundle bundle= intent.getExtras();
        name=(String) bundle.get("name");
        mapa= (Button) findViewById(R.id.maps);
        mapa.setOnClickListener(this);
        firebase.setAndroidContext(this);
        firebase=new Firebase(FIREBASE_URL);
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pass=dataSnapshot.child(name).child("password").getValue().toString();
                nameProfile.setText(pass);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        nameProfile= (TextView) findViewById(R.id.nameProfile);
        nameProfile.setText(pass);
        imageView =(ImageView) findViewById(R.id.setpicture);
        Button button= (Button) findViewById(R.id.picture);
        button.setOnClickListener(this);


    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()){
            case R.id.picture:
                Log.d("valor: ", "llega");
                final CharSequence[] options={"Take a picture","choose from gallery","Cancel"};
                final AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                builder.setTitle("Choose a option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int option) {
                        if(options[option]=="Take a picture"){
                            openCamera();
                        }
                        else if(options[option]=="choose from galery"){
                            Intent intent= new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");

                            ((Activity) v.getContext()).startActivityForResult(intent.createChooser(intent,"Choose app picture"),SELECT_PICTURE);
                        }
                        else if(options[option]=="Cancel"){
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
                break;
            case R.id.maps:
                Intent intent=new Intent(Perfil.this,MapsActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PHOTO_CODE:
                if(resultCode==RESULT_OK){
                    String dir= Environment.getExternalStorageDirectory()+ File.separator+ MEDIA_DIRECTORY+ File.separator+ TEMPORAL_PICTURE_NAME;
                    decodeBitmap(dir);
                }
                break;

            case SELECT_PICTURE:
                if(resultCode == RESULT_OK){
                    Uri path= data.getData();
                    imageView.setImageURI(path);
                }
                break;
        }
    }

    private void decodeBitmap(String dir) {
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeFile(dir);
        imageView.setImageBitmap(bitmap);
    }

    private void openCamera() {
        File file=new File(Environment.getExternalStorageDirectory(),MEDIA_DIRECTORY);
        file.mkdir();

        String path= Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY + File.separator + TEMPORAL_PICTURE_NAME;
        File newfile = new File(path);

        Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newfile));
        startActivityForResult(intent,PHOTO_CODE);
    }
}
