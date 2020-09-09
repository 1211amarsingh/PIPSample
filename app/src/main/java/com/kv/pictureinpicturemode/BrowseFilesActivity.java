package com.kv.pictureinpicturemode;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Arrays;


public class BrowseFilesActivity extends AppCompatActivity {

    private static String songOne =
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
    private static String songTwo =
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4";
    private static String songThree =
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.files_list);

        findViewById(R.id.video_one).setOnClickListener(onClickListener);
        findViewById(R.id.video_two).setOnClickListener(onClickListener);
        findViewById(R.id.video_three).setOnClickListener(onClickListener);


        if (!getSharedPreferences(Utils.APP_PREFERENCE, Activity.MODE_PRIVATE).getBoolean(Utils.IS_ICON_CREATED, false)) {
            createShortcut();
            getSharedPreferences(Utils.APP_PREFERENCE, Activity.MODE_PRIVATE).edit().putBoolean(Utils.IS_ICON_CREATED, true).apply();
        }
    }

    private void createShortcut() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
            ShortcutInfo shortcut;

            shortcut = new ShortcutInfo.Builder(this, "second_shortcut")
                    .setShortLabel("PIP")
                    .setLongLabel("PICTURE IN PICTURE MODE")
                    .setIcon(Icon.createWithResource(this, R.mipmap.ic_launcher))
                    .setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.co.in")))
                    .build();
            shortcutManager.setDynamicShortcuts(Arrays.asList(shortcut));
        } else {

            Intent shortCutInt = new Intent(getApplicationContext(),
                    BrowseFilesActivity.class);
            shortCutInt.setAction(Intent.ACTION_MAIN);
            Intent addInt = new Intent();
            addInt.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortCutInt);
            addInt.putExtra(Intent.EXTRA_SHORTCUT_NAME, "PIP App");
            addInt.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(),
                    R.mipmap.ic_launcher));
            // Set Install action in Intent
            addInt.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            // Broadcast the created intent
            getApplicationContext().sendBroadcast(addInt);
        }
    }

    private final View.OnClickListener onClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.video_one: {
                            showSelectedVideo(songOne);
                            break;
                        }
                        case R.id.video_two: {
                            showSelectedVideo(songTwo);
                            break;
                        }
                        case R.id.video_three:
                            startBottomSheet(songThree);
                            break;
                    }
                }
            };

    private void startBottomSheet(String songThree) {
        Intent i = new Intent();
        i.setClass(this, BottomNavigation.class);
        i.putExtra("videoUrl", songThree);
        startActivity(i);
    }

    public void showSelectedVideo(String url) {

        Intent i = new Intent();
        i.setClass(this, PictureInPictureActivity.class);
        i.putExtra("videoUrl", url);
        startActivity(i);
    }

    private void whatsappContact() {


//            TelephonyManager phoneMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

//             Log.d("Other",String.valueOf(phoneMgr.getLine1Number()));
    }
}
