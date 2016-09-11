package com.example.juangui.un_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class ServiceDetail extends AppCompatActivity {

        String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);

        Intent intent=getIntent();
        Bundle bundle= intent.getExtras();
        name=(String) bundle.get("name");

        Log.d("<<<<<<<<<<<<<<<<<<", name);
    }
}
