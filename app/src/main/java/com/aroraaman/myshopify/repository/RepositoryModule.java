package com.aroraaman.myshopify.repository;

import android.content.Context;

import com.aroraaman.myshopify.dagger.ForApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

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
}
