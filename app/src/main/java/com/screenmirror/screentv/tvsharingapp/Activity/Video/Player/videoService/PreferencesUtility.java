package com.screenmirror.screentv.tvsharingapp.Activity.Video.Player.videoService;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesUtility {
    private static final String TOGGLE_ALBUM_GRID = "TOGGLE_ALBUM_GRID";
    private static final String BACKGROUND_AUDIO = "BACKGROUND_AUDIO";
    private static final String SCEENORIENTATITION = "screen_orientation";
    private static final String REPEATMODE = "repeat_mode";
    private static final String KEY_THEME_SETTING = "key_theme_setting";
    private static PreferencesUtility sInstance;
    private static final String KEY_LAUGH_COUNT = "key_laugh_count";
    private static final String APP_OPEN_Splash = "app_open_splash";
    private static SharedPreferences mPreferences;

    public PreferencesUtility(final Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static PreferencesUtility getInstance(final Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesUtility(context.getApplicationContext());
        }
        return sInstance;
    }


    public boolean isAllowBackgroundAudio() {
        return mPreferences.getBoolean(BACKGROUND_AUDIO, true);
    }

    public void setAllowBackgroundAudio(final boolean b) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(BACKGROUND_AUDIO, b);
        editor.apply();
    }

    public int getScreenOrientation() {
        return mPreferences.getInt(SCEENORIENTATITION, 10);
    }

    public void setScreenOrientation(final int value) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(SCEENORIENTATITION, value);
        editor.apply();
    }

}
