package com.wear.ejemplo.proyectonotificaciones;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat.Builder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void notificando(View view)
    {
        TextView text = (TextView) findViewById(R.id.textViewNotificacion);
        text.setText("Notificación enviada");
        notificacion();
    }

    public void notificacion()
    {
        int notificationId = 001;

        Intent viewIntent = new Intent(this, MainActivity.class);
        viewIntent.putExtra("Aviso distancia", 1);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);
        Intent i = new Intent(MainActivity.this, actionActivity.class);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, i, 0);

        NotificationCompat.Builder notificationBuilder =
                (Builder) new Builder(this)
                        .setSmallIcon(R.drawable.notification_template_icon_bg)
                        .setContentTitle("NOTIFICANDO A WEAR")
                        .setContentText("Pulsa el botón para realizar acción")
                        .setContentIntent(viewPendingIntent)
                        .addAction(R.drawable.abc_btn_radio_material ,"Acción a desarrollar",pendingIntent);


        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

        notificationManager.notify(notificationId, notificationBuilder.build());
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
