package com.aroraaman.myshopify.model;

public class Item {

    public final String title;
    public final int quantity;

    public Item(String title, int quantity) {
        this.title = title;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return  title + " x" + quantity;
    }
}
