package com.screenmirror.screentv.tvsharingapp.Activity.Image;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.screenmirror.screentv.tvsharingapp.Activity.Image.model.pictureFacer;
import com.screenmirror.screentv.tvsharingapp.R;

import java.util.ArrayList;


public class recyclerViewPagerImageIndicator extends RecyclerView.Adapter<recyclerViewPagerImageIndicator.indicatorHolder> {

    ArrayList<pictureFacer> pictureList;
    Context pictureContx;
    private final imageIndicatorListener imageListerner;

    public recyclerViewPagerImageIndicator(ArrayList<pictureFacer> pictureList, Context pictureContx, imageIndicatorListener imageListerner) {
        this.pictureList = pictureList;
        this.pictureContx = pictureContx;
        this.imageListerner = imageListerner;
    }


    @NonNull
    @Override
    public indicatorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View cell = inflater.inflate(R.layout.indicator_holder, parent, false);
        return new indicatorHolder(cell);
    }

    @Override
    public void onBindViewHolder(@NonNull indicatorHolder holder, final int position) {

        final pictureFacer pic = pictureList.get(position);

        holder.positionController.setBackgroundColor(pic.getSelected() ? Color.parseColor("#00000000") : Color.parseColor("#8c000000"));

        Glide.with(pictureContx)
                .load(pic.getPicturePath())
                .apply(new RequestOptions().centerCrop())
                .into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //holder.card.setCardElevation(5);
                pic.setSelected(true);
                notifyDataSetChanged();
                imageListerner.onImageIndicatorClicked(position);
            }
        });

    }

    public class indicatorHolder extends RecyclerView.ViewHolder{

        public ImageView image;
        private CardView card;
        View positionController;

        indicatorHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageIndicator);
            card = itemView.findViewById(R.id.indicatorCard);
            positionController = itemView.findViewById(R.id.activeImage);
        }
    }

    @Override
    public int getItemCount() {
        return pictureList.size();
    }
}
