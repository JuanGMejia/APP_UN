package com.example.juangui.un_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //URL raiz, exclusiva de la sede Medellín
    public static final String ROOT_URL = "http://sia.unalmed.edu.co/";
    Button botonLogin;
    EditText passw, username;
    String user, pass;
    Firebase firebase;
    ProgressDialog loading;
    Switch recordarUsuario;
    private String FIREBASE_URL = "https://unapp-c52f0.firebaseio.com";
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebase.setAndroidContext(this);
        firebase = new Firebase(FIREBASE_URL);
        mAuth = FirebaseAuth.getInstance();
        botonLogin = (Button) findViewById(R.id.login);
        username = (EditText) findViewById(R.id.username);
        passw = (EditText) findViewById(R.id.passw);
        botonLogin.setOnClickListener(this);
        recordarUsuario = (Switch) findViewById(R.id.recordar);
        CargarPreferencias();
    }

    private void postSia(final String usernamesia, final String password) {

        //Manejo del POST al SIA
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL) //URL raíz
                .build(); //Contrucción del adaptador

        //Creando el objeto apra la interfaz
        RegisterAPI api = adapter.create(RegisterAPI.class);

        api.logear(

                //Pasar los valores
                usernamesia,
                password,

                //Creating an anonymous callback
                new Callback<Response>() {
                    @Override
                    public void success(Response result, Response response) {
                        //On success we will read the server's output using bufferedreader
                        //Creating a bufferedreader object
                        loading.getProgress();

                        String cadena = new String(((TypedByteArray) response.getBody()).getBytes());
                        String word = "Visitante";
                        //Si la respuesta contiene "Visitante" es porque no se logró iniciar sesión
                        if (cadena.contains(word)) {
                            loading.dismiss();
                            Toast.makeText(LoginActivity.this, "Usuario y/o contraseña incorrectos", Toast.LENGTH_LONG).show();

                        } else {
                            //Si se logeó, se crea un usuario en la Firebase con correo institucional y contraseña

                            loading.getProgress();
                            mAuth.createUserWithEmailAndPassword(usernamesia + "@unal.edu.co", password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            mAuth.signInWithEmailAndPassword(usernamesia + "@unal.edu.co", password)
                                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                            //Cuando se crea el usuario satisfactoriamente, se crean los campos que le corresponden
                                                            loading.dismiss();
                                                            GuardarPreferencias();
                                                            firebase.child("Users").child(usernamesia).child("Vehicles").child("Carro").child("Placa").setValue("");
                                                            firebase.child("Users").child(usernamesia).child("Vehicles").child("Carro").child("Capacity").setValue("");
                                                            firebase.child("Users").child(usernamesia).child("Vehicles").child("Moto").child("Placa").setValue("");
                                                            firebase.child("Users").child(usernamesia).child("Vehicles").child("Moto").child("Capacity").setValue("");
                                                            firebase.child("Users").child(usernamesia).child("Name").setValue("");
                                                            //Acción cuando se registra un usuario nuevo
                                                            Toast.makeText(LoginActivity.this, "Tirar al tutorial", Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                        }
                                    });
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        //If any error occured displaying the error as toast
                        Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void CargarPreferencias() {
        //Si el usuario eligió "guardar usuario" se le muestra cuando se abre el login
        SharedPreferences mispreferencias = getSharedPreferences("Preferencias usuario", Context.MODE_PRIVATE);
        if (mispreferencias.getBoolean("switch", false)) {
            recordarUsuario.setChecked(true);
        } else {
            recordarUsuario.setChecked(false);
        }
        username.setText(mispreferencias.getString("nombre", ""));
    }

    public void GuardarPreferencias() {
        //Guarda las preferencias
        SharedPreferences mispreferencias = getSharedPreferences("Preferencias usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mispreferencias.edit();
        boolean valorswitch = recordarUsuario.isChecked();
        editor.putBoolean("switch", valorswitch);
        //Guarda el nomrbe de usuario si el usuario lo eligió
        if (valorswitch) {
            editor.putString("nombre", username.getText().toString());
        } else {
            editor.putString("nombre", "");
        }
        editor.apply();
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            //Cuando se hace click al botón Login
            case R.id.login:
                user = username.getText().toString().trim();
                pass = passw.getText().toString().trim();
                //Se verifica que el usuario y contraseña no estén vacíos
                if (!user.equals("") && !pass.equals("")) {
                    mAuth.signInWithEmailAndPassword(user + "@unal.edu.co", pass)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    //Si se logró ahcer login contra la Firebase
                                    GuardarPreferencias();
                                    Toast.makeText(LoginActivity.this, "Existe!", Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Si falla el login contra la Firebase se intenta hacer un POST al sia para
                            //ver si existe el usuario
                            loading = ProgressDialog.show(LoginActivity.this, "Cargando", "Espera ...");
                            postSia(user, pass);
                        }
                    });
                } else if (user.equals("") && pass.equals("")) {
                    //Se verifica que no haya algún campo vacío
                    Toast.makeText(LoginActivity.this, "¡Campos requeridos!", Toast.LENGTH_LONG).show();
                } else if (pass.equals("")) {
                    Toast.makeText(LoginActivity.this, "¡Contraseña requerida!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, "¡Usuario requerido!", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
