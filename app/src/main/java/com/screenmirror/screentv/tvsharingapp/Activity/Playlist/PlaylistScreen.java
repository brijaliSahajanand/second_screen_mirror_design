package com.screenmirror.screentv.tvsharingapp.Activity.Playlist;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ads.adsdemosp.AdsClass.Ads_Interstitial;
import com.screenmirror.screentv.tvsharingapp.Activity.BaseActivity;
import com.screenmirror.screentv.tvsharingapp.Activity.CastToTv.Setting_Permission_Write;
import com.screenmirror.screentv.tvsharingapp.Activity.Playlist.Adapter.PlaylistAdapter;
import com.screenmirror.screentv.tvsharingapp.ExtraClass.GetData;
import com.screenmirror.screentv.tvsharingapp.Model.Playlist;
import com.screenmirror.screentv.tvsharingapp.R;


import java.util.List;



public class PlaylistScreen extends BaseActivity {

    ImageView iv_back;
    RecyclerView rv_playlist;
    List<Playlist> playlists;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        Declaration();
        Intialization();
    }
    private void Declaration() {
        iv_back = findViewById(R.id.iv_back);
        rv_playlist = findViewById(R.id.rv_playlist);    }

    private void Intialization() {
        findViewById(R.id.iv_cast).setOnClickListener(v -> {
            Ads_Interstitial.showAds_full(this, new Ads_Interstitial.OnFinishAds() {
                @Override
                public void onFinishAds(boolean b) {
                    startActivity(new Intent(PlaylistScreen.this, Setting_Permission_Write.class));
                }
            });
        });
        rv_playlist.setLayoutManager(new LinearLayoutManager(this));
        iv_back.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        playlists = GetData.GetPlaylist(this);
        PlaylistAdapter playlistAdapter = new PlaylistAdapter(this, playlists , true , null , null);
        rv_playlist.setAdapter(playlistAdapter);
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