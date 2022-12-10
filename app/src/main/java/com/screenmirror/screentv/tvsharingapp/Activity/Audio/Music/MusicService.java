package com.screenmirror.screentv.tvsharingapp.Activity.Audio.Music;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.NonNull;

import com.screenmirror.screentv.tvsharingapp.Model.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MusicService extends Service {

    public static final String TWO_ACTION_PLAY = "play";
    public static final String TWO_ACTION_PAUSE = "pause";
    public static final String TWO_ACTION_SHUFFLE = "shuffle";
    public static final String TWO_ACTION_NEXT = "Next";
    public static final String TWO_ACTION_PREVIOUS = "previous";
    public static final String TWO_ACTION_REPEAT = "repeat";
    public static final String TWO_ACTION = "action";
    private static final int DEFAULT_POSITION = 0;
    private static final String TAG = MusicService.class.getSimpleName();
    List<Song> songList = new ArrayList<>();
    Random random = new Random();
    MediaPlayer mediaPlayer;
    private int resumePosition = DEFAULT_POSITION;
    private AudioManager audioManager;
    Song song;

    private final IBinder mIBinder = new LocalBinder();

    private MediaPlayerHolder mMediaPlayerHolder;

    private MusicNotificationManager mMusicNotificationManager;

    private boolean sRestoredFromPause = false;

    public final boolean isRestoredFromPause() {
        return sRestoredFromPause;
    }

    public void setRestoredFromPause(boolean restore) {
        sRestoredFromPause = restore;
    }

    public final MediaPlayerHolder getMediaPlayerHolder() {
        return mMediaPlayerHolder;
    }

    public MusicNotificationManager getMusicNotificationManager() {
        return mMusicNotificationManager;
    }

    @Override
    public int onStartCommand(@NonNull final Intent intent, final int flags, final int startId) {

       /* if (intent.hasExtra(TWO_ACTION)) {
            //Request audio focus

            if (mMediaPlayerHolder != null) {
                songList = getSongList(getApplicationContext());

                String currentsong = SessionManagement.getStringValue(getApplicationContext(), AppConstant.SELECTED_SONG, "");

                Song song = new Gson().fromJson(currentsong, Song.class);


                String action = intent.getStringExtra(TWO_ACTION);
                Log.d(TAG, action);
                check(song);
                switch (action) {

                    case TWO_ACTION_PLAY:

                        mMediaPlayerHolder.setCurrentSong(song, songList);
                        mMediaPlayerHolder.initMediaPlayer(song);
                        SessionManagement.savePreferences(getApplicationContext(), AppConstant.SELECTED_SONG, new Gson().toJson(song));

                        Log.e(TAG, "onStartCommand: Play");

                        break;
                    case TWO_ACTION_PAUSE:
//                    pauseMedia();
                        mMediaPlayerHolder.resumeOrPause();
                        SessionManagement.savePreferences(getApplicationContext(), AppConstant.SELECTED_SONG, new Gson().toJson(mMediaPlayerHolder.getCurrentSong()));

                        Log.e(TAG, "onStartCommand: pause");
                        break;
                    case TWO_ACTION_SHUFFLE:
                        song = getRandomAudio(songList);
                        SessionManagement.savePreferences(getApplicationContext(), AppConstant.SELECTED_SONG, new Gson().toJson(song));
                        mMediaPlayerHolder.setCurrentSong(song, songList);
                        mMediaPlayerHolder.initMediaPlayer(song);
                        Log.e(TAG, "onStartCommand: Shuffle");
                        break;

                    case TWO_ACTION_NEXT:
                        mMediaPlayerHolder.skip(true);
                        SessionManagement.savePreferences(getApplicationContext(), AppConstant.SELECTED_SONG, new Gson().toJson(song));

//                    pauseMedia();
//                    getSkipSong(true ,ACTION_PLAY);
                        break;
                    case TWO_ACTION_PREVIOUS:
                        mMediaPlayerHolder.skip(false);
                        SessionManagement.savePreferences(getApplicationContext(), AppConstant.SELECTED_SONG, new Gson().toJson(song));

//                    pauseMedia();
//                    getSkipSong(false ,ACTION_PLAY);
                        break;
                    case TWO_ACTION_REPEAT:
                        mMediaPlayerHolder.instantReset();
                        SessionManagement.savePreferences(getApplicationContext(), AppConstant.SELECTED_SONG, new Gson().toJson(song));

                        break;

                    default:
                        stopSelf();
                }
                updateWidget(song, action);
            }else {

                initMediaPlayer();


                String currentsong = SessionManagement.getStringValue(getApplicationContext(), AppConstant.SELECTED_SONG, "");

                song = new Gson().fromJson(currentsong, Song.class);


                String action = intent.getStringExtra(TWO_ACTION);
                Log.d(TAG, action);
                switch (action) {
                    case TWO_ACTION_PLAY:
                        prepareMediaPlayerWith(song);
                        Log.e(TAG, "onStartCommand: Play");

                        break;
                    case TWO_ACTION_PAUSE:
                        pauseMedia();
                        Log.e(TAG, "onStartCommand: pause");
                        break;
                    case TWO_ACTION_SHUFFLE:
                        song = getRandomAudio(songList);
                        SessionManagement.savePreferences(getApplicationContext(), AppConstant.SELECTED_SONG, new Gson().toJson(song));
                        prepareMediaPlayerWith(song);
                        Log.e(TAG, "onStartCommand: Shuffle");
                        break;

                    case TWO_ACTION_NEXT:
                        pauseMedia();
                        getSkipSong(true, TWO_ACTION_PLAY);
                        break;
                    case TWO_ACTION_PREVIOUS:
                        pauseMedia();
                        getSkipSong(false, TWO_ACTION_PLAY);
                        break;
                    case TWO_ACTION_REPEAT:
                        pauseMedia();
                        String currentsong1 = SessionManagement.getStringValue(getApplicationContext(), AppConstant.SELECTED_SONG, "");
                        song = new Gson().fromJson(currentsong1, Song.class);
                        prepareMediaPlayerWith(song);
                        break;

//                    case TWO_ACTION_LISTVIEW:
//                        pauseMedia();
//                        int postion = intent.getIntExtra(EXTRA_ITEM_POSITION , 0);
//                        SessionManagement.savePreferences(getApplicationContext(), AppConstant.SELECTED_SONG, new Gson().toJson(audioList.get(postion)));
//                        prepareMediaPlayerWith(audioList.get(postion));
//                        Log.e(TAG, "onStartCommand: LISTVIEW");
//                        updateWidget(song, ACTION_PLAY);
//
//                        break;

                    default:
                        stopSelf();
                }


                updateWidget(song, action);

            }
        }*/
//        } else {
//            stopSelf();
//        }

        return START_NOT_STICKY;

    }

    @Override
    public void onDestroy() {
//        mMediaPlayerHolder.updateWidget(mMediaPlayerHolder.getCurrentSong(), ACTION_PAUSE);
        mMediaPlayerHolder.registerNotificationActionsReceiver(false);
        mMusicNotificationManager = null;
        mMediaPlayerHolder.release();

        super.onDestroy();
    }

    @Override
    public IBinder onBind(@NonNull final Intent intent) {
        if (mMediaPlayerHolder == null) {
            mMediaPlayerHolder = new MediaPlayerHolder(this);
            mMusicNotificationManager = new MusicNotificationManager(this);
            mMediaPlayerHolder.registerNotificationActionsReceiver(true);
        }
        return mIBinder;
    }

    public class LocalBinder extends Binder {
        public MusicService getInstance() {
            return MusicService.this;
        }
    }
}