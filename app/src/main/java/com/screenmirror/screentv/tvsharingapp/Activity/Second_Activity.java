package com.screenmirror.screentv.tvsharingapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ads.adsdemosp.AdsClass.Ads_ExitNativeFull;
import com.ads.adsdemosp.AdsClass.Ads_Interstitial;
import com.screenmirror.screentv.tvsharingapp.ExtraClass.Preference;
import com.screenmirror.screentv.tvsharingapp.R;

import static com.screenmirror.screentv.tvsharingapp.ExtraClass.Utils.Exit_Dialog;

public class Second_Activity extends BaseActivity {
    TextView iv_get_started;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_);
        iv_get_started = findViewById(R.id.iv_nxt_button);


        iv_get_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Ads_Interstitial.showAds_full(Second_Activity.this, new Ads_Interstitial.OnFinishAds() {
                    @Override
                    public void onFinishAds(boolean b) {
                        if (Preference.getscreenshow() != 2) {
                            Intent intent = new Intent(Second_Activity.this, Third_Activity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(Second_Activity.this, First_MainScreen.class);
                            startActivity(intent);
                        }


                    }
                });


            }
        });


    }

    boolean exit_flag = false;

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