package com.aroraaman.myshopify.myshopify.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.aroraaman.myshopify.R;
import com.aroraaman.myshopify.myshopify.MyShopifyApplication;

public class MainActivity extends AppCompatActivity {
    private ActivityComponent mComponent;
    private OrderStatsFragment mOrderStatsFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOrderStatsFragment = new OrderStatsFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, mOrderStatsFragment);
        transaction.commit();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mOrderStatsFragment.onConfigurationChanged(newConfig);
    }

    private ActivityComponent component() {
        if (mComponent == null) {
            mComponent = DaggerActivityComponent.builder()
                    .applicationComponent(MyShopifyApplication.from(this).getComponent())
                    .build();
        }
        return mComponent;
    }

    public static ActivityComponent getComponent(Context context) {
        return ((MainActivity) context).component();
    }
}
