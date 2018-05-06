package com.aroraaman.myshopify.repository;

import com.aroraaman.myshopify.model.Customer;
import com.aroraaman.myshopify.model.Item;
import com.aroraaman.myshopify.model.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class OrderParser implements IOrderParser {

    private static final String KEY_ORDERS = "orders";
    private static final String KEY_TOTAL_PRICE = "total_price";
    private static final String KEY_CUSTOMER = "customer";
    private static final String KEY_BILLING_ADDRESS = "billing_address";
    private static final String KEY_CUSTOMER_FIRST_NAME = "first_name";
    private static final String KEY_CUSTOMER_LAST_NAME = "last_name";
    private static final String KEY_ITEMS = "line_items";
    private static final String KEY_ITEM_TITLE = "title";
    private static final String KEY_ITEM_QTY = "fulfillable_quantity";

    @Override
    public ArrayList<Order> fromJson(String jsonString) {
        if (jsonString == null) {
            return null;
        }

        ArrayList<Order> ordersList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray orders = jsonObject.getJSONArray(KEY_ORDERS);
            int orderCount = orders.length();
            for (int i = 0; i < orderCount; ++i) {
                JSONObject order = (JSONObject) orders.get(i);

                double totalPrice = order.getDouble(KEY_TOTAL_PRICE);

                JSONObject customerObject = null;
                Customer customer = null;

                if (order.has(KEY_CUSTOMER)) {
                    customerObject = order.getJSONObject(KEY_CUSTOMER);
                } else if (order.has(KEY_BILLING_ADDRESS)) {
                    customerObject = order.getJSONObject(KEY_BILLING_ADDRESS);
                }
                if (customerObject != null) {
                    String firstName = customerObject.getString(KEY_CUSTOMER_FIRST_NAME);
                    String lastName = customerObject.getString(KEY_CUSTOMER_LAST_NAME);
                    customer = new Customer(firstName, lastName);
                }

                JSONArray itemsArray = order.getJSONArray(KEY_ITEMS);
                int itemCount = itemsArray.length();

                ArrayList<Item> items = new ArrayList<>();

                for (int j = 0; j < itemCount; ++j) {
                    JSONObject itemObject = itemsArray.getJSONObject(j);

                    String itemName = itemObject.getString(KEY_ITEM_TITLE);
                    int quantity = itemObject.getInt(KEY_ITEM_QTY);

                    Item item = new Item(itemName, quantity);
                    items.add(item);
                }

                ordersList.add(new Order(customer, items, totalPrice));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return ordersList;
    }

    @Override
    public String toJson(ArrayList<Order> orders) {
        if (orders == null) {
            return null;
        }

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
            return null;
        }

        return ordersObject.toString();
    }
}
