package com.example.mobilprogramlamaodev;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.Toast;

public class BroadcastReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        AudioManager audioManager= (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        if (intent.getAction().equals("com.Sensor.move")){
            audioManager.adjustVolume(AudioManager.ADJUST_UNMUTE,AudioManager.FLAG_PLAY_SOUND);
            Toast.makeText(context,"Unmute et",Toast.LENGTH_SHORT).show();
        }
       else if (intent.getAction().equals("com.Sensor.stop")){
            audioManager.adjustVolume(AudioManager.ADJUST_MUTE,AudioManager.FLAG_PLAY_SOUND);
            Toast.makeText(context,"mute et",Toast.LENGTH_SHORT).show();

        }


    }
}