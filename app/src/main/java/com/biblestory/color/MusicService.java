package com.biblestory.color;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Binder;
import android.os.IBinder;


public class MusicService extends Service implements OnErrorListener {

    //jangan ubah apapupun di sini
    private int length = 0;
    private final IBinder mBinder = new ServiceBinder();
    MediaPlayer mPlayer;

    class C04661 implements OnErrorListener {
        C04661() {
        }

        public boolean onError(MediaPlayer mp, int what, int extra) {
            onError(MusicService.this.mPlayer, what, extra);
            return true;
        }
    }

    public class ServiceBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    public IBinder onBind(Intent arg0) {
        return this.mBinder;
    }

    public void onCreate() {
        super.onCreate();
        this.mPlayer = MediaPlayer.create(this, R.raw.audio);
        this.mPlayer.setOnErrorListener(this);
        if (this.mPlayer != null) {
            this.mPlayer.setLooping(true);
        }
        this.mPlayer.setOnErrorListener(new C04661());
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        this.mPlayer.start();
        return START_STICKY;
    }

    public void pauseMusic() {
        if (this.mPlayer.isPlaying()) {
            this.mPlayer.pause();
            this.length = this.mPlayer.getCurrentPosition();
        }
    }

    public void resumeMusic() {
        if (!this.mPlayer.isPlaying()) {
            this.mPlayer.seekTo(this.length);
            this.mPlayer.start();
        }
    }

    public void stopMusic() {
        this.mPlayer.stop();
        this.mPlayer.release();
        this.mPlayer = null;
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mPlayer != null) {
            try {
                this.mPlayer.stop();
                this.mPlayer.release();
            } finally {
                this.mPlayer = null;
            }
        }
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (this.mPlayer != null) {
            try {
                this.mPlayer.stop();
                this.mPlayer.release();
            } finally {
                this.mPlayer = null;
            }
        }
        return false;
    }
}
