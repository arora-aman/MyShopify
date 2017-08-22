package com.aroraaman.myshopify.myshopify;

import javax.inject.Singleton;

import dagger.Component;

/**
 * A component whose lifetime is the life of the application.
 */
@Singleton
@Component(modules = {
        AppModule.class
})
public interface ApplicationComponent {
    void inject(MyShopifyApplication myShopifyApplication);
}
