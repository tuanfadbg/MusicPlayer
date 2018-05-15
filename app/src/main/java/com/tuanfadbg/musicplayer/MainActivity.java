package com.tuanfadbg.musicplayer;

import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
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

import com.tuanfadbg.musicplayer.Service.IServiceCallbacks;
import com.tuanfadbg.musicplayer.Service.MusicService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements IServiceCallbacks {
    LinearLayout miniPlayer, seekBarPlayer;
    RelativeLayout infoPlaying;
    ImageButton btnPrev, btnPlay, btnNext, btnReload, btnShuffle;
    TextView namePlaying, authorPlaying, currentDuration, duration;
    SeekBar seekBar;
    MusicService musicService;
    boolean isListMusic = true;
    boolean musicBound = false;
    Intent playIntent;
    ListMusicFragment listMusicFragment;
    Runnable runable;
    Handler mHandler;
    boolean isLooping = false;
    boolean isShuffle = false;

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
        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLooping) {
                    isLooping = false;
                    btnReload.setBackgroundColor(Color.TRANSPARENT);
                } else {
                    isLooping = true;
                    btnReload.setBackgroundResource(R.drawable.ic_background_active);
                }
                musicService.setLooping(isLooping);
            }
        });
        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShuffle) {
                    isShuffle = false;

                    btnShuffle.setBackgroundColor(Color.TRANSPARENT);

                } else {
                    isShuffle = true;
                    btnShuffle.setBackgroundResource(R.drawable.ic_background_active);
                }
                musicService.setShuffle(isShuffle);
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
        stopmHandler();
        stopService(playIntent);
        unbindService(musicConnection);
        musicService = null;
        super.onDestroy();
    }

    private void stopmHandler() {
        mHandler.removeCallbacks(runable);
    }

    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder musicBinder = (MusicService.MusicBinder) service;
            musicService = musicBinder.getService();
            listMusicFragment = (ListMusicFragment) getFragmentManager().findFragmentById(R.id.main_fragment);
            musicService.setSongs(listMusicFragment.listMusic);
            musicBound = true;
            musicService.setServiceCallbacks(MainActivity.this);
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
        btnShuffle.setVisibility(View.GONE);
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
        btnShuffle.setVisibility(View.VISIBLE);
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
        transaction.setCustomAnimations(R.animator.enter_from_right, R.animator.exit_from_left, R.animator.enter_from_right, R.animator.exit_from_right);
        transaction.add(R.id.main, musicInfoFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    public void play(int position) {
        musicService.setPosition(position);
        musicService.play();
        changelayoutOnPlaying();
        miniPlayer.setVisibility(View.VISIBLE);
        changeMaxDurationAndResetSeekBar();
        setRunableSeekBar();
    }

    private void setRunableSeekBar() {
        changeMaxDurationAndResetSeekBar();
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
        changeMaxDurationAndResetSeekBar();
    }
    public void previous() {
        musicService.previous();
        changelayoutOnPlaying();
        changeMaxDurationAndResetSeekBar();
    }
    public void changeMaxDurationAndResetSeekBar() {
        seekBar.setMax(musicService.getDuration());
        String strDuration = simpleDateFormat.format(new Date(musicService.getDuration()));
        duration.setText(strDuration);
        setSeekbar(0);
    }

    @Override
    public void changeLayoutWhenAutoNextPlayer() {
        changeMaxDurationAndResetSeekBar();
        changelayoutOnPlaying();

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
        btnShuffle = findViewById(R.id.btn_shuffler);
        namePlaying = findViewById(R.id.name_playing);
        authorPlaying = findViewById(R.id.author_playing);
        currentDuration = findViewById(R.id.current_duration);
        duration = findViewById(R.id.duration);
    }
}
