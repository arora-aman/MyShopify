package com.aroraaman.myshopify.repository;

import com.aroraaman.myshopify.model.Order;

import java.util.ArrayList;

interface IOrderStore {
    /**
     * Persist a list of orders as a JSON string in SharedPreferences.
     * @param orders The list of orders.
     */
    void persistOrders(ArrayList<Order> orders);

    /**
     * @return List of orders if json stored(for orders) in SharedPreferences is valid else null.
     */
    ArrayList<Order> getOrders();

    /**
     * Clears all orders stored in SharedPreferences.
     */
    void clearOrders();
}
