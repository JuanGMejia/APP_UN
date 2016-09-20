package com.example.juangui.un_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowServices extends Activity {

    Firebase firebase;
    private String FIREBASE_URL = "https://unapp-c52f0.firebaseio.com";
    //Elemento de la interfaz que recibirá los múltiples servicios
    private RecyclerView rv;
    //Lista de servicios que se actualizará cada que cambie la base de datos
    public static List<Service> services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_services_activity);

        firebase.setAndroidContext(this);
        firebase = new Firebase(FIREBASE_URL);

        services = new ArrayList<>();
        rv = (RecyclerView) findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        //Diágolo de espera mientras se cargan los servicios
        final ProgressDialog progress = ProgressDialog.show(this, "Cargando servicios",
                "Espera un momento...", true);

        //La acción de cargar servicios se hace en un nuevo hilo
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        //Se le pasa el diálogo de espera para cancelarlo cuando termine
                        initializeData(progress);
                    }
                });
            }
        }).start();
    }

    //Obtiene los servicios de la base de datos
    private void initializeData(final ProgressDialog progress) {

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Cada que cambie la base de datos se limpia la lista de servicios
                services.clear();
                //Hashmap de la lista de servicios
                HashMap jsonServices = (HashMap) dataSnapshot.child("Services").getValue();
                //Claves del json: usuarios
                Object usuarios[] = jsonServices.keySet().toArray();
                //Recorrer cada usuario en el arreglo
                int i = 0;
                while (i < usuarios.length) {
                    //Obtener el string del username actual
                    String poster = usuarios[i].toString();
                    String nombre = dataSnapshot.child("Users").child(usuarios[i].toString()).child("Name").getValue().toString();
                    DataSnapshot dsUsuario = dataSnapshot.child("Services").child(usuarios[i].toString());
                    String Hour = dsUsuario.child("Hour").getValue().toString();
                    String quotas = dsUsuario.child("Quotas").getValue().toString();
                    String Finish = dsUsuario.child("Location").child("Finish").getValue().toString();
                    String Start = dsUsuario.child("Location").child("Start").getValue().toString();
                    String Placa = dsUsuario.child("Vehicle").getValue().toString();
                    String capacidad = "";
                    String vehiculo = "";
                    String origen;
                    String destino;

                    //Con el usuario se buscan los vehiculos que posee y entre estos se busca
                    //el que tiene la misma placa que el servicio, y se obtiene su tipo y capacidad
                    for (DataSnapshot child : dataSnapshot.child("Users").child(usuarios[i].toString()).child("Vehicles").getChildren()) {
                        if(child.child("Plate").getValue().toString().equals(Placa)) {
                            vehiculo = child.getKey();
                            capacidad = child.child("Capacity").getValue().toString();
                        }
                    }

                    //Según el parqueadero de llegada se obtiene el origen y el destino
                    if (dataSnapshot.child("Minas").hasChild(Finish)) {
                        destino = "Minas";
                    } else {
                        destino = "Volador";
                    }
                    //Igualmente con el de salida
                    if (dataSnapshot.child("Minas").hasChild(Start)) {
                        origen = "Minas";
                    } else {
                        origen = "Volador";
                    }

                    //Se agrega al hashmap un objeto service con las caracteristicas obtenidas
                    services.add(new Service(nombre, poster, quotas,
                            capacidad, vehiculo,
                            Hour, Start,
                            origen, Finish, destino, Placa));

                    initializeAdapter();
                    i++;
                }
                //Cerrar el diálogo de espera
                progress.dismiss();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    //Adaptador que genera la vista
    private void initializeAdapter() {
        ShowServicesAdapter adapter = new ShowServicesAdapter(services, ShowServices.this);
        rv.setAdapter(adapter);
    }
}
