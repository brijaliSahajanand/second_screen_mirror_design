package com.screenmirror.screentv.tvsharingapp.ExtraClass;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;


import com.ads.adsdemosp.AdsClass.Ads_AppOpen;

import unified.vpn.sdk.CompletableCallback;
import unified.vpn.sdk.TrackingConstants;
import unified.vpn.sdk.UnifiedSdk;
import unified.vpn.sdk.VpnException;

import static com.screenmirror.screentv.tvsharingapp.ExtraClass.Utils.isdebuggle;
import static com.screenmirror.screentv.tvsharingapp.ExtraClass.Utils.warningdialog;


public class Appcontroller extends Application {
    private static Appcontroller ourInstance = new Appcontroller();

    public static Appcontroller getInstance() {
        return ourInstance;
    }

    private int numStarted = 0;
    public static boolean fast_start = false;

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;


        SetAppOpenads();
    }


    private void SetAppOpenads() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                Log.d("App_Controller", "onActivityCreated");
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.d("App_Controller", "onActivityStarted");
                if (!Preference.getcomingsoon()) {
                    if (numStarted == 0) {
                        if (!fast_start) {
                            Log.d("Splash_app_open", " IN ");

                            if (isdebuggle(activity)) {

                                if (warningdialog != null) {
                                    if (warningdialog.isShowing()) {
                                        return;
                                    }
                                }
                                Utils.Get_Developer_Dialog(activity);
                                Utils.debug_check(new Utils.no_debug() {
                                    @Override
                                    public void no_debug() {
                                        warningdialog.dismiss();
                                    }
                                });
                            } else {
                                Ads_AppOpen.OpenAppAds_Show(activity);
                            }

                        } else {
                            fast_start = false;
                        }
                    }

                }
                numStarted++;
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.d("App_Controller", "onActivityResumed");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.d("App_Controller", "onActivityPaused");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.d("App_Controller", "onActivityStopped");
                if (!Preference.getcomingsoon()) {
                    numStarted--;
                    if (numStarted == 0) {
                        Log.d("App_Controller", "background");
                    }
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
                Log.d("App_Controller", "onActivitySaveInstanceState");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.d("App_Controller", "onActivityDestroyed");
                if (numStarted == 0) {
                    Log.d("App_Controller", "onActivityDestroyed 1 1 =");
                    if (Preference.getisVpnConnect()) {
                        disconnectFromVnp();
                    }
                }
            }
        });

    }

    public void disconnectFromVnp() {
        UnifiedSdk.getInstance().getVpn().stop(TrackingConstants.GprReasons.M_UI, new CompletableCallback() {
            @Override
            public void complete() {
                Utils.vpnStart = false;
                Preference.setisVpnConnect(false);
            }

            @Override
            public void error(@NonNull VpnException e) {
                Utils.vpnStart = false;
                Preference.setisVpnConnect(false);
            }
        });
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }


    public static Appcontroller getApp() {
        if (ourInstance == null) {
            ourInstance = new Appcontroller();
        }
        return ourInstance;
    }

}