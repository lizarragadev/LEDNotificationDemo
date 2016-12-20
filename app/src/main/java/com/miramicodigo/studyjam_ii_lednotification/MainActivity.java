package com.miramicodigo.studyjam_ii_lednotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnIniciar;
    private Button btnDetener;

    private NotificationCompat.Builder notiBuilder;
    private NotificationManager nm;
    private View view;
    private int color;
    private int posicion = 0;
    private ColorBlink colorBlink;
    //Tendrias que hacer un juego de frecuencias de la cancion que reproduciras, por tiempos... xD
    private long intervalo[] = {400, 600, 800, 200, 500, 1000, 200, 500};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = getCurrentFocus();

        btnIniciar = (Button) findViewById(R.id.btnIniciar);
        btnDetener = (Button) findViewById(R.id.btnDetener);

        btnIniciar.setOnClickListener(this);
        btnDetener.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnIniciar:
                iniciar();
                break;
            case R.id.btnDetener:
                detener();
                break;
        }
    }

    public void iniciar() {
        Toast.makeText(getApplicationContext(), "Bloquee el movil", Toast.LENGTH_LONG).show();
        nm = (NotificationManager) MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
        notiBuilder = new NotificationCompat.Builder(MainActivity.this);
        notiBuilder.setContentTitle("Notificacion LED")
                .setContentText("Bloquee la pantalla")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.ic_launcher);
        colorBlink = new ColorBlink();
        colorBlink.execute();
    }

    public int generaColor() {
        return Color.rgb((int) (Math.random() * 255) + 1, (int) (Math.random() * 255) + 1, (int) (Math.random() * 255) + 1);
    }

    private class ColorBlink extends AsyncTask<Object, Object, Boolean> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(Object... arg0) {
            while (!colorBlink.isCancelled()) {
                color = generaColor();
                try {
                    if (posicion == intervalo.length - 1) {
                        posicion =0;
                    }
                    Thread.sleep(intervalo[posicion]);
                    posicion++;
                    notiBuilder.setLights(color, 500, 500);
                    Notification notification = notiBuilder.build();
                    nm.notify(1, notification);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
        }
    }

    public void detener() {
        nm.cancelAll();
        colorBlink.cancel(true);
    }
}
