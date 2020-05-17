package com.aroraaman.myshopify.myshopify.ui.orders

import android.content.Context
import android.graphics.Typeface
import android.widget.LinearLayout
import android.widget.TextView
import com.aroraaman.myshopify.model.Order

internal class OrderDetailView(listContext: Context?): LinearLayout(listContext) {
    private val customer = TextView(listContext)
    private val items = TextView(listContext)
    private val moreDetails = TextView(listContext)

    init {
        orientation = VERTICAL

        addView(customer)
        addView(items)
        addView(moreDetails)
    }

    fun setOrder(order: Order) {
        customer.text = order.customer?.toString() ?: "Unknown Customer"
        customer.typeface = Typeface.create(customer.typeface, Typeface.BOLD)

        items.text = order.items.joinToString(separator = "\n") { item -> "- $item" }

        val priceAndTime = "Summary: $${order.totalPrice} - ${order.createdAt}\n"

        moreDetails.text = priceAndTime
    }
}