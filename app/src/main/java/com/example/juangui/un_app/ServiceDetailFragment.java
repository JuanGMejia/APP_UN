package com.example.juangui.un_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ServiceDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ServiceDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiceDetailFragment extends Fragment {

    String nombre;
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
    TextView tvguia_cupos;
    TextView tvvehiculo;
    TextView tvplaca;
    TextView tvconductor;
    ImageView ivvehiculo;

    public ServiceDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_service_detail, container, false);

        take = (Button) v.findViewById(R.id.take);
        tvOrigen = (TextView) v.findViewById(R.id.tvOrigen);
        tvDestino = (TextView) v.findViewById(R.id.tvDestino);
        tvlugar_llegada = (TextView) v.findViewById(R.id.tvlugar_llegada);
        tvlugar_salida = (TextView) v.findViewById(R.id.tvlugar_salida);
        tvhora_salida = (TextView) v.findViewById(R.id.tvhora_salida);
        tvcupos = (TextView) v.findViewById(R.id.tvcupos);
        tvguia_cupos = (TextView) v.findViewById(R.id.tvguia_cupos);
        tvvehiculo = (TextView) v.findViewById(R.id.tvvehiculo);
        tvplaca = (TextView) v.findViewById(R.id.tvplaca);
        tvconductor = (TextView) v.findViewById(R.id.tvconductor);
        ivvehiculo = (ImageView) v.findViewById(R.id.ivvehiculo);

        //Se obtienen de los extras los datos del servicio en particular
        //Intent intent=getActivity().getIntent();
        Bundle bundle= getActivity().getIntent().getExtras();

        origen=(String) bundle.get("origen");
        destino=(String) bundle.get("destino");
        hora_salida=(String) bundle.get("hora_salida");
        lugar_salida =(String) bundle.get("lugar_salida");
        lugar_llegada =(String) bundle.get("lugar_llegada");
        quota =(String) bundle.get("quota");
        nombre =(String) bundle.get("conductor");
        sexo_poster =(String) bundle.get("sexo_poster");
        placa =(String) bundle.get("placa");
        vehiculo =(String) bundle.get("vehiculo");
        capacidad =(String) bundle.get("capacidad");



        //Se pinta el texto "Minas" de anaranjada y "Volador" de verde
        if (origen.equals("Minas")) {
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
        String salida = "Sale: " + hora_salida;
        tvhora_salida.setText(salida);
        if(capacidad.equals("1")) {
            tvguia_cupos.setText("Cupo libre:");
        }
        String cupos = quota + "/" + capacidad;
        tvcupos.setText(cupos);
        tvvehiculo.setText(vehiculo);
        tvplaca.setText(placa.toUpperCase());
        tvconductor.setText(nombre);
        //Si el vehículo es una moto se cambia la imagen del servicio por una moto. El carro es por default
        if (vehiculo.equals("Moto")) {
            ivvehiculo.setImageResource(getResources().getIdentifier("bike", "mipmap", getActivity().getPackageName()));
        }
        return v;
    }


}
