package com.example.shoponline_java.Model;

public class Category {
    private int id;
    private String title;
    private String picUrl;

    public Category(int id, String title, String picUrl) {
        this.id = id;
        this.title = title;
        this.picUrl = picUrl;
    }

    public Category(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
