package com.example.germaaan.pruebaftp;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class AsyncTaskDescargarFoto extends AsyncTask<String, Void, Boolean> {
    private static final String DESCARGAR_FOTO = "DescargarFoto";

    private FTPClient cliente = null;

    public Boolean doInBackground(String... archivos) {
        int reply;
        boolean resultado = false;
        OutputStream out = null;

        this.cliente = new FTPClient();
        try {
            this.cliente.connect(MainActivity.SERVIDOR, MainActivity.PUERTO);
            this.cliente.login(MainActivity.USUARIO, MainActivity.PASS);

            reply = this.cliente.getReplyCode();

            if (FTPReply.isPositiveCompletion(reply)) {
                Log.d(AsyncTaskDescargarFoto.DESCARGAR_FOTO, "Conexión realizada con éxito.");

                this.cliente.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
                this.cliente.enterLocalPassiveMode();


                String archivoRemoto = archivos[0];
                File archivoDescargado = new File(MainActivity.RUTA + archivoRemoto);
                out = new BufferedOutputStream(new FileOutputStream(archivoDescargado));

                resultado = this.cliente.retrieveFile(archivoRemoto, out);
                out.close();

                if (resultado) {
                    Log.d(AsyncTaskDescargarFoto.DESCARGAR_FOTO, "Foto " + archivoRemoto +
                            " descargada correctamente en " + archivoDescargado.getAbsolutePath());
                }

                this.cliente.logout();
            } else {
                Log.d(AsyncTaskDescargarFoto.DESCARGAR_FOTO, "Conexión rechazada por el servidor.");
                this.cliente.disconnect();
            }
        } catch (IOException e) {
            Log.e(AsyncTaskDescargarFoto.DESCARGAR_FOTO, "Error de entrada/salida: " + e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                    Log.d(AsyncTaskDescargarFoto.DESCARGAR_FOTO, "Flujo de escritura cerrado.");
                }

                if (this.cliente.isConnected()) {
                    this.cliente.disconnect();
                    Log.e(AsyncTaskDescargarFoto.DESCARGAR_FOTO, "Desconectado del servidor.");
                }
            } catch (IOException e) {
                Log.e(AsyncTaskDescargarFoto.DESCARGAR_FOTO, "Error de entrada/salida: "
                        + e.getMessage());
            }
        }

        return resultado;
    }
}
