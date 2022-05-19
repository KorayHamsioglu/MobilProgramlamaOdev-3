package com.example.mobilprogramlamaodev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class SingleSongActivity extends AppCompatActivity {

    private TextView txtSongName,txtDuration;
    private Button btnPlay,btnPause,btnStop,btnNext,btnPrevious;
    private SeekBar seekBar;
    private ImageView imageViewAlbum;
    private boolean isStop=false;
    ArrayList<Song> songList;
    Song song;
    MediaPlayer mediaPlayer=MyMediaPlayer.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_song);

        IntentFilter intentFilter=new IntentFilter("com.Sensor.stop");
        BroadcastReciever broadcastReciever=new BroadcastReciever();
        registerReceiver(broadcastReciever,intentFilter);

        IntentFilter intentFilter1=new IntentFilter("com.Sensor.move");
        BroadcastReciever broadcastReciever1=new BroadcastReciever();
        registerReceiver(broadcastReciever1,intentFilter1);




        txtSongName=findViewById(R.id.txtViewSongName);
        txtDuration=findViewById(R.id.txtDuration);
        btnPlay=findViewById(R.id.buttonPlay);
        btnPause=findViewById(R.id.buttonPause);
        btnStop=findViewById(R.id.buttonStop);
        btnNext=findViewById(R.id.buttonNext);
        btnPrevious=findViewById(R.id.buttonPrevious);
        imageViewAlbum=findViewById(R.id.imageViewAlbum);

        seekBar=findViewById(R.id.seekBar);

        songList= (ArrayList<Song>) getIntent().getSerializableExtra("LIST");
        setMusicInfo();




        SingleSongActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                }
                new Handler().postDelayed(this,50);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(mediaPlayer!=null && b==true){
                    mediaPlayer.seekTo(i);
                    
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if (isStop){

                   try {
                       mediaPlayer.setDataSource(song.getData());
                       mediaPlayer.prepare();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
                   mediaPlayer.start();
                   seekBar.setMax(mediaPlayer.getDuration());
                   isStop=false;
               }
               else{
                   mediaPlayer.start();

                   seekBar.setMax(mediaPlayer.getDuration());
               }



            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MyMediaPlayer.currentIndex==songList.size()-1){
                    MyMediaPlayer.currentIndex=0;
                    mediaPlayer.reset();
                    setMusicInfo();
                    isStop=false;
                }
                else{
                    MyMediaPlayer.currentIndex=MyMediaPlayer.currentIndex+1;
                    mediaPlayer.reset();
                    setMusicInfo();
                    isStop=false;
                }
            }
        });


        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyMediaPlayer.currentIndex==0){
                    MyMediaPlayer.currentIndex=songList.size()-1;
                    mediaPlayer.reset();
                    setMusicInfo();
                    isStop=false;
                }
                else{
                    MyMediaPlayer.currentIndex=MyMediaPlayer.currentIndex-1;
                    mediaPlayer.reset();
                    setMusicInfo();
                    isStop=false;
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    seekBar.setProgress(0);
                     isStop=true;

                }
                else{
                    seekBar.setProgress(0);
                    isStop=true;
                }
                mediaPlayer.reset();


            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
            }
        });




    }


    public void setMusicInfo(){
       song=songList.get(MyMediaPlayer.currentIndex);

       txtSongName.setText(song.getTitle());
       txtDuration.setText(convertDuration(song.getDuration()));
        MediaMetadataRetriever mediaMetadataRetriever=new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(song.getData());
        byte[] image=mediaMetadataRetriever.getEmbeddedPicture();
        Bitmap bitmap= BitmapFactory.decodeByteArray(image,0,image.length);
        imageViewAlbum.setImageBitmap(bitmap);

        try {
            mediaPlayer.setDataSource(song.getData());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.reset();
    }

    public static String convertDuration(String duration){
        Long sec=Long.parseLong(duration);
        return String.format("%02d"+":"+"%02d", TimeUnit.MILLISECONDS.toMinutes(sec)%TimeUnit.HOURS.toMinutes(1),
                                        TimeUnit.MILLISECONDS.toSeconds(sec)%TimeUnit.MINUTES.toSeconds(1) );

    }


}