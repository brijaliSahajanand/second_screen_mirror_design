package com.screenmirror.screentv.tvsharingapp.Activity.CastToTv;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.ads.adsdemosp.AdsClass.Ads_Interstitial;
import com.screenmirror.screentv.tvsharingapp.Activity.BaseActivity;
import com.screenmirror.screentv.tvsharingapp.R;


public class Setting_Permission_Write extends BaseActivity {

    ImageView lin_setting;
    public int CODE_WRITE_SETTINGS_PERMISSION = 101;
    ImageView iv_back;
    TextView txtHeader, ll_go_to_seting;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_permission_write);
        lin_setting = findViewById(R.id.lin_setting);
        txtHeader = findViewById(R.id.txtHeader);
        txtHeader.setText("Screen Cast");
        ll_go_to_seting = findViewById(R.id.ll_go_to_seting);
        lin_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Settings.System.canWrite(Setting_Permission_Write.this)) {
                        Intent intent = new Intent(Setting_Permission_Write.this, Mirroring_Activity.class);
                        startActivity(intent);
                    } else {
                        openAndroidPermissionsMenu();
                    }
                }
            }
        });
        ll_go_to_seting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Settings.System.canWrite(Setting_Permission_Write.this)) {
                        Ads_Interstitial.showAds_full(Setting_Permission_Write.this, new Ads_Interstitial.OnFinishAds() {
                            @Override
                            public void onFinishAds(boolean b) {
                                Intent intent = new Intent(Setting_Permission_Write.this, Mirroring_Activity.class);
                                startActivity(intent);
                            }
                        });
                    } else {
                        openAndroidPermissionsMenu();
                    }
                }

               /* Ads.showAds(Setting_Permission_Write.this, new Ads.OnFinishAds() {
                    @Override
                    public void onFinishAds(boolean b) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (Settings.System.canWrite(Setting_Permission_Write.this)) {
                                Intent intent = new Intent(Setting_Permission_Write.this, Mirroring_Activity.class);
                                startActivity(intent);
                            } else {
                                openAndroidPermissionsMenu();
                            }
                        }
                    }
                });*/


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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_WRITE_SETTINGS_PERMISSION && Settings.System.canWrite(this)) {
            Log.d("TAG", "MainActivity.CODE_WRITE_SETTINGS_PERMISSION success");
            Toast.makeText(Setting_Permission_Write.this, "Permission Granted", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(Setting_Permission_Write.this, "Please Allow Permission First", Toast.LENGTH_SHORT).show();
        }
    }

    private void openAndroidPermissionsMenu() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + Setting_Permission_Write.this.getPackageName()));
        Setting_Permission_Write.this.startActivityForResult(intent, CODE_WRITE_SETTINGS_PERMISSION);
    }
}