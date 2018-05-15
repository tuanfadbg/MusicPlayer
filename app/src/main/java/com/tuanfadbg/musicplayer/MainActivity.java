package com.tuanfadbg.musicplayer;

import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tuanfadbg.musicplayer.Service.MusicService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    LinearLayout miniPlayer, seekBarPlayer;
    RelativeLayout infoPlaying;
    ImageButton btnPrev, btnPlay, btnNext, btnReload, btnShuffler;
    TextView namePlaying, authorPlaying, currentDuration, duration;
    SeekBar seekBar;
    MusicService musicService;
    boolean isListMusic = true;
    boolean musicBound = false;
    Intent playIntent;
    ListMusicFragment listMusicFragment;
    Runnable runable;
    Handler mHandler;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");

    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        declare();
        infoPlaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (savedInstanceState != null) {
                    return;
                }

                changeToMusicInfoFragment();
                changeToMusicInfoLayout();
                isListMusic = false;
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previous();
            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicService.isPlaying()) {
                    pause();
                    changeLayoutOnStop();
                    if (isListMusic) {

                    }
                } else {
                    resume();
                    changelayoutOnPlaying();
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setSeekbar(progress);
                musicService.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void changeLayoutOnStop() {
        btnPlay.setImageResource(R.drawable.ic_stop);
    }

    private void changelayoutOnPlaying() {
        int position = musicService.getPosition();
        changeInfoPlaying(position);
        btnPlay.setImageResource(R.drawable.ic_play);
        changeActionBar(position);
    }

    private void changeActionBar(int position) {
        getSupportActionBar().setTitle(listMusicFragment.listMusic.get(position).getName());
    }

    private void changeInfoPlaying(int position) {
        namePlaying.setText(listMusicFragment.listMusic.get(position).getName());
        authorPlaying.setText(listMusicFragment.listMusic.get(position).getAuthor());
    }

    private void pause() {
        musicService.pause();
    }

    private void resume() {
        musicService.resume();
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(runable);
        stopService(playIntent);
        unbindService(musicConnection);
        musicService = null;
        super.onDestroy();
    }

    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder musicBinder = (MusicService.MusicBinder) service;
            musicService = musicBinder.getService();
            listMusicFragment = (ListMusicFragment) getFragmentManager().findFragmentById(R.id.main_fragment);
            musicService.setSongs(listMusicFragment.listMusic);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };
    @Override
    public void onBackPressed() {
        if(isListMusic == false) {
            changeToListMusicFragment();
            changeToListMusicLayout();
            isListMusic = true;
        } else {
            super.onBackPressed();
        }
    }

    private void changeToListMusicLayout() {
        hideSeekBar();
        infoPlaying.setVisibility(View.VISIBLE);
        btnReload.setVisibility(View.GONE);
        btnShuffler.setVisibility(View.GONE);
        miniPlayer.setWeightSum(20);
        miniPlayer.setMinimumHeight(0);
    }

    private void changeToListMusicFragment() {
        getFragmentManager().popBackStack();
    }

    private void changeToMusicInfoLayout() {

        showSeekBar();
        infoPlaying.setVisibility(View.GONE);
        btnReload.setVisibility(View.VISIBLE);
        btnShuffler.setVisibility(View.VISIBLE);
        miniPlayer.setWeightSum(15);
        miniPlayer.setMinimumHeight(150);
    }

    private void showSeekBar() {
        seekBarPlayer.setVisibility(View.VISIBLE);
    }


    private void hideSeekBar() {
        seekBarPlayer.setVisibility(View.GONE);
    }

    private void changeToMusicInfoFragment() {
        MusicInfoFragment musicInfoFragment = new MusicInfoFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.main, musicInfoFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void declare() {
        infoPlaying = findViewById(R.id.info_playing);
        seekBar = findViewById(R.id.seek_bar);
        seekBarPlayer = findViewById(R.id.seek_bar_player);
        miniPlayer = findViewById(R.id.mini_player);
        btnPrev = findViewById(R.id.btn_previous);
        btnPlay = findViewById(R.id.btn_play);
        btnNext = findViewById(R.id.btn_next);
        btnReload = findViewById(R.id.btn_reload);
        btnShuffler = findViewById(R.id.btn_shuffler);
        namePlaying = findViewById(R.id.name_playing);
        authorPlaying = findViewById(R.id.author_playing);
        currentDuration = findViewById(R.id.current_duration);
        duration = findViewById(R.id.duration);
    }

    public void play(int position) {
        musicService.setPosition(position);
        musicService.play();
        changelayoutOnPlaying();
        miniPlayer.setVisibility(View.VISIBLE);
        changeMaxDuration();
        setRunableSeekBar();
    }

    private void setRunableSeekBar() {
        changeMaxDuration();
        mHandler = new Handler();
        runable = new Runnable() {
            @Override
            public void run() {
                if (musicService.isPlaying()) {
                    setSeekbar(musicService.getCurrentPosition());
                }
                mHandler.postDelayed(this, 1000);
            }
        };

        MainActivity.this.runOnUiThread(runable);
    }

    private void setSeekbar(int currentPosition) {
        setCurrentProgressSeekBar(currentPosition);
        setCurrentTimeInSeekBar(currentPosition);
    }

    private void setCurrentTimeInSeekBar(int currentTimeInSeekBar) {
        currentDuration.setText(simpleDateFormat.format(new Date(currentTimeInSeekBar)));
    }

    private void setCurrentProgressSeekBar(int progressSeekBar) {
        seekBar.setProgress(progressSeekBar);
    }

    public void next() {
        musicService.next();
        changelayoutOnPlaying();
        changeMaxDuration();
    }
    public void previous() {
        musicService.previous();
        changelayoutOnPlaying();
        changeMaxDuration();
    }
    public void changeMaxDuration() {
        seekBar.setMax(musicService.getDuration());
        String strDuration = simpleDateFormat.format(new Date(musicService.getDuration()));
        duration.setText(strDuration);
    }
}
