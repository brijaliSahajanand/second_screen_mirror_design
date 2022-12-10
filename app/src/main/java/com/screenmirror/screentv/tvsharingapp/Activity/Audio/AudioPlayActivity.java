package com.screenmirror.screentv.tvsharingapp.Activity.Audio;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.ads.adsdemosp.AdsClass.Ads_Interstitial;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.screenmirror.screentv.tvsharingapp.Activity.Audio.Music.EqualizerUtils;
import com.screenmirror.screentv.tvsharingapp.Activity.Audio.Music.MusicNotificationManager;
import com.screenmirror.screentv.tvsharingapp.Activity.Audio.Music.MusicService;
import com.screenmirror.screentv.tvsharingapp.Activity.Audio.Music.PlaybackInfoListener;
import com.screenmirror.screentv.tvsharingapp.Activity.Audio.Music.PlayerAdapter;
import com.screenmirror.screentv.tvsharingapp.Activity.BaseActivity;
import com.screenmirror.screentv.tvsharingapp.ExtraClass.GetData;
import com.screenmirror.screentv.tvsharingapp.Model.Song;
import com.screenmirror.screentv.tvsharingapp.R;


import java.util.ArrayList;
import java.util.List;

public class AudioPlayActivity extends BaseActivity {

    //View
    ImageView iv_audioplay;


    ImageView iv_back, shuffle, previous, play, next, repeat;
    TextView tv_full_songname, tv_full_artistname, time_left, time_duration;
    SeekBar seekTo2;
    LinearLayout ll_ads_container;
    LinearLayout lladview;
    TextView txt_ads_load;

