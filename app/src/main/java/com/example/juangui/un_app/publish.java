package com.example.juangui.un_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Spinner;
import com.firebase.client.Firebase;

import java.util.ArrayList;

public class publish extends AppCompatActivity {
    Spinner places;
    ArrayList placesarray;
    private String FIREBASE_URL="https://unapp-c52f0.firebaseio.com";
    Firebase firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        firebase.setAndroidContext(this);
        firebase=new Firebase(FIREBASE_URL);
        
        places = (Spinner) findViewById(R.id.spinnerplaces);
    }
}
