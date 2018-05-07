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
}