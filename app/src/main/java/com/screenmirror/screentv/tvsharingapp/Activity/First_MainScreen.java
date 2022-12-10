package com.screenmirror.screentv.tvsharingapp.Activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.ads.adsdemosp.AdsClass.Ads_Interstitial;
import com.screenmirror.screentv.tvsharingapp.Activity.CastToTv.Setting_Permission_Write;
import com.screenmirror.screentv.tvsharingapp.Activity.Help.HelpScreen;
import com.screenmirror.screentv.tvsharingapp.Activity.TvBrand.TvBrandScreen;
import com.screenmirror.screentv.tvsharingapp.Activity.VPN.Sample_Connection;
import com.screenmirror.screentv.tvsharingapp.BuildConfig;
import com.screenmirror.screentv.tvsharingapp.ExtraClass.Preference;
import com.screenmirror.screentv.tvsharingapp.R;


public class First_MainScreen extends BaseActivity {
    ImageView iv_screen_mirror, iv_connect_guide, iv_casttotv, iv_vpn ,ln_privacy_app ,ln_share_app,ln_rate_us;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first__main_screen);

        Declaration();
        Intialization();


    }


    private void Declaration() {

        ln_rate_us = findViewById(R.id.ln_rate_us);
        ln_share_app = findViewById(R.id.ln_share_app);
        ln_privacy_app = findViewById(R.id.ln_privacy_app);
        iv_screen_mirror = findViewById(R.id.iv_screen_mirror);
        iv_connect_guide = findViewById(R.id.iv_connect_guide);
        iv_casttotv = findViewById(R.id.iv_casttotv);
        iv_vpn = findViewById(R.id.iv_vpn);

        if (Preference.getVPN_Show() && Preference.getVn_header_show()) {
            iv_vpn.setVisibility(View.VISIBLE);
        } else {
            iv_vpn.setVisibility(View.GONE);
        }

        iv_vpn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ads_Interstitial.showAds_full(First_MainScreen.this, new Ads_Interstitial.OnFinishAds() {
                    @Override
                    public void onFinishAds(boolean b) {
                        startActivity(new Intent(First_MainScreen.this, Sample_Connection.class)
                                .putExtra("type_connection", "disconnect"));
                    }
                });
            }
        });


        ln_privacy_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Preference.getprivacy_policy().isEmpty()) {

                    String urlString = Preference.getprivacy_policy();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setPackage("com.android.chrome");
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        // Chrome browser presumably not installed so allow user to choose instead
                        intent.setPackage(null);
                        startActivity(intent);
                    }
                }
            }
        });
        ln_share_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                    String shareMessage = "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });
        ln_rate_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID));
                startActivity(intent);
            }
        });



    }

    private void Intialization() {

        iv_screen_mirror.setOnClickListener(v -> {
            Ads_Interstitial.showAds_full(this, new Ads_Interstitial.OnFinishAds() {
                @Override
                public void onFinishAds(boolean b) {
                    startActivity(new Intent(First_MainScreen.this, Setting_Permission_Write.class));
                }
            });
        });

        iv_casttotv.setOnClickListener(v -> {
            Ads_Interstitial.showAds_full(this, new Ads_Interstitial.OnFinishAds() {
                @Override
                public void onFinishAds(boolean b) {
                    startActivity(new Intent(First_MainScreen.this, TvBrandScreen.class));
                }
            });

        });

        iv_connect_guide.setOnClickListener(v -> {
            Ads_Interstitial.showAds_full(this, new Ads_Interstitial.OnFinishAds() {
                @Override
                public void onFinishAds(boolean b) {
                    startActivity(new Intent(First_MainScreen.this, HelpScreen.class));
                }
            });
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        /*try {

            VideoView view = (VideoView) findViewById(R.id.videoview);
            String path = "android.resource://" + getPackageName() + "/" + R.raw.bgvideo;
            view.setVideoURI(Uri.parse(path));
            view.start();
            view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                }
            });

        } catch (Exception exception) {

        }*/



//        setStatusBarTransparent();
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