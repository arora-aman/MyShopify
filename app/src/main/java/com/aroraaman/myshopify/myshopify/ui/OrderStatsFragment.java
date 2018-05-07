package com.aroraaman.myshopify.myshopify.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;

import javax.inject.Inject;

public class OrderStatsFragment extends Fragment {
    @Inject
    ViewModelProvider.Factory mFactory;

    private OrdersViewModel mViewModel;

    private LinearLayout mRootLayout;
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


        mLoadingTextView = new TextView(getContext());
        mLoadingTextView.setLayoutParams(params);
        mLoadingTextView.setGravity(Gravity.CENTER);
        mLoadingTextView.setText(R.string.fetching_data);

        mRootLayout.addView(mLoadingTextView);

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
        }
    }
}
