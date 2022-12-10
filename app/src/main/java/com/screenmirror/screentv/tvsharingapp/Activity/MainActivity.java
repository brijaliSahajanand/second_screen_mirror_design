package com.screenmirror.screentv.tvsharingapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ads.adsdemosp.AdsClass.Ads_Interstitial;
import com.screenmirror.screentv.tvsharingapp.Activity.Audio.AudioFolderActivity;
import com.screenmirror.screentv.tvsharingapp.Activity.CastToTv.Setting_Permission_Write;
import com.screenmirror.screentv.tvsharingapp.Activity.Image.Images_Activity;
import com.screenmirror.screentv.tvsharingapp.Activity.Playlist.PlaylistScreen;
import com.screenmirror.screentv.tvsharingapp.Activity.Video.VideoFolderListActivity;
import com.screenmirror.screentv.tvsharingapp.ExtraClass.Utils;
import com.screenmirror.screentv.tvsharingapp.R;


public class MainActivity extends BaseActivity {
    LinearLayout iv_video, iv_photo, iv_audio, iv_playlist;
    ImageView iv_back, iv_cast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Declaration();
        Intialization();

    }

    private void Declaration() {
        iv_back = findViewById(R.id.iv_back);
        iv_cast = findViewById(R.id.iv_cast);
        iv_video = findViewById(R.id.iv_video);
        iv_photo = findViewById(R.id.iv_photo);
        iv_audio = findViewById(R.id.iv_audio);
        iv_playlist = findViewById(R.id.iv_playlist);
    }

    private void Intialization() {

        iv_back.setOnClickListener(v -> {
            onBackPressed();
        });
        
        iv_cast.setOnClickListener(v->{
            Ads_Interstitial.showAds_full(this, new Ads_Interstitial.OnFinishAds() {
                @Override
                public void onFinishAds(boolean b) {
                    startActivity(new Intent(MainActivity.this, Setting_Permission_Write.class));
                }
            });
        });

        iv_video.setOnClickListener(v->{
            Ads_Interstitial.showAds_full(this, new Ads_Interstitial.OnFinishAds() {
                @Override
                public void onFinishAds(boolean b) {
                    Utils.UpdateAds = false;
                    startActivity(new Intent(MainActivity.this, VideoFolderListActivity.class));
                }
            });
        });

        iv_photo.setOnClickListener(v->{
            Ads_Interstitial.showAds_full(this, new Ads_Interstitial.OnFinishAds() {
                @Override
                public void onFinishAds(boolean b) {
                    Utils.UpdateAds = false;
                    startActivity(new Intent(MainActivity.this, Images_Activity.class));
                }
            });
        });

        iv_audio.setOnClickListener(v->{
            Ads_Interstitial.showAds_full(this, new Ads_Interstitial.OnFinishAds() {
                @Override
                public void onFinishAds(boolean b) {
                    Utils.UpdateAds = false;
                    startActivity(new Intent(MainActivity.this, AudioFolderActivity.class));
                }
            });
        });

        iv_playlist.setOnClickListener(v->{
            Ads_Interstitial.showAds_full(this, new Ads_Interstitial.OnFinishAds() {
                @Override
                public void onFinishAds(boolean b) {
                    Utils.UpdateAds = false;
                    startActivity(new Intent(MainActivity.this, PlaylistScreen.class));
                }
            });
        });

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