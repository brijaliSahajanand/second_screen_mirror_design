package com.screenmirror.screentv.tvsharingapp.Activity.Video.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ads.adsdemosp.AdsClass.Ads_Adapter_List;
import com.screenmirror.screentv.tvsharingapp.Interface.FolderItemInterface;
import com.screenmirror.screentv.tvsharingapp.Model.VideoFolder;
import com.screenmirror.screentv.tvsharingapp.R;

import java.util.ArrayList;
import java.util.List;


public class VideoFolderLlistAdapter extends RecyclerView.Adapter<VideoFolderLlistAdapter.ViewHolder> implements Filterable {

    Context context;
    List<VideoFolder> mData, filterdata;

    FolderItemInterface folderItemInterface;

    public VideoFolderLlistAdapter(Context context, List<VideoFolder> mData) {
        this.context = context;
        this.mData = mData;
        this.filterdata = mData;
        folderItemInterface = (FolderItemInterface) context;

        if (Ads_Adapter_List.admob_nativehashmap != null) {
            Ads_Adapter_List.admob_nativehashmap.clear();
        }

    }

    public void UpdateData(List<VideoFolder> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_videofolder, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (mData.get(position) != null) {

            holder.ll_detail.setVisibility(View.VISIBLE);
            holder.lnr_ads_show.setVisibility(View.GONE);

            holder.tv_foldername.setText(mData.get(position).getFoldername());
            holder.tv_no_video.setText("video " + mData.get(position).getTotalvideo());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    folderItemInterface.folderclick(mData, position);
                }
            });
        } else {

            holder.ll_detail.setVisibility(View.GONE);
            holder.lnr_ads_show.setVisibility(View.VISIBLE);

            Ads_Adapter_List.NativeFull_Show(context, holder.llnative, holder.llline, position);


        }


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_foldername, tv_no_video;
        LinearLayout ll_detail, lnr_ads_show, llline, llnative;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_foldername = itemView.findViewById(R.id.tv_foldername);
            tv_no_video = itemView.findViewById(R.id.tv_no_video);

            ll_detail = itemView.findViewById(R.id.ll_detail);

            lnr_ads_show = (LinearLayout) itemView.findViewById(R.id.lnr_ads_show);
            llline = (LinearLayout) itemView.findViewById(com.ads.adsdemosp.R.id.llline);
            llnative = (LinearLayout) itemView.findViewById(com.ads.adsdemosp.R.id.llnative);


        }
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                final String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mData = filterdata;
                } else {
                    List<VideoFolder> filteredList = new ArrayList<>();
                    for (VideoFolder row : filterdata) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getFoldername().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    mData = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mData;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                try {
                    mData = (ArrayList<VideoFolder>) filterResults.values;
                    if (mData.size() <= 0) {
//                        Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
                    }
                    notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
