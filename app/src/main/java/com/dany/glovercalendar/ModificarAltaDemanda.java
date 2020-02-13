package com.dany.glovercalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dany.glovercalendar.entidades.AltaDemanda;
import com.dany.glovercalendar.utilidades.Utility;

import java.util.ArrayList;

public class ModificarAltaDemanda extends AppCompatActivity {

    ListView listViewAltaDemanda;
    ArrayList<String> listaInformacion_lv;
    ArrayList<AltaDemanda> listaAltaDemanda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_alta_demanda);

        listViewAltaDemanda = (ListView)findViewById(R.id.lv_ModAltaDemanda);

        Bundle bundle = getIntent().getExtras();

        listaAltaDemanda = (ArrayList<AltaDemanda>) bundle.getSerializable(Utility.BUNDLE);

        cargarListaAD();

        ArrayAdapter adaptador = new ArrayAdapter(this, R.layout.listview_item, listaInformacion_lv);
        listViewAltaDemanda.setAdapter(adaptador);

        listViewAltaDemanda.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AltaDemanda registro  = listaAltaDemanda.get(position);
                Intent intent = new Intent(ModificarAltaDemanda.this, EmergenteModAltaDemanda.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Utility.BUNDLE, registro);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }

    private void cargarListaAD() {
        listaInformacion_lv = new ArrayList<>();
        for (int i = 0; i < listaAltaDemanda.size(); i++) {
            listaInformacion_lv.add(Utility.printFecha(listaAltaDemanda.get(i).getFecha()) + " - " + listaAltaDemanda.get(i).getPedidos() + " Pedidos");
        }
    }
}

