package bz.luoye.fillparentvideoview;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "bz_MainActivity";
    private ViewGroup root_view;
    private FillParentVideoView fillParentVideoView;
    private int videoLastPosition = -1;
    private final static String videoPath = "/sdcard/bzmedia/TimHortons.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        root_view = findViewById(R.id.root_view);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != fillParentVideoView && fillParentVideoView.isPlaying()) {
            videoLastPosition = fillParentVideoView.getCurrentPosition();
            fillParentVideoView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestPermission();
        if (null != fillParentVideoView && videoLastPosition != -1) {
            try {
                fillParentVideoView.seekTo(videoLastPosition);
                fillParentVideoView.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private boolean requestPermission() {
        ArrayList<String> permissionList = new ArrayList<>();
        if (!PermissionUtil.isPermissionGranted(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        String[] permissionStrings = new String[permissionList.size()];
        permissionList.toArray(permissionStrings);

        if (permissionList.size() > 0) {
            PermissionUtil.requestPermission(this, permissionStrings, PermissionUtil.CODE_REQ_PERMISSION);
            return false;
        } else {
            Log.d(TAG, "All the required permissions are available");
            return true;
        }
    }

    public void start(View view) {
        if (!requestPermission()) {
            return;
        }
        if (null != fillParentVideoView) {
            root_view.removeView(fillParentVideoView);
        }
        fillParentVideoView = new FillParentVideoView(this);
        root_view.addView(fillParentVideoView);
        fillParentVideoView.setVideoPath(videoPath);
        fillParentVideoView.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != fillParentVideoView) {
            fillParentVideoView.stopPlayback();
        }
    }
}
