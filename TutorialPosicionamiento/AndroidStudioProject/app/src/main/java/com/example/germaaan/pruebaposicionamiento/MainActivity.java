package com.example.germaaan.pruebaposicionamiento;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends ActionBarActivity implements LocationListener {
    private static final String MAIN_ACTIVITY = "MainActivity";

    boolean estadoGPS = false;
    boolean estadoRed = false;
    private double latitud = 0.0;
    private double longitud = 0.0;

    private LocationManager locationManager = null;
    private Location coordenadas = null;

    private LatLng posicion = null;
    private GoogleMap mapa = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void obtenerCoordenadas(View view) {
        try {
            this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            this.actualizarEstadoGPS();
            this.actualizarEstadoConexionRed();

            if (estadoGPS) {
                if (this.coordenadas == null) {
                    this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, this);
                } else {
                    this.coordenadas = this.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
            } else if (estadoRed) {
                if (this.coordenadas == null) {
                    this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 100, this);
                } else {
                    this.coordenadas = this.locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            } else {
                Toast.makeText(this, "GPS y conexión de red desactivados", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(MainActivity.MAIN_ACTIVITY, "Error obteniendo coordenadas: " + e.getMessage());
        }
    }

    public void detenerActualización(View view) {
        this.locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.coordenadas = location;

        this.latitud = this.coordenadas.getLatitude();
        this.longitud = this.coordenadas.getLongitude();

        Toast.makeText(this, "Coordenadas: " + this.latitud + "," + this.longitud, Toast.LENGTH_SHORT).show();
        this.dibujarMapa();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        switch (i) {
            case LocationProvider.AVAILABLE:
                Toast.makeText(this, "El proveedor de localización " + s + " ha pasado a estar disponible.", Toast.LENGTH_SHORT).show();
                break;
            case LocationProvider.OUT_OF_SERVICE:
                Toast.makeText(this, "El proveedor de localización " + s + " ha pasado a estar fuera de servicio.", Toast.LENGTH_SHORT).show();
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Toast.makeText(this, "El proveedor de localización " + s + " ha pasado a estar temporalmente no disponible.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(this, "El proveedor de localización " + s + " ha sido activado.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(this, "El proveedor de localización " + s + " ha sido desactivado.", Toast.LENGTH_SHORT).show();
    }

    private void actualizarEstadoGPS() {
        this.estadoGPS = this.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void actualizarEstadoConexionRed() {
        this.estadoRed = this.locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void dibujarMapa() {
        this.posicion = new LatLng(this.coordenadas.getLatitude(), this.coordenadas.getLongitude());

        this.mapa = ((MapFragment) getFragmentManager().findFragmentById(R.id.fragmentMapa)).getMap();
        this.mapa.clear();

        this.mapa.addMarker(new MarkerOptions().position(this.posicion).title("Estás aquí!"));
        this.mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(this.posicion, 12.0f));
    }
}
