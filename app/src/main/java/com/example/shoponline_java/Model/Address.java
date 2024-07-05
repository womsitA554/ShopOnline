package com.example.shoponline_java.Model;

import java.io.Serializable;

public class Address implements Serializable {
    private String userId;
    private String address;
    private Address(){

    }

    public Address(String userId, String address) {
        this.userId = userId;
        this.address = address;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
