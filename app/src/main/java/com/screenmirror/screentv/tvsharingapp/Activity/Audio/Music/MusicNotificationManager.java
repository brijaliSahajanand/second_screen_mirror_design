package com.screenmirror.screentv.tvsharingapp.Activity.Audio.Music;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.VectorDrawable;
import android.icu.text.CaseMap;
import android.os.Build;
import android.text.Spanned;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.screenmirror.screentv.tvsharingapp.Activity.Splash_Activity;
import com.screenmirror.screentv.tvsharingapp.Model.Song;
import com.screenmirror.screentv.tvsharingapp.R;

import static android.app.PendingIntent.FLAG_IMMUTABLE;


public class MusicNotificationManager {

    public static final int NOTIFICATION_ID = 101;
    static final String PLAY_PAUSE_ACTION = "com.musicplayer.mp3apps.PLAYPAUSE";
    static final String NEXT_ACTION = "com.musicplayer.mp3apps.NEXT";
    static final String PREV_ACTION = "com.musicplayer.mp3apps.PREV";
    private final String CHANNEL_ID = "com.musicplayer.mp3apps.CHANNEL_ID";

    public static int REQUEST_CODE = 100;
    public final NotificationManager mNotificationManager;
    public static MusicService mMusicService;
    public static NotificationCompat.Builder mNotificationBuilder;
    private int mAccent;


    MusicNotificationManager(@NonNull final MusicService musicService) {
        mMusicService = musicService;
        mNotificationManager = (NotificationManager) mMusicService.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void setAccentColor(final int color) {
        mAccent = MusicUtil.getColorFromResource(mMusicService, color, R.color.blue);
    }

    public final NotificationManager getNotificationManager() {
        return mNotificationManager;
    }

    public final NotificationCompat.Builder getNotificationBuilder() {
        return mNotificationBuilder;
    }

    public static PendingIntent playerAction(@NonNull final String action) {

/*
        final Intent pauseIntent = new Intent();
        pauseIntent.setAction(action);

        PendingIntent clickIntent;
        if (Build.VERSION.SDK_INT >= 31) {
            clickIntent = PendingIntent.getActivity(mMusicService,
                    REQUEST_CODE, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT | FLAG_IMMUTABLE);
        } else {
            clickIntent = PendingIntent.getActivity(mMusicService,
                    REQUEST_CODE, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
*/


        final Intent pauseIntent = new Intent();
        pauseIntent.setAction(action);

        PendingIntent clickIntent;
        if (Build.VERSION.SDK_INT >= 31) {
            clickIntent = PendingIntent.getBroadcast(mMusicService,
                    REQUEST_CODE, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT | FLAG_IMMUTABLE);

        } else {
            clickIntent = PendingIntent.getBroadcast(mMusicService,
                    REQUEST_CODE, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        }
        return clickIntent;


        // return clickIntent;
    }


    public Notification createNotification() {
        if (mNotificationBuilder != null) {
            mNotificationBuilder = null;
        }


        final Song song = mMusicService.getMediaPlayerHolder().getCurrentSong();
        mNotificationBuilder = new NotificationCompat.Builder(mMusicService, CHANNEL_ID);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
        final Intent openPlayerIntent = new Intent(mMusicService, Splash_Activity.class);
       /* openPlayerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);*/

       /* final PendingIntent contentIntent = PendingIntent.getActivity(mMusicService, REQUEST_CODE,
                openPlayerIntent, 0);

*/
        PendingIntent contentIntent;
        if (Build.VERSION.SDK_INT >= 31) {
            contentIntent = PendingIntent.getActivity(mMusicService,
                    REQUEST_CODE, openPlayerIntent, PendingIntent.FLAG_UPDATE_CURRENT | FLAG_IMMUTABLE);
        } else {
            contentIntent = PendingIntent.getActivity(mMusicService,
                    REQUEST_CODE, openPlayerIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        String artist = "", songTitle = "";
        if (song.getArtistName() != null) {
            artist = song.getArtistName();
        }
        if (song.getSongTitle() != null) {
            songTitle = song.getSongTitle();
        }
        final Spanned spanned = MusicUtil.buildSpanned(mMusicService.getString(R.string.playing_song, artist, songTitle));
        Bitmap icon = BitmapFactory.decodeResource(mMusicService.getResources(), R.mipmap.ic_launcher);

        mNotificationBuilder
                .setShowWhen(false)
                .setSmallIcon(R.mipmap.ic_launcher)
//                .setLargeIcon(getLargeIcon())
                .setLargeIcon(icon)
                .setColor(mAccent)
                .setContentTitle(spanned)
                .setContentText(song.getAlbumName())
                .setContentIntent(contentIntent)
                .addAction(notificationAction(PREV_ACTION))
                .addAction(notificationAction(PLAY_PAUSE_ACTION))
                .addAction(notificationAction(NEXT_ACTION))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        mNotificationBuilder.setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0, 1, 2));

        return mNotificationBuilder.build();
    }


    @NonNull
    private NotificationCompat.Action notificationAction(@NonNull final String action) {
        int icon;
        switch (action) {
            default:
            case PREV_ACTION:
                icon = R.drawable.ic_skip_previous_notification;
                break;
            case PLAY_PAUSE_ACTION:

                icon = mMusicService.getMediaPlayerHolder().getState() != PlaybackInfoListener.State.PAUSED ?
                        R.drawable.ic_pause_notification : R.drawable.ic_play_notification;
                break;
            case NEXT_ACTION:
                icon = R.drawable.ic_skip_next_notification;
                break;
        }
        return new NotificationCompat.Action.Builder(icon, action, playerAction(action)).build();
    }



    @RequiresApi(26)
    private void createNotificationChannel() {
        if (mNotificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            final NotificationChannel notificationChannel =
                    new NotificationChannel(CHANNEL_ID,
                            mMusicService.getString(R.string.app_name),
                            NotificationManager.IMPORTANCE_LOW);

            notificationChannel.setDescription(
                    mMusicService.getString(R.string.app_name));

            notificationChannel.enableLights(false);
            notificationChannel.enableVibration(false);
            notificationChannel.setShowBadge(false);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Bitmap getLargeIcon() {

        final VectorDrawable vectorDrawable = (VectorDrawable) mMusicService.getDrawable(R.drawable.ic_launcher_background);

        final int largeIconSize = mMusicService.getResources().getDimensionPixelSize(R.dimen._200sdp);
        final Bitmap bitmap = Bitmap.createBitmap(largeIconSize, largeIconSize, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);

        if (vectorDrawable != null) {
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            vectorDrawable.setTint(mAccent);
            vectorDrawable.setAlpha(100);
            vectorDrawable.draw(canvas);
        }

        return bitmap;
    }

    public void CancleNotification() {
        NotificationManager notificationManager = (NotificationManager) mMusicService.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }
}
