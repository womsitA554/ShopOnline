package com.example.shoponline_java.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Item implements Serializable {
    private String title;
    private String description;
    private ArrayList<String> picUrl;
    private double price;
    private double oldPrice;
    private int review;
    private double rating;
    private int numberInCart;
    private HashMap<String, Integer> sizes;

    public Item(){
        this.sizes = new HashMap<>();
    }

    public Item(String title, String description, ArrayList<String> picUrl, double price, double oldPrice, int review, double rating, int numberInCart) {
        this.title = title;
        this.description = description;
        this.picUrl = picUrl;
        this.price = price;
        this.oldPrice = oldPrice;
        this.review = review;
        this.rating = rating;
        this.numberInCart = numberInCart;
        this.sizes = new HashMap<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(ArrayList<String> picUrl) {
        this.picUrl = picUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }

    public HashMap<String, Integer> getSizes() {
        return sizes;
    }

    public void setSizes(HashMap<String, Integer> sizes) {
        this.sizes = sizes;
    }

    public void addSize(String size, int quantity){
        this.sizes.put(size, quantity);
    }
}
