package com.aroraaman.myshopify.repository;

import com.aroraaman.myshopify.model.Order;

import org.json.JSONException;

import java.util.ArrayList;

public interface IOrderParser {

    /**
     * Parses a list of orders from a JSON String.
     * @param jsonText The JSON string
     * @return List of orders.
     *
     * @throws JSONException if <code>jsonText</code> can't be parsed into a list of orders.
     */
    ArrayList<Order> getOrders(String jsonText) throws JSONException;
}
