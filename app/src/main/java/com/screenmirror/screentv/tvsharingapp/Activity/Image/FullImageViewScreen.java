package com.screenmirror.screentv.tvsharingapp.Activity.Image;

import android.os.Bundle;

import com.ads.adsdemosp.AdsClass.Ads_Interstitial;
import com.screenmirror.screentv.tvsharingapp.Activity.BaseActivity;
import com.screenmirror.screentv.tvsharingapp.Activity.Image.viewimg.pictureBrowserFragment;
import com.screenmirror.screentv.tvsharingapp.R;


public class FullImageViewScreen extends BaseActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image_view_screen);

        pictureBrowserFragment browser = pictureBrowserFragment.newInstance(ImageDisplay.imagefulllist, ImageDisplay.imageposition, FullImageViewScreen.this);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            browser.setEnterTransition(new Fade());
            browser.setExitTransition(new Fade());
        }*/

        findViewById(R.id.iv_back).setOnClickListener(v->{
            onBackPressed();
        });

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.previewFrameLayout, browser)
                .addToBackStack(null)
                .commit();
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