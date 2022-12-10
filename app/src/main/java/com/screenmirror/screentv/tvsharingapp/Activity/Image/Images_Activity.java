package com.screenmirror.screentv.tvsharingapp.Activity.Image;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ads.adsdemosp.AdsClass.Ads_Adapter_List;
import com.ads.adsdemosp.AdsClass.Ads_Interstitial;
import com.ads.adsdemosp.AdsClass.Ads_Native;
import com.ads.adsdemosp.Ids_Class;
import com.screenmirror.screentv.tvsharingapp.Activity.BaseActivity;
import com.screenmirror.screentv.tvsharingapp.Activity.CastToTv.Setting_Permission_Write;
import com.screenmirror.screentv.tvsharingapp.Activity.Image.adapter.PicHolder;
import com.screenmirror.screentv.tvsharingapp.Activity.Image.adapter.pictureFolderAdapter;
import com.screenmirror.screentv.tvsharingapp.Activity.Image.model.imageFolder;
import com.screenmirror.screentv.tvsharingapp.Activity.Image.model.pictureFacer;
import com.screenmirror.screentv.tvsharingapp.ExtraClass.Utils;
import com.screenmirror.screentv.tvsharingapp.R;
import com.screenmirror.screentv.tvsharingapp.SpacesItemDecoration;

import java.util.ArrayList;


public class Images_Activity extends BaseActivity implements itemClickListener/*, ImageRecycleAdapter.pictureActionListrener*/ {


    RecyclerView rv_images_list;
    ImageView iv_back;
    pictureFolderAdapter pictureFolderAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        rv_images_list = findViewById(R.id.rv_images_list);
        iv_back = findViewById(R.id.iv_back);

        iv_back.setOnClickListener(v -> {
            onBackPressed();
        });

        findViewById(R.id.iv_cast).setOnClickListener(v -> {
            Ads_Interstitial.showAds_full(this, new Ads_Interstitial.OnFinishAds() {
                @Override
                public void onFinishAds(boolean b) {
                    startActivity(new Intent(Images_Activity.this, Setting_Permission_Write.class));
                }
            });
        });
        ArrayList<imageFolder> folds = getPicturePaths();

        if (folds.isEmpty()) {
        } else {
          /*  if (Ads_Native.adsScreen == Ids_Class.Native_adsscreen) {
                Ads_Native.adsScreen = 0;
                folds.add(0, null);
            } else {
                Ads_Native.adsScreen++;
            }*/
            pictureFolderAdapter = new pictureFolderAdapter(folds, Images_Activity.this, this);
            final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(Images_Activity.this, R.anim.item_anim);
          //  rv_images_list.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));


          GridLayoutManager  gridLayoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
            rv_images_list.setLayoutManager(gridLayoutManager);
            int spacingInPixels = getResources().getDimensionPixelSize(R.dimen._5sdp);
            rv_images_list.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
            rv_images_list.setAdapter(pictureFolderAdapter);



        }
    }

    private ArrayList<imageFolder> getPicturePaths() {
        ArrayList<imageFolder> picFolders = new ArrayList<>();
        ArrayList<String> picPaths = new ArrayList<>();
        Uri allImagesuri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID};
        Cursor cursor = this.getContentResolver().query(allImagesuri, projection, null, null, null);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
            }
            do {
                imageFolder folds = new imageFolder();
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                String folder = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                String datapath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));

                //String folderpaths =  datapath.replace(name,"");
                String folderpaths = datapath.substring(0, datapath.lastIndexOf(folder + "/"));
                folderpaths = folderpaths + folder + "/";
                if (!picPaths.contains(folderpaths)) {
                    picPaths.add(folderpaths);

                    folds.setPath(folderpaths);
                    folds.setFolderName(folder);
                    folds.setFirstPic(datapath);
                    folds.addpics();
                    picFolders.add(folds);
                } else {
                    for (int i = 0; i < picFolders.size(); i++) {
                        if (picFolders.get(i).getPath().equals(folderpaths)) {
                            picFolders.get(i).setFirstPic(datapath);
                            picFolders.get(i).addpics();
                        }
                    }
                }
            } while (cursor.moveToNext());
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < picFolders.size(); i++) {
            Log.d("picture folders", picFolders.get(i).getFolderName() + " and path = " + picFolders.get(i).getPath() + " " + picFolders.get(i).getNumberOfPics());
        }

        //reverse order ArrayList
       /* ArrayList<imageFolder> reverseFolders = new ArrayList<>();

        for(int i = picFolders.size()-1;i > reverseFolders.size()-1;i--){
            reverseFolders.add(picFolders.get(i));
        }*/

        return picFolders;
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
    @Override
    public void onPicClicked(PicHolder holder, int position, ArrayList<pictureFacer> pics) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.UpdateAds) {
            Utils.UpdateAds = false;
            if (Ads_Adapter_List.admob_nativehashmap != null) {
                Ads_Adapter_List.admob_nativehashmap.clear();
            }
            if (pictureFolderAdapter != null) {
                pictureFolderAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onPicClicked(String pictureFolderPath, String folderName) {
        Ads_Interstitial.showAds_full(this, new Ads_Interstitial.OnFinishAds() {
            @Override
            public void onFinishAds(boolean b) {
                Intent move = new Intent(Images_Activity.this, ImageDisplay.class);
                move.putExtra("folderPath", pictureFolderPath);
                move.putExtra("folderName", folderName);
                startActivity(move);
            }
        });


    }
}
