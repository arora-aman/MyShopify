package com.aroraaman.myshopify.myshopify.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.aroraaman.myshopify.model.Order
import com.aroraaman.myshopify.repository.IShopifyRepository
import com.aroraaman.myshopify.repository.ResourceWrapper
import java.util.*

class OrdersViewModel(private val mShopifyRepository: IShopifyRepository) : ViewModel() {

    fun getOrders(webAddress: String): LiveData<ResourceWrapper<ArrayList<Order>>>? {
        return mShopifyRepository.getOrders(webAddress)
    }

    fun amountSpentProcessor(orders: ArrayList<Order>?, firstName: String, lastName: String): Data<Double>? {
        if (orders == null) {
            return null
        }

        var reqdCustomerExpenditure = 0.0
        var othersExpenditure = 0.0

        for (order in orders) {
            if (order.customer != null && firstName == order.customer.firstName && lastName == order.customer.lastName) {
                reqdCustomerExpenditure += order.totalPrice
            } else {
                othersExpenditure += order.totalPrice
            }
        }

        return Data(reqdCustomerExpenditure, othersExpenditure, firstName + " " + lastName)
    }

    fun quantityProcessor(orders: ArrayList<Order>?, title: String): Data<Int>? {
        if (orders == null) {
            return null
        }

        var reqdItemsSold = 0
        var otherItemsSold = 0

        for (order in orders) {
            for (item in order.items) {
                if (title == item.title) {
                    reqdItemsSold += item.quantity
                } else {
                    otherItemsSold += item.quantity
                }
            }
        }

        return Data(reqdItemsSold, otherItemsSold, title)
    }

    /**
     * Class to hold data processed by the view model.
     *
     * @param <T> Type of data processed by the view model.
    </T> */
    class Data<T>(
            /**
             * The value of data corresponding to the requested parameters.
             */
            val requiredValue: T,
            /**
             * The value of data corresponding to parameters but the requested ones.
             */
            val othersValue: T,
            /**
             * Value of the required parameter
             */
            val reqdParameter: String) {

        override fun equals(o: Any?): Boolean {
            if (this === o) {
                return true
            }
            if (o == null || javaClass != o.javaClass) {
                return false
            }

            val data = o as Data<*>?

            if (requiredValue != data!!.requiredValue) {
                return false
            }
            return if (othersValue != data.othersValue) {
                false
            } else reqdParameter == data.reqdParameter
        }
    }
}