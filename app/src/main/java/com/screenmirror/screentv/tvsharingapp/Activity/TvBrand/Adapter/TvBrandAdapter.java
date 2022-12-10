package com.screenmirror.screentv.tvsharingapp.Activity.TvBrand.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.screenmirror.screentv.tvsharingapp.R;

import java.util.List;

public class TvBrandAdapter extends RecyclerView.Adapter<TvBrandAdapter.ViewHolder> {

    Activity activity;
    List<String> mData;
    tvnameclick tvnameclick;

    int for_icon = 1;

    public TvBrandAdapter(Activity activity, List<String> mData, tvnameclick tvnameclick) {
        this.activity = activity;
        this.mData = mData;
        this.tvnameclick = tvnameclick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.adapter_tv, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tv_name.setText(mData.get(position));

        holder.itemView.setOnClickListener(v -> {
            tvnameclick.tvname();
        });

        /*switch (for_icon){
            case 1:
                holder.iv_ic.setImageResource(R.drawable.ic_name_1);
                for_icon = 2;
                break;

            case 2:
                holder.iv_ic.setImageResource(R.drawable.ic_name_2);
                for_icon = 3;
                break;

            case 3:
                holder.iv_ic.setImageResource(R.drawable.ic_name_3);
                for_icon = 4;
                break;

            case 4:
                holder.iv_ic.setImageResource(R.drawable.ic_name_4);
                for_icon = 5;
                break;

            case 5:
                holder.iv_ic.setImageResource(R.drawable.ic_name_5);
                for_icon = 6;
                break;

            case 6:
                holder.iv_ic.setImageResource(R.drawable.ic_name_6);
                for_icon = 1;
                break;
        }*/

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface tvnameclick {
        void tvname();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        ImageView iv_ic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            iv_ic = itemView.findViewById(R.id.iv_ic);
        }
    }
}
