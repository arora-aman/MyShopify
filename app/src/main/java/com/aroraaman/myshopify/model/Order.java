package com.aroraaman.myshopify.model;

import java.util.ArrayList;

public class Order {

    public final Customer customer;
    public final ArrayList<Item> items;
    public final double totalPrice;

    public Order(Customer customer, ArrayList<Item> items, double totalPrice) {
        this.customer = customer;
        this.items = items;
        this.totalPrice = totalPrice;
    }
}
