package com.tuanfadbg.musicplayer;

import java.util.ArrayList;

public class ListMusic {
    public ArrayList getList() {
        ArrayList<Song> listMusic = new ArrayList<>();
        listMusic.add(new Song(0,"Cho tôi xin một vé đi tuổi thơ", R.raw.cho_toi_xin_mot_ve_di_tuoi_tho, "Link Lee"));
        listMusic.add(new Song(1,"Đời dạy tôi", R.raw.doi_day_toi, "Only C"));
        listMusic.add(new Song(2,"Làm tình nguyện hết mình", R.raw.lam_tinh_nguyen_het_minh, "Ba Con Soi"));
        listMusic.add(new Song(3,"Mình yêu nhau đi", R.raw.minh_yeu_nhau_di, "Bích Phương"));
        listMusic.add(new Song(4,"Người âm phủ", R.raw.nguoi_am_phu, "I Dont Know"));
        listMusic.add(new Song(5,"Nơi này có anh", R.raw.noi_nay_co_anh, "Sơn Tùng MTP"));
        listMusic.add(new Song(6,"Yêu 5", R.raw.yeu_5, "I dont Know"));
        return listMusic;
    }
}
