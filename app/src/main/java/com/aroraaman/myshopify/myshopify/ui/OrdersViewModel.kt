package com.aroraaman.myshopify.myshopify.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import com.aroraaman.myshopify.model.Order
import com.aroraaman.myshopify.model.Province
import com.aroraaman.myshopify.repository.IShopifyRepository
import com.aroraaman.myshopify.repository.ResourceWrapper

class OrdersViewModel(private val mShopifyRepository: IShopifyRepository) : ViewModel() {

    private val TAG = "OrdersViewModel"

    fun getOrders(webAddress: String): LiveData<ResourceWrapper<ArrayList<Order>>>? {
        return mShopifyRepository.getOrders(webAddress)
    }

    fun getProvinceData(orders: ArrayList<Order>?): ArrayList<ProvinceData> {
        if (orders == null) {
            return ArrayList()
        }

        val provinceMap = HashMap<Province, ArrayList<Order>>()
        for (order in orders) {
            if (order.customer == null) {
                continue
            }

            val province = order.customer.province
            var provinceOrders: ArrayList<Order>? = provinceMap[province]
            if (provinceOrders == null) {
                provinceOrders = ArrayList()
            }

            provinceOrders.add(order)
            provinceMap.put(province, provinceOrders)
        }

        val provinceData = ArrayList<ProvinceData>()

        provinceMap.forEach { (k, v) -> provinceData.add(ProvinceData(v[0].customer.province, v)) }

        return provinceData;
    }

     fun getYearData(orders: ArrayList<Order>?, year: Int): ArrayList<Order> {
         if (orders == null) {
             return ArrayList()
         }
        val yearList = ArrayList<Order>()

        orders.forEach { order ->
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            val createdAt = format.calendar
            createdAt.time = format.parse(order.createdAt)

            if (createdAt.get(DateFormat.Field.YEAR.calendarField) == year) {
                yearList.add(order)
            }
        }

        return yearList
    }

    data class ProvinceData internal constructor(val province: Province, val orders: ArrayList<Order>)
}