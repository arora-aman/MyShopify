package com.aroraaman.myshopify.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.aroraaman.myshopify.dagger.ForApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class RepositoryModule {
    @Singleton
    @Provides
    IOrderStore providesOrderStore(@ForApplication Context context, IOrderParser orderParser) {
        return new OrderStore(context, orderParser);
    }

    @Singleton
    @Provides
    IOrderParser providesOrderParser() {
        return new OrderParser();
    }

    @Singleton
    @Provides
    IShopifyRepository providesShopifyRepository(IOrderStore orderStore, IOrderParser orderParser) {
        return new ShopifyRepository(orderStore, orderParser,
                new OkHttpClient(), new Handler(Looper.getMainLooper()));
    }
}
