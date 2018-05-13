package com.tuanfadbg.musicplayer;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    LinearLayout miniPlayer;
    RelativeLayout infoPlaying;
    ImageButton btnPrev, btnPlay, btnNext, btnReload, btnShuffler;
    TextView namePlaying, authorPlaying;
    SeekBar seekBarPlayer;
    boolean isListMusic = true;
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

    }

    @Override
    public void onBackPressed() {
        if(isListMusic == false) {
            changeToListMusicFragment();
            changeToListMusicLayout();
            isListMusic = false;
        }
        super.onBackPressed();
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
        MusicInfoFragment musicInfoFragment = new MusicInfoFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.main, musicInfoFragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
        seekBarPlayer.setVisibility(View.INVISIBLE);
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
        seekBarPlayer = findViewById(R.id.seek_bar_player);
        miniPlayer = findViewById(R.id.mini_player);
        btnPrev = findViewById(R.id.btn_previous);
        btnPlay = findViewById(R.id.btn_play);
        btnNext = findViewById(R.id.btn_next);
        btnReload = findViewById(R.id.btn_reload);
        btnShuffler = findViewById(R.id.btn_shuffler);
        namePlaying = findViewById(R.id.name_playing);
        authorPlaying = findViewById(R.id.author_playing);

    }

}
