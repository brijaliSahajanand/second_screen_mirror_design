package com.screenmirror.screentv.tvsharingapp.Model;

public class ItemPlaylist {

    boolean isVideo;
    int id;
    VideoModel videoModel;
    Song song;


    public boolean isVideo() {
        return isVideo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public VideoModel getVideoModel() {
        return videoModel;
    }

    public void setVideoModel(VideoModel videoModel) {
        this.videoModel = videoModel;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }


    @Override
    public boolean equals(Object o) {
        ItemPlaylist itemPlaylist = (ItemPlaylist) o;
        if (this.id == itemPlaylist.getId()) {
            return true;
        } else {
            return false;
        }

    }


}
