package com.example.juangui.un_app;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class ServiceDetail extends AppCompatActivity {

    String poster;
    String sexo_poster;
    String hora_salida;
    String lugar_salida;
    String origen;
    String lugar_llegada;
    String destino;
    String vehiculo;
    String placa;
    String capacidad;
    String quota;

    Button take;
    TextView tvOrigen;
    TextView tvDestino;
    TextView tvlugar_llegada;
    TextView tvlugar_salida;
    TextView tvhora_salida;
    TextView tvcupos;
    TextView tvvehiculo;
    TextView tvplaca;
    TextView tvconductor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);

        take = (Button) findViewById(R.id.take);
        tvOrigen = (TextView) findViewById(R.id.tvOrigen);
        tvDestino = (TextView) findViewById(R.id.tvDestino);
        tvlugar_llegada = (TextView) findViewById(R.id.tvlugar_llegada);
        tvlugar_salida = (TextView) findViewById(R.id.tvlugar_salida);
        tvhora_salida = (TextView) findViewById(R.id.tvhora_salida);
        tvcupos = (TextView) findViewById(R.id.tvcupos);
        tvvehiculo = (TextView) findViewById(R.id.tvvehiculo);
        tvplaca = (TextView) findViewById(R.id.tvplaca);
        tvconductor = (TextView) findViewById(R.id.tvconductor);

        Intent intent=getIntent();
        Bundle bundle= intent.getExtras();

        origen=(String) bundle.get("origen");
        destino=(String) bundle.get("destino");
        hora_salida=(String) bundle.get("hora_salida");
        lugar_salida =(String) bundle.get("lugar_salida");
        lugar_llegada =(String) bundle.get("lugar_llegada");
        quota =(String) bundle.get("quota");
        poster =(String) bundle.get("poster");
        sexo_poster =(String) bundle.get("sexo_poster");
        placa =(String) bundle.get("placa");
        vehiculo =(String) bundle.get("vehiculo");
        capacidad =(String) bundle.get("capacidad");

        if(origen.equals("Minas"))
        {
            tvOrigen.setTextColor(Color.parseColor("#F0AB4E"));
            tvDestino.setTextColor(Color.parseColor("#1C892F"));
        } else {
            tvOrigen.setTextColor(Color.parseColor("#1C892F"));
            tvDestino.setTextColor(Color.parseColor("#F0AB4E"));
        }

        tvOrigen.setText(origen);
        tvDestino.setText(destino);
        tvlugar_llegada.setText(lugar_llegada);
        tvlugar_salida.setText(lugar_salida);
        tvhora_salida.setText("Salida: " + hora_salida);
        tvcupos.setText(quota + "/" + capacidad);
        tvvehiculo.setText(vehiculo);
        tvplaca.setText(placa);
        tvconductor.setText(poster);


    }
}
