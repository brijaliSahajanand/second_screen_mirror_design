package com.screenmirror.screentv.tvsharingapp.Model;

import androidx.annotation.Nullable;

import java.util.List;

public class Folder {

    String name;
    int songcount;
    String path ;
    List<Song> songList;

    public Folder(String name, int songcount, String path, List<Song> songList) {
        this.name = name;
        this.songcount = songcount;
        this.path = path;
        this.songList = songList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSongcount() {
        return songcount;
    }

    public void setSongcount(int songcount) {
        this.songcount = songcount;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<Song> getSongList() {
        return songList;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

    @Override
    public boolean equals(@Nullable Object obj) {

        Folder model = (Folder) obj;
        if (this.name.equals(model.getName())) {
            return true;
        } else {
            return false;
        }
    }
}
