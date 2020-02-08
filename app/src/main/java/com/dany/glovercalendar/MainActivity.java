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
        Intent verAltaDemanda = new Intent(this, VerAltaDemanda.class);
        startActivity(verAltaDemanda);
    }

    //Boton del VerEfectivo
    public void BotonEfectivo (View view) {
        Intent efectivo = new Intent(this, VerEfectivo.class);
        startActivity(efectivo);
    }

    //Boton de Cerrar Sesion
    public void logout (View view) {
        FirebaseAuth.getInstance().signOut();
        Intent logout = new Intent(this, LoginScreen.class);
        startActivity(logout);
        finish();
    }

}
