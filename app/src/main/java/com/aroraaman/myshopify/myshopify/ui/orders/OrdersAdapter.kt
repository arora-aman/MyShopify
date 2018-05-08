package com.aroraaman.myshopify.myshopify.ui.orders

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.aroraaman.myshopify.model.Order

class OrdersAdapter : BaseAdapter() {
    var orders = ArrayList<Order>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItem(position: Int): Any {
        return orders[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return orders.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView

        if (view == null) {
            view = TextView(parent?.context)
        }

        (view as TextView).text = orders[position].toString()

        return view
    }
}