package com.example.mobilprogramlamaodev;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PlaylistsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private Button btnCreate;
    private EditText editTextPlaylistName;
    ArrayList<PlaylistContent> playlistContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlists);

        btnCreate=findViewById(R.id.buttonCreate);
        editTextPlaylistName=findViewById(R.id.editTextPlaylistName);
        recyclerView=findViewById(R.id.recyclerViewPlaylists);
        loadData();
       if(getIntent().hasExtra("object")){
           PlaylistContent playlistContent= (PlaylistContent) getIntent().getSerializableExtra("object");
           playlistContents.add(playlistContent);

       }

      if(playlistContents.size()!=0){
          recyclerView.setLayoutManager(new LinearLayoutManager(this));
          recyclerView.setAdapter(new AdapterPlaylist(playlistContents,getApplicationContext()));
      }
        saveData();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PlaylistsActivity.this,ChooseActivity.class);
                intent.putExtra("PlaylistName",editTextPlaylistName.getText().toString());
                startActivity(intent);
            }
        });



    }

    public void saveData(){
        SharedPreferences sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        Gson gson=new Gson();
        String data=gson.toJson(playlistContents);
        editor.putString("playlists",data);
        editor.apply();


    }

    public void loadData(){

        SharedPreferences sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
        Gson gson=new Gson();
        String data=sharedPreferences.getString("playlists",null);
        Type type=new TypeToken<ArrayList<PlaylistContent>>() {}.getType();
        playlistContents=gson.fromJson(data,type);

        if(playlistContents==null){
            playlistContents=new ArrayList<>();
        }

    }
}