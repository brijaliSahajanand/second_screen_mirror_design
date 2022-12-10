package com.screenmirror.screentv.tvsharingapp.Activity.Image.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ads.adsdemosp.AdsClass.Ads_Adapter_List;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.screenmirror.screentv.tvsharingapp.Activity.Image.itemClickListener;
import com.screenmirror.screentv.tvsharingapp.Activity.Image.model.pictureFacer;
import com.screenmirror.screentv.tvsharingapp.R;

import java.util.ArrayList;

import static androidx.core.view.ViewCompat.setTransitionName;


public class picture_Adapter extends RecyclerView.Adapter<PicHolder> {

    private ArrayList<pictureFacer> pictureList;
    private Context pictureContx;
    private final itemClickListener picListerner;

    public picture_Adapter(ArrayList<pictureFacer> pictureList, Context pictureContx, itemClickListener picListerner) {
        this.pictureList = pictureList;
        this.pictureContx = pictureContx;
        this.picListerner = picListerner;

        if (Ads_Adapter_List.admob_nativehashmap != null) {
            Ads_Adapter_List.admob_nativehashmap.clear();
        }
    }

    @NonNull
    @Override
    public PicHolder onCreateViewHolder(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View cell = inflater.inflate(R.layout.pic_holder_item, container, false);
        return new PicHolder(cell);
    }

    @Override
    public void onBindViewHolder(@NonNull final PicHolder holder, final int position) {

        final pictureFacer image = pictureList.get(position);

        if (image != null) {

            holder.ll_detail.setVisibility(View.VISIBLE);
            holder.lnr_ads_show.setVisibility(View.GONE);


            Glide.with(pictureContx)
                    .load(image.getPicturePath())
                    .apply(new RequestOptions().centerCrop())
                    .into(holder.picture);

            setTransitionName(holder.picture, String.valueOf(position) + "_image");

            holder.picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    picListerner.onPicClicked(holder,position, pictureList);
                }
            });

        } else {
            holder.ll_detail.setVisibility(View.GONE);
            holder.lnr_ads_show.setVisibility(View.VISIBLE);
            Ads_Adapter_List.NativeFull_Show(pictureContx, holder.llnative, holder.llline, position);
            /*if (admob_nativehashmap.get(position) == null) {
                if (position == 0) {

                    NativeAds.Native_adtype_Q(pictureContx, holder.ll_nativeadfullview, "native",holder.lnr_TV_ads1);

                } else {

                }

            } else {
                if (admob_nativehashmap.get(position) != null) {
                    if (admob_nativehashmap.get(position).getParent() != null)
                        ((ViewGroup) admob_nativehashmap.get(position).getParent()).removeView(admob_nativehashmap.get(position));
                    holder.ll_nativeadfullview.removeAllViews();
                    holder.lnr_TV_ads1.setVisibility(View.GONE);
                    holder.ll_nativeadfullview.setVisibility(View.VISIBLE);
                    holder.ll_nativeadfullview.addView(admob_nativehashmap.get(position));
                }
            }*/
        }

    }

    @Override
    public int getItemCount() {
        return pictureList.size();
    }
}
