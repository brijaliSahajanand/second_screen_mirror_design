package com.screenmirror.screentv.tvsharingapp.Activity.CastToTv;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ads.adsdemosp.AdsClass.Ads_Interstitial;
import com.screenmirror.screentv.tvsharingapp.Activity.BaseActivity;
import com.screenmirror.screentv.tvsharingapp.R;


public class Screen_Cast_Permission extends BaseActivity {

    ImageView lin_setting;
    WifiManager wifi;

    ImageView iv_back;
    TextView ll_cast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.miracast_permission_dailog);


        lin_setting = findViewById(R.id.lin_setting);
        ll_cast = findViewById(R.id.ll_cast);
        this.wifi = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        lin_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wifi.isWifiEnabled()) {
                    enablingWiFiDisplay();
                    return;
                }
                wifi.setWifiEnabled(true);
                enablingWiFiDisplay();
            }
        });
        ll_cast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wifi.isWifiEnabled()) {
                    enablingWiFiDisplay();
                    return;
                }
                wifi.setWifiEnabled(true);
                enablingWiFiDisplay();
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
    public void onBackPressed() {
        Ads_Interstitial.BackshowAds_full(this, new Ads_Interstitial.OnFinishAds() {
            @Override
            public void onFinishAds(boolean b) {
                finish();
            }
        });
    }

    public void enablingWiFiDisplay() {
        try {
            startActivity(new Intent("android.settings.WIFI_DISPLAY_SETTINGS"));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
//            startActivity(new Intent("com.samsung.wfd.LAUNCH_WFD_PICKER_DLG"));
            try {
                startActivity(new Intent("android.settings.CAST_SETTINGS"));
            } catch (Exception unused) {
                Toast makeText = Toast.makeText(getApplicationContext(), "Device not supported", Toast.LENGTH_SHORT);
                makeText.setGravity(17, 0, 0);
                makeText.show();
            }
        }
    }

}
