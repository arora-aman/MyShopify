package com.aroraaman.myshopify.model;

import java.util.ArrayList;

public class Order {
    public static final String ORDERS_KEY = "orders";
    public final static String TOTAL_PRICE_KEY = "total_price";

    public final Customer customer;
    public final ArrayList<Item> items;
    public final double totalPrice;

    public Order(Customer customer, ArrayList<Item> items, double totalPrice) {
        this.customer = customer;
        this.items = items;
        this.totalPrice = totalPrice;
    }
}
