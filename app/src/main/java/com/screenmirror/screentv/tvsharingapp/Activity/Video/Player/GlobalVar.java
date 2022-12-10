package com.screenmirror.screentv.tvsharingapp.Activity.Video.Player;


import com.google.android.exoplayer2.SimpleExoPlayer;
import com.screenmirror.screentv.tvsharingapp.Activity.Video.Player.videoService.VideoService;
import com.screenmirror.screentv.tvsharingapp.Model.VideoModel;

import java.util.ArrayList;
import java.util.List;

public class GlobalVar {
    private static final GlobalVar ourInstance = new GlobalVar();

    public static GlobalVar getInstance() {
        return ourInstance;
    }

    private GlobalVar() {
    }


    public VideoModel playingVideo;
    public VideoService videoService;
    public boolean isMutilSelectEnalble = false;
    public long seekPosition = 0;
    public boolean isPlaying = true;
    public boolean isNeedRefreshFolder = false;
    public boolean isOpenFromIntent = false;

    public List<VideoModel> videoItemsPlaylist = new ArrayList<>();
    public ArrayList<VideoModel> allVideoItems = new ArrayList<>();
//    public FolderItem folderItem;


    public boolean isSeekBarProcessing = false;

    public String getPath() {
        if (playingVideo != null)
            return playingVideo.getFilepath();
        return "77777777777";
    }

    public SimpleExoPlayer getPlayer() {
        if (videoService == null)
            return null;
        return videoService.getVideoPlayer();
    }

    public boolean isPlayingAsPopup() {
        if (videoService == null) return false;
        return videoService.isPlayingAsPopup();
    }

    public void playNext() {
        if (videoService == null) return;
        videoService.handleAction(VideoService.NEXT_ACTION);
    }

    public void playPrevious() {
        if (videoService == null) return;
        videoService.handleAction(VideoService.PREVIOUS_ACTION);
    }

    public void closeNotification() {
        if (videoService == null) return;
        videoService.closeBackgroundMode();
    }

    public void openNotification() {

        if (videoService == null) return;
        videoService.openBackgroundMode();
    }

    public void pausePlay() {
        if (videoService == null) return;
        videoService.handleAction(VideoService.TOGGLEPAUSE_ACTION);
    }

    public void pause() {
        if (videoService != null) {
//            if (videoService.isPlaying()) {
            videoService.StopNotification();
//            }
        }

    }

    public void createShuffle() {
        if (videoService == null) return;
        videoService.createShuffleArray();
    }

    public int getCurrentPosition() {
        if (videoService == null) return -1;
        return videoService.getCurrentPosition();
    }

}
