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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextClock;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;


public class publish extends AppCompatActivity implements View.OnClickListener{
    Spinner places;
    Spinner places1;
    Spinner quotascar;

    String PlacesSpinner1,PlacesSpinner2,QuotasSpinner,quota;
    EditText Hour,minutes;
    private String FIREBASE_URL="https://unapp-c52f0.firebaseio.com";
    Firebase firebase;
    Button cancelpublish;
    Button publishService;
    String name;
    String License;
    String valorhora;
    String valorminute;
    RadioButton minas;
    String Sistema="";
    RadioButton volador,am,pm;
    RadioGroup radioGroup,radioGroupSistem;
    ArrayAdapter<String> adaptador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        Intent intent=getIntent();
        Bundle bundle= intent.getExtras();
        name=(String) bundle.get("name");
        License=(String) bundle.get("License");
        volador=(RadioButton) findViewById(R.id.volador);
        minas=(RadioButton) findViewById(R.id.minas);
        am=(RadioButton) findViewById(R.id.am);
        pm=(RadioButton) findViewById(R.id.pm);
        cancelpublish=(Button) findViewById(R.id.buttoncancelpublish);
        cancelpublish.setOnClickListener(this);
        publishService=(Button) findViewById(R.id.buttonpublish);
        publishService.setOnClickListener(this);
        radioGroup=(RadioGroup) findViewById(R.id.radioGroupopciones);
        radioGroupSistem=(RadioGroup) findViewById(R.id.radioGrouposistema);
        Hour=(EditText) findViewById(R.id.Hour);
        minutes=(EditText) findViewById(R.id.minutes);
        firebase.setAndroidContext(this);
        firebase=new Firebase(FIREBASE_URL);
        
        places = (Spinner) findViewById(R.id.spinnerplaces);
        places1=(Spinner) findViewById(R.id.spinnerplaces1);
        quotascar=(Spinner) findViewById(R.id.spinnerquotas);




        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                QuotasSpinner="1";
                String quota=dataSnapshot.child(name).child("vehicle").getValue().toString();
                if(quota.equals("1")){
                    String[] quotas={"1","2","3","4"};
                    adaptador= new ArrayAdapter<String>(publish.this,android.R.layout.simple_spinner_item,quotas);
                    quotascar.setAdapter(adaptador);
                }
                else if(quota.equals("2")){
                    String[] quotas={"1"};
                    adaptador= new ArrayAdapter<String>(publish.this,android.R.layout.simple_spinner_item,quotas);
                    quotascar.setAdapter(adaptador);
                }




            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        radioGroupSistem.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.am){
                    Sistema="am";
                }
                else{
                    Sistema="pm";
                }
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.volador){
                    Selecciondatos("volador");
                }
                else if(checkedId==R.id.minas){
                    Selecciondatos("minas");
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.buttonpublish:

                valorhora=Hour.getText().toString();
                valorminute=minutes.getText().toString();

                if(PlacesSpinner1!=null && PlacesSpinner2!=null && QuotasSpinner!=null && !valorhora.equals("") && !valorminute.equals("") && !Sistema.equals("")){
                    firebase.child("Service").child(name).child("Location").child("Start").setValue(PlacesSpinner1);
                    firebase.child("Service").child(name).child("Location").child("Finish").setValue(PlacesSpinner2);
                    firebase.child("Service").child(name).child("quotas").setValue(QuotasSpinner);
                    firebase.child("Service").child(name).child("Hour").setValue(valorhora+":"+valorminute+". "+Sistema);
                    firebase.child("Service").child(name).child("Customers").setValue("");
                }
               else{
                    AlertDialog.Builder alerta=new AlertDialog.Builder(v.getContext());
                    alerta.setMessage("Fill missing data")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert=alerta.create();
                    alert.setTitle("Alert");
                    alert.show();
                }

                break;
            case R.id.buttoncancelpublish:
                 finish();
                break;

        }

    }

    public void Selecciondatos(final String param){
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                String [] Ids;
                Object [] array;
                if(param.equals("volador")) {
                    HashMap json= (HashMap) dataSnapshot.child("Places").getValue();
                    array=json.keySet().toArray();
                    Ids= new String[array.length];
                    for(int i=0;i<Ids.length;i++){
                        Ids[i]= array[i].toString();
                    }

                    adaptador= new ArrayAdapter<String>(publish.this,android.R.layout.simple_spinner_item,Ids);
                    places.setAdapter(adaptador);

                    HashMap json1= (HashMap) dataSnapshot.child("PlacesMinas").getValue();
                    array=json1.keySet().toArray();
                    Ids= new String[array.length];
                    for(int i=0;i<Ids.length;i++){
                        Ids[i]= array[i].toString();
                    }

                    adaptador= new ArrayAdapter<String>(publish.this,android.R.layout.simple_spinner_item,Ids);
                    places1.setAdapter(adaptador);

                    PlacesSpinner1="Parqueadero Bloque 12";
                    PlacesSpinner2="Parqueadero M3";
                }
                else {

                    HashMap json1= (HashMap) dataSnapshot.child("PlacesMinas").getValue();
                    array=json1.keySet().toArray();
                    Ids= new String[array.length];
                    for(int i=0;i<Ids.length;i++){
                        Ids[i]= array[i].toString();
                    }

                    adaptador= new ArrayAdapter<String>(publish.this,android.R.layout.simple_spinner_item,Ids);
                    places.setAdapter(adaptador);


                    HashMap json= (HashMap) dataSnapshot.child("Places").getValue();
                    array=json.keySet().toArray();
                    Ids= new String[array.length];
                    for(int i=0;i<Ids.length;i++){
                        Ids[i]= array[i].toString();
                    }

                    adaptador= new ArrayAdapter<String>(publish.this,android.R.layout.simple_spinner_item,Ids);
                    places1.setAdapter(adaptador);

                    PlacesSpinner2="Parqueadero Bloque 12";
                    PlacesSpinner1="Parqueadero M3";
                }

                places.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                        PlacesSpinner1=(String.valueOf(places.getSelectedItem())).trim();


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                places1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        PlacesSpinner2=(String.valueOf(places1.getSelectedItem())).trim();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                quotascar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        QuotasSpinner=(String.valueOf(quotascar.getSelectedItem())).trim();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                quota=dataSnapshot.child(name).child("vehicle").getValue().toString();




            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });


    }
}
