package com.example.mobilprogramlamaodev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    private Button btnPlaylist;
    private Button btnAllSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        btnPlaylist=findViewById(R.id.buttonPlaylist);
        btnAllSongs=findViewById(R.id.buttonMedia);


        btnPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MenuActivity.this,PlaylistsActivity.class);
                startActivity(intent);
            }
        });


        btnAllSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MenuActivity.this,Playlist.class);
                startActivity(intent);
            }
        });
    }
}