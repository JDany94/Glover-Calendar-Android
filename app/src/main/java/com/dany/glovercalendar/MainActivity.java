package com.dany.glovercalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dany.glovercalendar.entidades.AltaDemanda;
import com.dany.glovercalendar.utilidades.Utility;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<AltaDemanda> listaAltaDemanda;

    //Firebase
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    private String userID;
    private CollectionReference altaDemanda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();
        altaDemanda = fStore.collection(Utility.USERS).document(userID).collection(Utility.AD);

        eliminarAltaDemandaVieja();
    }

    //Boton Alta Demanda
    public void BotonAltaDemanda (View view) {
        startActivity(new Intent(this, VerAltaDemanda.class));
    }

    //Boton VerEfectivo
    public void BotonEfectivo (View view) {
        startActivity(new Intent(this, VerEfectivo.class));
    }

    //Boton Perfil
    public void BotonProfile (View view) {
        startActivity(new Intent(this, ProfileScreen.class));
    }

    private void eliminarAltaDemandaVieja() {

        final ArrayList<AltaDemanda> lista;
        lista = new ArrayList<>();

        altaDemanda.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    AltaDemanda registro = documentSnapshot.toObject(AltaDemanda.class);
                    lista.add(registro);
                }
                listaAltaDemanda = lista;
                for (int i = 0; i < listaAltaDemanda.size(); i++){
                    if (!Utility.fechaValida(listaAltaDemanda.get(i).getFecha())) {
                        String id = listaAltaDemanda.get(i).getId();
                        altaDemanda.document(id).delete();
                    }
                }
            }
        });
    }

}
