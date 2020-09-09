package com.kv.pictureinpicturemode;


import android.app.PictureInPictureParams;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Rational;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;


public class PictureInPictureActivity extends AppCompatActivity {

    private VideoView vv;

    private Button pip;
    private Uri videoUri;

    private PictureInPictureParams.Builder pictureInPictureParamsBuilder;

    private String defaultVideo = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_picture_in_picture);
        vv = findViewById(R.id.videoView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            pictureInPictureParamsBuilder = new PictureInPictureParams.Builder();
        }
        findViewById(R.id.play).setOnClickListener(onClickListener);
        findViewById(R.id.pause).setOnClickListener(onClickListener);
        pip = findViewById(R.id.pip);
        pip.setOnClickListener(onClickListener);
        setVideoView(getIntent());
    }

    private final View.OnClickListener onClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.play: {
                            vv.start();
                            break;
                        }
                        case R.id.pause: {
                            vv.pause();
                            break;
                        }
                        case R.id.pip:
                            pictureInPictureMode();
                            break;
                    }
                }
            };

    private void setVideoView(Intent i) {
        vv.setMediaController(new MediaController(this));
        String vUrl = i.getStringExtra("videoUrl");
        if (vUrl != null && !vUrl.isEmpty()) {
            videoUri = Uri.parse(vUrl);
        } else {
            videoUri = Uri.parse(defaultVideo);
        }
        vv.setVideoURI(videoUri);
        vv.requestFocus();

        vv.start();
    }

    private void pictureInPictureMode() {
        Rational aspectRatio = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            aspectRatio = new Rational(vv.getWidth(), vv.getHeight());
            pictureInPictureParamsBuilder.setAspectRatio(aspectRatio).build();
            enterPictureInPictureMode(pictureInPictureParamsBuilder.build());
        } else {
            Toast.makeText(this, "PIP Mode Not Supported", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUserLeaveHint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (!isInPictureInPictureMode()) {
                Rational aspectRatio = new Rational(vv.getWidth(), vv.getHeight());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    pictureInPictureParamsBuilder.setAspectRatio(aspectRatio).build();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    enterPictureInPictureMode(pictureInPictureParamsBuilder.build());
                }
            }
        }
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        if (isInPictureInPictureMode) {
            changeView(0);
        } else {
            changeView(1);
        }
    }

    @Override
    public void onNewIntent(Intent i) {
        setVideoView(i);
    }

    @Override
    public void onStop() {
        if (vv.isPlaying()) {
            vv.stopPlayback();
        }
        super.onStop();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            // Hide the status bar
            changeView(0);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

            // Hide the status bar
            changeView(1);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    private void changeView(int status) {
        if (status == 0) {
            getSupportActionBar().hide();
            pip.setVisibility(View.GONE);
        } else {         //stauts 1= visible actionbar & pip button
            getSupportActionBar().show();
            pip.setVisibility(View.VISIBLE);
        }
    }
}
