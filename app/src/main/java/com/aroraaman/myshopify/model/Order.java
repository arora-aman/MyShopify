package com.aroraaman.myshopify.model;

import java.util.ArrayList;

public class Order {

    public final Customer customer;
    public final ArrayList<Item> items;
    public final double totalPrice;
    public final String createdAt;

    public Order(Customer customer, ArrayList<Item> items, double totalPrice, String createdAt) {
        this.customer = customer;
        this.items = items;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
    }
}
