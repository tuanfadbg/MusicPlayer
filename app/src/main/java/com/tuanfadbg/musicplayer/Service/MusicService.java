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

public class MusicService extends Service{
    MediaPlayer player;
    ArrayList<Song> songs;
    int position;

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

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
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
    }
    public void next() {
        position = (position + 1)%songs.size();
        play();
    }
    public void previous() {
        position = (position + songs.size() - 1)%songs.size();
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
}
