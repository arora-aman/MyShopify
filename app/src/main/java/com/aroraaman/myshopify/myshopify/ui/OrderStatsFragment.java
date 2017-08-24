package com.aroraaman.myshopify.myshopify.ui;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aroraaman.myshopify.R;
import com.aroraaman.myshopify.model.Order;
import com.aroraaman.myshopify.repository.ResourceWrapper;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

import javax.inject.Inject;

public class OrderStatsFragment extends LifecycleFragment {
    @Inject
    ViewModelProvider.Factory mFactory;

    private OrdersViewModel mViewModel;

    private LinearLayout mRootLayout;
    private PieChart mChartExpenditure;
    private PieChart mChartItemsSold;
    private TextView mLoadingTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.getComponent(getActivity()).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootLayout = new LinearLayout(getContext());
        mRootLayout.setOrientation(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ?
                LinearLayout.VERTICAL : LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mRootLayout.setLayoutParams(params);
        mRootLayout.setPadding(10, 0, 10, 0);

        mChartExpenditure = createChart();
        mChartItemsSold = createChart();

        mLoadingTextView = new TextView(getContext());
        mLoadingTextView.setLayoutParams(params);
        mLoadingTextView.setGravity(Gravity.CENTER);
        mLoadingTextView.setText(R.string.fetching_data);

        mRootLayout.addView(mLoadingTextView);
        mRootLayout.addView(mChartExpenditure);
        mRootLayout.addView(mChartItemsSold);

        mViewModel = ViewModelProviders.of(this, mFactory).get(OrdersViewModel.class);
        mViewModel.getOrders("https://shopicruit.myshopify.com/admin/orders.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6")
                .observe(this, new OrderObserver());

        return mRootLayout;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mRootLayout.setOrientation(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ?
                LinearLayout.VERTICAL : LinearLayout.HORIZONTAL);
        mChartExpenditure.setLayoutParams(getOrientationBasedLayoutParams(newConfig.orientation));
        mChartItemsSold.setLayoutParams(getOrientationBasedLayoutParams(newConfig.orientation));
    }

    private PieChart createChart() {
        PieChart chart = new PieChart(getContext());

        int orientation = getResources().getConfiguration().orientation;

        LinearLayout.LayoutParams params = getOrientationBasedLayoutParams(orientation);
        chart.setLayoutParams(params);

        chart.setVisibility(View.GONE);

        return chart;
    }

    private void setChartData(PieChart chart, ArrayList<ChartEntry> entries, String dataSetLabel, String centerText) {
        chart.setDrawHoleEnabled(false);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        for (ChartEntry entry : entries) {
            pieEntries.add(new PieEntry(entry.value, entry.label));
            colors.add(entry.color);
        }
        PieDataSet dataSet = new PieDataSet(pieEntries, dataSetLabel);
        dataSet.setColors(colors);

        PieData data = chart.getData();
        if (data == null) {
            data = new PieData(dataSet);
        } else {
            data.setDataSet(dataSet);
        }
        data.setValueTextSize(0f);
        chart.setData(data);
        chart.setCenterText(centerText);
        chart.setCenterTextSize(12);
        chart.setCenterTextOffset(0, 10);

        chart.getDescription().setEnabled(false);
        chart.setDrawEntryLabels(false);

        chart.notifyDataSetChanged();
        chart.invalidate();

        chart.setVisibility(View.VISIBLE);
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

            final OrdersViewModel.Data<Double> customerExpenditureData = mViewModel.amountSpentProcessor(orders.data, "Napoleon", "Batz");
            final OrdersViewModel.Data<Integer> itemsSoldData = mViewModel.quantityProcessor(orders.data, "Awesome Bronze Bag");

            setChartData(mChartExpenditure, createPieChartEntries(customerExpenditureData),
                    getString(R.string.customer_expenditure),
                    getString(R.string.customer_expenditure_label, customerExpenditureData.reqdParameter, customerExpenditureData.requiredValue));
            setChartData(mChartItemsSold, createPieChartEntries(itemsSoldData),
                    getString(R.string.items_sold),
                    getString(R.string.items_sold_label, itemsSoldData.reqdParameter, itemsSoldData.requiredValue));

            mLoadingTextView.setVisibility(View.GONE);
        }

        <T extends Number> ArrayList<ChartEntry> createPieChartEntries(OrdersViewModel.Data<T> data) {
            ArrayList<ChartEntry> pieEntries = new ArrayList<>();

            pieEntries.add(new ChartEntry(data.reqdParameter,
                    data.requiredValue.floatValue(), Color.rgb(39, 111, 191)));
            pieEntries.add(new ChartEntry(getString(R.string.others),
                    data.othersValue.floatValue(), Color.rgb(175, 91, 91)));

            return pieEntries;
        }
    }
    
    private static class ChartEntry {
        final String label;
        final float value;
        final int color;

        ChartEntry(String label, float value, int color) {
            this.label = label;
            this.value = value;
            this.color = color;
        }
    }
}
