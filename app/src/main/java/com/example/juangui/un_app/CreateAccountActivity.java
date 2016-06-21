package com.example.juangui.un_app;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class CreateAccountActivity extends AppCompatActivity {

    EditText user,pass,pass2;
    CheckBox car,moto;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        user=(EditText) findViewById(R.id.user);
        pass=(EditText) findViewById(R.id.pass);
        pass2=(EditText) findViewById(R.id.pass2);
        car=(CheckBox) findViewById(R.id.car);
        moto=(CheckBox) findViewById(R.id.moto);
    }

    public void Save(View v){
        String name=user.getText().toString();
        String passw=pass.getText().toString();
        String passw2=pass2.getText().toString();
        int vehi=0;
        if(car.isChecked()){
            vehi++;
        }
        if(moto.isChecked()){
            vehi+=2;
        }

        DB base= new DB(this,"Base",null,1);
        SQLiteDatabase db= base.getWritableDatabase();
        if(db!=null){
            ContentValues file= new ContentValues();
            file.put("username",name);
            file.put("password",passw);
            file.put("Vehicle",vehi);
            long i=db.insert("users",null,file);
            if(i>0){
                Toast.makeText(this,"Account created!!",Toast.LENGTH_SHORT).show();
            }
        }
    }


}
