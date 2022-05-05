package com.example.mobilprogramlamaodev;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterChoose extends RecyclerView.Adapter<AdapterChoose.ViewHolder> {


    ArrayList<Song> songList;
    ArrayList<Song> playlistSongs=new ArrayList<>();
    Context context;
    String name;
    Button btnComplete;



    public AdapterChoose(ArrayList<Song> songList, Context context,String name) {
        this.songList = songList;
        this.context = context;
        this.name=name;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.item_add,parent,false);

        return new AdapterChoose.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

           Song songData=songList.get(position);
           holder.textViewSongName.setText(songData.getTitle());
           holder.btnAdd.setVisibility(View.VISIBLE);


        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playlistSongs.add(songData);
                holder.btnAdd.setVisibility(View.INVISIBLE);


            }
        });
        ChooseActivity.btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaylistContent playlistContent=new PlaylistContent(playlistSongs,name);
                Intent intent=new Intent(context,PlaylistsActivity.class);
                intent.putExtra("object",playlistContent);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

      /*  holder.btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });*/




    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewSong;
        TextView textViewSongName;
        Button btnAdd,btnComplete;

        public ViewHolder(View itemView) {
            super(itemView);
            imageViewSong=itemView.findViewById(R.id.imageViewSong);
            textViewSongName=itemView.findViewById(R.id.textViewSongName);
            btnAdd=itemView.findViewById(R.id.buttonAdd);







        }

    }


}
