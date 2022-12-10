package com.screenmirror.screentv.tvsharingapp.Activity.Audio.Music;

import android.app.Activity;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.screenmirror.screentv.tvsharingapp.Model.Album;
import com.screenmirror.screentv.tvsharingapp.Model.Song;

import java.util.List;


public interface PlayerAdapter {

    void initMediaPlayer(@NonNull final Song song);

    void setSongForFirstTime(Song song);

    void release();

    boolean isMediaPlayer();

    boolean isPlaying();

    void resumeOrPause();

    void reset();

    boolean isReset();

    void instantReset();

    void repeatsong();

    Song getCurrentSong();

    List<Song> getQuesongList();

    void resetQue();

    String getNavigationArtist();

    void setNavigationArtist(@NonNull final String navigationArtist);

    Album getNavigationAlbum();

    void setNavigationAlbum(@Nullable final Album navigationAlbum);

    void setCurrentSong(@NonNull final Song song, @NonNull final List<Song> songs);

    void skip(final boolean isNext);

    void openEqualizer(@NonNull final Activity activity);

    void seekTo(final int position);

    void setPlaybackInfoListener(final PlaybackInfoListener playbackInfoListener);

    void setinterfaceSongChagne(SongAdapterInterface songAdapterInterface);

    @PlaybackInfoListener.State
    int getState();

    @PlaybackInfoListener.State
    int setStatusForMedia(final @PlaybackInfoListener.State int state);

    int getPlayerPosition();

    void registerNotificationActionsReceiver(final boolean isRegister);

    MediaPlayer getMediaPlayer();

    void onPauseActivity();

    void onResumeActivity();

    int audioSessionId();

    void mute();

    void unmute();

    boolean getmuteornote();
}
