package com.screenmirror.screentv.tvsharingapp.Activity.Playlist;

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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ads.adsdemosp.AdsClass.Ads_Interstitial;
import com.ads.adsdemosp.AdsClass.Ads_Native;
import com.ads.adsdemosp.Ids_Class;
import com.screenmirror.screentv.tvsharingapp.Activity.BaseActivity;
import com.screenmirror.screentv.tvsharingapp.Activity.CastToTv.Setting_Permission_Write;
import com.screenmirror.screentv.tvsharingapp.Activity.Playlist.Adapter.ItemPlaylistAdapter;
import com.screenmirror.screentv.tvsharingapp.Activity.Video.Player.GlobalVar;
import com.screenmirror.screentv.tvsharingapp.Activity.Video.Player.videoService.VideoService;
import com.screenmirror.screentv.tvsharingapp.ExtraClass.GetData;
import com.screenmirror.screentv.tvsharingapp.R;



public class ItemPlaylistScreen extends BaseActivity {

    //View
    ImageView iv_back;
    public TextView tv_name, tv_nodata;
    public RecyclerView rv_playlistitem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_playlist_screen);

        Declaration();
        Intialization();
    }

    private void Declaration() {
        iv_back = findViewById(R.id.iv_back);
        tv_name = findViewById(R.id.txtHeader);
        tv_nodata = findViewById(R.id.tv_nodata);
        rv_playlistitem = findViewById(R.id.rv_playlistitem);


    }

    private void Intialization() {


        findViewById(R.id.iv_cast).setOnClickListener(v -> {
            Ads_Interstitial.showAds_full(this, new Ads_Interstitial.OnFinishAds() {
                @Override
                public void onFinishAds(boolean b) {
                    startActivity(new Intent(ItemPlaylistScreen.this, Setting_Permission_Write.class));
                }
            });
        });

        rv_playlistitem.setLayoutManager(new LinearLayoutManager(this));
        iv_back.setOnClickListener(v -> {
            onBackPressed();
        });

        tv_name.setText(GetData.PlayName);
        if (GetData.itemPlaylistList != null) {
            if (GetData.itemPlaylistList.size() > 0) {
                inItService();
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (mGlobalVar.videoService == null) {
                            handler.postDelayed(this, 10);
                        } else {
                            Log.d("stopnoti", "run: ");
                            mGlobalVar.pause();
                            handler.removeCallbacks(this);
                        }

                    }
                };
                runnable.run();

                tv_nodata.setVisibility(View.GONE);
                rv_playlistitem.setVisibility(View.VISIBLE);


                if (Ads_Native.adsScreen == Ids_Class.Native_adsscreen) {
                    Ads_Native.adsScreen = 0;
                    GetData.itemPlaylistList.add(0, null);

                } else {
                    Ads_Native.adsScreen++;
                }


                ItemPlaylistAdapter itemPlaylistAdapter = new ItemPlaylistAdapter(ItemPlaylistScreen.this, GetData.itemPlaylistList);
                rv_playlistitem.setAdapter(itemPlaylistAdapter);

            } else {
                tv_nodata.setVisibility(View.VISIBLE);
                rv_playlistitem.setVisibility(View.GONE);
            }
        }

    }

    protected static Intent _playIntent;
    GlobalVar mGlobalVar = GlobalVar.getInstance();

    public void inItService() {
        _playIntent = new Intent(ItemPlaylistScreen.this, VideoService.class);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(_playIntent);
            } else {
                startService(_playIntent);
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return;
        }

        bindService(_playIntent, videoServiceConnection, Context.BIND_AUTO_CREATE);
    }

    protected ServiceConnection videoServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            VideoService.VideoBinder binder = (VideoService.VideoBinder) service;
            GlobalVar.getInstance().videoService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (GlobalVar.getInstance().videoService != null) {
            stopService(new Intent(this, VideoService.class));
            mGlobalVar.videoService = null;
            unbindService(videoServiceConnection);
        }
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