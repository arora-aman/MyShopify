package com.aroraaman.myshopify.model;

public class Item {
    public static final String ITEMS_KEY = "line_items";
    public static final String TITLE_KEY = "title";
    public static final String QUANTITY_KEY = "fulfillable_quantity";

    public final String title;
    public final int quantity;

    public Item(String title, int quantity) {
        this.title = title;
        this.quantity = quantity;
    }
}
