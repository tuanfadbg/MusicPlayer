package com.tuanfadbg.musicplayer;

public class Song {
    private int id;
    private String name;
    private int resource;
    private String author;

    public Song(int id, String name, int resource, String author) {
        this.id = id;
        this.name = name;
        this.resource = resource;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
