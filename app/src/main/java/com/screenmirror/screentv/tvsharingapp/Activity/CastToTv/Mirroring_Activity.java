package com.screenmirror.screentv.tvsharingapp.Activity.CastToTv;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ads.adsdemosp.AdsClass.Ads_Interstitial;
import com.screenmirror.screentv.tvsharingapp.Activity.BaseActivity;
import com.screenmirror.screentv.tvsharingapp.R;

public class Mirroring_Activity extends BaseActivity {
    TextView lin_start_mirroring;
    ImageView iv_back;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mirroring);
        lin_start_mirroring = findViewById(R.id.lin_start_mirroring);

        lin_start_mirroring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ads_Interstitial.showAds_full(Mirroring_Activity.this, new Ads_Interstitial.OnFinishAds() {
                    @Override
                    public void onFinishAds(boolean b) {
                        Intent intent = new Intent(Mirroring_Activity.this, Screen_Cast_Permission.class);
                        startActivity(intent);
                    }
                });

            }
        });

        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    @Override
    protected void onResume() {


        super.onResume();
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