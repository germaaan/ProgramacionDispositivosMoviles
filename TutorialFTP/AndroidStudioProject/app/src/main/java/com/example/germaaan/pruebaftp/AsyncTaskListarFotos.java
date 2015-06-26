package com.example.germaaan.pruebaftp;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class AsyncTaskListarFotos extends AsyncTask<Object, Void, ArrayList<String>> {
    private static final String LISTAR_FOTOS = "ListarFotos";

    private FTPClient cliente = null;
    private FTPFile[] archivos = null;

    public ArrayList<String> doInBackground(Object... params) {
        int reply;
        ArrayList<String> listado = null;

        this.cliente = new FTPClient();

        try {
            this.cliente.connect(MainActivity.SERVIDOR, MainActivity.PUERTO);
            this.cliente.login(MainActivity.USUARIO, MainActivity.PASS);

            reply = this.cliente.getReplyCode();

            if (FTPReply.isPositiveCompletion(reply)) {
                Log.d(AsyncTaskListarFotos.LISTAR_FOTOS, "Conexión realizada con éxito.");

                listado = new ArrayList<String>();
                this.cliente.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
                this.cliente.enterLocalPassiveMode();

                this.archivos = this.cliente.listFiles();
                Log.d(AsyncTaskListarFotos.LISTAR_FOTOS, "Tamaño del listado de la galeria: " +
                        (this.archivos.length - 2) / 2);

                for (int i = 2; i < this.archivos.length; i++) {
                    String nombre = this.archivos[i].getName();
                    Log.d(AsyncTaskListarFotos.LISTAR_FOTOS, "Nombre: " + nombre);
                    listado.add(nombre);
                }

                this.cliente.logout();
            } else {
                Log.d(AsyncTaskListarFotos.LISTAR_FOTOS, "Conexión rechazada por el servidor.");
                this.cliente.disconnect();
            }
        } catch (IOException e) {
            Log.e(AsyncTaskListarFotos.LISTAR_FOTOS, "Error de entrada/salida: " + e.getMessage());
        } finally {
            try {
                if (this.cliente.isConnected()) {
                    this.cliente.disconnect();
                    Log.e(AsyncTaskListarFotos.LISTAR_FOTOS, "Desconectado del servidor.");
                }
            } catch (IOException e) {
                Log.e(AsyncTaskListarFotos.LISTAR_FOTOS, "Error de entrada/salida: " + e.getMessage());
            }
        }

        return listado;
    }
}
