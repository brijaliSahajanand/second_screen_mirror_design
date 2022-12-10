package com.screenmirror.screentv.tvsharingapp.Model;

import android.content.Context;

import androidx.annotation.NonNull;

import com.screenmirror.screentv.tvsharingapp.R;

import java.util.ArrayList;
import java.util.List;


public class Album {

    private List<Song> mSongs;

    private int mAlbumPosition;

    String album_id , album_name , album_artist , album_art;
    int no_songs ;

    public Album(String album_id, String album_name, String album_artist, String album_art , int no_songs) {
        this.album_id = album_id;
        this.album_name = album_name;
        this.album_artist = album_artist;
        this.album_art = album_art;
        this.no_songs = no_songs;
    }

    public Album() {
        mSongs = new ArrayList<>();
    }

    public static String getYearForAlbum(@NonNull final Context context, final int year) {
        return year != 0 && year != -1 ? String.valueOf(year) : context.getString(R.string.unknown_year);
    }

    public final int getAlbumPosition() {
        return mAlbumPosition;
    }

    public void setAlbumPosition(final int albumPosition) {
        mAlbumPosition = albumPosition;
    }

    public final List<Song> getSongs() {
        return mSongs;
    }

    public final String getTitle() {
        return getFirstSong().getAlbumName();
    }

    public final int getArtistId() {
        return getFirstSong().getArtistId();
    }

    final String getArtistName() {
        return getFirstSong().getArtistName();
    }

    public final int getYear() {
        return getFirstSong().getYear();
    }

    final int getSongCount() {
        return mSongs.size();
    }

    @NonNull
    private Song getFirstSong() {
        return mSongs.isEmpty() ? Song.EMPTY_SONG : mSongs.get(0);
    }


    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public String getAlbum_artist() {
        return album_artist;
    }

    public void setAlbum_artist(String album_artist) {
        this.album_artist = album_artist;
    }

    public String getAlbum_art() {
        return album_art;
    }

    public void setAlbum_art(String album_art) {
        this.album_art = album_art;
    }

    public int getNo_songs() {
        return no_songs;
    }

    public void setNo_songs(int no_songs) {
        this.no_songs = no_songs;
    }
}
