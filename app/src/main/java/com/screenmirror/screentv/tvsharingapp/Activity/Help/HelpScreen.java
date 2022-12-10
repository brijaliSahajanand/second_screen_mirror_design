package com.screenmirror.screentv.tvsharingapp.Activity.Help;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.ads.adsdemosp.AdsClass.Ads_Interstitial;
import com.screenmirror.screentv.tvsharingapp.Activity.BaseActivity;
import com.screenmirror.screentv.tvsharingapp.R;




public class HelpScreen extends BaseActivity {
    ConstraintLayout consLay1, consLay2, consLay3, consLay4, consLay5, consLay6, consLay7;
    TextView txtD1, txtD2, txtD3, txtD4, txtD5, txtD6, txtD7;
    ImageView img1, img2, img3, img4, img5, img6, img7;

    ImageView iv_back;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_screen);

        Declaration();
        Intialization();
    }

    private void Declaration() {
        consLay1 = findViewById(R.id.consLay1);
        consLay2 = findViewById(R.id.consLay2);
        consLay3 = findViewById(R.id.consLay3);
        consLay4 = findViewById(R.id.consLay4);
        consLay5 = findViewById(R.id.consLay5);
        consLay6 = findViewById(R.id.consLay6);
        consLay7 = findViewById(R.id.consLay7);


        txtD1 = findViewById(R.id.txtD1);
        txtD2 = findViewById(R.id.txtD2);
        txtD3 = findViewById(R.id.txtD3);
        txtD4 = findViewById(R.id.txtD4);
        txtD5 = findViewById(R.id.txtD5);
        txtD6 = findViewById(R.id.txtD6);
        txtD7 = findViewById(R.id.txtD7);

        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        img5 = findViewById(R.id.img5);
        img6 = findViewById(R.id.img6);
        img7 = findViewById(R.id.img7);



        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



    }

    @Override
    public void onBackPressed() {
        Ads_Interstitial.BackshowAds_full(this, new Ads_Interstitial.OnFinishAds() {
            @Override
            public void onFinishAds(boolean b) {
                finish();
            }
        });
    }

    private void Intialization() {

        consLay1.setOnClickListener(v -> {
            HandleView(txtD1, img1);
        });
        consLay2.setOnClickListener(v -> {
            HandleView(txtD2, img2);
        });

        consLay3.setOnClickListener(v -> {
            HandleView(txtD3, img3);
        });

        consLay4.setOnClickListener(v -> {
            HandleView(txtD4, img4);
        });

        consLay5.setOnClickListener(v -> {
            HandleView(txtD5, img5);
        });

        consLay6.setOnClickListener(v -> {
            HandleView(txtD6, img6);
        });

        consLay7.setOnClickListener(v -> {
            HandleView(txtD7, img7);
        });
    }

    private void HandleView(TextView textdes, ImageView imageView) {

        if (textdes.getVisibility() == View.GONE) {
//            expandOrCollapse(textdes,"expand");
            ObjectAnimator rotate = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 180f);
            rotate.setDuration(100);
            rotate.start();

            expand(textdes);


        } else {
//            expandOrCollapse(textdes, "collapse");
            collapse(textdes);
            ObjectAnimator rotate = ObjectAnimator.ofFloat(imageView, "rotation", 180f, 0f);
            rotate.setDuration(100);
            rotate.start();
        }

    }

    public void expandOrCollapse(final View v, String exp_or_colpse) {
        TranslateAnimation anim = null;
        if (exp_or_colpse.equals("expand")) {
            anim = new TranslateAnimation(0.0f, 0.0f, -v.getHeight(), 0.0f);
            v.setVisibility(View.VISIBLE);
        } else {
            anim = new TranslateAnimation(0.0f, 0.0f, 0.0f, -v.getHeight());
            Animation.AnimationListener collapselistener = new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    v.setVisibility(View.GONE);
                }
            };

            anim.setAnimationListener(collapselistener);
        }

        // To Collapse
        //

        anim.setDuration(100);
        anim.setInterpolator(new AccelerateInterpolator(0.5f));
        v.startAnimation(anim);
    }

    public static void expand(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? WindowManager.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
//        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        a.setDuration(100);
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
//        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        a.setDuration(100);
        v.startAnimation(a);
    }
}