package com.example.juangui.un_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class lookservices extends AppCompatActivity implements View.OnClickListener{
    private String FIREBASE_URL = "https://unapp-c52f0.firebaseio.com";
    Firebase firebase;
    Spinner Dates;
    ArrayAdapter<String> adaptador;
    String [] Ids;
    TextView detailsstart,detailsfinish,detailsquotas,detailslicense,details;
    String user,Longit,Lat,place,place1,LatF,LongitF,placeF;
    boolean x;
    boolean y;
    Button go;
    Button salir;
    int quotas;
    String name;
    String custom="";
    String service="";
    int compare;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookservices);
        firebase.setAndroidContext(this);
        firebase=new Firebase(FIREBASE_URL);
        go=(Button) findViewById(R.id.buttonGo);
        salir=(Button) findViewById(R.id.buttonCancel);
        Intent intent=getIntent();
        Bundle bundle= intent.getExtras();
//        name=(String) bundle.get("name");
        go.setOnClickListener(this);
        salir.setOnClickListener(this);
        Dates = (Spinner) findViewById(R.id.spinnerDates);
        details=(TextView) findViewById(R.id.Details);
        detailsstart=(TextView) findViewById(R.id.DetailsStart);
        detailsstart.setOnClickListener(this);
        detailsfinish=(TextView) findViewById(R.id.DetailsFinish);
        detailsfinish.setOnClickListener(this);
        detailsquotas=(TextView) findViewById(R.id.DetailsQuotas);
        detailslicense=(TextView) findViewById(R.id.DetailsLicense);
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot){
                HashMap json= (HashMap) dataSnapshot.child("Service").getValue();
                Object array[]=json.keySet().toArray();
                Ids= new String[array.length];
                int j=0;
                for(int i=0;i<Ids.length;i++){
                    String time = dataSnapshot.child("Service").child(array[i].toString()).child("Hour").getValue().toString();
                    String times[]=time.replace(".",",").split(",");
                    time=times[0]+""+times[1];
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                    String actualTime = sdf.format(new Date());

                    try {
                        compare=(sdf.parse(actualTime).compareTo(sdf.parse(time)));
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                    if(compare<=0){

                    Ids[j]= array[i]+" "+dataSnapshot.child("Service").child(array[i].toString()).child("Hour").getValue().toString();
                        j++;
                    }
                    else{

                        firebase.child("Service").child(array[i].toString()).removeValue();
                    }
                }
                String []insert=new String[j];
                for(int i=0;i<j;i++){
                    insert[i]=Ids[i];
                }

                adaptador= new ArrayAdapter<String>(lookservices.this,android.R.layout.simple_spinner_item,insert);
                Dates.setAdapter(adaptador);
                Dates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        user=Dates.getSelectedItem().toString().split(" ")[0];
                        detailsstart.setText("Salida: "+dataSnapshot.child("Service").child(user).child("Location").child("Start").getValue().toString());
                        detailsfinish.setText("Llegada: "+dataSnapshot.child("Service").child(user).child("Location").child("Finish").getValue().toString());
                        detailslicense.setText("Placa: "+dataSnapshot.child(user).child("license").getValue().toString());
                        detailsquotas.setText("Cupos: "+dataSnapshot.child("Service").child(user).child("quotas").getValue().toString());
                        quotas=Integer.parseInt(dataSnapshot.child("Service").child(user).child("quotas").getValue().toString());
                    if(dataSnapshot.child("Service").child(user).hasChild("Customers")) {
                        custom = dataSnapshot.child("Service").child(user).child("Customers").getValue().toString();
                    }
//                    service=dataSnapshot.child(name).child("Service").getValue().toString();

                        String array1[];
                        array1=dataSnapshot.child("Service").child(user).child("Customers").getValue().toString().split(",");
                        boolean dato=false;
                        for(int i=0;i<array1.length;i++){
                            if(array1[i].trim().equals(name)) {
                                dato=true;
                                break;
                            }
                        }
                    if(dato){
                        go.setText("Cancelar servicio");
                    }
                    else{
                        go.setText("Pedir Cupo");
                    }
                    place=dataSnapshot.child("Service").child(user).child("Location").child("Start").getValue().toString();
                    HashMap json= (HashMap) dataSnapshot.child("Places").getValue();
                    place1=dataSnapshot.child("Service").child(user).child("Location").child("Start").getValue().toString();
                    placeF = dataSnapshot.child("Service").child(user).child("Location").child("Finish").getValue().toString();
                    HashMap json1= (HashMap) dataSnapshot.child("PlacesMinas").getValue();
                    boolean x=true;
                    Object [] array;
                    array=json.keySet().toArray();
                    Ids= new String[array.length];
                    for(int i=0;i<Ids.length;i++){
                    if(array[i].toString().equals(place)){
                        x=false;
                        Longit=dataSnapshot.child("Places").child(place).child("Longitude").getValue().toString();
                        Lat=dataSnapshot.child("Places").child(place).child("Latitude").getValue().toString();
                        LatF=dataSnapshot.child("PlacesMinas").child(placeF).child("Latitude").getValue().toString();
                        LongitF=dataSnapshot.child("PlacesMinas").child(placeF).child("Longitude").getValue().toString();

                    }
                    }
                    if(x) {
                        array = json1.keySet().toArray();
                        Ids = new String[array.length];
                        for (int i = 0; i < Ids.length; i++) {
                            if (array[i].toString().equals(place1)) {

                                Longit = dataSnapshot.child("PlacesMinas").child(place1).child("Longitude").getValue().toString();
                                Lat = dataSnapshot.child("PlacesMinas").child(place1).child("Latitude").getValue().toString();
                                LatF=dataSnapshot.child("Places").child(placeF).child("Latitude").getValue().toString();
                                LongitF=dataSnapshot.child("Places").child(placeF).child("Longitude").getValue().toString();
                            }
                        }
                    }


                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonGo:
                if(go.getText().toString().equals("Cancelar servicio")){
                    quotas++;
                    firebase.child("Service").child(user).child("quotas").setValue(quotas);
                    firebase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String array[];
                            array=dataSnapshot.child("Service").child(user).child("Customers").getValue().toString().split(",");
                            String dato="";
                            for(int i=0;i<array.length;i++){
                                if(!array[i].trim().equals(name)) {
                                    if(i+1==array.length){
                                        dato += array[i];
                                    }else{
                                    dato += array[i] + ",";
                                    }
                                }
                            }
                            firebase.child("Service").child(user).child("Customers").setValue(dato);



                            array=dataSnapshot.child(name).child("Service").getValue().toString().split(",");
                            dato="";

                            for(int i=0;i<array.length;i++){
                                if(!array[i].trim().equals(user)) {
                                    if(i+1==array.length){
                                        dato += array[i];
                                    }else{
                                        dato += array[i] + ",";
                                    }
                                }
                            }
                            firebase.child(name).child("Service").setValue(dato);
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });



                }
                else {
                    if (quotas == 0) {
                        AlertDialog.Builder alerta = new AlertDialog.Builder(v.getContext());
                        alerta.setMessage("Cupos completos")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = alerta.create();
                        alert.setTitle("Alert");
                        alert.show();
                    } else {
                        if (!user.equals(name)){
                            quotas--;
                        firebase.child("Service").child(user).child("quotas").setValue(quotas);
                        if (custom.equals("")) {
                            firebase.child("Service").child(user).child("Customers").setValue(name);
                        } else {
                            firebase.child("Service").child(user).child("Customers").setValue(custom + "," + name);
                        }
                        if (service.equals("")) {
                            firebase.child(name).child("Service").setValue(user);
                        } else {
                            firebase.child(name).child("Service").setValue(service + "," + user);
                        }

                        }
                        else{
                            AlertDialog.Builder alerta = new AlertDialog.Builder(v.getContext());
                            alerta.setMessage("No puede tomar su propio servicio")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alert = alerta.create();
                            alert.setTitle("Alert");
                            alert.show();
                        }
                    }
                }

                break;
            case R.id.DetailsStart:
                Intent intent=new Intent(this,MapsActivity.class);
                intent.putExtra("lat",Lat);
                intent.putExtra("longit",Longit);
                intent.putExtra("place",place);
                startActivity(intent);
                break;
            case R.id.DetailsFinish:
                Intent intent1=new Intent(this,MapsActivity.class);
                intent1.putExtra("lat",LatF);
                intent1.putExtra("longit",LongitF);
                intent1.putExtra("place",placeF);
                startActivity(intent1);
                break;
            case R.id.buttonCancel:
                finish();
                break;
        }
    }
}
