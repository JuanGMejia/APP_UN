package com.example.juangui.un_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Prueba extends AppCompatActivity implements View.OnClickListener{

    ImageView publish;
    ImageView look;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);
        publish=(ImageView) findViewById(R.id.publish);
        publish.setOnClickListener(this);
        look=(ImageView) findViewById(R.id.look);
        look.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.publish:

                Log.d("------->", "Publicar");
                break;
            case R.id.look:

                Log.d("------->", "Mirar");
                break;

            default:
                break;
        }
    }
}
