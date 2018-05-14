package com.tuanfadbg.musicplayer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tuanfadbg.musicplayer.Adapter.ListMusicAdapter;

import java.util.ArrayList;

public class ListMusicFragment extends Fragment {
    ListView listViewMusic;
    ArrayList<Song> listMusic;
    ListMusicAdapter listMusicAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_music_fragment, container, false);
        declare(view);
        listViewMusic.setAdapter(listMusicAdapter);
        listViewMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity) getActivity()).play(position);
            }
        });
        return view;
    }
    private void declare(View view) {
        listViewMusic = view.findViewById(R.id.list_music);
        listMusic = new ListMusic().getList();
        listMusicAdapter = new ListMusicAdapter(getContext(), R.layout.list_music_item, listMusic);
    }
}
