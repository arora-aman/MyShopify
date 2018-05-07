package com.aroraaman.myshopify.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.aroraaman.myshopify.dagger.ViewModelKey;
import com.aroraaman.myshopify.myshopify.ui.orders.OrdersViewModel;
import com.aroraaman.myshopify.repository.IShopifyRepository;

import java.util.Map;

import javax.inject.Provider;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module
public class ViewModelModule {
    @Singleton
    @Provides
    ViewModelProvider.Factory provideViewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> providers) {
        return new ViewModelFactory(providers);
    }

    @ViewModelKey(OrdersViewModel.class)
    @IntoMap
    @Provides
    ViewModel provideOrdersViewModel(IShopifyRepository shopifyRepository) {
        return new OrdersViewModel(shopifyRepository);
    }
}
