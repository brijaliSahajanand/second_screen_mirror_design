package com.screenmirror.screentv.tvsharingapp.Activity.VPN;

import android.content.Intent;
import android.content.res.Resources;
import android.net.VpnService;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ads.adsdemosp.AdsClass.Ads_Adapter_List;
import com.ads.adsdemosp.AdsClass.Ads_ExitNativeFull;
import com.ads.adsdemosp.AdsClass.Ads_Interstitial;
import com.ads.adsdemosp.AdsClass.Ads_VN_Screen;

import com.bumptech.glide.Glide;
import com.screenmirror.screentv.tvsharingapp.Activity.BaseActivity;
import com.screenmirror.screentv.tvsharingapp.ExtraClass.Preference;
import com.screenmirror.screentv.tvsharingapp.ExtraClass.Utils;
import com.screenmirror.screentv.tvsharingapp.R;
import com.screenmirror.screentv.tvsharingapp.retrofit.APIClient;
import com.screenmirror.screentv.tvsharingapp.retrofit.APIInterface;
import com.screenmirror.screentv.tvsharingapp.retrofit.TraficLimitResponse;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;


import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import unified.vpn.sdk.Callback;
import unified.vpn.sdk.CompletableCallback;
import unified.vpn.sdk.HydraTransport;
import unified.vpn.sdk.OpenVpnTransport;
import unified.vpn.sdk.RemainingTraffic;
import unified.vpn.sdk.SessionConfig;
import unified.vpn.sdk.TrackingConstants;
import unified.vpn.sdk.TrafficRule;
import unified.vpn.sdk.UnifiedSdk;
import unified.vpn.sdk.VpnException;

import static com.screenmirror.screentv.tvsharingapp.ExtraClass.Utils.Exit_Dialog;


public class Sample_Connection extends BaseActivity {

    LinearLayout lnr_connecter;
    TextView txt_status, tv_country;
    MKLoader mkloaderReg;
    ImageView img_off_on, iv_flag, imgBack;

    LinearLayout rlt_disconnect_layout;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect__v_p_n__screen2);

