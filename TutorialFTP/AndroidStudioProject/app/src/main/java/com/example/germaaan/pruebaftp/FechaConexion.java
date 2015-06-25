package com.example.germaaan.pruebaftp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FechaConexion {
    public static boolean comprobarConexion(Context ctx) {
        boolean conexion = false;

        ConnectivityManager gestor = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] redes = gestor.getAllNetworkInfo();

        for (int i = 0; i < redes.length; i++) {
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                conexion = true;
            }
        }

        return conexion;
    }

    public static String getFechaHoraActual() {
        Date fecha = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("ddMMyy-hhmmss");
        return formateador.format(fecha);
    }
}
