package com.tuanfadbg.musicplayer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;

public class MusicInfoFragment extends Fragment {
    ImageView imgDisc;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.music_info_fragment, container, false);
        declare(view);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.pic_disc);
        imgDisc.setAnimation(animation);
        animation.start();
        return view;
    }
    private void declare(View view) {
        imgDisc = view.findViewById(R.id.pic_disc);
    }
}
