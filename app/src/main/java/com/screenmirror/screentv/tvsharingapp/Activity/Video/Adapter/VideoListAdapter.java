package com.screenmirror.screentv.tvsharingapp.Activity.Video.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ads.adsdemosp.AdsClass.Ads_Adapter_List;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.screenmirror.screentv.tvsharingapp.ExtraClass.GetData;
import com.screenmirror.screentv.tvsharingapp.Interface.VideoInerface;
import com.screenmirror.screentv.tvsharingapp.Model.VideoModel;
import com.screenmirror.screentv.tvsharingapp.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.screenmirror.screentv.tvsharingapp.ExtraClass.GetData.DialogSelectToplaylist;



public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> implements Filterable {

    Context context;
    List<VideoModel> mData, filterdata;

    VideoInerface videoInerface;

    Animation animation;

    public VideoListAdapter(Context context, List<VideoModel> mData) {
        this.context = context;
        this.mData = mData;
        this.filterdata = mData;
        videoInerface = (VideoInerface) context;

        if (Ads_Adapter_List.admob_nativehashmap != null) {
            Ads_Adapter_List.admob_nativehashmap.clear();
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_videolist, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (mData.get(position) != null) {

            holder.ll_detail.setVisibility(View.VISIBLE);
            holder.lnr_ads_show.setVisibility(View.GONE);

            holder.tv_name.setText(mData.get(position).getVideoTitle());

            holder.tv_size.setText("Size: " + mData.get(position).getFilesize());

            Glide.with(context).load(mData.get(position).getStr_thumb())
                    .apply(new RequestOptions().error(R.color.colorAccent)).placeholder(R.color.colorAccent)
                    .into(holder.iv_video);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videoInerface.videoclick(mData, position);
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
                                GetData.shareVideo(context, mData.get(position).getFilepath());
                                break;
                            case R.id.action_addtoplaylist:
//                                ShowConfirmDilaog(position);

                                DialogSelectToplaylist((Activity)context , mData.get(position) , null);

                                break;
                        }
                        return false;
                    }
                });

                popupMenu.inflate(R.menu.menu_video);
                popupMenu.show();
            }
        });

    }



    private void ShowConfirmDilaog(int position) {
        try {
            final Dialog dialog2 = new Dialog((Activity) context);
            dialog2.requestWindowFeature(1);
            if (dialog2.getWindow() != null) {
                dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                dialog2.getWindow().getAttributes().windowAnimations = R.style.DefaultDialogAnimation;
            }
            dialog2.setContentView(R.layout.new_dialog_junk_cancel);
            dialog2.setCancelable(false);
            dialog2.setCanceledOnTouchOutside(false);
            dialog2.getWindow().setLayout(-1, -1);
            dialog2.getWindow().setGravity(17);
            dialog2.findViewById(R.id.dialog_img).setVisibility(View.GONE);
            ((TextView) dialog2.findViewById(R.id.dialog_title)).setText("Delete Video");
            ((TextView) dialog2.findViewById(R.id.dialog_msg)).setText("Are you sure you want to delete video?");


            dialog2.findViewById(R.id.ll_no).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    dialog2.dismiss();

                }
            });
            dialog2.findViewById(R.id.ll_yes).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (delete(context, new File(mData.get(position).getFilepath()))) {

                        Toast.makeText(context, "Video Delete Successfully", Toast.LENGTH_SHORT).show();

                        mData.remove(position);


                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                    dialog2.dismiss();
                }

            });
            dialog2.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        return !file.exists();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView iv_video;
        TextView tv_duration, tv_name, tv_size;
        LinearLayout ll_menu;
        LinearLayout ll_detail, lnr_ads_show, llline, llnative;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_video = itemView.findViewById(R.id.iv_video);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_size = itemView.findViewById(R.id.tv_size);
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
                    List<VideoModel> filteredList = new ArrayList<>();
                    for (VideoModel row : filterdata) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getVideoTitle().toLowerCase().contains(charString.toLowerCase())) {
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
                    mData = (ArrayList<VideoModel>) filterResults.values;
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
