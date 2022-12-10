package com.screenmirror.screentv.tvsharingapp.Activity.Audio.Adapter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ads.adsdemosp.AdsClass.Ads_Adapter_List;
import com.bumptech.glide.Glide;
import com.screenmirror.screentv.tvsharingapp.Interface.AudioClickInterface;
import com.screenmirror.screentv.tvsharingapp.Model.Song;
import com.screenmirror.screentv.tvsharingapp.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.screenmirror.screentv.tvsharingapp.ExtraClass.GetData.DialogSelectToplaylist;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.ViewHolder> implements Filterable {

    Context context;
    List<Song> mData, filterdata;
    AudioClickInterface audioClickInterface;


    public AudioAdapter(Context context, List<Song> mData) {
        this.context = context;
        this.mData = mData;
        this.filterdata = mData;
        audioClickInterface = (AudioClickInterface) context;


        if (Ads_Adapter_List.admob_nativehashmap != null) {
            Ads_Adapter_List.admob_nativehashmap.clear();
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_audiolist, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mData.get(position) != null) {

            holder.ll_detail.setVisibility(View.VISIBLE);
            holder.lnr_ads_show.setVisibility(View.GONE);
            holder.tv_songname.setText(mData.get(position).getSongTitle());
            holder.tv_artistname.setText(mData.get(position).getArtistName());
            Glide.with(context).load(mData.get(position).getMart()).placeholder(R.drawable.avtar)
                    .skipMemoryCache(false)
                    .into(holder.iv_song);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    audioClickInterface.audioclick(mData.get(position));
                }
            });

            setPopmenu(holder, position);
        } else {

            holder.ll_detail.setVisibility(View.GONE);
            holder.lnr_ads_show.setVisibility(View.VISIBLE);

            Ads_Adapter_List.NativeFull_Show(context, holder.llnative, holder.llline, position);
        }
    }

    private void setPopmenu(ViewHolder holder, int position) {
        holder.ll_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(context, v);

                Menu itemofmenu = popupMenu.getMenu();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.action_share:
                                Uri uri = Uri.parse(mData.get(position).getSongPath());
                                Intent share = new Intent(Intent.ACTION_SEND);
                                share.setType("audio/*");
                                share.putExtra(Intent.EXTRA_STREAM, uri);
                                context.startActivity(Intent.createChooser(share, "Share Sound File"));
                                break;
                            case R.id.action_addtoplaylist:
                                DialogSelectToplaylist((Activity)context , null , mData.get(position));
                                break;
                        }
                        return false;
                    }
                });

                popupMenu.inflate(R.menu.menu_video);
//                itemofmenu.findItem(R.id.action_delete).setVisible(false);
                popupMenu.show();
            }
        });

    }



    public static boolean delete(final Context context, final File file) {
        final String where = MediaStore.MediaColumns.DATA + "=?";
        final String[] selectionArgs = new String[]{
                file.getAbsolutePath()
        };
        final ContentResolver contentResolver = context.getContentResolver();
        final Uri filesUri = MediaStore.Files.getContentUri("external");

        contentResolver.delete(filesUri, where, selectionArgs);

        if (file.exists()) {

            contentResolver.delete(filesUri, where, selectionArgs);
        }
        return !false;
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_songname, tv_artistname;
        ImageView iv_song;
        LinearLayout ll_menu;
        LinearLayout ll_detail, lnr_ads_show, llline, llnative;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_songname = itemView.findViewById(R.id.tv_songname);
            tv_artistname = itemView.findViewById(R.id.tv_artistname);
            iv_song = itemView.findViewById(R.id.iv_song);
            ll_menu = itemView.findViewById(R.id.ll_menu);
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
                    List<Song> filteredList = new ArrayList<>();
                    for (Song row : filterdata) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getSongTitle().toLowerCase().contains(charString.toLowerCase())) {
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
                    mData = (ArrayList<Song>) filterResults.values;
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
