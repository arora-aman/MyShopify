package com.aroraaman.myshopify.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.aroraaman.myshopify.model.Customer;
import com.aroraaman.myshopify.model.Item;
import com.aroraaman.myshopify.model.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class OrderStore implements IOrderStore {
    private static final String PREF_NAME = "com.aroraaman.myshopify.repo_orders";
    private static final String KEY_ORDERS_JSON = "KEY_ORDERS_JSON";

    private static final String KEY_CUSTOMER = Customer.CUSTOMER_KEY;
    private static final String KEY_CUSTOMER_FIRST_NAME = Customer.FIRST_NAME_KEY;
    private static final String KEY_CUSTOMER_LAST_NAME = Customer.LAST_NAME_KEY;
    private static final String KEY_TOTAL_PRICE = Order.TOTAL_PRICE_KEY;
    private static final String KEY_ITEMS = Item.ITEMS_KEY;
    private static final String KEY_ITEM_TITLE = Item.TITLE_KEY;
    private static final String KEY_ITEM_QTY = Item.QUANTITY_KEY;
    private static final String KEY_ORDERS = Order.ORDERS_KEY;

    private SharedPreferences mPrefs;

    OrderStore(Context context) {
        mPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void persistOrders(ArrayList<Order> orders) {
        JSONObject ordersObject = new JSONObject();
        JSONArray ordersArray = new JSONArray();
        try {
            for (Order order : orders) {
                JSONObject orderObject = new JSONObject();
                if (order.customer != null) {
                    JSONObject customerObject = new JSONObject();
                    customerObject.putOpt(KEY_CUSTOMER_FIRST_NAME, order.customer.firstName);
                    customerObject.putOpt(KEY_CUSTOMER_LAST_NAME, order.customer.lastName);
                    orderObject.putOpt(KEY_CUSTOMER, customerObject);
                }

                orderObject.put(KEY_TOTAL_PRICE, order.totalPrice);

                JSONArray items = new JSONArray();

                for (Item item : order.items) {
                    JSONObject itemObject = new JSONObject();
                    itemObject.putOpt(KEY_ITEM_TITLE, item.title);
                    itemObject.putOpt(KEY_ITEM_QTY, item.quantity);

                    items.put(itemObject);
                }

                orderObject.put(KEY_ITEMS, items);
                ordersArray.put(orderObject);
            }

            ordersObject.put(KEY_ORDERS, ordersArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mPrefs.edit().putString(KEY_ORDERS_JSON, ordersObject.toString()).apply();
    }

    @Override
    public ArrayList<Order> getOrders() throws JSONException {
        String json = mPrefs.getString(KEY_ORDERS_JSON, null);
        ArrayList<Order> orders = new ArrayList<>();
        orders = new OrderParser().getOrders(json);
        return orders;
    }

    @Override
    public void clearOrders() {
        mPrefs.edit().remove(KEY_ORDERS_JSON).apply();
    }
}