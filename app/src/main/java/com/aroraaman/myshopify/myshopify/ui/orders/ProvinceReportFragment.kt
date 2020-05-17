package com.aroraaman.myshopify.myshopify.ui.orders

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import com.aroraaman.myshopify.R
import com.aroraaman.myshopify.myshopify.ui.orders.OrdersViewModel.ProvinceData
import java.util.*
import kotlin.collections.ArrayList

class ProvinceReportFragment : Fragment() {

    private val mAdapter = ProvinceOrdersAdapter()

    private lateinit var mProvinceDataListView: ListView
    private lateinit var mProvinceData: ArrayList<ProvinceData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mProvinceData = arguments?.getSerializable(KEY_PROVINCE_DATA) as ArrayList<ProvinceData>
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_report, null)

        val heading = view?.findViewById<TextView>(R.id.reportHeading)
        heading?.text = getString(R.string.orders_by_province)

        mProvinceDataListView = view?.findViewById(R.id.reportList)!!
        mProvinceDataListView.adapter = mAdapter

        updateProvinceData(mProvinceData)

        return view
    }

    override fun onStop() {
        super.onStop()
        mAdapter.moveAllViewsToPool(mProvinceDataListView)
    }

    fun updateProvinceData(data: ArrayList<ProvinceData>) {
        mAdapter.orders = data
    }


    private class ProvinceOrdersAdapter : BaseAdapter() {

        private val mViewPool = Stack<OrderDetailView>()

        var orders = ArrayList<ProvinceData>()
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

        fun moveAllViewsToPool(list: ListView) {
            var i = 0
            while (true) {
                val view = (list.getChildAt(i++) ?: return) as LinearLayout

                val holder = view.tag as ProvinceOrdersViewHolder

                holder.orderDetails.forEach { v ->
                    view.removeView(v)
                    mViewPool.push(v)
                }
            }
        }

        var count2 = 0

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val data = orders[position]
            val holder: ProvinceOrdersViewHolder
            var view = convertView

            if (view == null) {
                view = LinearLayout(parent?.context)

                view.orientation = LinearLayout.VERTICAL

                val provinceHeading = TextView(parent?.context)
                provinceHeading.setTextAppearance(android.R.style.TextAppearance_Material_Subhead)
                view.addView(provinceHeading)

                holder = ProvinceOrdersViewHolder(provinceHeading, ArrayList(data.orders.size))
                view.tag = holder
            } else {
                holder = view.tag as ProvinceOrdersViewHolder
            }

            view = view as LinearLayout

            holder.orderDetails.forEach { v -> view.removeView(v) }

            while (holder.orderDetails.size > data.orders.size) {
                mViewPool.push(holder.orderDetails.removeAt(holder.orderDetails.size - 1))
            }

            while (holder.orderDetails.size < data.orders.size && !mViewPool.empty()) {
                holder.orderDetails.add(mViewPool.pop())
            }

            while (holder.orderDetails.size < data.orders.size) {
                holder.orderDetails.add(OrderDetailView(parent?.context))
            }

            holder.province.text = parent?.context?.getString(R.string.province_title,
                    data.province.province, data.province.provinceCode)

            for (i in data.orders.indices) {
                holder.orderDetails[i].setOrder(data.orders[i])
                view.addView(holder.orderDetails[i])
            }

            return view
        }
    }

    companion object {
        private class ProvinceOrdersViewHolder(val province: TextView, val orderDetails: ArrayList<OrderDetailView>)

        private const val KEY_PROVINCE_DATA = "KEY_PROVINCE_DATA"

        fun newInstance(data: ArrayList<ProvinceData>): ProvinceReportFragment {
            val fragment = ProvinceReportFragment()

            val args = Bundle()
            args.putSerializable(KEY_PROVINCE_DATA, data)

            fragment.arguments = args

            return fragment
        }
    }
}

