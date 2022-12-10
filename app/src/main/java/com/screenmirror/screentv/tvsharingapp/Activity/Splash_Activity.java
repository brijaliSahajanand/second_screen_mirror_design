package com.screenmirror.screentv.tvsharingapp.Activity;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.net.VpnService;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ads.adsdemosp.AdsClass.Ads_AppOpen;
import com.ads.adsdemosp.AdsClass.Ads_SplashAppOpen;
import com.ads.adsdemosp.Appcontroller;
import com.ads.adsdemosp.Ids_Class;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.screenmirror.screentv.tvsharingapp.Activity.VPN.Sample_Connection;
import com.screenmirror.screentv.tvsharingapp.BuildConfig;
import com.screenmirror.screentv.tvsharingapp.ExtraClass.Preference;
import com.screenmirror.screentv.tvsharingapp.ExtraClass.Utils;
import com.screenmirror.screentv.tvsharingapp.R;
import com.screenmirror.screentv.tvsharingapp.retrofit.APIClient;
import com.screenmirror.screentv.tvsharingapp.retrofit.APIInterface;
import com.screenmirror.screentv.tvsharingapp.retrofit.Example;
import com.screenmirror.screentv.tvsharingapp.retrofit.Pro_IPModel;
import com.screenmirror.screentv.tvsharingapp.retrofit.TraficLimitResponse;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import unified.vpn.sdk.AuthMethod;
import unified.vpn.sdk.ClientInfo;
import unified.vpn.sdk.CompletableCallback;
import unified.vpn.sdk.HydraTransport;
import unified.vpn.sdk.HydraTransportConfig;
import unified.vpn.sdk.OpenVpnTransport;
import unified.vpn.sdk.OpenVpnTransportConfig;
import unified.vpn.sdk.RemainingTraffic;
import unified.vpn.sdk.SdkNotificationConfig;
import unified.vpn.sdk.SessionConfig;
import unified.vpn.sdk.TrackingConstants;
import unified.vpn.sdk.TrafficRule;
import unified.vpn.sdk.TransportConfig;
import unified.vpn.sdk.UnifiedSdk;
import unified.vpn.sdk.User;
import unified.vpn.sdk.VpnException;
import unified.vpn.sdk.VpnState;

import static com.screenmirror.screentv.tvsharingapp.ExtraClass.Utils.isdebuggle;
import static com.screenmirror.screentv.tvsharingapp.ExtraClass.Utils.warningdialog;

public class Splash_Activity extends BaseActivity {
    public static List<String> admob_native_list = new ArrayList<>();
    public static List<String> admob_native_banner_list = new ArrayList<>();
    public static List<String> admob_interstitial_list = new ArrayList<>();
    public static List<String> admob_app_open_list = new ArrayList<>();
    public static List<String> admob_adaptive_banner_list = new ArrayList<>();

