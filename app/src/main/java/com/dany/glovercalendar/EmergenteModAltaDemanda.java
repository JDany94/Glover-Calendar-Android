package com.dany.glovercalendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.dany.glovercalendar.entidades.AltaDemanda;
import com.dany.glovercalendar.utilidades.DatePickerFragment;
import com.dany.glovercalendar.utilidades.Utility;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EmergenteModAltaDemanda extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextView tv_Contador, tv_Fecha;
    AltaDemanda registro;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    private String userID;
    private CollectionReference altaDemanda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergente_mod_alta_demanda);

        tv_Contador = (TextView)findViewById(R.id.tv_contador_ModAD);
        tv_Fecha = (TextView)findViewById(R.id.tv_Fecha_ModAD);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();
        altaDemanda = fStore.collection(Utility.USERS).document(userID).collection(Utility.AD);

        Bundle bundle = getIntent().getExtras();

        registro = (AltaDemanda) bundle.getSerializable(Utility.BUNDLE);

        tv_Fecha.setText(Utility.printFecha(registro.getFecha()));
        tv_Contador.setText(registro.getPedidos());
    }

    //Boton para incrementar contador
    public void BotonIncrementar (View view) {
        int contador = Integer.parseInt(tv_Contador.getText().toString());
        contador++;
        tv_Contador.setText(String.valueOf(contador));
    }

    //Boton para decrementar contador
    public void BotonDecrementar (View view) {
        int contador = Integer.parseInt(tv_Contador.getText().toString());
        if (contador <= 0) {
            Toast.makeText(this, "Ya estas en 0", Toast.LENGTH_SHORT).show();
        } else {
            contador--;
            tv_Contador.setText(String.valueOf(contador));
        }
    }

    //Boton para guardar cambio
    public void BotonGuardarCambio (View view) {

        Map <String, Object> map = new HashMap<>();
        map.put("id", registro.getId());
        map.put("fecha", registro.getFecha());
        map.put("pedidos", tv_Contador.getText().toString());

        altaDemanda.document(registro.getId()).update(map);

        Toast.makeText(this, "Cambios Guardados.", Toast.LENGTH_SHORT).show();
        Intent volver = new Intent(this, VerAltaDemanda.class);
        startActivity(volver);
        finish();
    }

    //Boton para seleccionar la fecha
    public void BotonFecha (View view) {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        registro.setFecha(new Date(year - 1900, month, dayOfMonth));
        tv_Fecha.setText(Utility.printFecha(registro.getFecha()));
    }

    //Boton para eliminar registro
    public void BotonEliminar (View view) {

        altaDemanda.document(registro.getId()).delete();

        Toast.makeText(this, "Eliminado.", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(this, VerAltaDemanda.class));
        finish();
    }

}
