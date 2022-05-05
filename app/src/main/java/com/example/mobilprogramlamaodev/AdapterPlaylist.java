package com.example.mobilprogramlamaodev;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AdapterPlaylist extends RecyclerView.Adapter<AdapterPlaylist.ViewHolder> {

    ArrayList<PlaylistContent> playlistContents;
    Context context;

    public AdapterPlaylist(ArrayList<PlaylistContent> playlistContents, Context context) {
        this.playlistContents = playlistContents;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_playlist,parent,false);
        return new AdapterPlaylist.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlaylistContent playlistContent=playlistContents.get(position);
        holder.textViewPlaylist.setText(playlistContent.getPlaylistName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,Playlist.class);
                intent.putExtra("playlistObject",playlistContent);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.btnDeleteFromPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               SharedPreferences sharedPreferences= context.getSharedPreferences("data",Context.MODE_PRIVATE);
               SharedPreferences.Editor editor=sharedPreferences.edit();
               Gson gson=new Gson();
                String data=sharedPreferences.getString("playlists",null);
                Type type=new TypeToken<ArrayList<PlaylistContent>>() {}.getType();
                playlistContents=gson.fromJson(data,type);
                playlistContents.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyDataSetChanged();
                String dataPut=gson.toJson(playlistContents);
                editor.putString("playlists",dataPut);
                editor.apply();


            }
        });

    }

    @Override
    public int getItemCount() {
        return playlistContents.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textViewPlaylist;
        Button btnDeleteFromPlaylist;


        public ViewHolder(View itemView) {
            super(itemView);
           textViewPlaylist=itemView.findViewById(R.id.textViewPlaylist);
           btnDeleteFromPlaylist=itemView.findViewById(R.id.buttonDeleteFromPlaylist);



        }
    }
}
