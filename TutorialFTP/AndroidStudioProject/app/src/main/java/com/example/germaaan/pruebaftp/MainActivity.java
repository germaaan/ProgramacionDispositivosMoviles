package com.example.germaaan.pruebaftp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.concurrent.ExecutionException;


public class MainActivity extends ActionBarActivity {
    public static final String SERVIDOR = "caminoapp.bugs3.com";
    public static final String USUARIO = "u223647139.prueba";
    public static final String PASS = "prueba2015";
    public static final int PUERTO = 21;
    public static final String RUTA = Environment.getExternalStorageDirectory() + "/DCIM/PruebaFTP/";

    private static final String MAIN_ACTIVITY = "MainActivity";

    private String nombreArchivo = null;
    private boolean fotoSubida = false;
    private boolean estadoRed = false;

    private ImageView fotoCapturada = null;
    private File directorio = null;
    private File archivoFoto = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.fotoCapturada = (ImageView) this.findViewById(R.id.imageViewFoto);

        this.directorio = new File(MainActivity.RUTA);
        this.directorio.mkdirs();
    }

    public void sacarFotos(View view) {
        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        this.nombreArchivo = FechaConexion.getFechaHoraActual() + ".png";
        this.archivoFoto = new File(directorio, this.nombreArchivo);

        this.fotoSubida = false;

        Uri uri = Uri.fromFile(this.archivoFoto);
        i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(i, 1);
    }

    public void subirFotos(View view) {
        if (!this.fotoSubida) {
            this.estadoRed = FechaConexion.comprobarConexion(this.getBaseContext());

            if (this.estadoRed) {
                AsyncTaskSubirFotos task = new AsyncTaskSubirFotos();

                try {
                    if (this.archivoFoto != null) {
                        Toast.makeText(this, "Subiendo foto al servidor.", Toast.LENGTH_SHORT).show();
                        this.fotoSubida = task.execute(this.archivoFoto).get();

                        if (this.fotoSubida) {
                            Toast.makeText(this, "Foto subida al servidor.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "No se ha podido subir la foto al servidor.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "No se ha capturado ninguna foto.", Toast.LENGTH_SHORT).show();
                    }
                } catch (InterruptedException e) {
                    Log.e(MainActivity.MAIN_ACTIVITY, "Error de interrupción: " + e.getMessage());
                } catch (ExecutionException e) {
                    Log.e(MainActivity.MAIN_ACTIVITY, "Error de ejecución: " + e.getMessage());
                }
            } else {
                Toast.makeText(this, "No hay conexion a internet.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Esta foto ya ha sido subida al servidor.", Toast.LENGTH_SHORT).show();
        }
    }

    public void listarFotos(View view) {
        Intent i = new Intent(MainActivity.this, ActivityListarFotos.class);
        startActivity(i);
    }

    protected void onActivityResult(int solicitud, int resultado, Intent datos) {
        if (solicitud == 1 && resultado == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(MainActivity.RUTA + this.nombreArchivo);
            this.fotoCapturada.setImageBitmap(bitmap);
        }
    }
}
