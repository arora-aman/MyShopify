package com.aroraaman.myshopify.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Order {

    public final Customer customer;
    public final ArrayList<Item> items;
    public final double totalPrice;
    public final String createdAt;
    public final LocalDate createdAtDate;

    public Order(Customer customer, ArrayList<Item> items, double totalPrice, String createdAt) {
        this.customer = customer;
        this.items = items;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.createdAtDate = LocalDate.parse(createdAt, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"));
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Item item : items) {
            stringBuilder.append("- ")
                    .append(item.toString())
                    .append("\n");
        }

        String customerString = "Unknown\n\n";
        if (customer != null) {
            customerString = customer.toString() + "\n\n";
        }

        return  customerString
                + stringBuilder.toString() + "\n"
                + "Total Price: " + totalPrice + "\n\n"
                + "Created At: " + createdAt+ "\n" +
                "-------------------------------------";
    }
}
