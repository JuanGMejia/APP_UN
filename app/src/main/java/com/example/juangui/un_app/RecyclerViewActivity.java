package com.example.juangui.un_app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecyclerViewActivity extends Activity {

    private String FIREBASE_URL="https://unapp-c52f0.firebaseio.com";
    Firebase firebase;
    public static List<Service> services;
    private RecyclerView rv;
    String [] Ids;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_activity);
        firebase.setAndroidContext(this);
        firebase = new Firebase(FIREBASE_URL);

        services = new ArrayList<>();

        rv=(RecyclerView)findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);


        initializeData();

    }

    private void initializeData(){
        // services = new ArrayList<>();

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap json= (HashMap) dataSnapshot.child("Service").getValue();
                Object array[]=json.keySet().toArray();
                Ids= new String[array.length];
                int j=0;
                for(int i=0;i<Ids.length;i++){
                    String poster = array[i].toString();
                    String sexo=dataSnapshot.child("Service").child(array[i].toString()).child("sexo").getValue().toString();
                    String Hour=dataSnapshot.child("Service").child(array[i].toString()).child("Hour").getValue().toString();
                    String quotas=dataSnapshot.child("Service").child(array[i].toString()).child("quotas").getValue().toString();
                    String Finish=dataSnapshot.child("Service").child(array[i].toString()).child("Location").child("Finish").getValue().toString();
                    String Start=dataSnapshot.child("Service").child(array[i].toString()).child("Location").child("Start").getValue().toString();
                    String Placa=dataSnapshot.child("Service").child(array[i].toString()).child("Vehiculo").getValue().toString();
                    String origen;
                    String destino;
                    String capacidad = "4";
                    String Vehiculo = "Carro";

                    if(dataSnapshot.child("Minas").hasChild(Finish)){
                        destino="Minas";
                    }
                    else{
                        destino="Volador";
                    }
                    if(dataSnapshot.child("Minas").hasChild(Start)){
                        origen="Minas";
                    }
                    else{
                        origen="Volador";
                    }
                    services.add(new Service(poster, quotas,
                            capacidad, Vehiculo, sexo,
                           Hour, Start,
                           origen, Finish, destino, Placa));
                    initializeAdapter();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        //services.add(new Service("Emma Wilson", "23 years old"));
        //services.add(new Service("Lavery Maiss", "25 years old"));
        //services.add(new Service("Lillie Watts", "35 years old"));
        //services.add(new Service("Emma Wilson", "23 years old"));
        //services.add(new Service("Lavery Maiss", "25 years old"));
        //services.add(new Service("Lillie Watts", "35 years old"));
        //services.add(new Service("Emma Wilson", "23 years old"));
        //services.add(new Service("Lavery Maiss", "25 years old"));
//       services.add(new Service("1", "1",
//               "1", "1", "1",
//               "1", "1",
//               "1", "1", "1", "1"));
    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(services, RecyclerViewActivity.this);
        rv.setAdapter(adapter);
    }
}
