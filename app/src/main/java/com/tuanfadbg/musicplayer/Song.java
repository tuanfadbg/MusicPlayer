package com.tuanfadbg.musicplayer;

public class Song {
    private String name;
    private int resource;
    private String author;
    public Song(String name, int resource, String author) {

        this.name = name;
        this.resource = resource;
        this.author = author;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


}
