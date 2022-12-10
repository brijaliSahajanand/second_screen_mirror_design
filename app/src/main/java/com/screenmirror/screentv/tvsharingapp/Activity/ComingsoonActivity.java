package com.screenmirror.screentv.tvsharingapp.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.screenmirror.screentv.tvsharingapp.R;


public class ComingsoonActivity extends BaseActivity {

    ImageView imgclose;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comingsoon);
        imgclose = findViewById(R.id.imgclose);
        imgclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                finishAffinity();
            }
        });


    }
}