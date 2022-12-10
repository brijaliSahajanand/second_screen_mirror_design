package com.screenmirror.screentv.tvsharingapp.Activity.Playlist.Adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ads.adsdemosp.AdsClass.Ads_Adapter_List;
import com.ads.adsdemosp.AdsClass.Ads_Interstitial;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.screenmirror.screentv.tvsharingapp.Activity.Audio.AudioPlayActivity;
import com.screenmirror.screentv.tvsharingapp.Activity.Playlist.ItemPlaylistScreen;
import com.screenmirror.screentv.tvsharingapp.Activity.Video.Player.GlobalVar;
import com.screenmirror.screentv.tvsharingapp.Activity.Video.Player.PlayVideoActivity;
import com.screenmirror.screentv.tvsharingapp.ExtraClass.GetData;
import com.screenmirror.screentv.tvsharingapp.ExtraClass.Preference;
import com.screenmirror.screentv.tvsharingapp.Model.ItemPlaylist;
import com.screenmirror.screentv.tvsharingapp.Model.Playlist;
import com.screenmirror.screentv.tvsharingapp.Model.Song;
import com.screenmirror.screentv.tvsharingapp.Model.VideoModel;
import com.screenmirror.screentv.tvsharingapp.R;


import java.util.ArrayList;
import java.util.List;


public class ItemPlaylistAdapter extends RecyclerView.Adapter<ItemPlaylistAdapter.ViewHolder> {

    ItemPlaylistScreen context;
    List<ItemPlaylist> mData;

