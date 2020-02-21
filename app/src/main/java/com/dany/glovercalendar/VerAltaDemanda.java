package com.dany.glovercalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dany.glovercalendar.entidades.AltaDemanda;
import com.dany.glovercalendar.utilidades.Utility;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VerAltaDemanda extends AppCompatActivity {

    CompactCalendarView calendario;
    List<Event> listaEventos;
    ArrayList<AltaDemanda> listaAltaDemanda;
    TextView tv_contador_total, tv_mes, tv_contador28Dias;
    int contadorDePedidos;
    ProgressBar pb;

    //Firebase
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    private String userID;
    private CollectionReference altaDemanda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_alta_demanda);

        tv_contador_total = (TextView)findViewById(R.id.tv_contador_totales);
        tv_mes = (TextView)findViewById(R.id.tv_mes_VerAD);
        tv_contador28Dias = (TextView)findViewById(R.id.tv_contador28DiasVerAD);
        calendario = (CompactCalendarView)findViewById(R.id.calendar_View);
        pb = (ProgressBar) findViewById(R.id.progressBar_Calendar);
        final FloatingActionsMenu Menu = (FloatingActionsMenu) findViewById(R.id.BotonFlotanteVerAD);
        final FloatingActionButton BotonFlotanteAgregarAD = (FloatingActionButton)findViewById(R.id.BotonFlotanteAgregarAD);
        final FloatingActionButton BotonFlotanteModAD = (FloatingActionButton)findViewById(R.id.BotonFlotanteModAD);
        final SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM - yyyy", Locale.getDefault()); // Como se muestra el mes y el ano del calendario
        calendario.shouldDrawIndicatorsBelowSelectedDays(true); //Se ve el evento con el dia seleccionado
        calendario.setUseThreeLetterAbbreviation(true);
        tv_mes.setText(dateFormatMonth.format(System.currentTimeMillis()));

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();
        altaDemanda = fStore.collection(Utility.USERS).document(userID).collection(Utility.AD);

        pb.setVisibility(View.VISIBLE);

        listaAltaDemanda = llenarListaConFireBase(); // Se carga la lista con la base de datos de Firebase
                                                    // Se carga el calendario y se muestra 28dias atras

        // Ver alta demanda de cada dia al clickear en él
        calendario.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();

                listaEventos = new ArrayList<>();
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
                Intent intent = new Intent(VerAltaDemanda.this, AgregarAltaDemanda.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Utility.BUNDLE, listaAltaDemanda);
                intent.putExtras(bundle);
                startActivity(intent);
                Menu.collapse();
                finish();
            }
        });
        BotonFlotanteModAD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VerAltaDemanda.this, ModificarAltaDemanda.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Utility.BUNDLE, listaAltaDemanda);
                intent.putExtras(bundle);
                startActivity(intent);
                Menu.collapse();
                finish();
            }
        });
    }

    private void cargarCalendario() {
        for (int i = 0; i < listaAltaDemanda.size(); i++) {
            long epoch = listaAltaDemanda.get(i).getFecha().getTime();
            String pedidos = listaAltaDemanda.get(i).getPedidos();
            Event evento;
            if (esAltaDemandaNormal(listaAltaDemanda.get(i).getFecha())) {
                evento = new Event(Color.parseColor(Utility.EVENT_COLOR_NORMAL), epoch, pedidos);
            } else {
                evento = new Event(Color.parseColor(Utility.EVENT_COLOR_ESPECIAL), epoch, pedidos);
            }
            calendario.addEvent(evento);
        }
        obtenerContadorDePedidos();
    }

    private boolean esAltaDemandaNormal(Date date) {
        if (date.getDay() == 5 || date.getDay() == 6 || date.getDay() == 0) {
            return true;
        }
        return false;
    }

    private void obtenerContadorDePedidos() {
        contadorDePedidos = 0;
        for (int i = 0; i < listaAltaDemanda.size(); i++)
            contadorDePedidos+= Integer.parseInt(listaAltaDemanda.get(i).getPedidos());
        tv_contador_total.setText(String.valueOf(contadorDePedidos));
    }

    private void verHace28Dias () {
        boolean hubo = false;
        Date today = new Date();
        Date hoy = new Date(today.getYear(), today.getMonth(), today.getDate());

        long voD = (hoy.getTime() - 2419200000L); // Restarle 28 dias a "hoy"

        for (int i = 0; i < listaAltaDemanda.size(); i++) {
            if (listaAltaDemanda.get(i).getFecha().getTime() == voD){
                tv_contador28Dias.setText(Utility.printFecha(listaAltaDemanda.get(i).getFecha()) + " - " + listaAltaDemanda.get(i).getPedidos());
                hubo = true;
                break;
            }
        }
        if (!hubo)
            tv_contador28Dias.setText("-");
    }

    private ArrayList<AltaDemanda> llenarListaConFireBase (){

        final ArrayList<AltaDemanda> lista;
        lista = new ArrayList<>();

        altaDemanda.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    AltaDemanda registro = documentSnapshot.toObject(AltaDemanda.class);
                    lista.add(registro);
                }
                pb.setVisibility(View.INVISIBLE);
                cargarCalendario();
                verHace28Dias();
            }
        });
        return lista;
    }

}
