package com.example.juangui.un_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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
    // elemento de la interfaz que recibirá los múltiples servicios
    private RecyclerView rv;
    // lista de servicios que se actualizará cada que cambie la base de datos
    public static List<Service> services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_activity);

        firebase.setAndroidContext(this);
        firebase = new Firebase(FIREBASE_URL);

        services = new ArrayList<>();
        rv = (RecyclerView) findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        final ProgressDialog progress = ProgressDialog.show(this, "Cargando servicios",
                "Espera un momento...", true);

        new Thread(new Runnable() {
            @Override
            public void run()
            {
                // do the thing that takes a long time

                runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        initializeData(progress);

                    }
                });
            }
        }).start();


//        initializeData();
    }

    // obtiene los servicios de la base de datos
    private void initializeData(final ProgressDialog progress) {

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // cada que cambie la base de datos se limpia la lista de servicios
                services.clear();
                // hasmap de la lista de servicios
                HashMap jsonServices = (HashMap) dataSnapshot.child("Services").getValue();
                // claves del json: usuarios
                Object usuarios[] = jsonServices.keySet().toArray();
                // recorrer cada usuario en el arreglo
                int i = 0;
                while (i < usuarios.length) {
                    // obtener el string del usuario actual
                    //TODO: Mirar si el usuario tiene nombre
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

                    // con el usuario se buscan los vehiculos que posee y entre estos se busca
                    // el que tiene la misma placa que el servicio, y se obtiene su tipo y capacidad
                    for (DataSnapshot child : dataSnapshot.child("Users").child(usuarios[i].toString()).child("Vehicles").getChildren()) {
                        if(child.child("Plate").getValue().toString().equals(Placa)) {
                            vehiculo = child.getKey();
                            capacidad = child.child("Capacity").getValue().toString();
                        }
                    }

                    // según el parqueadero de llegada se obtiene el origen y el destino
                    if (dataSnapshot.child("Minas").hasChild(Finish)) {
                        destino = "Minas";
                    } else {
                        destino = "Volador";
                    }
                    // igualmente con el de salida
                    if (dataSnapshot.child("Minas").hasChild(Start)) {
                        origen = "Minas";
                    } else {
                        origen = "Volador";
                    }

                    // se agrega al hashmap un objeto service con las caracteristicas obtenidas
                    services.add(new Service(nombre, poster, quotas,
                            capacidad, vehiculo,
                            Hour, Start,
                            origen, Finish, destino, Placa));

                    initializeAdapter();
                    i++;
                }
                progress.dismiss();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    // adaptador que genera la vista
    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(services, ShowServices.this);
        rv.setAdapter(adapter);
    }
}
