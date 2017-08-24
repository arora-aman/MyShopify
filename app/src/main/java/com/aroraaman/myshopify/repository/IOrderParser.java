package com.aroraaman.myshopify.repository;

import com.aroraaman.myshopify.model.Order;

import java.util.ArrayList;

public interface IOrderParser {

    /**
     * Parses a list of orders from a JSON String.
     *
     * @param jsonString The JSON string.
     * @return List of orders if <code>jsonString</code> is valid json string else null.
     */
    ArrayList<Order> fromJson(String jsonString);

    /**
     * Converts a list of orders into a valid json string.
     * @param orders The list of orders.
     * @return Null if <code>orders</code> is null else a JSON string representing the list.
     */
    String toJson(ArrayList<Order> orders);
}
