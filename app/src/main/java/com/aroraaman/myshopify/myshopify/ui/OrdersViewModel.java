package com.aroraaman.myshopify.myshopify.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.aroraaman.myshopify.model.Item;
import com.aroraaman.myshopify.model.Order;
import com.aroraaman.myshopify.repository.IShopifyRepository;
import com.aroraaman.myshopify.repository.ResourceWrapper;

import java.util.ArrayList;

public class OrdersViewModel extends ViewModel {
    private final IShopifyRepository mShopifyRepository;

    public OrdersViewModel(IShopifyRepository shopifyRepository) {
        mShopifyRepository = shopifyRepository;
    }

    public LiveData<ResourceWrapper<ArrayList<Order>>> getOrders(String webAddress) {
        return mShopifyRepository.getOrders(webAddress);
    }

    public Data<Double> amountSpentProcessor(ArrayList<Order> orders, String firstName, String lastName) {
        if (orders == null) {
            return null;
        }

        double reqdCustomerExpenditure = 0;
        double othersExpenditure = 0;

        for (Order order : orders) {
            if (order.customer != null && firstName.equals(order.customer.firstName) && lastName.equals(order.customer.lastName)) {
                reqdCustomerExpenditure += order.totalPrice;
            } else {
                othersExpenditure += order.totalPrice;
            }
        }

        return new Data<>(reqdCustomerExpenditure, othersExpenditure, firstName + " " + lastName);
    }

    public Data<Integer> quantityProcessor(ArrayList<Order> orders, String title) {
        if (orders == null) {
            return null;
        }

        int reqdItemsSold = 0;
        int otherItemsSold = 0;

        for (Order order : orders) {
            for (Item item : order.items) {
                if (title.equals(item.title)) {
                    reqdItemsSold += item.quantity;
                } else {
                    otherItemsSold += item.quantity;
                }
            }
        }

        return new Data<>(reqdItemsSold, otherItemsSold, title);
    }

    /**
     * Class to hold data processed by the view model.
     *
     * @param <T> Type of data processed by the view model.
     */
    static class Data<T> {
        /**
         * The value of data corresponding to the requested parameters.
         */
        final T requiredValue;
        /**
         * The value of data corresponding to parameters but the requested ones.
         */
        final T othersValue;

        /**
         * Value of the required parameter
         */
        final String reqdParameter;

        Data(T requiredValue, T othersValue, String reqdParameter) {
            this.requiredValue = requiredValue;
            this.othersValue = othersValue;
            this.reqdParameter = reqdParameter;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Data<?> data = (Data<?>) o;

            if (!requiredValue.equals(data.requiredValue)) {
                return false;
            }
            if (!othersValue.equals(data.othersValue)) {
                return false;
            }
            return reqdParameter.equals(data.reqdParameter);
        }
    }
}