    boolean vn_show = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_);

        Ads_AppOpen.isShowingAd = true;
        Appcontroller.fast_start = false;



       if (isdebuggle(Splash_Activity.this)) {
            Utils.Get_Developer_Dialog(Splash_Activity.this);
            Utils.debug_check(new Utils.no_debug() {
                @Override
                public void no_debug() {
                    Log.d("oncheck_debug", "splash ");
                    warningdialog.dismiss();
                    if (Utils.isNetworkConnected(Splash_Activity.this)) {
                        subScribePushChannel();
                    } else {
                        Utils.internetDialog(Splash_Activity.this);
                        Utils.internetcheck(new Utils.no_internet() {
                            @Override
                            public void no_internet() {
                                if (Utils.isNetworkConnected(Splash_Activity.this)) {
                                    subScribePushChannel();
                                    Utils.internet_dialog.dismiss();
                                }
                            }
                        });
                    }
                }
            });
        } else {
            subScribePushChannel();
        }


    }

    private void subScribePushChannel() {

        CheckUpSetting();

        try {
            FirebaseMessaging.getInstance().subscribeToTopic("Screencast_Latest_BZ_2")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void CallIpApi() {
        // Utils.Progress_show(Splash_Activity.this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ip-api.com/json/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIInterface apiInterface = retrofit.create(APIInterface.class);

        Call<Pro_IPModel> call = apiInterface.getipdata();
        call.enqueue(new Callback<Pro_IPModel>() {
            @Override
            public void onResponse(Call<Pro_IPModel> call, Response<Pro_IPModel> response) {
                // Utils.hideProgress();
                if (response.isSuccessful()) {
                    String Region = response.body().getRegionName();
                    String Country = response.body().getCountry();
                    String City = response.body().getCity();
                    Preference.setStateNameVN(Region);
                    Preference.setCountryNmeVN(Country);
                    Preference.setCityNmeVN(City);

//                    Log.d("publicip", "Region = " + Region);
//                    Log.d("publicip", "Country = " + Country);
//                    Log.d("publicip", "City = " + City);
                } else {
//                    Log.d("publicip", "onResponse: failed");
                }

                setting_api();
            }

            @Override
            public void onFailure(Call<Pro_IPModel> call, Throwable t) {
//                Log.d("publicip", "onfailure: failed");
                setting_api();
            }
        });
    }


    List<String> unknown_url_list = new ArrayList<>();
    public void setting_api() {

        HashMap<String, String> SendData = new HashMap<>();
        SendData.put("package", this.getPackageName());
        SendData.put("country", Preference.getCountryNmeVN());
        SendData.put("state", Preference.getStateNameVN());
        SendData.put("city", Preference.getCityNmeVN());

        APIInterface apiInterface_local = APIClient.getClient().create(APIInterface.class);
        Call<Example> call = apiInterface_local.setting_api(SendData);

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                if (response.isSuccessful()) {

                    Example.Package aPackage = response.body().getPackage();
//                    Log.d("spalsh_ads", "onResponse: " + new Gson().toJson(aPackage));
                    Preference.setads_click(aPackage.getAdsClick());
                    Preference.setmaintenance(aPackage.getMaintenance());
                    Preference.setback_click(aPackage.getBackClick());
                    Preference.set_id(aPackage.getId());
                    Preference.setPackage(aPackage.getPackage());

                    Preference.setversion_name(aPackage.getVersionName());
                    Preference.setapp_msg(aPackage.getAppMsg());
                    Preference.setupdate_url(aPackage.getUpdateUrl());
                    Preference.setis_update(aPackage.getIsUpdate());
                    Preference.setads_type(aPackage.getAdsType());
                    Preference.setsplash_ads_type(aPackage.getSplashAdsType());
                    Preference.setlink(aPackage.getLink());
                    Preference.setprivacy_policy(aPackage.getPrivacyPolicy());
                    Preference.setVn_header_show(response.body().getPackage().getVnHeaderShow());

                    admob_adaptive_banner_list = response.body().getPackage().getAdmob_banner_id_list();
                    admob_native_list = response.body().getPackage().getAdmob_native_id_list();
                    admob_native_banner_list = response.body().getPackage().getAdmob_bottom_native_id_list();
                    admob_interstitial_list = response.body().getPackage().getAdmob_interstitial_id_list();
                    admob_app_open_list = response.body().getPackage().getApp_open_id_list();


                    if (BuildConfig.DEBUG) {
//                        Preference.setads_type("admob");
//                        admob_adaptive_banner_list = new ArrayList<>();
//                        admob_native_list = new ArrayList<>();
//                        admob_native_banner_list = new ArrayList<>();
//                        admob_interstitial_list = new ArrayList<>();
//                        admob_app_open_list = new ArrayList<>();
//                        admob_adaptive_banner_list.add("ca-app-pub-3940256099942544/6300978111");
//                        admob_native_list.add("ca-app-pub-3940256099942544/2247696110");
//                        admob_native_banner_list.add("ca-app-pub-3940256099942544/2247696110");
//                        admob_interstitial_list.add("ca-app-pub-3940256099942544/1033173712");
//                        admob_app_open_list.add("ca-app-pub-3940256099942544/3419835294");
                    }


                    Preference.setAppOpen_click(aPackage.app_open_interstitial_click);
                    Preference.setAppOpen_back_click(aPackage.app_open_back_interstitial_click);
                    Preference.setAppOpen_inter_show(aPackage.app_open_interstitial_show);
                    // FirebaseAnalytics.getInstance(Splash_Activity.this).setAnalyticsCollectionEnabled(aPackage.firebaseanalytics);
                    Preference.setPrivacy_policy_html(response.body().privacypolicy);
                    Preference.setnative_button_color(aPackage.admob_native_btn_color);
                    Preference.setnative_btn_text_color(aPackage.admob_native_btn_text_color);
                    Preference.setcomingsoon(aPackage.comingsoon);
                    Preference.setquiz_header_show(aPackage.quiz_header_show);
                    Preference.setLetsgoscreen(aPackage.firstscreen);
                    Preference.setNextscreen(aPackage.secondscreen);
                    Preference.setNative_by_page(aPackage.native_by_page);
                    Preference.setquiz_show(response.body().getPackage().getIs_quiz());

                    Preference.setPrivacy_policy_html(response.body().privacypolicy);

                    Utils.country_List = response.body().countryList;
                    Preference.setCountry_list(Utils.country_List);
                    try {
                        Preference.setUrl_type(aPackage.url_type);
                        Preference.setUrl_default(aPackage.url_default);
                        unknown_url_list = response.body().getPackage().urlArrays;
                    } catch (Exception e) {

                    }

                    Preference.setIs_big_native_quiz(aPackage.is_big_native_quiz);
                    Preference.setIs_small_native_quiz(aPackage.is_small_native_quiz);

                    Preference.setadmob_reward_id(aPackage.admob_reward_id);
                    Preference.setFb_native_id(aPackage.fb_native_id);
                    Preference.setFb_native_banner_id(aPackage.fb_native_banner_id);
                    Preference.setFb_interstitial_id(aPackage.fb_interstitial_id);

                    Preference.setVn_direct_connect(response.body().getPackage().vn_direct_connect);

                    if (response.body().getPackage().is_start_with_zero) {
                        Preference.setFulladsArrayINT(0);
                        Preference.setNativeArrayINT(0);
                        Preference.setNativeBannerArrayINT(0);
                        Preference.setOpenAdsArrayINT(0);
                    }

                    try {
                        if (!response.body().getPackage().vnid.isEmpty() && !response.body().getPackage().vnpassword.isEmpty()) {
                            Preference.setVnid(response.body().getPackage().vnid);
                            Preference.setVnpassword(response.body().getPackage().vnpassword);
                        }
                    } catch (NullPointerException n) {
                        n.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (BuildConfig.DEBUG) {
                        vn_show = response.body().vn_show;
//                        Log.d("VPN_Show", "SHOW VPN:- " + response.body().vn_show);
                    } else {
                        vn_show = response.body().vn_show;
                    }

                    Preference.setVPN_Show(vn_show);

                    if (aPackage.bottom_ads_type.equals("nativebanner")) {
                        Preference.setisBottom_native(true);
                    } else {
                        Preference.setisBottom_native(false);
                    }

                    Preference.setBottomads_type(aPackage.bottom_ads_type);

                    //quiz
                    Preference.setAdmob_page(response.body().getPackage().admob_page);
                    Preference.setinter_admob(response.body().getPackage().inter_admob_click);
                    Preference.setnative_admob(response.body().getPackage().native_admob_click);
                    Preference.setbanner_admob(response.body().getPackage().banner_admob_click);
                    Preference.setAds_open_admob(response.body().getPackage().ads_open_admob_click);
                    Preference.setInter_back_admob(response.body().getPackage().inter_back_admob_click);
                    Preference.setRendomserver(response.body().getPackage().random_server);
                    Preference.set_server_short(response.body().getPackage().country_code);
                    Preference.setserver_name(response.body().getPackage().country_name);
                    Preference.setServer_image(response.body().getPackage().images_url);

                    Preference.setbuttonanimate(response.body().getPackage().ads_buttom_animation);
                    Preference.setanimationtype(response.body().getPackage().ads_button_animation_type);
                    Preference.setNative_btn_type(aPackage.native_btn_type);
                    Preference.setButton_animation_native(aPackage.button_animation_native);

                    Preference.setadmob_native_content_text_color(response.body().getPackage().admob_native_content_text_color);
                    Preference.setadmob_native_bg_color(response.body().getPackage().admob_native_bg_color);
                    Preference.setfb_native_btn_color(response.body().getPackage().fb_native_btn_color);
                    Preference.setfb_native_btn_text_color(response.body().getPackage().fb_native_btn_text_color);
                    Preference.setfb_native_content_text_color(response.body().getPackage().fb_native_content_text_color);
                    Preference.setfb_native_bg_color(response.body().getPackage().fb_native_bg_color);
                    Preference.setscreenshow(response.body().getPackage().screen_show);
                    Preference.setad_one_by_one_ids(response.body().getPackage().ad_one_by_one_ids);
                    Preference.setgift_view_show(response.body().getPackage().gift_view_show);
                    FirebaseAnalytics.getInstance(Splash_Activity.this).setAnalyticsCollectionEnabled(aPackage.firebaseanalytics);
//                    if (BuildConfig.DEBUG) {
//                        Debug_AdsSetUP();
//                    } else {
                    Release_AdsSetUP();
//                    }

                    if (response.body().getPackage().getMaintenance()) {
                        GetUnderMaintenanceDialog(1, response.body().getPackage().getAppMsg(), response.body().getPackage().getUpdateUrl());
                    } else {
                        if (compareVersionNames(GetVersionCode(), response.body().getPackage().getVersionName()) == -1) {
                            GetUnderMaintenanceDialog(2, response.body().getPackage().getAppMsg(), response.body().getPackage().getUpdateUrl());
                        } else {


                            int version = Integer.parseInt(response.body().getPackage().versionCode);
                            int real_version = BuildConfig.VERSION_CODE;
                            if (version == real_version) {
                                if (response.body().getPackage().update_coming_soon) {
                                    Preference.setcomingsoon(true);
                                    startActivity(new Intent(Splash_Activity.this, ComingsoonActivity.class));
                                    finish();
                                    return;
                                }
                            }

                            if (Preference.getVPN_Show()) {
                                initHydraSdk();
                            } else {
                                LoadAds();
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {

                Log.d("seeting_api", "onFailure: " + t.toString());
                Toast.makeText(Splash_Activity.this, "Something Wen't Wrong", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void IntentActivity() {

        if (!Preference.getcomingsoon()) {
            if (Preference.getVPN_Show()) {
                if (Preference.getVn_direct_connect()) {
                    AutoVNStart();
                    return;
                }
            }
        }
        LoadAds();
    }

    private void AutoVNStart() {
        if (Preference.getRendomserver()) {
            Utils.setUpCountry();
        }
        ConnectVN();
    }

    private void ConnectVN() {

        if (Preference.getisVpnConnect()) {
            Utils.vpnStart = true;// it will use after restart this activity
            status("connected");
//            startUIUpdateTask();
        } else {
            prepareVpn();
        }
    }

    private void prepareVpn() {
        if (!Utils.vpnStart) {
            Utils.isConnectingToInternet(Splash_Activity.this, new Utils.OnCheckNet() {
                @Override
                public void OnCheckNet(boolean b) {
                    if (b) {

                        Intent intent = VpnService.prepare(Splash_Activity.this);
                        if (intent != null) {
                            startActivityForResult(intent, 1);
                        } else {
                            startVpn();
                        }
                    } else {
                        finishAffinity();
                    }
                }
            });

        }
    }

    private void startVpn() {
        status("connecting");
        connectToVpn();
    }

    public void isLoggedIn(unified.vpn.sdk.Callback<Boolean> callback) {
        UnifiedSdk.getInstance().getBackend().isLoggedIn(callback);
    }

    public void connectToVpn() {
        isLoggedIn(new unified.vpn.sdk.Callback<Boolean>() {
            @Override
            public void success(@NonNull Boolean aBoolean) {
                if (aBoolean) {
                    List<String> fallbackOrder = new ArrayList<>();
                    fallbackOrder.add(HydraTransport.TRANSPORT_ID);
                    fallbackOrder.add(OpenVpnTransport.TRANSPORT_ID_TCP);
                    fallbackOrder.add(OpenVpnTransport.TRANSPORT_ID_UDP);
                    List<String> bypassDomains = new LinkedList<>();
                    bypassDomains.add("*facebook.com");
                    bypassDomains.add("*wtfismyip.com");
                    UnifiedSdk.getInstance().getVpn().start(new SessionConfig.Builder()
                            .withReason(TrackingConstants.GprReasons.M_UI)
                            .withTransportFallback(fallbackOrder)
                            .withVirtualLocation(Preference.getServer_short().toLowerCase())
                            .withTransport(HydraTransport.TRANSPORT_ID)
                            .addDnsRule(TrafficRule.Builder.bypass().fromDomains(bypassDomains))
                            .build(), new CompletableCallback() {
                        @Override
                        public void complete() {
                            //Log.d("MainActivity12", "complete");
                            Utils.vpnStart = true;// it will use after restart this activity
                            status("connected");
//                            startUIUpdateTask();
                        }

                        @Override
                        public void error(@NonNull VpnException e) {
                            //Log.d("MainActivity12", "error = " + e.getMessage());
                            status("connect");
                            Utils.vpnStart = false;


                            if (e.getMessage().contains("TRAFFIC_EXCEED")) {
                                Set_Limit_size();
                            } else {
                                LoadAds();
                            }
                        }
                    });
                }
            }

            @Override
            public void failure(@NonNull VpnException e) {
                LoadAds();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Utils.isConnectingToInternet(Splash_Activity.this, new Utils.OnCheckNet() {
                @Override
                public void OnCheckNet(boolean b) {
                    if (b) {
                        startVpn();
                    } else {
                        finishAffinity();
                    }
                }
            });
        } else {
            LoadAds();
        }
    }
    private Handler mUIHandler = new Handler(Looper.getMainLooper());
    final Runnable mUIUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            checkRemainingTraffic();
            mUIHandler.postDelayed(mUIUpdateRunnable, 10000);
        }
    };

    private void checkRemainingTraffic() {
        UnifiedSdk.getInstance().getBackend().remainingTraffic(new unified.vpn.sdk.Callback<RemainingTraffic>() {
            @Override
            public void success(RemainingTraffic remainingTraffic) {
                updateRemainingTraffic(remainingTraffic);
            }

            @Override
            public void failure(VpnException e) {
            }
        });
    }


    protected void updateRemainingTraffic(RemainingTraffic remainingTrafficResponse) {

        String trafficUsed = megabyteCount(remainingTrafficResponse.getTrafficUsed());
        String trafficLimit = megabyteCount(remainingTrafficResponse.getTrafficLimit()) + "Mb";
        if (remainingTrafficResponse.getTrafficLimit() <= remainingTrafficResponse.getTrafficUsed()) {
            mUIHandler.removeCallbacks(mUIUpdateRunnable);
            Set_Limit_size();
        }
    }

    public static String megabyteCount(long bytes) {
        return String.format(Locale.getDefault(), "%.0f", (double) bytes / 1024 / 1024);
    }


    public void status(String status) {
        //Log.d("MainActivity12", "status = " + status);
        if (status.equals("connect")) {
            Utils.vpnStart = false;
            Preference.setisVpnConnect(false);
        } else if (status.equals("connecting")) {
            Preference.setisVpnConnect(false);
        } else if (status.equals("connected")) {

            Preference.setisVpnConnect(true);
            LoadAds();

        }
    }

    private void Set_Limit_size() {
        int New_limit_traffic = 1000;
        long total_bytes = New_limit_traffic * 1048576;
        Delete_ApiCall(total_bytes);
    }


    private void Delete_ApiCall(long total_bytes) {
        Utils.showProgressDialog(this);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api-prod.northghost.com/partner/subscribers/")
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        APIInterface mApiInterface = retrofit.create(APIInterface.class);
        Call<TraficLimitResponse> call = mApiInterface.Call_Delete_Trafic(String.valueOf(Preference.getAura_user_id()) + "/traffic?access_token=" + Preference.getAccessToken());
        call.enqueue(new Callback<TraficLimitResponse>() {
            @Override
            public void onResponse(Call<TraficLimitResponse> call, Response<TraficLimitResponse> response) {
                Utils.hideProgressDialog();
                if (response.isSuccessful()) {
                    Add_Trafic_size(total_bytes);
                } else {
                    LoadAds();
                }

            }

            @Override
            public void onFailure(Call<TraficLimitResponse> call, Throwable t) {
                Utils.hideProgressDialog();
                LoadAds();
            }
        });
    }

    private void Add_Trafic_size(long total_bytes) {
        Utils.showProgressDialog(this);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api-prod.northghost.com/partner/subscribers/")
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        APIInterface mApiInterface = retrofit.create(APIInterface.class);
        Call<TraficLimitResponse> call = mApiInterface.Call_Add_Trafic(String.valueOf(Preference.getAura_user_id()) + "/traffic?access_token=" + Preference.getAccessToken() + "&traffic_limit=" + String.valueOf(total_bytes));
        call.enqueue(new Callback<TraficLimitResponse>() {
            @Override
            public void onResponse(Call<TraficLimitResponse> call, Response<TraficLimitResponse> response) {
                Utils.hideProgressDialog();
                if (response.isSuccessful()) {
                    LoadAds();
                } else {
                    LoadAds();
                }

            }

            @Override
            public void onFailure(Call<TraficLimitResponse> call, Throwable t) {
                Utils.hideProgressDialog();
                LoadAds();
            }
        });
    }


    Dialog undermaintenancedialog;

    TextView tv_maintenance_msg;
    Button btn_ok, btn_cancel;

    private void GetUnderMaintenanceDialog(int flag, String Maintenance, String link) {

        undermaintenancedialog = new Dialog(Splash_Activity.this);
        undermaintenancedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        undermaintenancedialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        undermaintenancedialog.setContentView(R.layout.dialog_undermaintenanace);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        final int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        undermaintenancedialog.getWindow().setLayout(width, height);
        undermaintenancedialog.setCancelable(false);
        undermaintenancedialog.show();

        tv_maintenance_msg = undermaintenancedialog.findViewById(R.id.tv_maintenance_msg);
        btn_ok = undermaintenancedialog.findViewById(R.id.btn_ok);
        btn_cancel = undermaintenancedialog.findViewById(R.id.btn_cancel);

        if (flag == 2) {
            btn_ok.setText("Update");
            btn_cancel.setVisibility(View.VISIBLE);
        }

        if (flag == 3) {
            tv_maintenance_msg.setText("Internet is required to use this app");
        } else {
            tv_maintenance_msg.setText(Maintenance);
        }


        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag == 1) {
                    finish();
                } else if (flag == 2) {

                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
                    }
                } else if (flag == 3) {
                    finish();
                }

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 1) {
                    undermaintenancedialog.dismiss();
                    LoadAds();

                } else {
                    finish();
                }
            }
        });

    }


    public int compareVersionNames(String oldVersionName, String newVersionName) {
        int res = 0;

        String[] oldNumbers = oldVersionName.split("\\.");
        String[] newNumbers = newVersionName.split("\\.");
        int maxIndex = Math.min(oldNumbers.length, newNumbers.length);

        for (int i = 0; i < maxIndex; i++) {
            int oldVersionPart = Integer.valueOf(oldNumbers[i]);
            int newVersionPart = Integer.valueOf(newNumbers[i]);
            if (oldVersionPart < newVersionPart) {
                res = -1;
                break;
            } else if (oldVersionPart > newVersionPart) {
                res = 1;
                break;
            }
        }

        if (res == 0 && oldNumbers.length != newNumbers.length) {
            res = (oldNumbers.length > newNumbers.length) ? 1 : -1;
        }
        return res;
    }

    private String GetVersionCode() {
        String version = null;
        PackageManager manager = getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(getPackageName(), 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            version = "1.0";
        }
//        String version = info.versionName;
        return version;
    }


    CountDownTimer countDownTimer = null;

    private void LoadAds() {

        if (!Preference.getcomingsoon()) {
            LoadNativeAds();
        }

        Ads_SplashAppOpen.Splash_OpenAppAds_Load(Splash_Activity.this, new Ads_SplashAppOpen.OnFinishAds() {
            @Override
            public void onFinishAds(boolean b) {
                if(Preference.getVPN_Show()) {
                    UnifiedSdk.getVpnState(new unified.vpn.sdk.Callback<VpnState>() {
                        @Override
                        public void success(@NonNull VpnState vpnState) {
                            if (vpnState == VpnState.CONNECTED) {

                            } else {
                                Utils.vpnStart = false;
                                Preference.setisVpnConnect(false);
                            }

                            IntentActivy();
                        }

                        @Override
                        public void failure(@NonNull VpnException e) {
                            Utils.vpnStart = false;
                            Preference.setisVpnConnect(false);
                            IntentActivy();
                        }
                    });
                }else {
                    IntentActivy();
                }
            }
        });
    }

    public static int fromstart = 0;

    private void IntentActivy() {

        if (Preference.getcomingsoon()) {
            if (Preference.getisVpnConnect()) {
                disconnectFromVnp();
            } else {
                startActivity(new Intent(Splash_Activity.this, ComingsoonActivity.class));
                finish();
            }
        } else {

            if (Preference.getisVpnConnect()) {
                if (!Preference.getVPN_Show()) {
                    disconnectFromVnp();
                } else {

                    startActivity(new Intent(Splash_Activity.this, First_Activity.class));
                    finish();


                }
            } else {
                if (Preference.getVPN_Show()) {
                    Intent intent = new Intent(Splash_Activity.this, Sample_Connection.class);
                    intent.putExtra("type_connection", "connection");
                    startActivity(intent);
                    finish();
                } else {

                    if (Preference.getisVpnConnect()) {
                        disconnectFromVnp();
                    } else {

                        startActivity(new Intent(Splash_Activity.this, First_Activity.class));
                        finish();


                    }

                }
            }
        }


    }

    public void disconnectFromVnp() {
        UnifiedSdk.getInstance().getVpn().stop(TrackingConstants.GprReasons.M_UI, new CompletableCallback() {
            @Override
            public void complete() {
                Utils.vpnStart = false;

                if (Preference.getcomingsoon()) {
                    startActivity(new Intent(Splash_Activity.this, ComingsoonActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(Splash_Activity.this, First_Activity.class));
                    finish();
                }

            }

            @Override
            public void error(@NonNull VpnException e) {
                //Toast.makeText(Pro_MainActivity.this, "Disconnect error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public boolean vpn() {
        String iface = "";
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface.isUp())
                    iface = networkInterface.getName();
                Log.d("DEBUG", "IFACE NAME: " + iface);
                if (iface.contains("tun") || iface.contains("ppp") || iface.contains("pptp")) {
                    return true;
                }
            }
        } catch (SocketException e1) {
            e1.printStackTrace();
        }

        return false;
    }


    Dialog Waringdialog;
    TextView tv_maintenance_msg1;
    Button btn_ok1, btn_cancel1;

    private void GetWaringDialog() {

        Waringdialog = new Dialog(Splash_Activity.this);
        Waringdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Waringdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Waringdialog.setContentView(R.layout.dialog_undermaintenanace);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        final int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        Waringdialog.getWindow().setLayout(width, height);
        Waringdialog.setCancelable(false);
        Waringdialog.show();

        tv_maintenance_msg1 = Waringdialog.findViewById(R.id.tv_maintenance_msg);
        btn_ok1 = Waringdialog.findViewById(R.id.btn_ok);
        btn_cancel1 = Waringdialog.findViewById(R.id.btn_cancel);


        tv_maintenance_msg1.setText("Please disconnect other VPN, after use our APP!");
        btn_ok1.setText("Setting");
        btn_cancel1.setVisibility(View.VISIBLE);
        btn_cancel1.setText("Ok");
        btn_ok1.setVisibility(View.GONE);


        btn_ok1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Waringdialog.dismiss();
                Intent intent = new Intent("android.net.vpn.SETTINGS");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 10);

            }
        });

        btn_cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Waringdialog.dismiss();
                finishAffinity();
            }
        });

    }


    private void Release_AdsSetUP() {

        Ids_Class.admobInterstitialIds_list = admob_interstitial_list;

        Ids_Class.admobNativeFullIds_list = admob_native_list;

        Ids_Class.admobNativeCustomIds_list = admob_native_banner_list;

        Ids_Class.admobBannerIds_list = admob_adaptive_banner_list;

        Ids_Class.admobAppopenIds_list = admob_app_open_list;

        Ids_Class.admob_Rewards_ids = Preference.getadmob_reward_id();

        Ids_Class.fb_Interstitial_ids = Preference.getFb_interstitial_id();
        Ids_Class.fb_NativeFull_ids = Preference.getFb_native_id();
        Ids_Class.fb_NativeBanner_ids = Preference.getFb_native_banner_id();

        Ids_Class.ads_type = Preference.getads_type();
        Ids_Class.ads_native_type = Preference.getBottomads_type();

        Ids_Class.ads_native_btn_color = Preference.getnative_button_color();
        Ids_Class.ads_native_text_color = Preference.getnative_btn_text_color();
        Ids_Class.ads_native_bg_color = Preference.getadmob_native_bg_color();
        Ids_Class.ads_native_in_text_color = Preference.getadmob_native_content_text_color();

        Ids_Class.buttonanimate = Preference.getbuttonanimate();
        Ids_Class.animation_type = Preference.getanimationtype();
        Ids_Class.button_animation_native = Preference.getButton_animation_native();
        Ids_Class.native_btn_type = Preference.getNative_btn_type();

        Ids_Class.Interstitial_adsclick = Preference.getads_click();
        Ids_Class.Interstitial_Backadsclick = Preference.getback_click();
        Ids_Class.Native_adsscreen = Preference.getNative_by_page();

        Ids_Class.ads_App_open_InterstitialAd = Preference.getAppOpen_inter_show();
        Ids_Class.AppOpen_adsclick = Preference.getAppOpen_click();
        Ids_Class.AppOpen_Backadsclick = Preference.getAppOpen_back_click();

        Ids_Class.ads_quiz_show = Preference.getquiz_show();

        Ids_Class.ads_quiz_by_page_show = Preference.getAdmob_page();
        Ids_Class.quiz_Interstitial_adsclick = Preference.getinter_admob();
        Ids_Class.quiz_Native_adsclick = Preference.getnative_admob();
        Ids_Class.quiz_Banner_adsclick = Preference.getbanner_admob();
        Ids_Class.quiz_AppOpen_adsclick = Preference.getAds_open_admob();
        Ids_Class.quiz_Interstitial_backadsclick = Preference.getInter_back_admob();

        Ids_Class.is_big_native_quiz = Preference.getIs_big_native_quiz();
        Ids_Class.is_small_native_quiz = Preference.getIs_small_native_quiz();
        Ids_Class.quiz_header_show = Preference.getquiz_header_show();

        Ids_Class.fb_ads_native_btn_color = Preference.getfb_native_btn_color();
        Ids_Class.fb_ads_native_text_color = Preference.getfb_native_btn_text_color();
        Ids_Class.fb_ads_native_bg_color = Preference.getfb_native_bg_color();
        Ids_Class.fb_ads_native_in_txt_color = Preference.getfb_native_content_text_color();

        Ids_Class.ad_one_by_one_ids = Preference.getad_one_by_one_ids();

        Ids_Class.quizLink = Preference.getlink();
    }

    private void LoadNativeAds() {
        Ids_Class.Laod_NativeAds(Splash_Activity.this);
    }


    private void CheckUpSetting() {

        UnifiedSdk.getVpnState(new unified.vpn.sdk.Callback<VpnState>() {
            @Override
            public void success(@NonNull VpnState vpnState) {

                switch (vpnState) {
                    case IDLE: {
                        Preference.setisVpnConnect(false);
                        Utils.vpnStart = false;
                        if (!vpn()) {
                            if (Preference.getCountryNmeVN().isEmpty() || Preference.getStateNameVN().isEmpty() || Preference.getCityNmeVN().isEmpty()) {
                                CallIpApi();
                            } else {
                                setting_api();
                            }
                        } else {
                            GetWaringDialog();
                        }
                        break;
                    }
                    case CONNECTED: {
                        Preference.setisVpnConnect(true);
                        Utils.vpnStart = true;
                        setting_api();
                        break;
                    }
                    case CONNECTING_VPN: {
                        break;
                    }

                    case CONNECTING_CREDENTIALS:
                    case CONNECTING_PERMISSIONS:
                    case PAUSED: {
                        break;
                    }
                }
            }

            @Override
            public void failure(@NonNull VpnException e) {
                Preference.setisVpnConnect(false);
                if (!vpn()) {
                    if (Preference.getCountryNmeVN().isEmpty() || Preference.getStateNameVN().isEmpty() || Preference.getCityNmeVN().isEmpty()) {
                        CallIpApi();
                    } else {
                        setting_api();
                    }
                } else {
                    GetWaringDialog();
                }
            }
        });
    }


    //////////////////For Vpn


    UnifiedSdk unifiedSDK;
    String publicVpnKey = "";
    String PasswordVpn = "";

    public void initHydraSdk() {

        publicVpnKey = Preference.getVnid();
        PasswordVpn = Preference.getVnpassword();

        createNotificationChannel();

        ClientInfo clientInfo;

        if (Preference.getUrl_type()) {
            clientInfo = ClientInfo.newBuilder()
                    .addUrls(unknown_url_list)
                    .carrierId(publicVpnKey)
                    .build();
        } else {
            clientInfo = ClientInfo.newBuilder()
                    .addUrl(Preference.getUrl_default())
                    .carrierId(publicVpnKey)
                    .build();
        }

        List<TransportConfig> transportConfigList = new ArrayList<>();
        transportConfigList.add(HydraTransportConfig.create());
        transportConfigList.add(OpenVpnTransportConfig.tcp());
        transportConfigList.add(OpenVpnTransportConfig.udp());
        UnifiedSdk.update(transportConfigList, CompletableCallback.EMPTY);
        unifiedSDK = UnifiedSdk.getInstance(clientInfo);
        SdkNotificationConfig notificationConfig = SdkNotificationConfig.newBuilder()
                .title(getResources().getString(R.string.app_name))
                .channelId(CHANNEL_ID)
                .build();
        UnifiedSdk.update(notificationConfig);
        loginToVpn();

    }

    private static final String CHANNEL_ID = "VPNMaster";

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "VPNMaster";
            String description = "VPN notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    public void loginToVpn() {
        Log.e("MainActivity12", "loginToVpn");
        AuthMethod authMethod = AuthMethod.anonymous();
        UnifiedSdk.getInstance().getBackend().login(authMethod, new unified.vpn.sdk.Callback<User>() {
            @Override
            public void success(@NonNull User user) {
                Preference.setAura_user_id(user.getSubscriber().getId());
                LoginAPi_Token();
            }

            @Override
            public void failure(@NonNull VpnException e) {
                Preference.setVPN_Show(false);
                LoadAds();
                Log.e("MainActivity12", "success = " + e);
//                IntentActivity();
            }
        });

    }


    private void LoginAPi_Token() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api-prod.northghost.com/partner/")
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        APIInterface apiInterface_local = retrofit.create(APIInterface.class);
        Call<TraficLimitResponse> call = apiInterface_local.Call_Add_Trafic("login?login=" + publicVpnKey + "&password=" + PasswordVpn);
        call.enqueue(new Callback<TraficLimitResponse>() {
            @Override
            public void onResponse(Call<TraficLimitResponse> call, Response<TraficLimitResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().result.equals("OK")) {
                        Preference.setAccessToken(response.body().access_token);
                        IntentActivity();
                    } else {
                        IntentActivity();
                    }
                } else {
                    IntentActivity();
                }
            }

            @Override
            public void onFailure(Call<TraficLimitResponse> call, Throwable t) {
                IntentActivity();
            }
        });
    }


}