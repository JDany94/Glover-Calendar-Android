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

import com.dany.glovercalendar.entidades.ConexionSQLiteHelper;
import com.dany.glovercalendar.entidades.AltaDemanda;
import com.dany.glovercalendar.utilidades.DatePickerFragment;
import com.dany.glovercalendar.utilidades.Utility;

import java.util.ArrayList;
import java.util.Calendar;

public class AgregarAltaDemanda extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextView tv_Contador, tv_Fecha;
    private String dia_Semana, dia_Mes, mes, anio;
    ArrayList<AltaDemanda> listaAltaDemanda;
    ConexionSQLiteHelper bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_alta_demanda);

        tv_Fecha = (TextView)findViewById(R.id.tv_fecha_AD);
        tv_Contador = (TextView)findViewById(R.id.tv_contadorAD);

        bd = new ConexionSQLiteHelper(getApplicationContext());
        listaAltaDemanda = bd.selectAllFromAD();
    }

    //Boton para seleccionar la fecha
    public void BotonFecha (View view) {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        dia_Semana = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        dia_Mes = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        mes = String.valueOf(c.get(Calendar.MONTH ) + 1);
        anio = String.valueOf(c.get(Calendar.YEAR));

        tv_Fecha.setText(Utility.printFecha(dia_Semana,dia_Mes,mes,anio));
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

    //Boton para guardar
    public void BotonGuardar (View view) {
        if (tv_Fecha.getText().equals("Seleccione la fecha..")) {
            Toast.makeText(this, "Falta llenar algunos campos..", Toast.LENGTH_SHORT).show();
        } else {
            if (Utility.fechaValida(dia_Semana, dia_Mes, mes, anio)) {
                if (diaSinAltaDemanda(dia_Mes, mes, anio)) {

                    bd.insertarAD(dia_Semana, dia_Mes, mes, anio, tv_Contador.getText().toString());

                    Toast.makeText(this, "Alta Demanda Registrada", Toast.LENGTH_SHORT).show();
                    Intent volver = new Intent(this, VerAltaDemanda.class);
                    startActivity(volver);
                    finish();

                } else {
                    AlertDialog.Builder alerta = new AlertDialog.Builder(AgregarAltaDemanda.this);
                    alerta.setMessage("¿Desea modificarla?")
                            .setCancelable(false)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    AltaDemanda registro = bd.buscarRegistro(dia_Mes, mes, anio);

                                    Intent intent = new Intent(AgregarAltaDemanda.this, EmergenteModAltaDemanda.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("registro", registro);
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

    //Boton Help
    public void BotonHelp (View view) {
        Intent help = new Intent(this, Help.class);
        startActivity(help);
    }

    private boolean diaSinAltaDemanda (String dia_Mes, String mes, String anio) {
        for (int i = 0; i < listaAltaDemanda.size(); i++) {
            if (listaAltaDemanda.get(i).getDia_mes().equals(dia_Mes) && listaAltaDemanda.get(i).getMes().equals(mes) && listaAltaDemanda.get(i).getAnio().equals(anio)) {
                return false;
            }
        }
        return true;
    }

}
