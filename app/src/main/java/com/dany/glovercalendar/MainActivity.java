package com.dany.glovercalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Boton de Alta Demanda
    public void BotonAltaDemanda (View view) {
        startActivity(new Intent(this, VerAltaDemanda.class));
    }

    //Boton del VerEfectivo
    public void BotonEfectivo (View view) {
        startActivity(new Intent(this, VerEfectivo.class));
    }

    //Boton de Cerrar Sesion
    public void logout (View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoginScreen.class));
        finish();
    }

}
