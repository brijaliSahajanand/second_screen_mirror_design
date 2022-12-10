package com.screenmirror.screentv.tvsharingapp.Activity.Audio;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ads.adsdemosp.AdsClass.Ads_Adapter_List;
import com.ads.adsdemosp.AdsClass.Ads_Interstitial;
import com.ads.adsdemosp.AdsClass.Ads_Native;
import com.ads.adsdemosp.Ids_Class;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.screenmirror.screentv.tvsharingapp.Activity.Audio.Adapter.AudioFolderAdapter;
import com.screenmirror.screentv.tvsharingapp.Activity.BaseActivity;
import com.screenmirror.screentv.tvsharingapp.Activity.CastToTv.Setting_Permission_Write;
import com.screenmirror.screentv.tvsharingapp.ExtraClass.GetData;
import com.screenmirror.screentv.tvsharingapp.ExtraClass.Utils;
import com.screenmirror.screentv.tvsharingapp.Interface.AudioFolderClickInterface;
import com.screenmirror.screentv.tvsharingapp.Model.Folder;
import com.screenmirror.screentv.tvsharingapp.R;

import java.util.ArrayList;
import java.util.List;




public class AudioFolderActivity extends BaseActivity implements AudioFolderClickInterface {

    //View
    ImageView iv_back, iv_search, iv_cast;
    TextView tv_no_folder, tv_nodata;
    RecyclerView rv_folderaudio;

    ProgressBar progressbar;
    ShimmerFrameLayout mShimmerViewContainer;
    //Variable
    List<Folder> folderList = new ArrayList<>();
    AudioFolderClickInterface audioFolderClickInterface;
    AudioFolderAdapter audioFolderAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_folder);
        audioFolderClickInterface = (AudioFolderClickInterface) this;
        Declaration();
        Initialization();
    }

    private void Declaration() {
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer.startShimmerAnimation();
        iv_back = findViewById(R.id.iv_back);

        tv_no_folder = findViewById(R.id.tv_no_folder);
        tv_nodata = findViewById(R.id.tv_nodata);
        rv_folderaudio = findViewById(R.id.rv_folderaudio);
        progressbar = findViewById(R.id.progressbar);
        iv_cast = findViewById(R.id.iv_cast);
    }

    private void Initialization() {


        iv_cast.setOnClickListener(v -> {
            Ads_Interstitial.showAds_full(this, new Ads_Interstitial.OnFinishAds() {
                @Override
                public void onFinishAds(boolean b) {
                    startActivity(new Intent(AudioFolderActivity.this, Setting_Permission_Write.class));
                }
            });
        });

        rv_folderaudio.setLayoutManager(new GridLayoutManager(AudioFolderActivity.this, 2));

        iv_back.setOnClickListener(v -> {
            onBackPressed();
        });
        new LoadData().execute();
    }


    public class LoadData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            folderList = new ArrayList<>();
            progressbar.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            folderList = GetData.getFolderList(AudioFolderActivity.this);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressbar.setVisibility(View.GONE);
            mShimmerViewContainer.stopShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.GONE);
            if (folderList.size() > 0) {
                tv_no_folder.setText(folderList.size() + " Folder");
               /* if (Ads_Native.adsScreen == Ids_Class.Native_adsscreen) {
                    Ads_Native.adsScreen = 0;
                    folderList.add(0, null);
                }else {
                    Ads_Native.adsScreen++;
                }
*/
                audioFolderAdapter = new AudioFolderAdapter(AudioFolderActivity.this, folderList);
                rv_folderaudio.setAdapter(audioFolderAdapter);

                rv_folderaudio.setVisibility(View.VISIBLE);
                tv_nodata.setVisibility(View.GONE);
            } else {
                tv_no_folder.setText("0 Folder");
                rv_folderaudio.setVisibility(View.GONE);
                tv_nodata.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void audiofolderclick(Folder folder) {
        Ads_Interstitial.showAds_full(this, new Ads_Interstitial.OnFinishAds() {
            @Override
            public void onFinishAds(boolean b) {
                startActivity(new Intent(AudioFolderActivity.this, AudioActivity.class).putExtra(GetData.songlist, new Gson().toJson(folder.getSongList())).putExtra(GetData.foldername, folder.getName()));

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.UpdateAds) {
            Utils.UpdateAds = false;
            if (Ads_Adapter_List.admob_nativehashmap != null) {
                Ads_Adapter_List.admob_nativehashmap.clear();
            }
            if (audioFolderAdapter != null) {
                audioFolderAdapter.notifyDataSetChanged();
            }
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