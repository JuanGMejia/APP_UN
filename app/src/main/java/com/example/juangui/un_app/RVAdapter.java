package com.example.juangui.un_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ServiceViewHolder>{

    public static Context context;
    //context = this.Context(List<Service> services, RecyclerViewActivity recyclerViewActivity);


    public static class ServiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView cv;
        TextView origen;
        TextView destino;
        TextView hora_salida;
        TextView cupos;
        ImageView imagen;

        ServiceViewHolder(View itemView) {

            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);

            origen = (TextView)itemView.findViewById(R.id.origen);
            destino = (TextView)itemView.findViewById(R.id.destino);
            hora_salida = (TextView)itemView.findViewById(R.id.hora_salida);
            cupos = (TextView)itemView.findViewById(R.id.cupos);

            imagen = (ImageView) itemView.findViewById(R.id.vehicle);
            cv.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Intent detalleServicio = new Intent(context, ServiceDetail.class);

            int clickeado = getAdapterPosition();

            detalleServicio.putExtra("origen", services.get(clickeado).origen);
            detalleServicio.putExtra("destino", services.get(clickeado).destino);
            detalleServicio.putExtra("hora_salida", services.get(clickeado).hora_salida);
            detalleServicio.putExtra("lugar_salida", services.get(clickeado).lugar_salida);
            detalleServicio.putExtra("lugar_llegada", services.get(clickeado).lugar_llegada);
            detalleServicio.putExtra("quota", services.get(clickeado).quota);
            detalleServicio.putExtra("poster", services.get(clickeado).poster);
            detalleServicio.putExtra("sexo_poster", services.get(clickeado).sexo_poster);
            detalleServicio.putExtra("placa", services.get(clickeado).placa);
            detalleServicio.putExtra("vehiculo", services.get(clickeado).vehiculo);
            detalleServicio.putExtra("capacidad", services.get(clickeado).capacidad);

            context.startActivity(detalleServicio);

        }
    }

    static List<Service> services;

    RVAdapter(List<Service> services, Context context){
        this.services = services;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ServiceViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        ServiceViewHolder pvh = new ServiceViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ServiceViewHolder personViewHolder, int i) {
        //personViewHolder.trayecto.setText(services.get(i).origen + " -> " + services.get(i).destino);
        personViewHolder.origen.setText(services.get(i).origen);
        personViewHolder.destino.setText(services.get(i).destino);
        personViewHolder.hora_salida.setText(services.get(i).hora_salida);
        if(services.get(i).quota == "1") {
            personViewHolder.cupos.setText(services.get(i).quota + " cupo");
        } else {
            personViewHolder.cupos.setText(services.get(i).quota + " cupos");
        }

        if(services.get(i).origen.equals("Minas"))
        {
            personViewHolder.origen.setTextColor(Color.parseColor("#F0AB4E"));

            personViewHolder.destino.setTextColor(Color.parseColor("#1C892F"));
        } else {
            personViewHolder.origen.setTextColor(Color.parseColor("#1C892F"));
            personViewHolder.destino.setTextColor(Color.parseColor("#F0AB4E"));
        }
        if(services.get(i).vehiculo.equals("Moto"))
        {
            personViewHolder.imagen.setImageResource(context.getResources().getIdentifier("bike", "mipmap", context.getPackageName()));
        }

    }

    @Override
    public int getItemCount() {
        return services.size();
    }
}
