package com.aroraaman.myshopify.myshopify.ui.orders

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
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
            provinceMap[province] = provinceOrders
        }

        val provinceData = ArrayList<ProvinceData>()

        provinceMap.forEach { (_, v) -> provinceData.add(ProvinceData(v[0].customer.province, v)) }

        return provinceData;
    }

     fun getYearData(orders: ArrayList<Order>?, year: Int): ArrayList<Order> {
         if (null == orders) {
             return ArrayList()
         }

        return ArrayList(orders.filter { order ->  order.createdAtDate.year == year })
    }

    data class ProvinceData internal constructor(val province: Province, val orders: ArrayList<Order>)
}