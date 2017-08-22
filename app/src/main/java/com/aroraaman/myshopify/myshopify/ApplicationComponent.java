package com.aroraaman.myshopify.myshopify;

import com.aroraaman.myshopify.repository.IOrderParser;
import com.aroraaman.myshopify.repository.RepositoryModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * A component whose lifetime is the life of the application.
 */
@Singleton
@Component(modules = {
        AppModule.class,
        RepositoryModule.class
})
public interface ApplicationComponent {
    void inject(MyShopifyApplication myShopifyApplication);

    IOrderParser getOrderParser();
}
