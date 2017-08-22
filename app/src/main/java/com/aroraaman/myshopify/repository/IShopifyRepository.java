package com.aroraaman.myshopify.repository;

import android.arch.lifecycle.LiveData;

import com.aroraaman.myshopify.model.Order;

import java.util.ArrayList;

public interface IShopifyRepository {
    /**
     * Makes a request to fetch latest list of orders.
     *
     * @return Last known list of orders wrapped around with state
     * {@link ResourceWrapper.State#LOADING}.
     */
    LiveData<ResourceWrapper<ArrayList<Order>>> getOrders(String webAddress);
}