package com.tuanfadbg.musicplayer.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.tuanfadbg.musicplayer.ListMusic;
import com.tuanfadbg.musicplayer.Song;

import java.util.ArrayList;

public class MusicService extends Service{
    MediaPlayer player;
    ArrayList<Song> songs;
    int position;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (player == null) {
            player = new MediaPlayer();

        }
        if (songs == null) {
            setSongs(new ListMusic().getList());
        }

        return super.onStartCommand(intent, flags, startId);
    }
    public MediaPlayer getPlayer() {
        return player;
    }

    public void setPlayer(MediaPlayer player) {
        this.player = player;
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

}
