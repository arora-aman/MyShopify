package com.aroraaman.myshopify.myshopify;

import android.arch.lifecycle.ViewModelProvider;

import com.aroraaman.myshopify.repository.RepositoryModule;
import com.aroraaman.myshopify.viewmodel.ViewModelModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * A component whose lifetime is the life of the application.
 */
@Singleton
@Component(modules = {
        AppModule.class,
        RepositoryModule.class,
        ViewModelModule.class
})
public interface ApplicationComponent {
    void inject(MyShopifyApplication myShopifyApplication);

    ViewModelProvider.Factory getViewModelFactory();
}
