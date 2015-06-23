package com.example.germaaan.pruebaposicionamiento;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements LocationListener {
    private static final String MAIN_ACTIVITY = "MainActivity";

    private double latitud;
    private double longitud;

    private LocationManager locationManager = null;
    private Location coordenadas = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void obtenerCoordenadas(View view) {
        try {
            this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            boolean estadoGPS = this.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean estadoRed = this.locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (estadoGPS) {
                if (this.coordenadas == null) {
                    this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

                    if (this.locationManager != null) {
                        this.coordenadas = this.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                }
            } else if (estadoRed) {
                this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

                if (this.locationManager != null) {
                    this.coordenadas = this.locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            }
        } catch (Exception e) {
            Log.e(MainActivity.MAIN_ACTIVITY, "Error obteniendo coordenadas: " + e.getMessage());
        }

        if (this.coordenadas != null) {
            this.latitud = coordenadas.getLatitude();
            this.longitud = coordenadas.getLongitude();

            Toast.makeText(this, "Coordenadas: " + this.latitud + "," + this.longitud, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No se han podido obtener las coordenadas.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
