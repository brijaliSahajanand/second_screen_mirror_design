package com.screenmirror.screentv.tvsharingapp.Model;

import androidx.annotation.Nullable;

import java.util.List;

public class VideoFolder {

    int folderid;
    String foldername ;
    int totalvideo;
    String str_path;
    String str_thumb ;
    List<VideoModel> videoList;



    public VideoFolder(int folderid, String foldername, int totalvideo, String str_path, String str_thumb , List<VideoModel> videoList) {
        this.folderid = folderid;
        this.foldername = foldername;
        this.totalvideo = totalvideo;
        this.str_thumb = str_thumb;
        this.videoList = videoList;
        this.str_path = str_path;
    }


    public int getFolderid() {
        return folderid;
    }

    public void setFolderid(int folderid) {
        this.folderid = folderid;
    }

    public String getFoldername() {
        return foldername;
    }

    public void setFoldername(String foldername) {
        this.foldername = foldername;
    }

    public int getTotalvideo() {
        return totalvideo;
    }

    public void setTotalvideo(int totalvideo) {
        this.totalvideo = totalvideo;
    }

    public String getStr_path() {
        return str_path;
    }

    public void setStr_path(String str_path) {
        this.str_path = str_path;
    }

    public String getStr_thumb() {
        return str_thumb;
    }

    public void setStr_thumb(String str_thumb) {
        this.str_thumb = str_thumb;
    }

    public List<VideoModel> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<VideoModel> videoList) {
        this.videoList = videoList;
    }

    @Override
    public boolean equals(@Nullable Object obj) {

        VideoFolder model = (VideoFolder) obj;
        if (this.folderid == (model.getFolderid())) {
            return true;
        } else {
            return false;
        }
    }
}
