package com.screenmirror.screentv.tvsharingapp.Activity.Image;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ads.adsdemosp.AdsClass.Ads_ExitNativeFull;
import com.ads.adsdemosp.AdsClass.Ads_Interstitial;
import com.ads.adsdemosp.AdsClass.Ads_Native;
import com.ads.adsdemosp.Ids_Class;
import com.screenmirror.screentv.tvsharingapp.Activity.BaseActivity;
import com.screenmirror.screentv.tvsharingapp.Activity.CastToTv.Setting_Permission_Write;
import com.screenmirror.screentv.tvsharingapp.Activity.Image.adapter.PicHolder;
import com.screenmirror.screentv.tvsharingapp.Activity.Image.adapter.picture_Adapter;
import com.screenmirror.screentv.tvsharingapp.Activity.Image.model.pictureFacer;
import com.screenmirror.screentv.tvsharingapp.ExtraClass.Utils;
import com.screenmirror.screentv.tvsharingapp.R;


import java.util.ArrayList;


public class ImageDisplay extends BaseActivity implements itemClickListener {

    RecyclerView imageRecycler;
    ArrayList<pictureFacer> allpictures;
    ProgressBar load;
    String foldePath;
    TextView folderName;
    GridLayoutManager gridLayoutManager;

    public static ArrayList<pictureFacer> imagefulllist = new ArrayList<>();
    public static int imageposition = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        folderName = findViewById(R.id.txtHeader);
        folderName.setText(getIntent().getStringExtra("folderName"));

        foldePath = getIntent().getStringExtra("folderPath");
        allpictures = new ArrayList<>();
        imageRecycler = findViewById(R.id.recycler);
        //  imageRecycler.addItemDecoration(new MarginDecoration(this));
        imageRecycler.hasFixedSize();
        load = findViewById(R.id.loader);


        gridLayoutManager = new GridLayoutManager(this, 3, RecyclerView.VERTICAL, false);
      /*  LinearLayout llnative = findViewById(R.id.llnative);
        TextView tv_ad_text_native2 = findViewById(R.id.tv_ad_text_native2);
        NativeAds.Native_adtype_Q(this, llnative, "native",tv_ad_text_native2);*/

        ImageView iv_back = findViewById(R.id.iv_back);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        findViewById(R.id.iv_cast).setOnClickListener(v -> {
            Ads_Interstitial.showAds_full(this, new Ads_Interstitial.OnFinishAds() {
                @Override
                public void onFinishAds(boolean b) {
                    startActivity(new Intent(ImageDisplay.this, Setting_Permission_Write.class));
                }
            });
        });

        imageRecycler.setLayoutManager(gridLayoutManager);
        if (allpictures.isEmpty()) {
            load.setVisibility(View.VISIBLE);
            allpictures = getAllImagesByFolder(foldePath);
            //  imageRecycler.setAdapter(new picture_Adapter(allpictures, ImageDisplay.this, this));
            load.setVisibility(View.GONE);


            if (Ads_Native.adsScreen == Ids_Class.Native_adsscreen) {
                Ads_Native.adsScreen = 0;
                if (Ads_ExitNativeFull.checkExitAdsLoaded()) {
                    allpictures.add(0, null);
                    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {

                            if (position == 0) {
                                return 3;
                            } else {
                                return 1;
                            }

                        }
                    });
                    imageRecycler.setLayoutManager(gridLayoutManager);
                }
            } else {
                Ads_Native.adsScreen++;
                imageRecycler.setLayoutManager(gridLayoutManager);
            }


           /* admob_nativehashmap.clear();
            if (NativeAds.admobnativeAd != null) {

                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {

                        if (position == 0) {
                            return 3;
                        } else {
                            return 1;
                        }

                    }
                });

                imageRecycler.setLayoutManager(gridLayoutManager);
                allpictures.add(0, null);
//                videofolderModelList = getVideoListForAds(videoFolderArrayList);
            } else {

                if (Preference.getquiz_show_ADS().equals("yes")) {
                    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {

                            if (position == 0) {
                                return 3;
                            } else {
                                return 1;
                            }

                        }
                    });

                    imageRecycler.setLayoutManager(gridLayoutManager);
                    allpictures.add(0, null);
//                    videofolderModelList = getVideoListForAds(videoFolderArrayList);
                } else {

                    imageRecycler.setLayoutManager(gridLayoutManager);
                }
            }*/


            final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(ImageDisplay.this, R.anim.item_anim);
            imageRecycler.setLayoutAnimation(controller);
            imageRecycler.setAdapter(new picture_Adapter(allpictures, ImageDisplay.this, this));


            load.setVisibility(View.GONE);

        } else {

        }
    }

    @Override
    public void onPicClicked(PicHolder holder, int position, ArrayList<pictureFacer> pics) {


        Ads_Interstitial.showAds_full(this, new Ads_Interstitial.OnFinishAds() {
            @Override
            public void onFinishAds(boolean b) {
                int pos = 0;

                ArrayList<pictureFacer> picslist = new ArrayList<>();
                picslist.addAll(pics);

                if (picslist.get(0) == null) {
                    picslist.remove(0);
                    pos = position - 1;
                } else {
                    pos = position;
                }

                int finalPos = pos;


                imagefulllist = new ArrayList<>();
                imagefulllist.addAll(picslist);
                imageposition = finalPos;

                startActivity(new Intent(ImageDisplay.this , FullImageViewScreen.class));
                /*pictureBrowserFragment browser = pictureBrowserFragment.newInstance(picslist, finalPos, ImageDisplay.this);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    browser.setEnterTransition(new Fade());
                    browser.setExitTransition(new Fade());
                }

                getSupportFragmentManager()
                        .beginTransaction()
                        .addSharedElement(holder.picture, position + "picture")
                        .add(R.id.displayContainer, browser)
                        .addToBackStack(null)
                        .commit();*/
            }
        });


    }

    @Override
    public void onPicClicked(String pictureFolderPath, String folderName) {

    }

    public ArrayList<pictureFacer> getAllImagesByFolder(String path) {
        ArrayList<pictureFacer> images = new ArrayList<>();
        Uri allVideosuri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE};
        Cursor cursor = ImageDisplay.this.getContentResolver().query(allVideosuri, projection, MediaStore.Images.Media.DATA + " like ? ", new String[]{"%" + path + "%"}, null);
        try {
            cursor.moveToFirst();
            do {
                pictureFacer pic = new pictureFacer();

                pic.setPicturName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)));

                pic.setPicturePath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));

                pic.setPictureSize(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)));

                images.add(pic);
            } while (cursor.moveToNext());
            cursor.close();
            ArrayList<pictureFacer> reSelection = new ArrayList<>();
            for (int i = images.size() - 1; i > -1; i--) {
                reSelection.add(images.get(i));
            }
            images = reSelection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return images;
    }

    @Override
    public void onBackPressed() {

        Ads_Interstitial.BackshowAds_full(this, new Ads_Interstitial.OnFinishAds() {
            @Override
            public void onFinishAds(boolean b) {
                Utils.UpdateAds = true;
                finish();
            }
        });

    }


}
