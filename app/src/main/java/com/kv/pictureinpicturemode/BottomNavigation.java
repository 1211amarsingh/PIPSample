package com.kv.pictureinpicturemode;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class BottomNavigation extends AppCompatActivity {

    BottomSheetBehavior behavior;
    Button button;
    VideoView videoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        initView();
    }


    private void initView() {
        videoView = findViewById(R.id.videoView);
        button = findViewById(R.id.openBottomSheetButton);
        final View bottomSheet = findViewById(R.id.nestedScrollView);
        behavior = BottomSheetBehavior.from(bottomSheet);

        behavior.setPeekHeight(200);    //Set the peek height
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i == BottomSheetBehavior.STATE_EXPANDED) {
                    button.setText("Expand");
                    Log.d("Amar Status", "expand");
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 300);
                    videoView.setLayoutParams(layoutParams);
                } else if (i == BottomSheetBehavior.STATE_COLLAPSED) {
                    button.setText("collapsed");
                    Log.d("Amar Status", "collapsed");
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(180, 150);
                    videoView.setLayoutParams(layoutParams);
//                } else if (i == BottomSheetBehavior.STATE_DRAGGING) {
//                    button.setText("drag");
//                    Log.d("Amar Status", "drag");
//                } else if (i == BottomSheetBehavior.STATE_SETTLING) {
//                    button.setText("settl");
//                    Log.d("Amar Status", "settl");
//                } else if (i == BottomSheetBehavior.STATE_HIDDEN) {
//                    button.setText("hidden");
//                    Log.d("Amar Status", "hidden");
                }
            }

            @Override
            public void onSlide(@NonNull View view, float slideOffset) {
//                Log.d("Amar Offset",String.valueOf(slideOffset));
                slideOffset = slideOffset + 3;
                videoView.animate().scaleX(1 + slideOffset).scaleY(1 + slideOffset).setDuration(0).start();
            }
        });
    }

    private int getDisplayRatio() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayheight = displayMetrics.heightPixels;
        int displaywidth = displayMetrics.widthPixels;
        return displaywidth;
    }
}
