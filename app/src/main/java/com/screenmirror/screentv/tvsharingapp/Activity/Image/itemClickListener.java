package com.screenmirror.screentv.tvsharingapp.Activity.Image;



import com.screenmirror.screentv.tvsharingapp.Activity.Image.adapter.PicHolder;
import com.screenmirror.screentv.tvsharingapp.Activity.Image.model.pictureFacer;

import java.util.ArrayList;

public interface itemClickListener {

    void onPicClicked(PicHolder holder, int position, ArrayList<pictureFacer> pics);
    void onPicClicked(String pictureFolderPath, String folderName);
}
