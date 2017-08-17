package com.aroraaman.myshopify;

import com.aroraaman.myshopify.model.Customer;
import com.aroraaman.myshopify.model.Item;
import com.aroraaman.myshopify.model.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderParser {

    public static ArrayList<Order> getOrders(String jsonText) throws JSONException {
        ArrayList<Order> ordersList = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(jsonText);
        JSONArray orders = jsonObject.getJSONArray(Order.ORDERS_KEY);
        int orderCount = orders.length();
        for (int i = 0; i < orderCount; ++i) {
            JSONObject order = (JSONObject) orders.get(i);

            double totalPrice = order.getDouble(Order.TOTAL_PRICE_KEY);

            JSONObject customerObject = null;
            Customer customer = null;

            if (order.has(Customer.CUSTOMER_KEY)) {
                customerObject = order.getJSONObject(Customer.CUSTOMER_KEY);
            } else if (order.has(Customer.BILLING_ADDRESS_KEY)) {
                customerObject = order.getJSONObject(Customer.BILLING_ADDRESS_KEY);
            }
            if (customerObject != null) {
                String firstName = customerObject.getString(Customer.FIRST_NAME_KEY);
                String lastName = customerObject.getString(Customer.LAST_NAME_KEY);
                customer = new Customer(firstName, lastName);
            }

            JSONArray itemsArray = order.getJSONArray(Item.ITEMS_KEY);
            int itemCount = itemsArray.length();

            ArrayList<Item> items = new ArrayList<>();

            for (int j = 0; j < itemCount; ++j) {
                JSONObject itemObject = itemsArray.getJSONObject(j);

                String itemName = itemObject.getString(Item.TITLE_KEY);
                int quantity = itemObject.getInt(Item.QUANTITY_KEY);

                Item item = new Item(itemName, quantity);
                items.add(item);
            }

            ordersList.add(new Order(customer, items, totalPrice));
        }

        return ordersList;
    }
}
