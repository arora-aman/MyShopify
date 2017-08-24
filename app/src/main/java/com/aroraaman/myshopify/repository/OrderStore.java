package com.aroraaman.myshopify.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.aroraaman.myshopify.model.Order;

import java.util.ArrayList;

class OrderStore implements IOrderStore {
    private static final String PREF_NAME = "com.aroraaman.myshopify.repo_orders";
    private static final String KEY_ORDERS_JSON = "KEY_ORDERS_JSON";

    private final IOrderParser mOrderParser;

    private SharedPreferences mPrefs;

    OrderStore(Context context, IOrderParser orderParser) {
        mPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mOrderParser = orderParser;
    }

    @Override
    public void persistOrders(ArrayList<Order> orders) {
        mPrefs.edit().putString(KEY_ORDERS_JSON, mOrderParser.toJson(orders)).apply();
    }

    @Override
    public ArrayList<Order> getOrders() {
        String json = mPrefs.getString(KEY_ORDERS_JSON, null);
        return mOrderParser.fromJson(json);
    }

    @Override
    public void clearOrders() {
        mPrefs.edit().remove(KEY_ORDERS_JSON).apply();
    }
}