    //variable
    private boolean sBound;
    List<Song> songlist = new ArrayList<>();
    int position;
    Animation animation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_play);

        Declaration();
        Intialization();
    }

    private void Declaration() {
        iv_audioplay = findViewById(R.id.iv_audioplay);

        iv_back = findViewById(R.id.iv_back);
        shuffle = findViewById(R.id.shuffle);
        previous = findViewById(R.id.previous);
        play = findViewById(R.id.play);
        next = findViewById(R.id.next);
        repeat = findViewById(R.id.repeat);
        tv_full_songname = findViewById(R.id.tv_full_songname);
        tv_full_artistname = findViewById(R.id.tv_full_artistname);
        time_left = findViewById(R.id.time_left);
        time_duration = findViewById(R.id.time_duration);
        seekTo2 = findViewById(R.id.seekTo2);


    }

    private void Intialization() {

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        animation.setFillAfter(true);


        initiaSeekbar();
        doBindService();
        if (getIntent() != null) {
            songlist = new Gson().fromJson(getIntent().getStringExtra(GetData.songlist), new TypeToken<List<Song>>() {
            }.getType());
            position = getIntent().getIntExtra(GetData.position, 0);

            Log.d("playsong", "Initization: ");

            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (mPlayerAdapter == null) {

                        handler.postDelayed(this, 10);
                    } else {
                        Log.d("playsong", "Initization: set ");
                        if (!seekTo2.isEnabled()) {
                            seekTo2.setEnabled(true);
                        }

                        mPlayerAdapter.setCurrentSong(songlist.get(position), songlist);
                        mPlayerAdapter.initMediaPlayer(songlist.get(position));

                        handler.removeCallbacks(this);

                    }

                }
            };
            runnable.run();


            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkIsPlayer()) {
                        mPlayerAdapter.resumeOrPause();
                    }
                }
            });

            previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkIsPlayer()) {
                        mPlayerAdapter.instantReset();
                        if (mPlayerAdapter.isReset()) {
                            mPlayerAdapter.reset();
//                            updateResetStatus(false);
                        }
                    }
                }
            });

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (checkIsPlayer()) {
                        mPlayerAdapter.skip(true);
                    }

                }
            });

            shuffle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Song song = new Song();
                    song = GetData.getRandomAudio(songlist);


                    if (!seekTo2.isEnabled()) {
                        seekTo2.setEnabled(true);
                    }

                    mPlayerAdapter.setCurrentSong(song, songlist);
                    mPlayerAdapter.initMediaPlayer(song);


                }
            });

            iv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

        }

    }

    private void initiaSeekbar() {


        seekTo2.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    //                    final int currentPositionColor = mSongPosition.getCurrentTextColor();
                    int userSelectedPosition = 0;

                    @Override
                    public void onStartTrackingTouch(@NonNull final SeekBar seekBar) {
                        mUserIsSeeking = true;
                    }

                    @Override
                    public void onProgressChanged(@NonNull final SeekBar seekBar, final int progress, final boolean fromUser) {
                        if (fromUser) {
                            userSelectedPosition = progress;

                        }

                        time_left.setText(Song.formatDuration(progress));

                    }

                    @Override
                    public void onStopTrackingTouch(@NonNull final SeekBar seekBar) {
                        if (mUserIsSeeking) {

                        }
                        mUserIsSeeking = false;
                        mPlayerAdapter.seekTo(userSelectedPosition);
                    }
                });
    }


    private boolean checkIsPlayer() {

        boolean isPlayer = mPlayerAdapter.isMediaPlayer();
        if (!isPlayer) {
            EqualizerUtils.notifyNoSessionId(AudioPlayActivity.this);
        }
        return isPlayer;
    }


    //for serviuce

    public MusicService mMusicService;
    public PlayerAdapter mPlayerAdapter;
    public MusicNotificationManager mMusicNotificationManager;
    public PlaybackListener mPlaybackListener;
    private boolean mUserIsSeeking = false;
    int mAccent;
    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(@NonNull final ComponentName componentName, @NonNull final IBinder iBinder) {
            mMusicService = ((MusicService.LocalBinder) iBinder).getInstance();
            mPlayerAdapter = mMusicService.getMediaPlayerHolder();
            mMusicNotificationManager = mMusicService.getMusicNotificationManager();
//            mMusicNotificationManager.setAccentColor(mAccent);

            if (mPlaybackListener == null) {
                mPlaybackListener = new PlaybackListener();
                mPlayerAdapter.setPlaybackInfoListener(mPlaybackListener);
            }

            // Check for necessary Prerequisites
//            initArtistsActivity();
        }

        @Override
        public void onServiceDisconnected(@NonNull final ComponentName componentName) {
            mMusicService = null;
        }
    };


    class PlaybackListener extends PlaybackInfoListener {

        @Override
        public void onPositionChanged(int position) {
            if (!mUserIsSeeking) {
                seekTo2.setProgress(position);
//                seekTo.setProgress(position);

            }
        }

        @Override
        public void onStateChanged(@State int state) {

            updatePlayingStatus();
            if (mPlayerAdapter.getState() != State.RESUMED && mPlayerAdapter.getState() != State.PAUSED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    updatePlayingInfo(false, true);
                }
            }
        }

        @Override
        public void onPlaybackCompleted() {
//            updateResetStatus(true);
        }
    }

    public void updatePlayingStatus() {
        final int drawable1 = mPlayerAdapter.getState() != PlaybackInfoListener.State.PAUSED ? R.drawable.full_pause : R.drawable.full_play;


        if (drawable1 == R.drawable.full_pause) {
            iv_audioplay.startAnimation(animation);
        } else {

            iv_audioplay.clearAnimation();
            animation.cancel();
            animation.reset();

        }

        play.post(() -> play.setImageResource(drawable1));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void updatePlayingInfo(final boolean restore, final boolean startPlay) {


        if (startPlay) {
            mPlayerAdapter.getMediaPlayer().start();
            new Handler().postDelayed(() -> mMusicService.startForeground(MusicNotificationManager.NOTIFICATION_ID, mMusicNotificationManager.createNotification()), 250);
        }

//        if (songAdapterInterface != null) {
//            songAdapterInterface.songChange();
//        }
        final Song selectedSong = mPlayerAdapter.getCurrentSong();

        final int duration = selectedSong.getSongDuration();

        seekTo2.setMax(duration);


        time_duration.setText(Song.formatDuration(duration));
//        final Spanned spanned = Utils.buildSpanned(getString(R.string.playing_song, selectedSong.getArtistName(), selectedSong.getSongTitle()));

        tv_full_songname.post(() -> tv_full_songname.setText(selectedSong.getSongTitle()));
        tv_full_artistname.setText(selectedSong.getArtistName());
//        song.post(() -> song.setText(spanned));

//        Utils.updateTextView(mPlayingAlbum, selectedSong.getAlbumName());


//        Glide.with(this).load(selectedSong.getMart())
//                .apply(new RequestOptions().error(R.drawable.audioplay)).placeholder(R.drawable.audioplay)
//                .into(iv_audioplay);


        List<Song> queSongList = new ArrayList<>();
        queSongList = mPlayerAdapter.getQuesongList();


        if (restore) {
//            mSeekBarAudio.setProgress(mPlayerAdapter.getPlayerPosition());
            seekTo2.setProgress(mPlayerAdapter.getPlayerPosition());
            updatePlayingStatus();
//            updateResetStatus(false);

            new Handler().postDelayed(() -> {
                //stop foreground if coming from pause state
                if (mMusicService.isRestoredFromPause()) {
                    mMusicService.stopForeground(false);
                    mMusicService.getMusicNotificationManager().getNotificationManager().notify(MusicNotificationManager.NOTIFICATION_ID, mMusicService.getMusicNotificationManager().getNotificationBuilder().build());
                    mMusicService.setRestoredFromPause(false);
                }
            }, 250);
        }
    }

    public void doBindService() {
        // Establish a connection with the service.  We use an explicit
        // class name because we want a specific service implementation that
        // we know will be running in our own process (and thus won't be
        // supporting component replacement by other applications).
        bindService(new Intent(this,
                MusicService.class), mConnection, Context.BIND_AUTO_CREATE);
        sBound = true;

        final Intent startNotStickyIntent = new Intent(this, MusicService.class);
        startService(startNotStickyIntent);


    }

    public void doUnbindService() {
        if (sBound) {
            // Detach our existing connection.
            unbindService(mConnection);
            /*Intent startNotStickyIntent = new Intent(this, MusicService.class);
            stopService(startNotStickyIntent);*/
            sBound = false;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mPlayerAdapter != null && mPlayerAdapter.isMediaPlayer()) {
            mPlayerAdapter.onResumeActivity();

        }
    }


    @Override
    public void onPause() {
        super.onPause();

        if (mPlayerAdapter != null && mPlayerAdapter.isMediaPlayer()) {
            mPlayerAdapter.onPauseActivity();
        }


    }

    @Override
    public void onStop() {


        try {

        } catch (Exception e) {
            // Receiver was probably already
        }
        super.onStop();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlaybackListener = null;
        doUnbindService();

//        stopService(new Intent(this, BroadcastService.class));
    }

    @Override
    public void onBackPressed() {
        Ads_Interstitial.BackshowAds_full(this, new Ads_Interstitial.OnFinishAds() {
            @Override
            public void onFinishAds(boolean b) {
                finish();
            }
        });
    }
}