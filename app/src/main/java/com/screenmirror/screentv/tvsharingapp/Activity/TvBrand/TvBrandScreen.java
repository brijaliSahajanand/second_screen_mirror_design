package com.screenmirror.screentv.tvsharingapp.Activity.TvBrand;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ads.adsdemosp.AdsClass.Ads_Interstitial;
import com.screenmirror.screentv.tvsharingapp.Activity.BaseActivity;
import com.screenmirror.screentv.tvsharingapp.Activity.CastToTv.Mirroring_Activity;
import com.screenmirror.screentv.tvsharingapp.Activity.CastToTv.Setting_Permission_Write;
import com.screenmirror.screentv.tvsharingapp.Activity.MainActivity;
import com.screenmirror.screentv.tvsharingapp.Activity.TvBrand.Adapter.TvBrandAdapter;
import com.screenmirror.screentv.tvsharingapp.R;


import java.util.ArrayList;
import java.util.List;



public class TvBrandScreen extends BaseActivity {

    //View
    ImageView iv_back, iv_cast;
    RecyclerView rv_tv_brand;
    List<String> tvbrandlist = new ArrayList<>();

    //Variable


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_brand_screen);

        Declaration();
        Intialization();
    }

    private void Declaration() {
        iv_back = findViewById(R.id.iv_back);
        rv_tv_brand = findViewById(R.id.rv_tv_brand);
        iv_cast = findViewById(R.id.iv_cast);



    }

    private void Intialization() {


        iv_cast.setOnClickListener(v -> {
            Ads_Interstitial.showAds_full(TvBrandScreen.this, new Ads_Interstitial.OnFinishAds() {
                @Override
                public void onFinishAds(boolean b) {
                    startActivity(new Intent(TvBrandScreen.this, Setting_Permission_Write.class));
                }
            });
        });

        rv_tv_brand.setLayoutManager(new LinearLayoutManager(this));


        tvbrandlist.add("SONY Bravia TV");
        tvbrandlist.add("Samsung Smart TV");
        tvbrandlist.add("LG Smart TV");
        tvbrandlist.add("TCL TV");
        tvbrandlist.add("SHARP Aquos");
        tvbrandlist.add("AOC TV");
        tvbrandlist.add("Hisense TV");
        tvbrandlist.add("Insignia TV");
        tvbrandlist.add("PHILIPS TV");
        tvbrandlist.add("Arcelik TV");
        tvbrandlist.add("Vestel TV");
        tvbrandlist.add("Element TV");
        tvbrandlist.add("JVC TV");
        tvbrandlist.add("RCA TV");
        tvbrandlist.add("Magnavox TV");
        tvbrandlist.add("Haier TV");
        tvbrandlist.add("PHILIPS TV");
        tvbrandlist.add("Razor Forge TV");
        tvbrandlist.add("LeEco");
        tvbrandlist.add("Google Nexus");
        tvbrandlist.add("Xiaomi Mi Box");
        tvbrandlist.add("LMT TV");
        tvbrandlist.add("Nvidia Shield");
        tvbrandlist.add("LEONE T LifeStick");
        tvbrandlist.add("Toshiba TV");
        tvbrandlist.add("Sanyo TV");
        tvbrandlist.add("Skyworth TV");
        tvbrandlist.add("Westinghouse TV");
        tvbrandlist.add("Westinghouse TV");
        tvbrandlist.add("Thomson TV");
        tvbrandlist.add("BAUHN TV");
        tvbrandlist.add("Infomir Magic Box");
        tvbrandlist.add("Vodafone TV");
        tvbrandlist.add("KAON 4K");
        tvbrandlist.add("FreeBox Mini 4K");
        tvbrandlist.add("Tsuyata Stick");
        tvbrandlist.add("lundl");
        tvbrandlist.add("Aconatic");
        tvbrandlist.add("Aiwa TV");
        tvbrandlist.add("ANAM");
        tvbrandlist.add("Anker");
        tvbrandlist.add("ASANZO");
        tvbrandlist.add("Asus");
        tvbrandlist.add("Ayonz");
        tvbrandlist.add("BenQ");
        tvbrandlist.add("Blaupunkt");
        tvbrandlist.add("Casper");
        tvbrandlist.add("CG");
        tvbrandlist.add("Changhong");
        tvbrandlist.add("Chimei");
        tvbrandlist.add("CHiQ");
        tvbrandlist.add("Condor");
//        tvbrandlist.add("Dish TV");
        tvbrandlist.add("Eko");
        tvbrandlist.add("Elsys");
        tvbrandlist.add("Ematic");
//        tvbrandlist.add("ENTV");
//        tvbrandlist.add("EPSON");
//        tvbrandlist.add("ESTLA");
        tvbrandlist.add("Foxcom");
        tvbrandlist.add("FPT Play");
//        tvbrandlist.add("Funai");
//        tvbrandlist.add("Globe Telecom");
//        tvbrandlist.add("Haier");
        tvbrandlist.add("Hansung");
//        tvbrandlist.add("Hisense");
        tvbrandlist.add("HORIZON");
        tvbrandlist.add("iFFalcon");
        tvbrandlist.add("Infinix");
//        tvbrandlist.add("Iriver");
        tvbrandlist.add("Itel");
        tvbrandlist.add("JBL");
        tvbrandlist.add("JVC");
//        tvbrandlist.add("KIVI");
        tvbrandlist.add("KODAK");
        tvbrandlist.add("Kogan");
//        tvbrandlist.add("KOODA");
//        tvbrandlist.add("Linsar");
//        tvbrandlist.add("Llyod");
//        tvbrandlist.add("LUCOMS");
//        tvbrandlist.add("Marcel");
        tvbrandlist.add("MarQ");
        tvbrandlist.add("Mediabox");
        tvbrandlist.add("Micromax");
        tvbrandlist.add("Motorola");
        tvbrandlist.add("MyBox");
        tvbrandlist.add("Nokia");
        tvbrandlist.add("OnePlus");
//        tvbrandlist.add("Orange");
        tvbrandlist.add("Panasonic");
//        tvbrandlist.add("PIXELA");
//        tvbrandlist.add("Polaroid");
//        tvbrandlist.add("PRISM Korea");
//        tvbrandlist.add("RCA");
//        tvbrandlist.add("RFL Electronics");
////        tvbrandlist.add("Robi Axiata");
////        tvbrandlist.add("Sceptre");
//        tvbrandlist.add("Seiki");
//        tvbrandlist.add("SFR");
//        tvbrandlist.add("SMARTEVER");
//        tvbrandlist.add("SONIQ Australia");
//        tvbrandlist.add("Syinix");
//        tvbrandlist.add("Telekom Malaysia");
//        tvbrandlist.add("Tempo");
//        tvbrandlist.add("theham");
//        tvbrandlist.add("TPV (Philips EMEA)");
//        tvbrandlist.add("Truvii");
//        tvbrandlist.add("Turbo-X");
//        tvbrandlist.add("UMAX");
//        tvbrandlist.add("Videostrong");
//        tvbrandlist.add("VinSmart");
//        tvbrandlist.add("VU Television");
//        tvbrandlist.add("Witooth");
//        tvbrandlist.add("XGIMI Technology");
//        tvbrandlist.add("ATVIO");
//        tvbrandlist.add("InFocus");
//        tvbrandlist.add("Element");
//        tvbrandlist.add("Hitachi");
//        tvbrandlist.add("Onn");
//        tvbrandlist.add("Polaroid");
//        tvbrandlist.add("Daewoo");
//        tvbrandlist.add("Kalley");
//        tvbrandlist.add("Ecostar");
//        tvbrandlist.add("Coocaa");
//        tvbrandlist.add("Hathway");
//        tvbrandlist.add("HQ");
//        tvbrandlist.add("Konka");
//        tvbrandlist.add("Premier");
//        tvbrandlist.add("Riviera");
//        tvbrandlist.add("EON Smart Box");
//        tvbrandlist.add("B UHD");
//        tvbrandlist.add("Artel");
//        tvbrandlist.add("Metz");
//        tvbrandlist.add("Orient");
//        tvbrandlist.add("Mystery");
//        tvbrandlist.add("ELENBERG");
//        tvbrandlist.add("Prpqtiain");
//        tvbrandlist.add("Prestigio");
//        tvbrandlist.add("TIM Vision Box");
//        tvbrandlist.add("Philco");
//        tvbrandlist.add("Hi Level");
//        tvbrandlist.add("Ghia");
//        tvbrandlist.add("Iris");
//        tvbrandlist.add("Prestiz");
//        tvbrandlist.add("Axen");
//        tvbrandlist.add("Noblex");
//        tvbrandlist.add("Indurama");
//        tvbrandlist.add("Stream");
        tvbrandlist.add("Onida");
        tvbrandlist.add("Sinotec");
        tvbrandlist.add("Polytron");
        tvbrandlist.add("QAAIN/IA");
        tvbrandlist.add("Sansui");
        tvbrandlist.add("RealMe");


        TvBrandAdapter tvBrandAdapter = new TvBrandAdapter(TvBrandScreen.this, tvbrandlist, new TvBrandAdapter.tvnameclick() {
            @Override
            public void tvname() {
                onPermissionGranted();

            }
        });
        rv_tv_brand.setAdapter(tvBrandAdapter);


        iv_back.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void onPermissionGranted() {
        Ads_Interstitial.showAds_full(TvBrandScreen.this, new Ads_Interstitial.OnFinishAds() {
            @Override
            public void onFinishAds(boolean b) {
                startActivity(new Intent(TvBrandScreen.this, MainActivity.class));
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
}