package com.aroraaman.myshopify.myshopify.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.Gravity;

import com.aroraaman.myshopify.R;
import com.aroraaman.myshopify.myshopify.MyShopifyApplication;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements IFragmentNavigator {
    private ActivityComponent mComponent;
    private OrderStatsFragment mOrderStatsFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOrderStatsFragment = new OrderStatsFragment();

        navigateToFragment(mOrderStatsFragment);
    }

    private ActivityComponent component() {
        if (mComponent == null) {
            mComponent = DaggerActivityComponent.builder()
                    .applicationComponent(MyShopifyApplication.from(this).getComponent())
                    .activityModule(new ActivityModule(this))
                    .build();
        }
        return mComponent;
    }

    public static ActivityComponent getComponent(Context context) {
        return ((MainActivity) context).component();
    }

    @Override
    public void slideToFragment(@NotNull Fragment fragment) {
        Fragment curr = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        curr.setExitTransition(new Slide(Gravity.LEFT));
        fragment.setEnterTransition(new Slide(Gravity.RIGHT));

        navigateToFragment(fragment);
    }

    @Override
    public void navigateToFragment(@NotNull Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }
}
