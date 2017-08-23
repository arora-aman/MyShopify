package com.aroraaman.myshopify.repository;

import com.aroraaman.myshopify.model.Order;

import java.util.ArrayList;

public interface IOrderParser {

    /**
     * Parses a list of orders from a JSON String.
     *
     * @param jsonText The JSON string
     * @return List of orders if <code>jsonText</code> is valid json string else null.
     */
    ArrayList<Order> getOrders(String jsonText);
}
