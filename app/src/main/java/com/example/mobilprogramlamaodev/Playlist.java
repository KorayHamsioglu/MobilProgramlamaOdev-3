package com.example.mobilprogramlamaodev;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class Playlist extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Song> songList=new ArrayList<>();
    Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);


        recyclerView=findViewById(R.id.recyclerView);
        if(checkPermission()==false){
            requestPermission();
            return;
        }

        String[] info={

               MediaStore.Audio.Media.DATA,
               MediaStore.Audio.Media.DISPLAY_NAME,
               MediaStore.Audio.Media.DURATION,

       };
        String selection=MediaStore.Audio.Media.IS_MUSIC+"!=0";
        if(getIntent().hasExtra("playlistObject")){
            PlaylistContent playlistContent= (PlaylistContent) getIntent().getSerializableExtra("playlistObject");
            songList=playlistContent.songList;

        }
       else {
            Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, info, selection, null, MediaStore.Audio.Media.DISPLAY_NAME);
            while (cursor.moveToNext()) {
                Song songData = new Song(cursor.getString(0), cursor.getString(1), cursor.getString(2));
                if (songData.getTitle().contains("mp3")){
                    songList.add(songData);
                }

            }
        }
        if(songList.size()==0){
            Toast.makeText(getApplicationContext(),"No mp3 files in storage",Toast.LENGTH_SHORT).show();
        }else{
          adapter=new Adapter(this,songList,getApplicationContext());
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        }
    }

    public boolean checkPermission(){
       int result= ContextCompat.checkSelfPermission(Playlist.this, Manifest.permission.READ_EXTERNAL_STORAGE);
       if (result==PackageManager.PERMISSION_GRANTED){
           return true;
       }
       else{
           return false;
       }
    }
    public void requestPermission(){
        ActivityCompat.requestPermissions(Playlist.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

    }


}