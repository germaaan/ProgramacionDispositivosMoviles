package com.example.germaaan.pruebaftp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class ActivityListarFotos extends ActionBarActivity {
    private static final String A_LISTAR_FOTOS = "AListarFotos";

    private ArrayList<String> listaFotos = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_fotos);

        ListView lista = (ListView) findViewById(R.id.listViewListaFotos);
        ArrayAdapter<String> adaptador;

        Object dump = null;
        AsyncTaskListarFotos task = new AsyncTaskListarFotos();

        try {
            ArrayList<String> listado = task.execute(dump).get();

            Iterator<String> itr = listado.iterator();

            while (itr.hasNext()) {
                String nombre = itr.next();
                Log.d(ActivityListarFotos.A_LISTAR_FOTOS, "Foto recibida: " + nombre);
                this.listaFotos.add(nombre);
            }

            adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, this.listaFotos);
            lista.setAdapter(adaptador);

            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> padre, View vista, int posicion, long id) {
                    String fotoSeleccionada = listaFotos.get(posicion);

                    System.out.println(fotoSeleccionada);

                    Log.d(ActivityListarFotos.A_LISTAR_FOTOS, "Foto seleccionada: " + fotoSeleccionada);

                    Intent i = new Intent(ActivityListarFotos.this, ActivityFotoSeleccionada.class);
                    i.putExtra("fotoSeleccionada", fotoSeleccionada);
                    startActivity(i);
                }
            });
        } catch (InterruptedException e) {
            Log.e(ActivityListarFotos.A_LISTAR_FOTOS, "Error de interrupción: " + e.getMessage());
        } catch (ExecutionException e) {
            Log.e(ActivityListarFotos.A_LISTAR_FOTOS, "Error de ejecución: " + e.getMessage());
        }
    }
}
