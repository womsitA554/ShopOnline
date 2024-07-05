package com.example.shoponline_java.Model;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private String orderId;
    private String userId;
    private List<Item> cartItems;
    private String paymentMethod;
    private String address;
    private String orderTime;
    private double totalPrice;

    public Order() {
    }

    public Order(String orderId, String userId, List<Item> cartItems, String paymentMethod, String address, String orderTime, double totalPrice) {
        this.orderId = orderId;
        this.userId = userId;
        this.cartItems = cartItems;
        this.paymentMethod = paymentMethod;
        this.address = address;
        this.orderTime = orderTime;
        this.totalPrice = totalPrice;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Item> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<Item> cartItems) {
        this.cartItems = cartItems;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
