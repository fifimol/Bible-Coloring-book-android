package com.biblestory.color;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.AttributeSet;
import android.view.View;

public class MusicActivity extends AppBaseActivity {


    //ganti backsound di raw folder


    protected ServiceConnection Scon = new C04651();
    protected boolean mIsBound = false;
    protected Intent musicIntent;
    protected Boolean playMusic = Boolean.valueOf(true);

    class C04651 implements ServiceConnection {
        C04651() {
        }

        public void onServiceConnected(ComponentName name, IBinder binder) {
        }

        public void onServiceDisconnected(ComponentName name) {
        }
    }

    public  View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(view, str, context, attributeSet);
    }

    public View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(str, context, attributeSet);
    }

    protected void loadMusicPrefs() {
        this.playMusic = Boolean.valueOf(getSharedPreferences("PrincessColor", 0).getBoolean("playMusic", true));
    }

    protected void doBindService() {
        bindService(new Intent(this, MusicService.class), this.Scon, BIND_AUTO_CREATE);
        this.mIsBound = true;
    }

    protected void doUnbindService() {
        if (this.mIsBound) {
            unbindService(this.Scon);
            this.mIsBound = false;
        }
    }

    protected void playMusic() {
        loadMusicPrefs();
        if (this.playMusic.booleanValue()) {
            doBindService();
            if (this.musicIntent == null) {
                this.musicIntent = new Intent();
                this.musicIntent.setClass(this, MusicService.class);
            }
            startService(this.musicIntent);
        }
    }

    protected void stopMusic() {
        doUnbindService();
        if (this.musicIntent != null) {
            stopService(this.musicIntent);
        }
    }

    protected void onPause() {
        super.onPause();
        stopMusic();
    }

    protected void onResume() {
        super.onResume();
        playMusic();
    }

    protected void onDestroy() {
        super.onDestroy();
    }
}
