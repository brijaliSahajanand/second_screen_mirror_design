package com.screenmirror.screentv.tvsharingapp.Activity.Audio.Music;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class MusicUtil {

    public static int getColorFromResource(@NonNull final Context context, final int resource, final int emergencyColor) {
        int color;
        try {
            color = ContextCompat.getColor(context, resource);
        } catch (Exception e) {
            color = ContextCompat.getColor(context, emergencyColor);
        }
        return color;
    }

    public static Spanned buildSpanned(@NonNull final String res) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                Html.fromHtml(res, Html.FROM_HTML_MODE_LEGACY) :
                Html.fromHtml(res);
    }
}
