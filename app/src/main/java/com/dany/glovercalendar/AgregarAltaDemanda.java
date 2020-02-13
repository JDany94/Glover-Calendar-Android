package com.dany.glovercalendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

public class AgregarAltaDemanda extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextView tv_Contador, tv_Fecha;
    Date fecha;
    ArrayList<AltaDemanda> listaAltaDemanda;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    private String userID;
    private CollectionReference altaDemanda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_alta_demanda);

        tv_Fecha = (TextView)findViewById(R.id.tv_fecha_AD);
        tv_Contador = (TextView)findViewById(R.id.tv_contadorAD);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();
        altaDemanda = fStore.collection(Utility.USERS).document(userID).collection(Utility.AD);

        Bundle bundle = getIntent().getExtras();
        listaAltaDemanda = (ArrayList<AltaDemanda>) bundle.getSerializable(Utility.BUNDLE);
    }

    //Boton para seleccionar la fecha
    public void BotonFecha (View view) {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        fecha = new Date(year - 1900, month, dayOfMonth);
        tv_Fecha.setText(Utility.printFecha(fecha));
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

    //Boton Help
    public void BotonHelp (View view) {
        startActivity(new Intent(this, Help.class));
    }

    //Boton Database Firebase
    public void Guardar (View view) {
        if (tv_Fecha.getText().equals("Seleccione la fecha..")) {
            Toast.makeText(this, "Falta llenar algunos campos..", Toast.LENGTH_SHORT).show();
        } else {
            if (Utility.fechaValida(fecha)) {
                if (diaSinAltaDemanda(fecha)) {

                    DocumentReference documentReference = altaDemanda.document();
                    String idFecha = documentReference.getId();
                    String pedidos = tv_Contador.getText().toString();
                    AltaDemanda registro = new AltaDemanda(idFecha, fecha, pedidos);
                    documentReference.set(registro);

                    Toast.makeText(this, "Alta Demanda Registrada", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(this, VerAltaDemanda.class));
                    finish();

                } else {
                    AlertDialog.Builder alerta = new AlertDialog.Builder(AgregarAltaDemanda.this);
                    alerta.setMessage("¿Desea modificarla?")
                            .setCancelable(false)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    AltaDemanda registro = buscarRegistro(fecha);
                                    Intent intent = new Intent(AgregarAltaDemanda.this, EmergenteModAltaDemanda.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable(Utility.BUNDLE, registro);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    tv_Fecha.setText("Seleccione la fecha..");
                                    tv_Contador.setText("0");
                                    dialog.cancel();
                                }
                            });
                    AlertDialog titulo = alerta.create();
                    titulo.setTitle("Este dia ya tiene Alta Demanda registrada..");
                    titulo.show();
                }
            } else {
                Toast.makeText(this, "La fecha no pertenece a los ultimos 28 dias..", Toast.LENGTH_LONG).show();
            }
        }
    }

    private AltaDemanda buscarRegistro(Date fecha) {
        AltaDemanda registro = new AltaDemanda();
        for (int i = 0; i < listaAltaDemanda.size(); i++) {
            if (listaAltaDemanda.get(i).getFecha().equals(fecha)) {
                registro = listaAltaDemanda.get(i);
                return registro;
            }
        }
        return registro;
    }

    private boolean diaSinAltaDemanda (Date date) {
        for (int i = 0; i < listaAltaDemanda.size(); i++) {
            if (listaAltaDemanda.get(i).getFecha().equals(date)) {
                return false;
            }
        }
        return true;
    }
}
