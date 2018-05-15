package com.tuanfadbg.musicplayer.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.tuanfadbg.musicplayer.ListMusic;
import com.tuanfadbg.musicplayer.Song;

import java.util.ArrayList;
import java.util.Random;

public class MusicService extends Service{
    MediaPlayer player;
    ArrayList<Song> songs;
    int position;
    IServiceCallbacks serviceCallbacks;
    boolean isShuffle = false;
    private IBinder musicBind = new MusicBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        player.release();
        return true;
    }

    public void setServiceCallbacks(IServiceCallbacks callbacks) {
        serviceCallbacks = callbacks;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();
        initMusicPlayer();
        position = 0;
    }
    public void initMusicPlayer() {
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void resume() {
        player.start();
    }

    public void pause() {
        player.pause();
    }

    public void setLooping(boolean isLooping) {
        player.setLooping(isLooping);
    }

    public void setShuffle(boolean shuffle) {
        this.isShuffle = shuffle;
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
    public boolean isPlaying() {
        return player.isPlaying();
    }
    public void play() {
        player.reset();
        Log.e("Service", "play " + getPosition());
        player = MediaPlayer.create(this, songs.get(position).getResource());
        player.start();
        setAutoNext();
    }
    public void setAutoNext() {
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                next();
                if (serviceCallbacks != null) {
                    serviceCallbacks.changeLayoutWhenAutoNextPlayer();
                }
            }
        });
    }
    public void next() {
        if (isShuffle) {
            position = getNextPositionShuffle();
        } else {
            position = (position + 1)%songs.size();
        }
        play();
    }
    public void previous() {
        if (isShuffle) {
            position = getNextPositionShuffle();
        } else {
            position = (position + songs.size() - 1)%songs.size();
        }
        play();
    }
    public void seekTo(int msec) {
        player.seekTo(msec);;
    }

    public int getDuration() {
        return player.getDuration();
    }
    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }
    public int getNextPositionShuffle() {
        int pos = getPosition();
        int temp = new Random().nextInt(songs.size());
        while (pos != temp) {
            temp = new Random().nextInt(songs.size());
            if (pos != temp){
                pos = temp;
                break;
            }
        }
        return pos;
    }
}
