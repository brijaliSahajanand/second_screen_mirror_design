package com.screenmirror.screentv.tvsharingapp.Activity.Playlist.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ads.adsdemosp.AdsClass.Ads_Interstitial;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.screenmirror.screentv.tvsharingapp.Activity.Playlist.ItemPlaylistScreen;
import com.screenmirror.screentv.tvsharingapp.ExtraClass.GetData;
import com.screenmirror.screentv.tvsharingapp.ExtraClass.Preference;
import com.screenmirror.screentv.tvsharingapp.Model.ItemPlaylist;
import com.screenmirror.screentv.tvsharingapp.Model.Playlist;
import com.screenmirror.screentv.tvsharingapp.Model.Song;
import com.screenmirror.screentv.tvsharingapp.Model.VideoModel;
import com.screenmirror.screentv.tvsharingapp.R;


import java.util.ArrayList;
import java.util.List;

import static com.screenmirror.screentv.tvsharingapp.ExtraClass.GetData.AddNameToPlaylist;
import static com.screenmirror.screentv.tvsharingapp.ExtraClass.GetData.PlayName;
import static com.screenmirror.screentv.tvsharingapp.ExtraClass.GetData.itemPlaylistList;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {

    Context context;
    List<Playlist> mData;
    boolean isNextScreen = false;
    VideoModel videoModel;
    Song song;

    public PlaylistAdapter(Context context, List<Playlist> mData, boolean isNextScreen, VideoModel videoModel, Song song) {
        this.context = context;
        this.mData = mData;
        this.isNextScreen = isNextScreen;
        this.videoModel = videoModel;
        this.song = song;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.adapter_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tv_playlistname.setText(mData.get(position).getName());


        if (position == 0) {
            holder.ll_menu.setVisibility(View.GONE);
            holder.ll_add.setVisibility(View.VISIBLE);

            holder.itemView.setOnClickListener(v -> {
                OpenAddPlaylistDialog(1, position);
            });

            holder.tv_playlistname.setTextColor(ColorStateList.valueOf(context.getResources().getColor(R.color.colorPrimary)));

        } else {
            holder.ll_menu.setVisibility(View.VISIBLE);
            holder.ll_add.setVisibility(View.GONE);
            holder.tv_playlistname.setTextColor(ColorStateList.valueOf(context.getResources().getColor(R.color.black)));

            holder.itemView.setOnClickListener(v -> {
                if (!isNextScreen) {
                    AddVideoOrSongToPlaylist(mData.get(position));
                } else {
                    //setIntent
                    PlayName = mData.get(position).getName();
                    if(mData.get(position).getItemPlaylists() == null){
                        itemPlaylistList = new ArrayList<>();
                    }else {
                        if(mData.get(position).getItemPlaylists().size() > 0){
                            itemPlaylistList = mData.get(position).getItemPlaylists();
                        }else {
                            itemPlaylistList = new ArrayList<>();
                        }
                    }

                    Ads_Interstitial.showAds_full((Activity) context, new Ads_Interstitial.OnFinishAds() {
                        @Override
                        public void onFinishAds(boolean b) {
                            context.startActivity(new Intent(context, ItemPlaylistScreen.class));
                        }
                    });


                }
            });


            holder.ll_menu.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(context, v);
                popup.getMenuInflater()
                        .inflate(R.menu.menu_playlist, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.rename:
                                OpenAddPlaylistDialog(2, position);
//                                Toast.makeText(context, "Rename", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.remove:
//                                Toast.makeText(context, "Remove", Toast.LENGTH_SHORT).show();
                                RemoveDialog(position);
                                return true;

                            default:
                                return true;
                        }
                    }
                });

                popup.show();

            });
        }
    }

    private void AddVideoOrSongToPlaylist(Playlist playlist) {

        String list = Preference.getplaylist();
        List<Playlist> fetchplaylist = new ArrayList<>();

        if (!list.isEmpty()) {
            fetchplaylist = new Gson().fromJson(list, new TypeToken<List<Playlist>>() {
            }.getType());


            if (fetchplaylist.contains(playlist)) {

                int pos = fetchplaylist.indexOf(playlist);
                List<ItemPlaylist> allitemPlaylist = fetchplaylist.get(pos).getItemPlaylists();

                ItemPlaylist itemPlaylist = new ItemPlaylist();


                if (videoModel != null) {
                    itemPlaylist.setId(videoModel.getVideoid());
                    itemPlaylist.setVideo(true);
                    itemPlaylist.setVideoModel(videoModel);
                    itemPlaylist.setSong(null);

                    if (allitemPlaylist != null) {
                        if (allitemPlaylist.size() > 0) {
                            if (allitemPlaylist.contains(itemPlaylist)) {
                                Toast.makeText(context, "Video already exist in playlist", Toast.LENGTH_SHORT).show();
                                if (GetData.selectplaylistdialog != null) {
                                    if (GetData.selectplaylistdialog.isShowing()) {
                                        GetData.selectplaylistdialog.dismiss();
                                    }
                                }
                                return;
                            } else {
                                allitemPlaylist.add(itemPlaylist);
                            }
                        } else {
                            allitemPlaylist.add(itemPlaylist);
                        }
                    } else {
                        allitemPlaylist = new ArrayList<>();
                        allitemPlaylist.add(itemPlaylist);
                    }

                    Toast.makeText(context, "Video added to " + playlist.getName(), Toast.LENGTH_SHORT).show();
                } else if (song != null) {
                    itemPlaylist.setId(song.getMid());
                    itemPlaylist.setVideo(false);
                    itemPlaylist.setVideoModel(null);
                    itemPlaylist.setSong(song);
                    if (allitemPlaylist != null) {
                        if (allitemPlaylist.size() > 0) {
                            if (allitemPlaylist.contains(itemPlaylist)) {
                                Toast.makeText(context, "Audio already exist in playlist", Toast.LENGTH_SHORT).show();
                                if (GetData.selectplaylistdialog != null) {
                                    if (GetData.selectplaylistdialog.isShowing()) {
                                        GetData.selectplaylistdialog.dismiss();
                                    }
                                }
                                return;
                            } else {
                                allitemPlaylist.add(itemPlaylist);
                            }
                        } else {
                            allitemPlaylist.add(itemPlaylist);
                        }
                    } else {
                        allitemPlaylist = new ArrayList<>();
                        allitemPlaylist.add(itemPlaylist);
                    }

                    Toast.makeText(context, "Audio added to " + playlist.getName(), Toast.LENGTH_SHORT).show();
                }


                fetchplaylist.get(pos).setItemPlaylists(allitemPlaylist);

                Preference.setplaylist(new Gson().toJson(fetchplaylist));


            }


        }

        if (GetData.selectplaylistdialog != null) {
            if (GetData.selectplaylistdialog.isShowing()) {
                GetData.selectplaylistdialog.dismiss();
            }
        }

    }


    private void RemoveDialog(int position) {

        String list = Preference.getplaylist();
        List<Playlist> fetchplaylist = new ArrayList<>();

        if (!list.isEmpty()) {
            fetchplaylist = new Gson().fromJson(list, new TypeToken<List<Playlist>>() {
            }.getType());

            if (fetchplaylist.contains(mData.get(position))) {


                fetchplaylist.remove(mData.get(position));
                Preference.setplaylist(new Gson().toJson(fetchplaylist));

                mData.remove(position);
                notifyDataSetChanged();

              /*  mData = new ArrayList<>();
                mData = GetData.GetPlaylist((Activity) context);
                notifyDataSetChanged();*/
            }
        }

    }

    Dialog addplaylistdialog;
    EditText et_playlist;
    Button btn_action;

    private void OpenAddPlaylistDialog(int flag, int position) {  // flag == 1  new , else update
        addplaylistdialog = new Dialog((Activity) context);
        addplaylistdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addplaylistdialog.setContentView(R.layout.dialog_addplaylist);
        addplaylistdialog.getWindow().setGravity(Gravity.BOTTOM);
        addplaylistdialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        addplaylistdialog.getWindow().getAttributes().windowAnimations = R.style.ExitDialogAnimation;

        et_playlist = addplaylistdialog.findViewById(R.id.et_playlist);
        btn_action = addplaylistdialog.findViewById(R.id.btn_action);

        if (flag != 1) {
            //update
            btn_action.setText("Update");
            et_playlist.setText(mData.get(position).getName());
        } else {

            //add
            btn_action.setText("Add");
        }

        btn_action.setOnClickListener(v -> {
            if (flag != 1) {
                //update

                if (!et_playlist.getText().toString().trim().isEmpty()) {

                    if (UpdateName(mData.get(position).getName(), et_playlist.getText().toString().trim())) {
                        mData.get(position).setName(et_playlist.getText().toString().trim());
                        notifyDataSetChanged();
                    }

                }

            } else {
                if (!et_playlist.getText().toString().trim().isEmpty()) {
                    if (AddNameToPlaylist((Activity) context, et_playlist.getText().toString().trim())) {
                        Playlist playlist = new Playlist();
                        playlist.setName(et_playlist.getText().toString().trim());
                        mData.add(playlist);
                        notifyDataSetChanged();
                    }
                }
            }

            addplaylistdialog.dismiss();

        });


        addplaylistdialog.show();
    }

    private boolean UpdateName(String oldname, String name) {


        String list = Preference.getplaylist();


        List<Playlist> fetchplaylistList = new ArrayList<>();
        if (!list.isEmpty()) {
            fetchplaylistList = new Gson().fromJson(list, new TypeToken<List<Playlist>>() {
            }.getType());


            Playlist playlist = new Playlist();
            playlist.setName(oldname);

            if (fetchplaylistList.contains(playlist)) {
                int pos = fetchplaylistList.indexOf(playlist);
                fetchplaylistList.get(pos).setName(name);
                Preference.setplaylist(new Gson().toJson(fetchplaylistList));
                return true;
            }

        }

        return false;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_playlistname;
        LinearLayout ll_menu ,ll_add;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_playlistname = itemView.findViewById(R.id.tv_playlistname);
            ll_menu = itemView.findViewById(R.id.ll_menu);
            ll_add = itemView.findViewById(R.id.ll_add);
        }
    }
}
