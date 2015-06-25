package com.example.germaaan.pruebaftp;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class AsyncTaskSubirFotos extends AsyncTask<File, Void, Boolean> {
    private static final String SUBIR_FOTOS = "SubirFotos";

    private FTPClient cliente = null;

    public Boolean doInBackground(File... archivos) {
        int reply;
        boolean resultado = false;
        BufferedInputStream bis = null;

        this.cliente = new FTPClient();

        try {
            this.cliente.connect(MainActivity.SERVIDOR, MainActivity.PUERTO);
            this.cliente.login(MainActivity.USUARIO, MainActivity.PASS);

            reply = this.cliente.getReplyCode();

            if (FTPReply.isPositiveCompletion(reply)) {
                Log.d(AsyncTaskSubirFotos.SUBIR_FOTOS, "Conexión realizada con éxito.");

                this.cliente.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
                this.cliente.enterLocalPassiveMode();

                for (int i = 0; i < archivos.length; i++) {
                    bis = new BufferedInputStream(new FileInputStream(archivos[i]));

                    Log.d(AsyncTaskSubirFotos.SUBIR_FOTOS, "Foto a subir: " + archivos[i].getName());
                    resultado = this.cliente.storeFile(archivos[i].getName(), bis);

                    if (resultado) {
                        Log.d(AsyncTaskSubirFotos.SUBIR_FOTOS, archivos[i].getName() +
                                " subida con éxito al servidor.");
                    } else {
                        Log.d(AsyncTaskSubirFotos.SUBIR_FOTOS, "No se ha podido subir + "
                                + archivos[i].getName() + " al servidor.");
                    }

                    bis.close();
                    Log.e(AsyncTaskSubirFotos.SUBIR_FOTOS, "Cerrado flujo de entrada.");
                }

                this.cliente.logout();
            } else {
                Log.d(AsyncTaskSubirFotos.SUBIR_FOTOS, "Conexión rechazada por el servidor.");
                this.cliente.disconnect();
            }
        } catch (IOException e) {
            Log.e(AsyncTaskSubirFotos.SUBIR_FOTOS, "Error de entrada/salida: " + e.getMessage());
        } finally {
            try {
                if (this.cliente.isConnected()) {
                    this.cliente.disconnect();
                    Log.e(AsyncTaskSubirFotos.SUBIR_FOTOS, "Desconectado del servidor.");
                }
            } catch (IOException e) {
                Log.e(AsyncTaskSubirFotos.SUBIR_FOTOS, "Error de entrada/salida: " + e.getMessage());
            }
        }

        return resultado;
    }
}