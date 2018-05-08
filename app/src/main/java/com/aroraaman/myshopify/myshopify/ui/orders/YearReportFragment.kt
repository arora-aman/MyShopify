package com.aroraaman.myshopify.myshopify.ui.orders

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import com.aroraaman.myshopify.R
import com.aroraaman.myshopify.model.Order

class YearReportFragment : Fragment() {

    private val mAdapter = OrdersAdapter()

    lateinit private var mOrdersListView: ListView

    lateinit private var mOrders: ArrayList<Order>
    private var mYear: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mYear = this.arguments.getInt(KEY_YEAR)
        mOrders = this.arguments.getSerializable(KEY_ORDERS_LIST) as ArrayList<Order>
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_report, null)

        val heading = view?.findViewById<TextView>(R.id.reportHeading)
        heading?.text = getString(R.string.year_report_title, mYear)

        mOrdersListView = view?.findViewById(R.id.reportList)!!
        mOrdersListView.adapter = mAdapter

        updateDataSet(mOrders)

        return view
    }

    fun updateDataSet(orders: ArrayList<Order>) {
        mOrders = orders
        mAdapter.orders = orders
    }

    companion object {
        private val KEY_YEAR = "KEY_YEAR"
        private val KEY_ORDERS_LIST ="KEY_ORDERS_LIST"

        fun newInstance(year: Int, orders: ArrayList<Order>) : YearReportFragment {
            val fragment = YearReportFragment()

            val args = Bundle()
            args.putInt(KEY_YEAR, year)
            args.putSerializable(KEY_ORDERS_LIST, orders)

            fragment.arguments = args

            return fragment
        }
    }

}