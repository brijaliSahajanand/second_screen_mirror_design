package com.screenmirror.screentv.tvsharingapp.Activity.Video;

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
import com.screenmirror.screentv.tvsharingapp.ExtraClass.Utils;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.screenmirror.screentv.tvsharingapp.Activity.BaseActivity;
import com.screenmirror.screentv.tvsharingapp.Activity.CastToTv.Setting_Permission_Write;
import com.screenmirror.screentv.tvsharingapp.Activity.Video.Adapter.VideoFolderLlistAdapter;
import com.screenmirror.screentv.tvsharingapp.ExtraClass.GetData;
import com.screenmirror.screentv.tvsharingapp.Interface.FolderItemInterface;
import com.screenmirror.screentv.tvsharingapp.Model.VideoFolder;
import com.screenmirror.screentv.tvsharingapp.R;


import java.util.ArrayList;
import java.util.List;


public class VideoFolderListActivity extends BaseActivity implements FolderItemInterface {

    //View
    ImageView iv_back, iv_search;
    TextView tv_foldername, tv_no_folder, tv_nodata;
    RecyclerView rv_folderlist;
    ProgressBar progressbar;
    ShimmerFrameLayout mShimmerViewContainer;

    //Variable
    List<VideoFolder> videoFolderList = new ArrayList<>();
    FolderItemInterface folderItemInterface;
    VideoFolderLlistAdapter videoFolderLlistAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_folder_list);

        folderItemInterface = (FolderItemInterface) this;
        Declaration();
        Initialization();
    }

    private void Declaration() {
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer.startShimmerAnimation();
        iv_back = findViewById(R.id.iv_back);
        tv_foldername = findViewById(R.id.tv_foldername);
        tv_no_folder = findViewById(R.id.tv_no_folder);
        tv_nodata = findViewById(R.id.tv_nodata);
        rv_folderlist = findViewById(R.id.rv_folderlist);

        progressbar = findViewById(R.id.progressbar);


    }

    private void Initialization() {


        findViewById(R.id.iv_cast).setOnClickListener(v -> {
            Ads_Interstitial.showAds_full(this, new Ads_Interstitial.OnFinishAds() {
                @Override
                public void onFinishAds(boolean b) {
                    startActivity(new Intent(VideoFolderListActivity.this, Setting_Permission_Write.class));
                }
            });
        });

        rv_folderlist.setLayoutManager(new GridLayoutManager(VideoFolderListActivity.this,2));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        new LoadData().execute();

    }

    @Override
    public void folderclick(List<VideoFolder> mData, int position) {

        Ads_Interstitial.showAds_full(this, new Ads_Interstitial.OnFinishAds() {
            @Override
            public void onFinishAds(boolean b) {
                startActivity(new Intent(VideoFolderListActivity.this, VideoListActivity.class)
                        .putExtra(GetData.foldername, mData.get(position).getFoldername())
                        .putExtra(GetData.videolist, new Gson().toJson(mData.get(position).getVideoList())));
            }
        });


    }


    public class LoadData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            videoFolderList = new ArrayList<>();
            progressbar.setVisibility(View.GONE);

        }

        @Override
        protected Void doInBackground(Void... voids) {
            videoFolderList = GetData.getVideoFolderList(VideoFolderListActivity.this);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressbar.setVisibility(View.GONE);
            mShimmerViewContainer.stopShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.GONE);
            if (videoFolderList.size() > 0) {

                tv_nodata.setVisibility(View.GONE);
                rv_folderlist.setVisibility(View.VISIBLE);
                tv_no_folder.setText(videoFolderList.size() + " Folder");

                videoFolderLlistAdapter = (new VideoFolderLlistAdapter(VideoFolderListActivity.this, videoFolderList));
                rv_folderlist.setAdapter(videoFolderLlistAdapter);
            } else {
                tv_no_folder.setText("0 Folder");
                tv_nodata.setVisibility(View.VISIBLE);
                rv_folderlist.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Utils.UpdateAds) {
            Utils.UpdateAds = false;
            if (Ads_Adapter_List.admob_nativehashmap != null) {
                Ads_Adapter_List.admob_nativehashmap.clear();
            }
            if (videoFolderLlistAdapter != null) {
                videoFolderLlistAdapter.notifyDataSetChanged();
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