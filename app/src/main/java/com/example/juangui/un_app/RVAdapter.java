package com.example.juangui.un_app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ServiceViewHolder>{

    public static Context context;
    //context = this.Context(List<Service> services, RecyclerViewActivity recyclerViewActivity);


    public static class ServiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView cv;
        TextView trayecto;
        TextView hora_salida;

        ServiceViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            trayecto = (TextView)itemView.findViewById(R.id.trayecto);
            hora_salida = (TextView)itemView.findViewById(R.id.hora_salida);
            cv.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(">>>>>>>>>>>>>>>>>", String.valueOf(getAdapterPosition()));
            Intent detalleServicio = new Intent(context, ServiceDetail.class);
            detalleServicio.putExtra("name", trayecto.getText().toString());
            context.startActivity(detalleServicio);
        }
    }

    List<Service> services;

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
        personViewHolder.trayecto.setText(services.get(i).origen + " -> " + services.get(i).destino);
        personViewHolder.hora_salida.setText(services.get(i).hora_salida);
    }

    @Override
    public int getItemCount() {
        return services.size();
    }
}
