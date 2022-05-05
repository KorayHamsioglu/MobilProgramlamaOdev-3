package com.example.mobilprogramlamaodev;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;

public class PlaylistContent implements Serializable {

    ArrayList<Song> songList;
    String PlaylistName;


    public PlaylistContent(ArrayList<Song> songList, String playlistName) {
        this.songList = songList;
        this.PlaylistName = playlistName;
    }


    public ArrayList<Song> getSongList() {
        return songList;
    }

    public void setSongList(ArrayList<Song> songList) {
        this.songList = songList;
    }

    public String getPlaylistName() {
        return PlaylistName;
    }

    public void setPlaylistName(String playlistName) {
        PlaylistName = playlistName;
    }
}
