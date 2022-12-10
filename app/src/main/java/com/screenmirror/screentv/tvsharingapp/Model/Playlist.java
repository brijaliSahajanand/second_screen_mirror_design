package com.screenmirror.screentv.tvsharingapp.Model;

import java.util.List;

public class Playlist {

    String name;
    List<ItemPlaylist> itemPlaylists;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ItemPlaylist> getItemPlaylists() {
        return itemPlaylists;
    }

    public void setItemPlaylists(List<ItemPlaylist> itemPlaylists) {
        this.itemPlaylists = itemPlaylists;
    }

    @Override
    public boolean equals(Object o) {
        Playlist playlist = (Playlist) o;
        if(this.name.equals(playlist.getName())){
            return true;
        }else {
            return false;
        }
    }

}
