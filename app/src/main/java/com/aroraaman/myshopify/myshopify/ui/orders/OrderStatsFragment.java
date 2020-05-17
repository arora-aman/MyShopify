package com.aroraaman.myshopify.myshopify.ui.orders;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aroraaman.myshopify.R;
import com.aroraaman.myshopify.model.Order;
import com.aroraaman.myshopify.myshopify.ui.IFragmentNavigator;
import com.aroraaman.myshopify.myshopify.ui.MainActivity;
import com.aroraaman.myshopify.repository.ResourceWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.inject.Inject;

public class OrderStatsFragment extends Fragment {
    @Inject ViewModelProvider.Factory mFactory;
    @Inject IFragmentNavigator mFragmentNavigator;

    private OrdersViewModel mViewModel;

    private TextView mLoadingTextView;

    private LinearLayout mAnalysisLayout;

    private TextView mYearTextView;

    private TextView mProvinceReportHeading;
    private ListView mProvinceListView;

    private ProvinceDataAdapter mProvinceDataAdapter;

    private ProvinceReportFragment mProvinceReportFragment;
    private YearReportFragment mYearReportFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.getComponent(getActivity()).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_orders, null);

        mLoadingTextView = view.findViewById(R.id.loadingView);

        mProvinceDataAdapter = new ProvinceDataAdapter();

        mAnalysisLayout = view.findViewById(R.id.analysisLayout);


        mYearTextView = view.findViewById(R.id.yearReport);

        mProvinceReportHeading = view.findViewById(R.id.provinceReportHeading);
        mProvinceListView = view.findViewById(R.id.provinceListView);

        mProvinceListView.setAdapter(mProvinceDataAdapter);

        mViewModel = ViewModelProviders.of(this, mFactory).get(OrdersViewModel.class);
        mViewModel.getOrders("https://shopicruit.myshopify.com/admin/orders.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6")
                .observe(this, new OrderObserver());

        return view;
    }

    @NonNull
    private LinearLayout.LayoutParams getOrientationBasedLayoutParams(int orientation) {
        return new LinearLayout.LayoutParams(
                orientation == Configuration.ORIENTATION_PORTRAIT ? ViewGroup.LayoutParams.MATCH_PARENT : 0,
                orientation == Configuration.ORIENTATION_LANDSCAPE ? ViewGroup.LayoutParams.MATCH_PARENT : 0, 1);
    }

    private class OrderObserver implements Observer<ResourceWrapper<ArrayList<Order>>> {
        @Override
        public void onChanged(@Nullable ResourceWrapper<ArrayList<Order>> orders) {
            if (orders == null) {
                mLoadingTextView.setVisibility(View.VISIBLE);
                mLoadingTextView.setText(R.string.fetching_data);
                return;
            }
            ResourceWrapper.State state = orders.state;
            if (state == ResourceWrapper.State.REQUEST_FAILED) {
                Toast.makeText(getContext(), R.string.request_failed_error, Toast.LENGTH_SHORT)
                        .show();
                return;
            } else if (state == ResourceWrapper.State.ERROR) {
                Toast.makeText(getContext(), orders.error.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                return;
            } else if (state == ResourceWrapper.State.LOADING && orders.data == null) {
                return;
            }

            mLoadingTextView.setText(R.string.crunching_numbers);

            ArrayList<OrdersViewModel.ProvinceData> provinceData = mViewModel.getProvinceData(orders.data);
            ArrayList<Order> yearData = mViewModel.getYearData(orders.data, 2017);

            mYearTextView.setText(getString(R.string.year_report, yearData.size(), 2017));
            mProvinceDataAdapter.updateDataSet(provinceData);

            if (mYearReportFragment == null) {
                mYearReportFragment = YearReportFragment.Companion.newInstance(2017, yearData);
            } else {
                mYearReportFragment.updateDataSet(yearData);
            }

            if (mProvinceReportFragment == null) {
                mProvinceReportFragment = ProvinceReportFragment.Companion.newInstance(provinceData);
            } else {
                mProvinceReportFragment.updateProvinceData(provinceData);
            }

            mYearTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFragmentNavigator.navigateToFragment(mYearReportFragment);
                }
            });

            mProvinceReportHeading.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFragmentNavigator.navigateToFragment(mProvinceReportFragment);
                }
            });

            mLoadingTextView.setVisibility(View.GONE);
            mAnalysisLayout.setVisibility(View.VISIBLE);
        }
    }

    private class ProvinceDataAdapter extends BaseAdapter {

        private ArrayList<OrdersViewModel.ProvinceData> mProvinceData = new ArrayList<>();

        @Override
        public int getCount() {
            return mProvinceData.size();
        }

        @Override
        public Object getItem(int position) {
            return mProvinceData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            ProvinceReportViewHolder holder;
            if (view == null) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_province_report, null);
                TextView report = view.findViewById(R.id.provinceReportItem);
                holder = new ProvinceReportViewHolder(report);
                view.setTag(holder);
            } else {
                holder = (ProvinceReportViewHolder) view.getTag();
            }

            OrdersViewModel.ProvinceData data = mProvinceData.get(position);
            holder.provinceReport.setText(getString(R.string.province_report, data.getOrders().size(), data.getProvince().province));

            return view;
        }

        void updateDataSet(ArrayList<OrdersViewModel.ProvinceData> data) {
            mProvinceData = data;

            Collections.sort(mProvinceData, new Comparator<OrdersViewModel.ProvinceData>() {
                @Override
                public int compare(OrdersViewModel.ProvinceData o1, OrdersViewModel.ProvinceData o2) {
                    return o1.getProvince().province.compareTo(o2.getProvince().province);
                }
            });

            notifyDataSetChanged();
        }

    }

    private static class ProvinceReportViewHolder {
        TextView provinceReport;

        private ProvinceReportViewHolder(TextView provinceReport) {
            this.provinceReport = provinceReport;
        }
    }
}
