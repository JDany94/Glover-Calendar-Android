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

import com.dany.glovercalendar.entidades.ConexionSQLiteHelper;
import com.dany.glovercalendar.entidades.AltaDemanda;
import com.dany.glovercalendar.utilidades.DatePickerFragment;
import com.dany.glovercalendar.utilidades.Utility;

import java.util.Calendar;

public class EmergenteModAltaDemanda extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextView tv_Contador, tv_Fecha;
    private String dia_Semana, dia_Mes, mes, anio;
    ConexionSQLiteHelper bd;
    String [] idBuscado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergente_mod_alta_demanda);

        tv_Contador = (TextView)findViewById(R.id.tv_contador_ModAD);
        tv_Fecha = (TextView)findViewById(R.id.tv_Fecha_ModAD);

        bd = new ConexionSQLiteHelper(getApplicationContext());

        Bundle registroEnviado = getIntent().getExtras();
        AltaDemanda registro = null;

        if (registroEnviado!=null){
            registro = (AltaDemanda) registroEnviado.getSerializable("registro");

            dia_Semana = registro.getDia_semana();
            dia_Mes = registro.getDia_mes();
            mes = registro.getMes();
            anio = registro.getAnio();
            tv_Fecha.setText(Utility.printFecha(dia_Semana, dia_Mes, mes ,anio));
            tv_Contador.setText(registro.getPedidos());

            String [] id = {registro.getId()};
            idBuscado = id;
        } else {
            Toast.makeText(this, "vacio", Toast.LENGTH_SHORT).show();
        }
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
        bd.updatePorID_AD(dia_Semana, dia_Mes, mes, anio, tv_Contador.getText().toString(), idBuscado);
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
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        dia_Semana = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        dia_Mes = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        mes = String.valueOf(c.get(Calendar.MONTH) + 1);
        anio = String.valueOf(c.get(Calendar.YEAR));

        tv_Fecha.setText(Utility.printFecha(dia_Semana,dia_Mes,mes,anio));
    }

    //Boton para eliminar registro
    public void BotonEliminar (View view) {

        bd.eliminarPorID_AD(idBuscado);

        Toast.makeText(this, "Eliminado.", Toast.LENGTH_SHORT).show();

        Intent volver = new Intent(this, VerAltaDemanda.class);
        startActivity(volver);
        finish();
    }

}
