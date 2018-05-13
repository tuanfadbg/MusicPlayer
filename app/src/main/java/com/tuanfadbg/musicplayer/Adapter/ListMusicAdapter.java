package com.tuanfadbg.musicplayer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.tuanfadbg.musicplayer.Song;
import com.tuanfadbg.musicplayer.R;
import java.util.ArrayList;

public class ListMusicAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<Song> listMusic;

    public ListMusicAdapter(Context context, int layout, ArrayList<Song> listMusic) {
        this.context = context;
        this.layout = layout;
        this.listMusic = listMusic;
    }


    @Override
    public int getCount() {
        return listMusic.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    private class ViewHolder {
        TextView name;
        TextView position;
        TextView author;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);
            viewHolder.name = convertView.findViewById(R.id.name);
            viewHolder.position = convertView.findViewById(R.id.position);
            viewHolder.author = convertView.findViewById(R.id.author);
            convertView.setTag(viewHolder);
        } else  {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(listMusic.get(position).getName());
        viewHolder.position.setText(position + 1 + "");
        viewHolder.author.setText(listMusic.get(position).getAuthor());
        return convertView;
    }
}
