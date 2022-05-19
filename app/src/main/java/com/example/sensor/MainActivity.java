package com.example.sensor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    Sensor sensorLight,sensorAccelerator;
    SensorManager sensorManager;
    float light,acceleratorX,acceleratorY,acceleratorZ;
    double totalAcceleration;
    TextView textViewAccelarator,textViewLight;
    String hareketDurumu="a";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewAccelarator=findViewById(R.id.textViewAccelarator);
        textViewLight=findViewById(R.id.textViewLight);

        sensorManager= (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorLight=sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorAccelerator=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


    }

    @Override
    public void onResume(){
        super.onResume();
        sensorManager.registerListener(this,sensorLight,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,sensorAccelerator,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause(){
        super.onPause();

        sensorManager.unregisterListener(this);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
                 createNotificationChannel();

                if(sensorEvent.sensor.getType()==Sensor.TYPE_LIGHT){
                    light=sensorEvent.values[0];
                    textViewLight.setText(String.valueOf(light));
                }
                if (sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
                    acceleratorX=sensorEvent.values[0];
                    acceleratorY=sensorEvent.values[1];
                    acceleratorZ=sensorEvent.values[2];
                    double newValue=(double) (acceleratorX*acceleratorX+acceleratorY*acceleratorY+acceleratorZ*acceleratorZ);
                    totalAcceleration=Math.sqrt(newValue);
                    textViewAccelarator.setText(String.valueOf(totalAcceleration));

                }

                if (!hareketDurumu.equals("masadavehareketsiz") && light>50 && (totalAcceleration<9.81)){
                    Intent intent=new Intent();
                    intent.setAction("com.Sensor.stop");
                    intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                    hareketDurumu="masadavehareketsiz";

                    sendBroadcast(intent);
                    NotificationManagerCompat notificationManager= NotificationManagerCompat.from(getApplicationContext());
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "CHANNEL_ID")
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentTitle("Sensor Bilgisi")
                            .setContentText("Telefon Masada ve Hareketsiz")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                    notificationManager.notify(1, builder.build());


                }
                else if(!hareketDurumu.equals("ceptevehareketsiz") && light<50 && ( totalAcceleration<9.81)){
                    Intent intent=new Intent();
                    intent.setAction("com.Sensor.stop");
                    intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                    hareketDurumu="ceptevehareketsiz";

                    sendBroadcast(intent);
                    NotificationManagerCompat notificationManager= NotificationManagerCompat.from(getApplicationContext());
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "CHANNEL_ID")
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentTitle("Sensor Bilgisi")
                            .setContentText("Telefon Cepte ve Hareketsiz")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                    notificationManager.notify(1, builder.build());



                }
                else if (( totalAcceleration>=9.81)&& !hareketDurumu.equals("ceptevehareketli")){
                    Intent intent=new Intent();
                    intent.setAction("com.Sensor.move");
                    intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                    hareketDurumu="ceptevehareketli";
                    sendBroadcast(intent);
                    if(light<50){
                        NotificationManagerCompat notificationManager= NotificationManagerCompat.from(getApplicationContext());
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "CHANNEL_ID")
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setContentTitle("Sensor Bilgisi")
                                .setContentText("Telefon Cepte ve Hareketli")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                        notificationManager.notify(1, builder.build());

                    }



                }

            }




    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", "Channel", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("SensorState");
            channel.setSound(null,null);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }

    }
}