    public ItemPlaylistAdapter(ItemPlaylistScreen context, List<ItemPlaylist> mData) {
        this.context = context;
        this.mData = mData;

        if (Ads_Adapter_List.admob_nativehashmap != null) {
            Ads_Adapter_List.admob_nativehashmap.clear();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_itemplaylist, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (mData.get(position) != null) {

            holder.ll_detail.setVisibility(View.VISIBLE);
            holder.lnr_ads_show.setVisibility(View.GONE);


            if (!mData.get(position).isVideo()) {
                holder.tv_header.setText(mData.get(position).getSong().getSongTitle());
                holder.tv_sub_header.setText(mData.get(position).getSong().getArtistName());

                holder.iv_image.setImageResource(R.drawable.avtar);


                /*Glide.with(context).load(R.drawable.avtar)
                        .apply(new RequestOptions().error(R.color.colorAccent)).placeholder(R.color.colorAccent)
                        .into(holder.iv_image);*/

                holder.itemView.setOnClickListener(v -> {

                    Ads_Interstitial.showAds_full(context, new Ads_Interstitial.OnFinishAds() {
                        @Override
                        public void onFinishAds(boolean b) {
                            List<Song> songList = new ArrayList<>();
                            songList.add(mData.get(position).getSong());
                            context.startActivity(new Intent(context, AudioPlayActivity.class).putExtra(GetData.songlist, new Gson().toJson(songList)).putExtra(GetData.position, 0));

                        }
                    });


                });

            } else {

                holder.tv_header.setText(mData.get(position).getVideoModel().getVideoTitle());
                holder.tv_sub_header.setText("Size: " + mData.get(position).getVideoModel().getFilesize() + " (" + mData.get(position).getVideoModel().getDateadded() + ")");

                Glide.with(context).load(mData.get(position).getVideoModel().getStr_thumb())
                        .apply(new RequestOptions().error(R.color.colorAccent)).placeholder(R.color.colorAccent)
                        .into(holder.iv_image);


                holder.itemView.setOnClickListener(v -> {

                    Ads_Interstitial.showAds_full(context, new Ads_Interstitial.OnFinishAds() {
                        @Override
                        public void onFinishAds(boolean b) {
                            List<VideoModel> videoList = new ArrayList<>();
                            videoList.add(mData.get(position).getVideoModel());
                            GlobalVar.getInstance().videoItemsPlaylist = videoList;
                            GlobalVar.getInstance().playingVideo = videoList.get(0);
                            GlobalVar.getInstance().seekPosition = 0;
                            if (GlobalVar.getInstance().getPlayer() == null) {
                                return;
                            } else if (!GlobalVar.getInstance().isMutilSelectEnalble) {
                                if (!GlobalVar.getInstance().isPlayingAsPopup()) {
                                    GlobalVar.getInstance().videoService.playVideo(GlobalVar.getInstance().seekPosition, false);
                                    Intent intent = new Intent(context, PlayVideoActivity.class);
                                    context.startActivity(intent);
                                    if (GlobalVar.getInstance().videoService != null)
                                        GlobalVar.getInstance().videoService.releasePlayerView();
                                } else {

                                }
                            }
                        }
                    });


                });

            }


            holder.ll_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu popupMenu = new PopupMenu(context, v);

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {

                                case R.id.action_remove:
                                    RemoveFromPlaylist(position);
                                    break;
                                case R.id.action_share:
                                    if (mData.get(position).isVideo()) {
                                        GetData.shareVideo(context, mData.get(position).getVideoModel().getFilepath());
                                    } else {
                                        Uri uri = Uri.parse(mData.get(position).getSong().getSongPath());
                                        Intent share = new Intent(Intent.ACTION_SEND);
                                        share.setType("audio/*");
                                        share.putExtra(Intent.EXTRA_STREAM, uri);
                                        context.startActivity(Intent.createChooser(share, "Share Sound File"));
                                    }

                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.inflate(R.menu.menu_itemplaylist);
                    popupMenu.show();
                }
            });

        } else {
            holder.ll_detail.setVisibility(View.GONE);
            holder.lnr_ads_show.setVisibility(View.VISIBLE);

            Ads_Adapter_List.NativeFull_Show(context, holder.llnative, holder.llline, position);
        }

    }

    private void RemoveFromPlaylist(int position) {


        String list = Preference.getplaylist();


        List<Playlist> fetchplaylistList = new ArrayList<>();
        if (!list.isEmpty()) {
            fetchplaylistList = new Gson().fromJson(list, new TypeToken<List<Playlist>>() {
            }.getType());

            Playlist playlist = new Playlist();
            playlist.setName(GetData.PlayName);

            if (fetchplaylistList.contains(playlist)) {
                int pos = fetchplaylistList.indexOf(playlist);

                List<ItemPlaylist> itemPlaylists = new ArrayList<>();
                itemPlaylists = fetchplaylistList.get(pos).getItemPlaylists();

                if (itemPlaylists.contains(mData.get(position))) {
                    itemPlaylists.remove(mData.get(position));
                    fetchplaylistList.get(pos).setItemPlaylists(itemPlaylists);
                    Preference.setplaylist(new Gson().toJson(fetchplaylistList));
                    mData.remove(position);

                    if (mData.size() > 0) {
                        notifyDataSetChanged();
                    } else {
                        context.rv_playlistitem.setVisibility(View.GONE);
                        context.tv_nodata.setVisibility(View.VISIBLE);
                    }


                }
            }

        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_image;
        TextView tv_header;
        TextView tv_sub_header;
        LinearLayout  ll_detail;
        LinearLayout ll_menu,lnr_ads_show, llline, llnative;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_image = itemView.findViewById(R.id.iv_image);
            tv_header = itemView.findViewById(R.id.tv_header);
            tv_sub_header = itemView.findViewById(R.id.tv_sub_header);
            ll_menu = itemView.findViewById(R.id.ll_menu);
            lnr_ads_show = (LinearLayout) itemView.findViewById(R.id.lnr_ads_show);
            llline = (LinearLayout) itemView.findViewById(com.ads.adsdemosp.R.id.llline);
            llnative = (LinearLayout) itemView.findViewById(com.ads.adsdemosp.R.id.llnative);
            ll_detail = itemView.findViewById(R.id.ll_detail);
        }
    }
}
