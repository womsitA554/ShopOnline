package com.example.shoponline_java.Model;

public class User {
    private String PhoneNumber;
    private String Password;
    private String UserId;
    private String Address;

    public User(){

    }
    public User(String userId,String phoneNumber, String password , String address) {
        PhoneNumber = phoneNumber;
        Password = password;
        UserId = userId;
        Address = address;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
