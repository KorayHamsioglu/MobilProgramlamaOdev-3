package com.example.mobilprogramlamaodev;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;

import java.util.ArrayList;

public class ChooseActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Song> songList=new ArrayList<>();
    public static Button btnComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);


        recyclerView=findViewById(R.id.recyclerViewChoose);
        btnComplete=findViewById(R.id.buttonComplete);
        if(checkPermission()==false){
            requestPermission();
            return;
        }

        String playlistName=getIntent().getStringExtra("PlaylistName");
        String[] info={

                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,

        };
        String selection=MediaStore.Audio.Media.IS_MUSIC+"!=0";

        Cursor cursor=getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,info,selection,null,MediaStore.Audio.Media.DISPLAY_NAME);
        while(cursor.moveToNext()){

            Song songData=new Song(cursor.getString(0),cursor.getString(1),cursor.getString(2) );
            if(songData.getTitle().contains("mp3")){
                songList.add(songData);
            }

        }
        if(songList.size()==0){

        }else{
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new AdapterChoose(songList,getApplicationContext(),playlistName));
        }


    }




    public boolean checkPermission(){
        int result= ContextCompat.checkSelfPermission(ChooseActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result== PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else{
            return false;
        }
    }
    public void requestPermission(){
        ActivityCompat.requestPermissions(ChooseActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

    }
}