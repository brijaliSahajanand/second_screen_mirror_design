package com.screenmirror.screentv.tvsharingapp.Activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.ads.adsdemosp.AdsClass.Ads_ExitNativeFull;
import com.ads.adsdemosp.AdsClass.Ads_Interstitial;
import com.screenmirror.screentv.tvsharingapp.BuildConfig;
import com.screenmirror.screentv.tvsharingapp.ExtraClass.Preference;
import com.screenmirror.screentv.tvsharingapp.R;

import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ForwardToSettingsCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ForwardScope;

import java.util.List;


import static com.screenmirror.screentv.tvsharingapp.ExtraClass.Utils.Exit_Dialog;

public class Third_Activity extends BaseActivity {
    TextView iv_get_started;
    String[] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_);

        iv_get_started = findViewById(R.id.iv_nxt_button);

        iv_get_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(CheckPermission()){
                    onPermissionGranted();
                }else {
                    requestStoragePermission();
                }

            }
        });


    }


    private void requestStoragePermission() {
        PermissionX.init(Third_Activity.this)
                .permissions(permissions)
                .onForwardToSettings(new ForwardToSettingsCallback() {
                    @Override
                    public void onForwardToSettings(@NonNull ForwardScope scope, @NonNull List<String> deniedList) {
                        scope.showForwardToSettingsDialog(deniedList, "Core fundamental are based on these permissions",
                                "OK", "Cancel");
                    }
                }).request(new RequestCallback() {
            @Override
            public void onResult(boolean allGranted, @NonNull List<String> grantedList, @NonNull List<String> deniedList) {
                if (allGranted) {
//                    onPermissionGranted();
                }
            }
        });

    }


    private void onPermissionGranted() {
        Ads_Interstitial.showAds_full(Third_Activity.this, new Ads_Interstitial.OnFinishAds() {
            @Override
            public void onFinishAds(boolean b) {
                Intent intent = new Intent(Third_Activity.this, First_MainScreen.class);
                startActivity(intent);
            }
        });
    }

    private boolean CheckPermission(){
        for (int i = 0; i <permissions.length ; i++) {

            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }

        }
        return true;
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