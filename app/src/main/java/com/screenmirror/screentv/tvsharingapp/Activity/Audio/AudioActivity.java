package com.screenmirror.screentv.tvsharingapp.Activity.Audio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ads.adsdemosp.AdsClass.Ads_Interstitial;
import com.ads.adsdemosp.AdsClass.Ads_Native;
import com.ads.adsdemosp.Ids_Class;
import com.screenmirror.screentv.tvsharingapp.ExtraClass.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.screenmirror.screentv.tvsharingapp.Activity.Audio.Adapter.AudioAdapter;
import com.screenmirror.screentv.tvsharingapp.Activity.BaseActivity;
import com.screenmirror.screentv.tvsharingapp.Activity.CastToTv.Setting_Permission_Write;
import com.screenmirror.screentv.tvsharingapp.ExtraClass.GetData;
import com.screenmirror.screentv.tvsharingapp.Interface.AudioClickInterface;
import com.screenmirror.screentv.tvsharingapp.Model.Song;
import com.screenmirror.screentv.tvsharingapp.R;


import java.util.ArrayList;
import java.util.List;




public class AudioActivity extends BaseActivity implements AudioClickInterface {

    ImageView iv_back,  iv_cast;
    TextView tv_foldername, tv_no_folder, tv_nodata;
    RecyclerView rv_audio;

    List<Song> songList = new ArrayList<>();
    List<Song> songList_withoutads = new ArrayList<>();
    AudioClickInterface audioClickInterface;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        audioClickInterface = (AudioClickInterface) this;

        Declaration();
        Intialization();
    }

    private void Declaration() {
        iv_back = findViewById(R.id.iv_back);
        tv_foldername = findViewById(R.id.txtHeader);
        tv_no_folder = findViewById(R.id.tv_no_folder);
        tv_nodata = findViewById(R.id.tv_nodata);
        rv_audio = findViewById(R.id.rv_audio);
        iv_cast = findViewById(R.id.iv_cast);



    }

    private void Intialization() {


        iv_cast.setOnClickListener(v -> {
            Ads_Interstitial.showAds_full(this, new Ads_Interstitial.OnFinishAds() {
                @Override
                public void onFinishAds(boolean b) {
                    startActivity(new Intent(AudioActivity.this, Setting_Permission_Write.class));
                }
            });
        });

        rv_audio.setLayoutManager(new LinearLayoutManager(AudioActivity.this));


        if (getIntent() != null) {
            songList = new Gson().fromJson(getIntent().getStringExtra(GetData.songlist), new TypeToken<List<Song>>() {
            }.getType());
            tv_foldername.setText(getIntent().getStringExtra(GetData.foldername));
            tv_no_folder.setText(songList.size() + " Music");
            songList_withoutads.addAll(songList);

            if (songList.size() > 0) {
                if (Ads_Native.adsScreen == Ids_Class.Native_adsscreen) {
                    Ads_Native.adsScreen = 0;
                    songList.add(0, null);
                }else {
                    Ads_Native.adsScreen++;
                }

                rv_audio.setAdapter(new AudioAdapter(AudioActivity.this, songList));
                rv_audio.setVisibility(View.VISIBLE);
                tv_nodata.setVisibility(View.GONE);
            } else {
                rv_audio.setVisibility(View.GONE);
                tv_nodata.setVisibility(View.VISIBLE);
            }
        } else {
            tv_no_folder.setText(0 + " Music");

            rv_audio.setVisibility(View.GONE);
            tv_nodata.setVisibility(View.VISIBLE);
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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

    @Override
    public void audioclick(Song song) {


        Ads_Interstitial.showAds_full(this, new Ads_Interstitial.OnFinishAds() {
            @Override
            public void onFinishAds(boolean b) {
                int pos = 0;
                if (songList_withoutads.contains(song)) {
                    pos = songList_withoutads.indexOf(song);
                }

                startActivity(new Intent(AudioActivity.this, AudioPlayActivity.class).putExtra(GetData.songlist, new Gson().toJson(songList_withoutads)).putExtra(GetData.position, pos));

            }
        });


    }
}