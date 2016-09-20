package com.example.juangui.un_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

//Adaptador que permite mostrar en la actividad múltiples elementos (recycler view)
public class ShowServicesAdapter extends RecyclerView.Adapter<ShowServicesAdapter.ServiceViewHolder> {

    public static Context context;
    static List<Service> services;

    ShowServicesAdapter(List<Service> services, Context context) {
        //Services es una lista de objetos de la clase Service
        this.services = services;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ServiceViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //v es cada 'item': Una tarjeta con datos del servicio. Este item es el que se recicla
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        ServiceViewHolder pvh = new ServiceViewHolder(v);
        return pvh;
    }

    //Se obtienen los datos de cada servicio
    @Override
    public void onBindViewHolder(ServiceViewHolder personViewHolder, int i) {
        personViewHolder.origen.setText(services.get(i).origen);
        personViewHolder.destino.setText(services.get(i).destino);
        personViewHolder.hora_salida.setText(services.get(i).hora_salida);

        if (services.get(i).quota.equals("1")) {
            String texto = services.get(i).quota + " cupo";
            personViewHolder.cupos.setText(texto);
        } else {
            String texto = services.get(i).quota + " cupos";
            personViewHolder.cupos.setText(texto);
        }

        //Se pinta el texto "Minas" de anaranjada y "Volador" de verde
        if (services.get(i).origen.equals("Minas")) {
            personViewHolder.origen.setTextColor(Color.parseColor("#F0AB4E"));
            personViewHolder.destino.setTextColor(Color.parseColor("#1C892F"));
        } else {
            personViewHolder.origen.setTextColor(Color.parseColor("#1C892F"));
            personViewHolder.destino.setTextColor(Color.parseColor("#F0AB4E"));
        }

        //Si el vehículo es una moto se cambia la imagen del servicio por una moto. El carro es por default
        if (services.get(i).vehiculo.equals("Moto")) {
            personViewHolder.imagen.setImageResource(context.getResources().getIdentifier("bike", "mipmap", context.getPackageName()));
        }
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cv;
        TextView origen;
        TextView destino;
        TextView hora_salida;
        TextView cupos;
        ImageView imagen;

        ServiceViewHolder(View itemView) {

            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);

            origen = (TextView) itemView.findViewById(R.id.origen);
            destino = (TextView) itemView.findViewById(R.id.destino);
            hora_salida = (TextView) itemView.findViewById(R.id.hora_salida);
            cupos = (TextView) itemView.findViewById(R.id.cupos);
            imagen = (ImageView) itemView.findViewById(R.id.vehicle);

            cv.setOnClickListener(this);

        }

        //Al clickear una tarjeta en la lista de servicios, se lleva a la actividad de
        //detalles de un servicio
        @Override
        public void onClick(View view) {
            Intent detalleServicio = new Intent(context, ServiceDetail.class);
            //Se obtiene cual tarjeta fue clickeada (con un índice)
            int clickeado = getAdapterPosition();
            //De la lista de servicios, se obtiene con el índice los datos del clickeado
            //y se envían a la actividad de detalleServicio
            detalleServicio.putExtra("conductor", services.get(clickeado).nombre);
            detalleServicio.putExtra("origen", services.get(clickeado).origen);
            detalleServicio.putExtra("destino", services.get(clickeado).destino);
            detalleServicio.putExtra("hora_salida", services.get(clickeado).hora_salida);
            detalleServicio.putExtra("lugar_salida", services.get(clickeado).lugar_salida);
            detalleServicio.putExtra("lugar_llegada", services.get(clickeado).lugar_llegada);
            detalleServicio.putExtra("quota", services.get(clickeado).quota);
            detalleServicio.putExtra("poster", services.get(clickeado).poster);
            detalleServicio.putExtra("placa", services.get(clickeado).placa);
            detalleServicio.putExtra("vehiculo", services.get(clickeado).vehiculo);
            detalleServicio.putExtra("capacidad", services.get(clickeado).capacidad);
            context.startActivity(detalleServicio);
        }
    }
}
