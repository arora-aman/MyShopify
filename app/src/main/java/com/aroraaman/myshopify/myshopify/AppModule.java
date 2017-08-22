package com.aroraaman.myshopify.myshopify;

import android.app.Application;
import android.content.Context;

import com.aroraaman.myshopify.dagger.ForApplication;

import dagger.Module;
import dagger.Provides;

/**
 * Module for application wide dependencies.
 */
@Module
public class AppModule {

    private final Application mApplication;

    public AppModule(Application mApplication) {
        this.mApplication = mApplication;
    }

    @ForApplication
    @Provides
    Context provideApplicationContext() {
        return mApplication;
    }
}
