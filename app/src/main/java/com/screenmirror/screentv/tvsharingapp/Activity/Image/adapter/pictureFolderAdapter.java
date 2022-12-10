package com.screenmirror.screentv.tvsharingapp.Activity.Image.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ads.adsdemosp.AdsClass.Ads_Adapter_List;
import com.bumptech.glide.Glide;
import com.screenmirror.screentv.tvsharingapp.Activity.Image.itemClickListener;
import com.screenmirror.screentv.tvsharingapp.Activity.Image.model.imageFolder;
import com.screenmirror.screentv.tvsharingapp.R;

import java.util.ArrayList;



public class pictureFolderAdapter extends RecyclerView.Adapter<pictureFolderAdapter.FolderHolder>{

    private ArrayList<imageFolder> folders;
    private Context folderContx;
    private itemClickListener listenToClick;

    public pictureFolderAdapter(ArrayList<imageFolder> folders, Context folderContx, itemClickListener listen) {
        this.folders = folders;
        this.folderContx = folderContx;
        this.listenToClick = listen;
        if (Ads_Adapter_List.admob_nativehashmap != null) {
            Ads_Adapter_List.admob_nativehashmap.clear();
        }
    }

    @NonNull
    @Override
    public FolderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View cell = inflater.inflate(R.layout.picture_folder_item, parent, false);
        return new FolderHolder(cell);

    }

    @Override
    public void onBindViewHolder(@NonNull FolderHolder holder, int position) {
        final imageFolder folder = folders.get(position);

        if(folder != null) {

            try {
                holder.folderCard.setVisibility(View.VISIBLE);
                holder.lnr_ads_show.setVisibility(View.GONE);

                Glide.with(folderContx).load(folder.getFirstPic()).into(holder.folderPic);

                //setting the number of images
                String text = "" + folder.getFolderName();
                String folderSizeString = "" + folder.getNumberOfPics() + " Media";
                holder.folderSize.setText(folderSizeString);
                holder.folderName.setText(text);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listenToClick.onPicClicked(folder.getPath(), folder.getFolderName());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            holder.folderCard.setVisibility(View.GONE);
            holder.lnr_ads_show.setVisibility(View.VISIBLE);

            Ads_Adapter_List.NativeFull_Show(folderContx, holder.llnative, holder.llline, position);
        }

    }

    @Override
    public int getItemCount() {
        return folders.size();
    }


    public class FolderHolder extends RecyclerView.ViewHolder{
        ImageView folderPic;
        TextView folderName;
        //set textview for foldersize
        TextView folderSize;

        LinearLayout folderCard, lnr_ads_show, llline, llnative;

        public FolderHolder(@NonNull View itemView) {
            super(itemView);
            folderPic = itemView.findViewById(R.id.folderPic);
            folderName = itemView.findViewById(R.id.folderName);
            folderSize=itemView.findViewById(R.id.folderSize);
            folderCard = itemView.findViewById(R.id.folderCard);
            lnr_ads_show = (LinearLayout) itemView.findViewById(R.id.lnr_ads_show);
            llline = (LinearLayout) itemView.findViewById(com.ads.adsdemosp.R.id.llline);
            llnative = (LinearLayout) itemView.findViewById(com.ads.adsdemosp.R.id.llnative);
        }
    }

}
