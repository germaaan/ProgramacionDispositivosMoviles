package com.example.germaaan.pruebaftp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class ActivityFotoSeleccionada extends ActionBarActivity {
    private static final String FOTO_SELECCIONADA = "FotoSeleccionada";

    private String fotoSeleccionada = null;

    private boolean estadoRed = false;
    private boolean fotoDescargada = false;

    private ImageView fotoCapturada = null;
    private Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto_seleccionada);

        this.fotoSeleccionada = (String) getIntent().getSerializableExtra("fotoSeleccionada");

        this.fotoCapturada = (ImageView) this.findViewById(R.id.imageViewFoto);
        this.estadoRed = FechaConexion.comprobarConexion(this.getBaseContext());

        if (this.estadoRed) {
            AsyncTaskDescargarFoto task = new AsyncTaskDescargarFoto();

            try {
                this.fotoDescargada = task.execute(this.fotoSeleccionada).get();

                if (this.fotoDescargada) {
                    this.bitmap = BitmapFactory.decodeFile(MainActivity.RUTA + this.fotoSeleccionada);
                    this.fotoCapturada.setImageBitmap(this.bitmap);
                } else {
                    Toast.makeText(this, "No se ha podido descargar la foto al servidor.",
                            Toast.LENGTH_SHORT).show();
                }
            } catch (InterruptedException e) {
                Log.e(ActivityFotoSeleccionada.FOTO_SELECCIONADA, "Error de interrupción: " + e.getMessage());
            } catch (ExecutionException e) {
                Log.e(ActivityFotoSeleccionada.FOTO_SELECCIONADA, "Error de ejecución: " + e.getMessage());
            }
        } else {
            Toast.makeText(this, "No hay conexión a internet.", Toast.LENGTH_SHORT).show();
        }
    }
}