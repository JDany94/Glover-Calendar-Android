package com.dany.glovercalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dany.glovercalendar.entidades.ConexionSQLiteHelper;
import com.dany.glovercalendar.entidades.AltaDemanda;
import com.dany.glovercalendar.utilidades.Utility;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VerAltaDemanda extends AppCompatActivity {

    CompactCalendarView calendario;
    List<Event> listaEventos;
    ArrayList<AltaDemanda> listaAltaDemanda;
    TextView tv_contador_total, tv_mes, tv_contador28Dias;
    int contadorDePedidos;
    ConexionSQLiteHelper bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_alta_demanda);

        bd = new ConexionSQLiteHelper(getApplicationContext());
        listaAltaDemanda = bd.selectAllFromAD(); // Se carga la lista con la base de datos

        tv_contador_total = (TextView)findViewById(R.id.tv_contador_totales);
        tv_mes = (TextView)findViewById(R.id.tv_mes_VerAD);
        tv_contador28Dias = (TextView)findViewById(R.id.tv_contador28DiasVerAD);
        calendario = (CompactCalendarView)findViewById(R.id.calendar_View);

        final FloatingActionsMenu Menu = (FloatingActionsMenu) findViewById(R.id.BotonFlotanteVerAD);
        final FloatingActionButton BotonFlotanteAgregarAD = (FloatingActionButton)findViewById(R.id.BotonFlotanteAgregarAD);
        final FloatingActionButton BotonFlotanteModAD = (FloatingActionButton)findViewById(R.id.BotonFlotanteModAD);

        final SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM - yyyy", Locale.getDefault()); // Como se muestra el mes y el ano del calendario
        calendario.shouldDrawIndicatorsBelowSelectedDays(true); //Se ve el evento con el dia seleccionado
        calendario.setUseThreeLetterAbbreviation(true);
        tv_mes.setText(dateFormatMonth.format(System.currentTimeMillis()));

        eliminarAltaDemandaVieja(); // Elimina la alta demanda que ya no entra en los ultimos 28 dias

        cargarCalendario(); // Carga el calendario con las altas demandas de la base de datos

        verHace28Dias(); // Calcula si hace 28 dias hubo alta demanda y la muestra

        // Ver alta demanda de cada dia al clickear en el
        calendario.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();

                listaEventos = new ArrayList<Event>();
                listaEventos = calendario.getEvents(dateClicked);

                if (listaEventos.isEmpty()) {
                    Toast.makeText(context, "No hay pedidos en alta demanda este dia", Toast.LENGTH_SHORT).show();
                }else { //Dialogo Emergente con descripcion
                    String date = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date (listaEventos.get(0).getTimeInMillis()));
                    String pedidos = listaEventos.get(0).getData().toString();
                    Toast.makeText(context, date + " - " + pedidos + " Pedidos", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                tv_mes.setText(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });

        //Botones flotantes
        BotonFlotanteAgregarAD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent agregarAltaDemanda = new Intent(VerAltaDemanda.this, AgregarAltaDemanda.class);
                startActivity(agregarAltaDemanda);
                Menu.collapse();
                finish();
            }
        });
        BotonFlotanteModAD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent modAltaDemanda = new Intent(VerAltaDemanda.this, ModificarAltaDemanda.class);
                startActivity(modAltaDemanda);
                Menu.collapse();
                finish();
            }
        });
    }

    private void cargarCalendario() {
        for (int i = 0; i < listaAltaDemanda.size(); i++) {
            String humanDate = listaAltaDemanda.get(i).getDia_mes() + "/" + listaAltaDemanda.get(i).getMes() + "/" + listaAltaDemanda.get(0).getAnio();
            long epoch = 0;
            try {
                epoch = new SimpleDateFormat("dd/MM/yyyy").parse(humanDate).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String pedidos = listaAltaDemanda.get(i).getPedidos();
            Event evento;
            if (esAltaDemandaNormal(listaAltaDemanda.get(i).getDia_semana())) {
                evento = new Event(Color.parseColor(Utility.EVENT_COLOR_NORMAL), epoch, pedidos);
            } else {
                evento = new Event(Color.parseColor(Utility.EVENT_COLOR_ESPECIAL), epoch, pedidos);
            }
            calendario.addEvent(evento);
        }
        obtenerContadorDePedidos();
    }

    private boolean esAltaDemandaNormal(String dia_semana) {
        if (dia_semana.equals("6") || dia_semana.equals("7") || dia_semana.equals("1")) {
            return true;
        }
        return false;
    }

    private void obtenerContadorDePedidos() {
        for (int i = 0; i < listaAltaDemanda.size(); i++)
            contadorDePedidos+= Integer.parseInt(listaAltaDemanda.get(i).getPedidos());
        tv_contador_total.setText(String.valueOf(contadorDePedidos));
    }

    private void verHace28Dias () {

        boolean hubo = false;

        Calendar hoy = Calendar.getInstance();
        hoy.set(Calendar.HOUR, 0);
        hoy.set(Calendar.HOUR_OF_DAY, 0);
        hoy.set(Calendar.MINUTE, 0);
        hoy.set(Calendar.SECOND, 0);

        long vo_d = (hoy.getTimeInMillis() - 2419200000L); // Restarle 28 dias a "hoy"

        String dia = new SimpleDateFormat("dd").format(new Date (vo_d));
        String mes = new SimpleDateFormat("MM").format(new Date (vo_d));
        String anio = new SimpleDateFormat("yyyy").format(new Date (vo_d));

        for (int i = 0; i < listaAltaDemanda.size(); i++) {
            if (listaAltaDemanda.get(i).getDia_mes().equals(dia) && listaAltaDemanda.get(i).getMes().equals(mes) && listaAltaDemanda.get(i).getAnio().equals(anio)){
                tv_contador28Dias.setText(dia + "/"+ mes + "/" + anio + " - " + listaAltaDemanda.get(i).getPedidos());
                hubo = true;
                break;
            }
        }
        if (!hubo)
            tv_contador28Dias.setText("-");
    }

    private void eliminarAltaDemandaVieja() {
        for (int i = 0; i < listaAltaDemanda.size(); i++){
            if (!Utility.fechaValida(listaAltaDemanda.get(i).getDia_semana(), listaAltaDemanda.get(i).getDia_mes(), listaAltaDemanda.get(i).getMes(), listaAltaDemanda.get(i).getAnio())) {
                String [] id = {listaAltaDemanda.get(i).getId()};
                bd.eliminarPorID_AD(id);
            }
        }
    }
}
