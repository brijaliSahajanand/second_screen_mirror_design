package com.screenmirror.screentv.tvsharingapp.Activity.Video;

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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.screenmirror.screentv.tvsharingapp.Activity.BaseActivity;
import com.screenmirror.screentv.tvsharingapp.Activity.CastToTv.Setting_Permission_Write;
import com.screenmirror.screentv.tvsharingapp.Activity.Video.Adapter.VideoListAdapter;
import com.screenmirror.screentv.tvsharingapp.Activity.Video.Player.GlobalVar;
import com.screenmirror.screentv.tvsharingapp.Activity.Video.Player.PlayVideoActivity;
import com.screenmirror.screentv.tvsharingapp.Activity.Video.Player.videoService.VideoService;
import com.screenmirror.screentv.tvsharingapp.ExtraClass.GetData;
import com.screenmirror.screentv.tvsharingapp.ExtraClass.Utils;
import com.screenmirror.screentv.tvsharingapp.Interface.VideoInerface;
import com.screenmirror.screentv.tvsharingapp.Model.VideoModel;
import com.screenmirror.screentv.tvsharingapp.R;

import java.util.ArrayList;
import java.util.List;


public class VideoListActivity extends BaseActivity implements VideoInerface {


    //View
    ImageView iv_back, iv_search;
    TextView tv_no_video, tv_nodata, tv_foldername;
    RecyclerView rv_videolist;

    //Variable
    List<VideoModel> videoList = new ArrayList<>();
    VideoInerface videoInerface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        videoInerface = (VideoInerface) this;
        Declaration();
        Intialization();
    }

    private void Declaration() {
        iv_back = findViewById(R.id.iv_back);

        tv_no_video = findViewById(R.id.tv_nodata);
        tv_nodata = findViewById(R.id.tv_nodata);
        tv_foldername = findViewById(R.id.txtHeader);
        rv_videolist = findViewById(R.id.rv_videolist);
    }

    private void Intialization() {
        iv_back.setOnClickListener(v -> {
            onBackPressed();
        });


        findViewById(R.id.iv_cast).setOnClickListener(v -> {
            Ads_Interstitial.showAds_full(this, new Ads_Interstitial.OnFinishAds() {
                @Override
                public void onFinishAds(boolean b) {
                    startActivity(new Intent(VideoListActivity.this, Setting_Permission_Write.class));
                }
            });
        });

        rv_videolist.setLayoutManager(new LinearLayoutManager(VideoListActivity.this));
        inItService();


        if (getIntent() != null) {

            String foldername = "";
            foldername = getIntent().getStringExtra(GetData.foldername);
            if (foldername.isEmpty()) {
                tv_foldername.setText("Video");
            } else {
                tv_foldername.setText(getIntent().getStringExtra(GetData.foldername));
            }


            String video = getIntent().getStringExtra(GetData.videolist);
            if (!video.isEmpty()) {
                if (foldername.isEmpty()) {
                    videoList = GetData.getVideoList(VideoListActivity.this);
                } else {
                    videoList = new Gson().fromJson(video, new TypeToken<List<VideoModel>>() {
                    }.getType());
                }


                tv_no_video.setText("" + videoList.size() + " videos");


                if (videoList.size() > 0) {
                    tv_nodata.setVisibility(View.GONE);
                    rv_videolist.setVisibility(View.VISIBLE);

                    List<VideoModel> videoModelList = new ArrayList<>();

                    videoModelList.addAll(videoList);


                    if (Ads_Native.adsScreen == Ids_Class.Native_adsscreen) {
                        Ads_Native.adsScreen = 0;
                        videoModelList.add(0, null);
                    } else {
                        Ads_Native.adsScreen++;
                    }

//                    videoModelList = videoList;

                    rv_videolist.setAdapter(new VideoListAdapter(VideoListActivity.this, videoModelList));
                } else {
                    tv_nodata.setVisibility(View.VISIBLE);
                    rv_videolist.setVisibility(View.GONE);
                }


            } else {
                tv_nodata.setVisibility(View.VISIBLE);
                rv_videolist.setVisibility(View.GONE);
            }

        }

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
    }

    @Override
    public void videoclick(List<VideoModel> mData, int position) {
        Ads_Interstitial.showAds_full(this, new Ads_Interstitial.OnFinishAds() {
            @Override
            public void onFinishAds(boolean b) {
                int pos = 0;
                if (videoList.contains(mData.get(position))) {
                    pos = videoList.indexOf(mData.get(position));
                }

                GlobalVar.getInstance().videoItemsPlaylist = videoList;
                GlobalVar.getInstance().playingVideo = videoList.get(pos);
                GlobalVar.getInstance().seekPosition = 0;
                if (GlobalVar.getInstance().getPlayer() == null) {
                    return;
                } else if (!GlobalVar.getInstance().isMutilSelectEnalble) {
                    if (!GlobalVar.getInstance().isPlayingAsPopup()) {
                        GlobalVar.getInstance().videoService.playVideo(GlobalVar.getInstance().seekPosition, false);
                        Intent intent = new Intent(VideoListActivity.this, PlayVideoActivity.class);
                        startActivity(intent);
                        if (GlobalVar.getInstance().videoService != null)
                            GlobalVar.getInstance().videoService.releasePlayerView();
                    } else {

                    }
                }
            }
        });
    }


    protected static Intent _playIntent;
    GlobalVar mGlobalVar = GlobalVar.getInstance();

    public void inItService() {
        _playIntent = new Intent(VideoListActivity.this, VideoService.class);
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
        Utils.UpdateAds = true;
        Ads_Interstitial.BackshowAds_full(this, new Ads_Interstitial.OnFinishAds() {
            @Override
            public void onFinishAds(boolean b) {
                finish();
            }
        });


    }

}