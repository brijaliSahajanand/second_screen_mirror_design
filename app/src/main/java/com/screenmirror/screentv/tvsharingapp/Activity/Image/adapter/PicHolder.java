package com.screenmirror.screentv.tvsharingapp.Activity.Image.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.screenmirror.screentv.tvsharingapp.R;


public class PicHolder extends RecyclerView.ViewHolder{

    public ImageView picture;

    LinearLayout lnr_ads_show, llline, llnative;
    RelativeLayout ll_detail;
//    TextView tv_ad_text_native2;


    PicHolder(@NonNull View itemView) {
        super(itemView);

        picture = itemView.findViewById(R.id.image);

        ll_detail = itemView.findViewById(R.id.ll_detail);
//        tv_ad_text_native2 = itemView.findViewById(R.id.tv_ad_text_native2);

        lnr_ads_show = (LinearLayout) itemView.findViewById(R.id.lnr_ads_show);
        llline = (LinearLayout) itemView.findViewById(com.ads.adsdemosp.R.id.llline);
        llnative = (LinearLayout) itemView.findViewById(com.ads.adsdemosp.R.id.llnative);

    }
}
