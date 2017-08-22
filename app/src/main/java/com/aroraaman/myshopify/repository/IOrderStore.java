package com.aroraaman.myshopify.repository;

import com.aroraaman.myshopify.model.Order;

import org.json.JSONException;

import java.util.ArrayList;

interface IOrderStore {
    /**
     * Persist a list of orders as a JSON string in SharedPreferences.
     * @param orders The list of orders.
     */
    void persistOrders(ArrayList<Order> orders);

    /**
     * @return List of orders that was stored in SharedPreferences.
     *
     * @throws JSONException if the data stored in SharedPreferences can't be
     *                       parsed as a list of orders.
     */
    ArrayList<Order> getOrders() throws JSONException;

    /**
     * Clears all orders stored in SharedPreferences.
     */
    void clearOrders();
}
