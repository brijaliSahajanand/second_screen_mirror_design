package com.screenmirror.screentv.tvsharingapp.retrofit;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;





import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public  class APIClient {

    private static Retrofit retrofit = null;
    public static String img_url = "";

   public static Retrofit getClient() {

       img_url = stringFromJNI();


       Gson gson = new GsonBuilder()
               .setLenient()
               .create();
       retrofit = new Retrofit.Builder()
               .baseUrl(img_url)
               .addConverterFactory(GsonConverterFactory.create(gson))
               .build();
       return retrofit;

    }
    static {
        System.loadLibrary("native-lib");
    }


    public static native String stringFromJNI();
}