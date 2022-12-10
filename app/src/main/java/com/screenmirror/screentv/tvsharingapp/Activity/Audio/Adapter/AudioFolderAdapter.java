package com.screenmirror.screentv.tvsharingapp.Activity.Audio.Adapter;

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
import com.screenmirror.screentv.tvsharingapp.Interface.AudioFolderClickInterface;
import com.screenmirror.screentv.tvsharingapp.Model.Folder;
import com.screenmirror.screentv.tvsharingapp.R;

import java.util.ArrayList;
import java.util.List;


public class AudioFolderAdapter extends RecyclerView.Adapter<AudioFolderAdapter.ViewHolder> implements Filterable {

    Context context;
    List<Folder> mData, filterdata;
    AudioFolderClickInterface audioFolderClickInterface;

    public AudioFolderAdapter(Context context, List<Folder> mData) {
        this.context = context;
        this.mData = mData;
        this.filterdata = mData;
        audioFolderClickInterface = (AudioFolderClickInterface) context;

        if (Ads_Adapter_List.admob_nativehashmap != null) {
            Ads_Adapter_List.admob_nativehashmap.clear();
        }
    }

    public void UpdateData(List<Folder> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_audiofolder, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (mData.get(position) != null) {

            holder.ll_detail.setVisibility(View.VISIBLE);
            holder.lnr_ads_show.setVisibility(View.GONE);
            holder.tv_folder.setText(mData.get(position).getName());
           //  holder.tv_path.setText(mData.get(position).getPath());
            holder.tv_no_songs.setText(mData.get(position).getSongcount() + " song");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    audioFolderClickInterface.audiofolderclick(mData.get(position));
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
        TextView tv_folder,  tv_no_songs;
        LinearLayout ll_detail, lnr_ads_show, llline, llnative;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_folder = itemView.findViewById(R.id.tv_folder);

            tv_no_songs = itemView.findViewById(R.id.tv_no_songs);
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
                    List<Folder> filteredList = new ArrayList<>();
                    for (Folder row : filterdata) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
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
                    mData = (ArrayList<Folder>) filterResults.values;
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