//        LinearLayout llnative = findViewById(R.id.llnative);
//        TextView tv_ad_text_native2 = findViewById(R.id.tv_ad_text_native2);
//        NativeAds.Native_adtype(this, llnative, "native");



        lnr_connecter = findViewById(R.id.lnr_connecter);
        txt_status = findViewById(R.id.txt_status);
        mkloaderReg = findViewById(R.id.mkloaderReg);
        img_off_on = findViewById(R.id.img_off_on);
        iv_flag = findViewById(R.id.iv_flag);
        imgBack = findViewById(R.id.imgBack);

        tv_country = findViewById(R.id.tv_country);


        if (Preference.getRendomserver()) {
            Utils.setUpCountry();
        }


        if (Ads_Adapter_List.admob_nativehashmap != null) {
            Ads_Adapter_List.admob_nativehashmap.clear();
        }

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        String type_connection = getIntent().getStringExtra("type_connection");



        Glide.with(Sample_Connection.this).load(APIClient.img_url + Preference.getServer_image()).error(R.drawable.vpn_place).placeholder(R.drawable.vpn_place).into(iv_flag);
        tv_country.setText(Preference.getserver_name());

        lnr_connecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnecting()) {
                    return;
                }
                if (txt_status.getText().toString().equals(getResources().getString(R.string.switch_off))) {
                    disconnectFromVnp(false);
                } else if (txt_status.getText().toString().equals(getResources().getString(R.string.switch_on))) {
                    prepareVpn();

                }

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Utils.isConnectingToInternet(Sample_Connection.this, new Utils.OnCheckNet() {
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
            Toast.makeText(Sample_Connection.this, "Permission Deny !! ", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isConnecting() {
        boolean isConnecting = false;

        if (txt_status.getText().toString().equals(Sample_Connection.this.getResources().getString(R.string.switch_off)) || txt_status.getText().toString().equals(Sample_Connection.this.getResources().getString(R.string.switch_on))) {
            isConnecting = false;
        } else {
            isConnecting = true;
            Toast.makeText(this, "Server connecting...", Toast.LENGTH_SHORT).show();
        }


        return isConnecting;
    }

    private void prepareVpn() {
        if (!Utils.vpnStart) {
            Utils.isConnectingToInternet(Sample_Connection.this, new Utils.OnCheckNet() {
                @Override
                public void OnCheckNet(boolean b) {
                    if (b) {

                        Intent intent = VpnService.prepare(Sample_Connection.this);

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


    public void status(String status) {
        Log.d("MainActivity12", "status = " + status);
        if (status.equals("connect")) {
            Utils.vpnStart = false;
            //Utils.isVpnConnect = false;
            Preference.setisVpnConnect(false);

            txt_status.setText(getResources().getString(R.string.switch_on));
            mkloaderReg.setVisibility(View.GONE);
            //txt_connect.setText("Connect");
            img_off_on.setImageResource(R.drawable.ic_disconnect);

        } else if (status.equals("connecting")) {
            //Utils.isVpnConnect = false;
            Preference.setisVpnConnect(false);
            txt_status.setText("Connecting...\nPlease Wait!");
            mkloaderReg.setVisibility(View.VISIBLE);
        } else if (status.equals("connected")) {
            //Utils.isVpnConnect = true;
            Preference.setisVpnConnect(true);
            Ads_VN_Screen.VN_showAds(Sample_Connection.this, new Ads_Interstitial.OnFinishAds() {
                @Override
                public void onFinishAds(boolean b) {
                    startActivity(new Intent(Sample_Connection.this, Privacy_Policy_Screen.class));
                    finish();
                }
            });

            txt_status.setText(getResources().getString(R.string.switch_off));
            mkloaderReg.setVisibility(View.GONE);
            // txt_connect.setText("Disconnect");
            img_off_on.setImageResource(R.drawable.ic_connect);

        } else if (status.equals("tryDifferentServer")) {
            txt_status.setText("Try Different\nServer");
        } else if (status.equals("loading")) {
            txt_status.setText("Loading Server..");
        } else if (status.equals("invalidDevice")) {
            txt_status.setText("Invalid Device");
        } else if (status.equals("authenticationCheck")) {
            txt_status.setText("Authentication \n Checking...");
        }
    }


    public void isLoggedIn(Callback<Boolean> callback) {
        UnifiedSdk.getInstance().getBackend().isLoggedIn(callback);
    }

    public void connectToVpn() {
        isLoggedIn(new Callback<Boolean>() {
            @Override
            public void success(@NonNull Boolean aBoolean) {
                if (aBoolean) {
                    List<String> fallbackOrder = new ArrayList<>();
                    fallbackOrder.add(HydraTransport.TRANSPORT_ID);
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
                            Log.d("MainActivity12", "complete");
                            setStatus("CONNECTED");
                            startUIUpdateTask();
                        }

                        @Override
                        public void error(@NonNull VpnException e) {
                            Log.d("MainActivity12", "error = " + e.getMessage());
                            setStatus("DISCONNECTED");
                            if (e.getMessage().contains("TRAFFIC_EXCEED")) {
                                Ads_VN_Screen.VN_showAds(Sample_Connection.this, new Ads_Interstitial.OnFinishAds() {
                                    @Override
                                    public void onFinishAds(boolean b) {
                                        startActivity(new Intent(Sample_Connection.this, Privacy_Policy_Screen.class));
                                    }
                                });
                            } else if (e.getMessage().contains("Wrong state to call start")) {
                                Toast.makeText(Sample_Connection.this, "try again!", Toast.LENGTH_SHORT).show();
                                disconnectFromVnp(true);
                            }
                        }
                    });
                }
            }

            @Override
            public void failure(@NonNull VpnException e) {
            }
        });
    }

    public void disconnectFromVnp(boolean isfromConnnecting) {
        UnifiedSdk.getInstance().getVpn().stop(TrackingConstants.GprReasons.M_UI, new CompletableCallback() {
            @Override
            public void complete() {
                status("connect");
                Utils.vpnStart = false;
//                Utils.isVpnConnect = false;
                Preference.setisVpnConnect(false);
                /*if (!isfromConnnecting) {
                    Ads.showAds(Connect_VPN_Screen.this, new Ads.OnFinishAds() {
                        @Override
                        public void onFinishAds(boolean b) {
                            mUIHandler.removeCallbacks(mUIUpdateRunnable);
                             startActivity(new Intent(Connect_VPN_Screen.this, Privacy_Policy_Screen.class));
                        }
                    });
                }*/
            }

            @Override
            public void error(@NonNull VpnException e) {
                //Toast.makeText(Pro_MainActivity.this, "Disconnect error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void setStatus(String connectionState) {
        if (connectionState != null)
            switch (connectionState) {
                case "DISCONNECTED":
                    status("connect");
                    Utils.vpnStart = false;
                    break;
                case "CONNECTED":
                    Utils.vpnStart = true;// it will use after restart this activity
                    status("connected");
                    //CallIpApi();
                    break;
                case "WAIT":

                    break;
                case "AUTH":
                    break;
                case "RECONNECTING":
                    status("connecting");
                    break;
                case "NONETWORK":
                    break;
            }

    }


    protected void startUIUpdateTask() {
        mUIHandler.removeCallbacks(mUIUpdateRunnable);
        mUIHandler.post(mUIUpdateRunnable);
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
        UnifiedSdk.getInstance().getBackend().remainingTraffic(new Callback<RemainingTraffic>() {
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
        Log.d("MainActivity12", "updateRemainingTraffic = " + remainingTrafficResponse);
        Log.d("MainActivity12", "updateRemainingTraffic one = " + remainingTrafficResponse.isUnlimited());

        String trafficUsed = megabyteCount(remainingTrafficResponse.getTrafficUsed());
        String trafficLimit = megabyteCount(remainingTrafficResponse.getTrafficLimit()) + "Mb";
        Log.d("MainActivity12", "updateRemainingTraffic trafficUsed = " + trafficUsed);
        Log.d("MainActivity12", "updateRemainingTraffic trafficLimit = " + trafficLimit);
        if (remainingTrafficResponse.getTrafficLimit() <= remainingTrafficResponse.getTrafficUsed()) {
            mUIHandler.removeCallbacks(mUIUpdateRunnable);
            Set_Limit_size();
        }
    }


    public static String megabyteCount(long bytes) {
        return String.format(Locale.getDefault(), "%.0f", (double) bytes / 1024 / 1024);
    }


    private void Set_Limit_size() {
        int New_limit_traffic = 1000;
        long total_bytes = New_limit_traffic * 1048576;
        Delete_ApiCall(total_bytes);
    }

    APIInterface mApiInterface;

    private void Delete_ApiCall(long total_bytes) {
        Utils.showProgressDialog(this);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api-prod.northghost.com/partner/subscribers/")
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        mApiInterface = retrofit.create(APIInterface.class);
        Call<TraficLimitResponse> call = mApiInterface.Call_Delete_Trafic(String.valueOf(Preference.getAura_user_id()) + "/traffic?access_token=" + Preference.getAccessToken());
        call.enqueue(new retrofit2.Callback<TraficLimitResponse>() {
            @Override
            public void onResponse(Call<TraficLimitResponse> call, Response<TraficLimitResponse> response) {
                Utils.hideProgressDialog();
                if (response.isSuccessful()) {
                    Log.d("ScratchActivity1", "Delete_ApiCall  =  OK");
                    Add_Trafic_size(total_bytes);
                } else {

                    Log.d("ScratchActivity1", "Delete_ApiCall = " + response.message());
                    Toast.makeText(Sample_Connection.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<TraficLimitResponse> call, Throwable t) {
                Utils.hideProgressDialog();
                Toast.makeText(Sample_Connection.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Add_Trafic_size(long total_bytes) {
        Utils.showProgressDialog(this);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api-prod.northghost.com/partner/subscribers/")
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        mApiInterface = retrofit.create(APIInterface.class);
        Call<TraficLimitResponse> call = mApiInterface.Call_Add_Trafic(String.valueOf(Preference.getAura_user_id()) + "/traffic?access_token=" + Preference.getAccessToken() + "&traffic_limit=" + String.valueOf(total_bytes));
        call.enqueue(new retrofit2.Callback<TraficLimitResponse>() {
            @Override
            public void onResponse(Call<TraficLimitResponse> call, Response<TraficLimitResponse> response) {
                Utils.hideProgressDialog();
                if (response.isSuccessful()) {
                    Log.d("ScratchActivity1", "Add_Trafic_size  =  OK");
                    Toast.makeText(Sample_Connection.this, "Please connect again vpn!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("ScratchActivity1", "Add_Trafic_size = " + response.message());
                    Toast.makeText(Sample_Connection.this, "Something went wrong!", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<TraficLimitResponse> call, Throwable t) {
                Utils.hideProgressDialog();
                Toast.makeText(Sample_Connection.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                // Toast.makeText(Pro_SplashActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onResume() {


        Log.d("MainActivity12", "onResume");
        Log.d("MainActivity12", "onResume = " + Utils.vpnStart);
//        Log.d("MainActivity12", "onResume = " + Utils.isVpnConnect);
        Log.d("MainActivity12", "onResume = " + Preference.getisVpnConnect());

        Resources resources = Sample_Connection.this.getResources();
        String sb = "drawable/" + Preference.getServer_short().toLowerCase();
        //iv_flag.setImageResource(resources.getIdentifier(sb, null, Connect_VPN_Screen.this.getPackageName()));
        Glide.with(Sample_Connection.this).load(APIClient.img_url + Preference.getServer_image()).error(R.drawable.vpn_place).placeholder(R.drawable.vpn_place).into(iv_flag);
        tv_country.setText(Preference.getserver_name());

        checkRemainingTraffic();

        if (Preference.getisVpnConnect()) {
//        if (Utils.isVpnConnect) {
            startUIUpdateTask();
            Preference.setisVpnConnect(true);
//            Utils.isVpnConnect = true;
            txt_status.setText(getResources().getString(R.string.switch_off));
            mkloaderReg.setVisibility(View.GONE);
            img_off_on.setImageResource(R.drawable.ic_connect);
//            ll_status.setBackgroundResource(R.drawable.bg_button_round_plan);
//            rl_header.setBackgroundResource(R.drawable.bg_upper_vn_on);
        }

        super.onResume();
    }


    boolean exit_flag = false;


    @Override
    public void onBackPressed() {

        if (Preference.getisVpnConnect()) {
//        if (Utils.isVpnConnect) {


            Ads_Interstitial.BackshowAds_full(this, new Ads_Interstitial.OnFinishAds() {
                @Override
                public void onFinishAds(boolean b) {
                    finish();
                }
            });


        } else {
            if (Ads_ExitNativeFull.checkExitAdsLoaded()) {
                Exit_Dialog(this);
            } else {
                if (exit_flag) {
                    finishAffinity();
                } else {
                    exit_flag = true;
                    Toast.makeText(this, "Please tap again!", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            exit_flag = false;
                        }
                    }, 3000);
                }
            }
        }
    }
}