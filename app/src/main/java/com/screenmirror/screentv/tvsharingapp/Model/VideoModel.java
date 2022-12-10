package com.screenmirror.screentv.tvsharingapp.Model;

import androidx.annotation.Nullable;

public class VideoModel {

    int videoid;
    String videoTitle;
    int videoDuration;
    String videoUri;
    String foldername;
    int folderid;
    String str_path;
    String str_thumb;
    String filepath;
    String resolution;
    String dateadded;
    String filesize;

    public VideoModel() {
    }

    public VideoModel(int videoid, String videoTitle, int videoDuration, String videoUri,
                      String foldername, int folderid, String str_path,
                      String str_thumb, String filepath, String resolution, String dateadded, String filesize) {
        this.videoid = videoid;
        this.videoTitle = videoTitle;
        this.videoDuration = videoDuration;
        this.videoUri = videoUri;
        this.foldername = foldername;
        this.folderid = folderid;
        this.str_path = str_path;
        this.str_thumb = str_thumb;
        this.filepath = filepath;
        this.resolution = resolution;
        this.dateadded = dateadded;
        this.filesize = filesize;

    }

    public int getVideoid() {
        return videoid;
    }

    public void setVideoid(int videoid) {
        this.videoid = videoid;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public int getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(int videoDuration) {
        this.videoDuration = videoDuration;
    }

    public String getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(String videoUri) {
        this.videoUri = videoUri;
    }

    public String getFoldername() {
        return foldername;
    }

    public void setFoldername(String foldername) {
        this.foldername = foldername;
    }

    public int getFolderid() {
        return folderid;
    }

    public void setFolderid(int folderid) {
        this.folderid = folderid;
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

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getDateadded() {
        return dateadded;
    }

    public void setDateadded(String dateadded) {
        this.dateadded = dateadded;
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    @Override
    public boolean equals(@Nullable Object obj) {

        VideoModel model = (VideoModel) obj;
        if (this.videoid == model.getVideoid()) {
            return true;
        } else {
            return false;
        }
    }

}
